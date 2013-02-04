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
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.ExceptionDispatch;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.PriorityValue;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * Additional validation - all object name values must be unique. - all object id values must be unique.
 * 
 * @author x
 * 
 */
public class BACnetObject implements Serializable {
    private static final long serialVersionUID = 569892306207282576L;

    private final LocalDevice localDevice;
    private final ObjectIdentifier id;
    private final Map<PropertyIdentifier, Encodable> properties = new HashMap<PropertyIdentifier, Encodable>();
    private final List<ObjectCovSubscription> covSubscriptions = new ArrayList<ObjectCovSubscription>();

    public BACnetObject(LocalDevice localDevice, ObjectIdentifier id) {
        this.localDevice = localDevice;

        if (id == null)
            throw new IllegalArgumentException("object id cannot be null");
        this.id = id;

        try {
            setProperty(PropertyIdentifier.objectName, new CharacterString(id.toString()));

            // Set any default values.
            List<PropertyTypeDefinition> defs = ObjectProperties.getPropertyTypeDefinitions(id.getObjectType());
            for (PropertyTypeDefinition def : defs) {
                if (def.getDefaultValue() != null)
                    setProperty(def.getPropertyIdentifier(), def.getDefaultValue());
            }
        }
        catch (BACnetServiceException e) {
            // Should never happen, but wrap in an unchecked just in case.
            throw new BACnetRuntimeException(e);
        }
    }

    public ObjectIdentifier getId() {
        return id;
    }

    public int getInstanceId() {
        return id.getInstanceNumber();
    }

    public String getObjectName() {
        CharacterString name = getRawObjectName();
        if (name == null)
            return null;
        return name.getValue();
    }

    public CharacterString getRawObjectName() {
        return (CharacterString) properties.get(PropertyIdentifier.objectName);
    }

    public String getDescription() {
        CharacterString name = (CharacterString) properties.get(PropertyIdentifier.description);
        if (name == null)
            return null;
        return name.getValue();
    }

    //
    //
    // Get property
    //
    public Encodable getProperty(PropertyIdentifier pid) throws BACnetServiceException {
        if (pid.intValue() == PropertyIdentifier.objectIdentifier.intValue())
            return id;
        if (pid.intValue() == PropertyIdentifier.objectType.intValue())
            return id.getObjectType();

        // Check that the requested property is valid for the object. This will throw an exception if the
        // property doesn't belong.
        ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);

        // Do some property-specific checking here.
        if (pid.intValue() == PropertyIdentifier.localTime.intValue())
            return new Time();
        if (pid.intValue() == PropertyIdentifier.localDate.intValue())
            return new Date();

