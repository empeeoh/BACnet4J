package com.serotonin.bacnet4j.obj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedCovNotificationRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.ObjectUtils;

/**
 * Additional validation
 *   - all object name values must be unique.
 *   - all object id values must be unique.
 *   
 * @author x
 *
 */
public class BACnetObject {
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
        return (CharacterString)properties.get(PropertyIdentifier.objectName);
    }
    
    public String getDescription() {
        CharacterString name = (CharacterString)properties.get(PropertyIdentifier.description);
        if (name == null)
            return null;
        return name.getValue();
    }
    
    
    //
    ///
    /// Get property
    ///
    //
    public Encodable getProperty(PropertyIdentifier pid) throws BACnetServiceException {
        if (pid.intValue() == PropertyIdentifier.objectIdentifier.intValue())
            return id;
        if (pid.intValue() == PropertyIdentifier.objectType.intValue())
            return id.getObjectType();
        
        // Check that the requested property is valid for the object. This will throw an exception if the
        // property doesn't belong.
        ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);
        
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
        
        if (!(result instanceof SequenceOf))
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.propertyIsNotAnArray);
        
        SequenceOf<?> array = (SequenceOf<?>)result;
        int index = propertyArrayIndex.intValue();
        if (index == 0)
            return new UnsignedInteger(array == null ? 0 : array.getCount());
        
        if (array == null || index > array.getCount())
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
    ///
    /// Set property
    ///
    //
    public void setProperty(PropertyIdentifier pid, Encodable value) throws BACnetServiceException {
        ObjectProperties.validateValue(id.getObjectType(), pid, value);
        setPropertyImpl(pid, value);
    }
    
    @SuppressWarnings("unchecked")
    public void setProperty(PropertyIdentifier pid, int indexBase1, Encodable value) throws BACnetServiceException {
        ObjectProperties.validateSequenceValue(id.getObjectType(), pid, value);
        SequenceOf<Encodable> list = (SequenceOf<Encodable>)properties.get(pid);
        if (list == null) {
            list = new SequenceOf<Encodable>();
            setPropertyImpl(pid, list);
        }
        list.set(indexBase1, value);
    }
    
    public void setProperty(PropertyValue value) throws BACnetServiceException {
        if (value.getValue() == null) {
            if (value.getPropertyArrayIndex() == null)
                removeProperty(value.getPropertyIdentifier());
            else
                removeProperty(value.getPropertyIdentifier(), value.getPropertyArrayIndex());
        }
        else {
            PropertyIdentifier pid = value.getPropertyIdentifier();
            
            if (pid.intValue() == PropertyIdentifier.objectIdentifier.intValue())
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
            if (pid.intValue() == PropertyIdentifier.objectType.intValue())
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
            if (value.getPriority() != null)
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
            
            if (value.getPropertyArrayIndex() != null)
                setProperty(pid, value.getPropertyArrayIndex().intValue(), value.getValue());
            else
                setProperty(pid, value.getValue());
        }
    }
    
    private void setPropertyImpl(PropertyIdentifier pid, Encodable value) {
        Encodable oldValue = properties.get(pid);
        properties.put(pid, value);
        
        if (!ObjectUtils.isEqual(value, oldValue)) {
            // Check for subscriptions.
            if (ObjectCovSubscription.sendCovNotification(id.getObjectType(), pid)) {
                synchronized (covSubscriptions) {
                    long now = System.currentTimeMillis();
                    ObjectCovSubscription sub;
                    for (int i=covSubscriptions.size() - 1; i>=0; i--) {
                        sub = covSubscriptions.get(i);
                        if (sub.hasExpired(now))
                            covSubscriptions.remove(i);
                        else
                            sendCovNotification(sub, now);
                    }
                }
            }
        }
    }
    
    
    //
    ///
    /// COV subscriptions
    ///
    //
    public void addCovSubscription(Address from, Network network, UnsignedInteger subscriberProcessIdentifier,
            Boolean issueConfirmedNotifications, UnsignedInteger lifetime) throws BACnetServiceException {
        synchronized (covSubscriptions) {
            ObjectCovSubscription sub = findCovSubscription(from, subscriberProcessIdentifier);
            boolean confirmed = issueConfirmedNotifications.booleanValue();
            
            if (sub == null) {
                if (confirmed) {
                    // If the peer wants confirmed notifications, it must be in the remote device list.
                    RemoteDevice d = localDevice.getRemoteDevice(from);
                    if (d == null)
                        throw new BACnetServiceException(ErrorClass.services, ErrorCode.covSubscriptionFailed,
                                "From address not found in remote device list. Cannot send confirmed notifications");
                }
                
                sub = new ObjectCovSubscription(from, network, subscriberProcessIdentifier);
                covSubscriptions.add(sub);
            }
            
            sub.setIssueConfirmedNotifications(issueConfirmedNotifications.booleanValue());
            sub.setExpiryTime(lifetime.intValue());
        }
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
            if (sub.getPeer().equals(from)
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
                        sub.getSubscriberProcessIdentifier(), localDevice.getConfiguration().getId(), id, 
                        timeLeft, values);
                RemoteDevice d = localDevice.getRemoteDevice(sub.getPeer());
                localDevice.send(d, req);
            }
            else {
                // Unconfirmed
                UnconfirmedCovNotificationRequest req = new UnconfirmedCovNotificationRequest(
                        sub.getSubscriberProcessIdentifier(), localDevice.getConfiguration().getId(), id,
                        timeLeft, values);
                localDevice.sendUnconfirmed(sub.getPeer(), sub.getNetwork(), req);
            }
        }
        catch (BACnetException e) {
            LocalDevice.getExceptionListener().receivedException(e);
        }
    }
    
    
    
    
    
    
    
    
    public void validate() throws BACnetServiceException {
        // Ensure that all required properties have values.
        List<PropertyTypeDefinition> defs = ObjectProperties.getRequiredPropertyTypeDefinitions(id.getObjectType());
        for (PropertyTypeDefinition def : defs) {
            if (getProperty(def.getPropertyIdentifier()) == null)
                throw new BACnetServiceException(ErrorClass.property, ErrorCode.other,
                        "Required property not set: "+ def.getPropertyIdentifier());
        }
    }
    
    public void removeProperty(PropertyIdentifier pid) throws BACnetServiceException {
        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);
        if (def.isRequired())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
        properties.remove(pid);
    }

    public void removeProperty(PropertyIdentifier pid, UnsignedInteger propertyArrayIndex) throws BACnetServiceException {
        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinitionRequired(id.getObjectType(), pid);
        if (!def.isSequence())
            throw new BACnetServiceException(ErrorClass.property, ErrorCode.invalidArrayIndex);
        SequenceOf<?> sequence = (SequenceOf<?>)properties.get(pid);
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
