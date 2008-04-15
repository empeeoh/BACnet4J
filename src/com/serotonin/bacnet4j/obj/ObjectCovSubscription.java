package com.serotonin.bacnet4j.obj;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class ObjectCovSubscription {
    public static boolean supportedObjectType(ObjectType objectType) {
        return objectType.equals(ObjectType.accumulator) ||
                objectType.equals(ObjectType.binaryInput) ||
                objectType.equals(ObjectType.binaryOutput) ||
                objectType.equals(ObjectType.binaryValue) ||
                objectType.equals(ObjectType.lifeSafetyPoint) ||
                objectType.equals(ObjectType.multiStateInput) ||
                objectType.equals(ObjectType.multiStateOutput) ||
                objectType.equals(ObjectType.multiStateValue);
    }
    
    public static boolean sendCovNotification(ObjectType objectType, PropertyIdentifier pid) {
        if (supportedObjectType(objectType))
            return pid.equals(PropertyIdentifier.presentValue) ||
                    pid.equals(PropertyIdentifier.statusFlags);
        return false;
    }
    
    public static List<PropertyValue> getValues(BACnetObject obj) {
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        addValue(obj, values, PropertyIdentifier.presentValue);
        addValue(obj, values, PropertyIdentifier.statusFlags);
        return values;
    }
    
    private static void addValue(BACnetObject obj, List<PropertyValue> values, PropertyIdentifier pid) {
        try {
            Encodable value = obj.getProperty(pid);
            if (value != null)
                values.add(new PropertyValue(pid, value));
        }
        catch (BACnetServiceException e) {
            // Should never happen, so wrap in a RuntimeException
            throw new RuntimeException(e);
        }
    }
    
    private Address peer;
    private UnsignedInteger subscriberProcessIdentifier;
    private boolean issueConfirmedNotifications;
    private long expiryTime;
    
    public ObjectCovSubscription(Address peer, UnsignedInteger subscriberProcessIdentifier) {
        this.peer = peer;
        this.subscriberProcessIdentifier = subscriberProcessIdentifier;
    }

    public Address getPeer() {
        return peer;
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
        int left = (int)((expiryTime - now) / 1000);
        if (left < 1)
            return 1;
        return left;
    }
}
