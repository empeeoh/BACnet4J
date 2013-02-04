/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class ObjectCovSubscription implements Serializable {
    private static final long serialVersionUID = 3546250271813406695L;

    private static Set<ObjectType> supportedObjectTypes = new HashSet<ObjectType>();
    private static Set<PropertyIdentifier> supportedPropertyIdentifiers = new HashSet<PropertyIdentifier>();

    /** These types require a COV threshold, before any subscriptions are allowed */
    private static Set<ObjectType> covThresholdRequired = new HashSet<ObjectType>();

    static {
        supportedObjectTypes.add(ObjectType.accessDoor);
        supportedObjectTypes.add(ObjectType.accumulator);
        supportedObjectTypes.add(ObjectType.analogInput);
        supportedObjectTypes.add(ObjectType.analogOutput);
        supportedObjectTypes.add(ObjectType.analogValue);
        supportedObjectTypes.add(ObjectType.binaryInput);
        supportedObjectTypes.add(ObjectType.binaryOutput);
        supportedObjectTypes.add(ObjectType.binaryValue);
        supportedObjectTypes.add(ObjectType.lifeSafetyPoint);
        supportedObjectTypes.add(ObjectType.loop);
        supportedObjectTypes.add(ObjectType.multiStateInput);
        supportedObjectTypes.add(ObjectType.multiStateOutput);
        supportedObjectTypes.add(ObjectType.multiStateValue);
        supportedObjectTypes.add(ObjectType.pulseConverter);

        supportedPropertyIdentifiers.add(PropertyIdentifier.presentValue);
        supportedPropertyIdentifiers.add(PropertyIdentifier.statusFlags);
        supportedPropertyIdentifiers.add(PropertyIdentifier.doorAlarmState);

        covThresholdRequired.add(ObjectType.analogInput);
        covThresholdRequired.add(ObjectType.analogOutput);
        covThresholdRequired.add(ObjectType.analogValue);
        covThresholdRequired.add(ObjectType.loop);
        covThresholdRequired.add(ObjectType.pulseConverter);
    }

    public static void addSupportedObjectType(ObjectType objectType) {
        supportedObjectTypes.add(objectType);
    }

    public static void addSupportedPropertyIdentifier(PropertyIdentifier propertyIdentifier) {
        supportedPropertyIdentifiers.add(propertyIdentifier);
    }

    public static boolean supportedObjectType(ObjectType objectType) {
        return supportedObjectTypes.contains(objectType);
    }

    public static boolean sendCovNotification(ObjectType objectType, PropertyIdentifier pid, Real covThresholdValue) {
        if (!supportedObjectType(objectType))
            return false;

        if (pid != null && !supportedPropertyIdentifiers.contains(pid))
            return false;

        // Don't allow COV notifications when there is no threshold for Objects that require thresholds.
        if (covThresholdRequired.contains(objectType) && covThresholdValue == null)
            return false;

        return true;
    }

    public static List<PropertyValue> getValues(BACnetObject obj) {
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        for (PropertyIdentifier pid : supportedPropertyIdentifiers)
            addValue(obj, values, pid);
        return values;
    }

    private static void addValue(BACnetObject obj, List<PropertyValue> values, PropertyIdentifier pid) {
        try {
            // Ensure that the obj has the given property. The addition of doorAlarmState requires this.
            if (ObjectProperties.getPropertyTypeDefinition(obj.getId().getObjectType(), pid) != null) {
                Encodable value = obj.getProperty(pid);
                if (value != null)
                    values.add(new PropertyValue(pid, value));
            }
        }
        catch (BACnetServiceException e) {
            // Should never happen, so wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }

    private final Address address;
    private final OctetString linkService;
    private final UnsignedInteger subscriberProcessIdentifier;
    private boolean issueConfirmedNotifications;
    private long expiryTime;

    /**
     * The increment/threshold at which COV notifications should be sent out. Only applies to property identifiers that
     * are {@link Real}'s
     * and {@link ObjectType}'s mentioned in {@link ObjectCovSubscription#covThresholdRequired}.
     */
    private final Real covIncrement;

    /**
     * Contains the last sent values per property identifier. It is used to determine if a COV notification should be
     * sent.
     */
    private final Map<PropertyIdentifier, Encodable> lastSentValues = new HashMap<PropertyIdentifier, Encodable>();

    public ObjectCovSubscription(Address address, OctetString linkService, UnsignedInteger subscriberProcessIdentifier,
            Real covIncrement) {
        this.address = address;
        this.linkService = linkService;
        this.subscriberProcessIdentifier = subscriberProcessIdentifier;
        this.covIncrement = covIncrement;
    }

    public Address getAddress() {
        return address;
    }

    public OctetString getLinkService() {
        return linkService;
    }

    public boolean isIssueConfirmedNotifications() {
        return issueConfirmedNotifications;
    }

    public UnsignedInteger getSubscriberProcessIdentifier() {
        return subscriberProcessIdentifier;
    }

    public void setIssueConfirmedNotifications(boolean issueConfirmedNotifications) {
        this.issueConfirmedNotifications = issueConfirmedNotifications;
    }

    public void setExpiryTime(int seconds) {
        if (seconds == 0)
            expiryTime = -1;
        else
            expiryTime = System.currentTimeMillis() + seconds * 1000;
    }

    public boolean hasExpired(long now) {
        if (expiryTime == -1)
            return false;
        return expiryTime < now;
    }

    public int getTimeRemaining(long now) {
        if (expiryTime == -1)
            return 0;
        int left = (int) ((expiryTime - now) / 1000);
        if (left < 1)
            return 1;
        return left;
    }

    /**
     * Determine if a notification needs to be sent out based on the Threshold if relevant.
     * 
     * @param pid
     *            The {@link PropertyIdentifier} being updated
     * @param value
     *            The new value
     * @return true if a COV notification should be sent out, false otherwise.
     */
    public boolean isNotificationRequired(PropertyIdentifier pid, Encodable value) {
        Encodable lastSentValue = this.lastSentValues.get(pid);

        boolean notificationRequired = ThresholdCalculator.isValueOutsideOfThreshold(this.covIncrement, lastSentValue,
                value);

        if (notificationRequired) {
            this.lastSentValues.put(pid, value);
        }

        return notificationRequired;
    }

    /**
     * Utility Class to determine whether COV thresholds/increments have been surpassed.
     * 
     * @author japearson
     * 
     */
    public static class ThresholdCalculator {
        /**
         * Convert the given encodable value to a {@link Float} if possible.
         * 
         * @param value
         *            The value to attempt to convert to a {@link Float}.
         * @return A {@link Float} value if the {@link Encodable} can be converted, otherwise null.
         */
        private static Float convertEncodableToFloat(Encodable value) {
            Float floatValue = null;

            if (value instanceof Real) {
                floatValue = ((Real) value).floatValue();
            }

            return floatValue;
        }

        /**
         * Determine if the newValue has surpassed the threshold value compared with the original value.
         * <p>
         * When the originalValue is null, it is automatically assumed to be outside the threshold, because it means the
         * property hasn't been seen before.
         * <p>
         * If any of the parameters cannot be converted to a {@link Float}, then this method returns true when the
         * original and new value are not equal and false otherwise.
         * 
         * @param threshold
         *            The threshold value
         * @param originalValue
         *            The original or last sent value
         * @param newValue
         *            The new value to check
         * @return true if the new value is outside the threshold or false otherwise.
         */
        public static boolean isValueOutsideOfThreshold(Real threshold, Encodable originalValue, Encodable newValue) {
            Float floatThreshold = convertEncodableToFloat(threshold);
            Float floatOriginal = convertEncodableToFloat(originalValue);
            Float floatNewValue = convertEncodableToFloat(newValue);

            // This property hasn't been seen before, so a notification is required
            if (originalValue == null) {
                return true;
            }
            // Handle types that can't do threshold comparisons
            else if (floatThreshold == null || floatOriginal == null || floatNewValue == null) {
                return !originalValue.equals(newValue);
            }
            else {
                // Due to floating point maths, it's possible that where the difference should be equal to the threshold
                // and not be outside the threshold actually evaluates to true due to precision errors.  However since
                // this threshold is calculated only for use in deciding whether to trigger a COV notification, small
                // margins of error on boundary cases are acceptable.
                return Math.abs(floatNewValue - floatOriginal) > floatThreshold;
            }
        }
    }
}
