package com.serotonin.bacnet4j.obj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class ObjectCovSubscription {
    private static Set<ObjectType> supportedObjectTypes = new HashSet<ObjectType>();
    private static Set<PropertyIdentifier> supportedPropertyIdentifiers = new HashSet<PropertyIdentifier>();
    
    static {
        supportedObjectTypes.add(ObjectType.accumulator);
        supportedObjectTypes.add(ObjectType.binaryInput);
        supportedObjectTypes.add(ObjectType.binaryOutput);
        supportedObjectTypes.add(ObjectType.binaryValue);
        supportedObjectTypes.add(ObjectType.lifeSafetyPoint);
        supportedObjectTypes.add(ObjectType.multiStateInput);
        supportedObjectTypes.add(ObjectType.multiStateOutput);
        supportedObjectTypes.add(ObjectType.multiStateValue);
        
        supportedPropertyIdentifiers.add(PropertyIdentifier.presentValue);
        supportedPropertyIdentifiers.add(PropertyIdentifier.statusFlags);
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
    
    public static boolean sendCovNotification(ObjectType objectType, PropertyIdentifier pid) {
        return supportedObjectType(objectType) && supportedPropertyIdentifiers.contains(pid);
    }
    
    
    public static List<PropertyValue> getValues(BACnetObject obj) {
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        for (PropertyIdentifier pid : supportedPropertyIdentifiers)
            addValue(obj, values, pid);
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
    
    private final Address peer;
    private final Network network;
    private final UnsignedInteger subscriberProcessIdentifier;
    private boolean issueConfirmedNotifications;
    private long expiryTime;
    
    public ObjectCovSubscription(Address peer, Network network, UnsignedInteger subscriberProcessIdentifier) {
        this.peer = peer;
        this.network = network;
        this.subscriberProcessIdentifier = subscriberProcessIdentifier;
    }

    public Address getPeer() {
        return peer;
    }

    public Network getNetwork() {
        return network;
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