        return properties.get(pid);
    }

    public Encodable getPropertyRequired(PropertyIdentifier pid) throws BACnetServiceException {
        Encodable p = getProperty(pid);
        if (p == null)
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.unknownProperty);
        return p;
    }

    public Encodable getProperty(PropertyIdentifier pid, UnsignedInteger propertyArrayIndex)
            throws BACnetServiceException {
        Encodable result = getProperty(pid);
        if (propertyArrayIndex == null)
            return result;

        if (!(result instanceof SequenceOf<?>))
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.propertyIsNotAnArray);

        SequenceOf<?> array = (SequenceOf<?>) result;
        int index = propertyArrayIndex.intValue();
        if (index == 0)
            return new UnsignedInteger(array.getCount());

        if (index > array.getCount())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.invalidArrayIndex);

        return array.get(index);
    }

    public Encodable getPropertyRequired(PropertyIdentifier pid, UnsignedInteger propertyArrayIndex)
            throws BACnetServiceException {
        Encodable p = getProperty(pid, propertyArrayIndex);
        if (p == null)
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.unknownProperty);
        return p;
    }

    //
    //
    // Set property
    //
    public void setProperty(PropertyIdentifier pid, Encodable value) throws BACnetServiceException {
        ObjectProperties.validateValue(id.getObjectType(), pid, value);
        setPropertyImpl(pid, value);

        // If the relinquish default was set, make sure the present value gets updated as necessary.
        if (pid.equals(PropertyIdentifier.relinquishDefault))
            setCommandableImpl((PriorityArray) getProperty(PropertyIdentifier.priorityArray));
    }

    @SuppressWarnings("unchecked")
    public void setProperty(PropertyIdentifier pid, int indexBase1, Encodable value) throws BACnetServiceException {
        ObjectProperties.validateSequenceValue(id.getObjectType(), pid, value);
        SequenceOf<Encodable> list = (SequenceOf<Encodable>) properties.get(pid);
        if (list == null) {
            list = new SequenceOf<Encodable>();
            setPropertyImpl(pid, list);
        }
        list.set(indexBase1, value);
    }

    public void setProperty(PropertyValue value) throws BACnetServiceException {
        PropertyIdentifier pid = value.getPropertyIdentifier();

        if (pid.intValue() == PropertyIdentifier.objectIdentifier.intValue())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        if (pid.intValue() == PropertyIdentifier.objectType.intValue())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        if (pid.intValue() == PropertyIdentifier.priorityArray.intValue())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        //        if (pid.intValue() == PropertyIdentifier.relinquishDefault.intValue())
        //            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);

        if (ObjectProperties.isCommandable((ObjectType) getProperty(PropertyIdentifier.objectType), pid))
            setCommandable(value.getValue(), value.getPriority());
        else if (value.getValue() == null) {
            if (value.getPropertyArrayIndex() == null)
                removeProperty(value.getPropertyIdentifier());
            else
                removeProperty(value.getPropertyIdentifier(), value.getPropertyArrayIndex());
        }
        else {
            if (value.getPropertyArrayIndex() != null)
                setProperty(pid, value.getPropertyArrayIndex().intValue(), value.getValue());
            else
                setProperty(pid, value.getValue());
        }
    }

    public void setCommandable(Encodable value, UnsignedInteger priority) throws BACnetServiceException {
        int pri = 16;
        if (priority != null)
            pri = priority.intValue();

        PriorityArray priorityArray = (PriorityArray) getProperty(PropertyIdentifier.priorityArray);
        priorityArray.set(pri, createCommandValue(value));
        setCommandableImpl(priorityArray);
    }

    private void setCommandableImpl(PriorityArray priorityArray) throws BACnetServiceException {
        PriorityValue priorityValue = null;
        for (PriorityValue priv : priorityArray) {
            if (!priv.isNull()) {
                priorityValue = priv;
                break;
            }
        }

        Encodable newValue = getProperty(PropertyIdentifier.relinquishDefault);
        if (priorityValue != null)
            newValue = priorityValue.getValue();

        setPropertyImpl(PropertyIdentifier.presentValue, newValue);
    }

    private void setPropertyImpl(PropertyIdentifier pid, Encodable value) {
        Encodable oldValue = properties.get(pid);
        properties.put(pid, value);

        if (!ObjectUtils.equals(value, oldValue)) {
            // Check for subscriptions.
            if (ObjectCovSubscription.sendCovNotification(id.getObjectType(), pid, this.getCovIncrement())) {
                synchronized (covSubscriptions) {
                    long now = System.currentTimeMillis();
                    ObjectCovSubscription sub;
                    for (int i = covSubscriptions.size() - 1; i >= 0; i--) {
                        sub = covSubscriptions.get(i);
                        if (sub.hasExpired(now))
                            covSubscriptions.remove(i);
                        else if (sub.isNotificationRequired(pid, value))
                            sendCovNotification(sub, now);
                    }
                }
            }
        }
    }

    private PriorityValue createCommandValue(Encodable value) throws BACnetServiceException {
        if (value instanceof Null)
            return new PriorityValue((Null) value);

        ObjectType type = (ObjectType) getProperty(PropertyIdentifier.objectType);
        if (type.equals(ObjectType.accessDoor))
            return new PriorityValue((BaseType) value);
        if (type.equals(ObjectType.analogOutput) || type.equals(ObjectType.analogValue))
            return new PriorityValue((Real) value);
        if (type.equals(ObjectType.binaryOutput) || type.equals(ObjectType.binaryValue))
            return new PriorityValue((BinaryPV) value);
        return new PriorityValue((UnsignedInteger) value);
    }

    /**
     * return all implemented properties
     * 
     * @return
     */
    public List<PropertyIdentifier> getProperties() {
        ArrayList<PropertyIdentifier> list = new ArrayList<PropertyIdentifier>();
        for (PropertyIdentifier pid : properties.keySet())
            list.add(pid);
        return list;
    }

    //
    //
    // COV subscriptions
    //
    public void addCovSubscription(Address from, OctetString linkService, UnsignedInteger subscriberProcessIdentifier,
            Boolean issueConfirmedNotifications, UnsignedInteger lifetime) throws BACnetServiceException {
        synchronized (covSubscriptions) {
            ObjectCovSubscription sub = findCovSubscription(from, subscriberProcessIdentifier);
            boolean confirmed = issueConfirmedNotifications.booleanValue();

            if (sub == null) {
                // Ensure that this object is valid for COV notifications.
                if (!ObjectCovSubscription.sendCovNotification(id.getObjectType(), null, this.getCovIncrement()))
                    throw new BACnetServiceException(ErrorClass.services, ErrorCode.covSubscriptionFailed,
                            "Object is invalid for COV notifications");

                if (confirmed) {
                    // If the peer wants confirmed notifications, it must be in the remote device list.
                    RemoteDevice d = localDevice.getRemoteDevice(from);
                    if (d == null)
                        throw new BACnetServiceException(ErrorClass.services, ErrorCode.covSubscriptionFailed,
                                "From address not found in remote device list. Cannot send confirmed notifications");
                }

                sub = new ObjectCovSubscription(from, linkService, subscriberProcessIdentifier, this.getCovIncrement());
                covSubscriptions.add(sub);
            }

            sub.setIssueConfirmedNotifications(issueConfirmedNotifications.booleanValue());
            sub.setExpiryTime(lifetime.intValue());
        }
    }

    public Real getCovIncrement() {
        return (Real) properties.get(PropertyIdentifier.covIncrement);
    }

    public void removeCovSubscription(Address from, UnsignedInteger subscriberProcessIdentifier) {
        synchronized (covSubscriptions) {
            ObjectCovSubscription sub = findCovSubscription(from, subscriberProcessIdentifier);
            if (sub != null)
                covSubscriptions.remove(sub);
        }
    }

    private ObjectCovSubscription findCovSubscription(Address from, UnsignedInteger subscriberProcessIdentifier) {
        for (ObjectCovSubscription sub : covSubscriptions) {
            if (sub.getAddress().equals(from)
                    && sub.getSubscriberProcessIdentifier().equals(subscriberProcessIdentifier))
                return sub;
        }
        return null;
    }

    private void sendCovNotification(ObjectCovSubscription sub, long now) {
        try {
            UnsignedInteger timeLeft = new UnsignedInteger(sub.getTimeRemaining(now));
            SequenceOf<PropertyValue> values = new SequenceOf<PropertyValue>(ObjectCovSubscription.getValues(this));

            if (sub.isIssueConfirmedNotifications()) {
                // Confirmed
                ConfirmedCovNotificationRequest req = new ConfirmedCovNotificationRequest(
                        sub.getSubscriberProcessIdentifier(), localDevice.getConfiguration().getId(), id, timeLeft,
                        values);
                RemoteDevice d = localDevice.getRemoteDevice(sub.getAddress());
                localDevice.send(d, req);
            }
            else {
                // Unconfirmed
                UnconfirmedCovNotificationRequest req = new UnconfirmedCovNotificationRequest(
                        sub.getSubscriberProcessIdentifier(), localDevice.getConfiguration().getId(), id, timeLeft,
                        values);
                localDevice.sendUnconfirmed(sub.getAddress(), sub.getLinkService(), req);
            }
        }
        catch (BACnetException e) {
            ExceptionDispatch.fireReceivedException(e);
        }
    }

    public void validate() throws BACnetServiceException {
        // Ensure that all required properties have values.
        List<PropertyTypeDefinition> defs = ObjectProperties.getRequiredPropertyTypeDefinitions(id.getObjectType());
        for (PropertyTypeDefinition def : defs) {
            if (getProperty(def.getPropertyIdentifier()) == null)
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.other, "Required property not set: "
                        + def.getPropertyIdentifier());
        }
    }

    public void removeProperty(PropertyIdentifier pid) throws BACnetServiceException {
        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);
        if (def.isRequired())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        properties.remove(pid);
    }

    public void removeProperty(PropertyIdentifier pid, UnsignedInteger propertyArrayIndex)
            throws BACnetServiceException {
        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);
        if (!def.isSequence())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.invalidArrayIndex);
        SequenceOf<?> sequence = (SequenceOf<?>) properties.get(pid);
        if (sequence != null)
            sequence.remove(propertyArrayIndex.intValue());
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BACnetObject other = (BACnetObject) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
}
