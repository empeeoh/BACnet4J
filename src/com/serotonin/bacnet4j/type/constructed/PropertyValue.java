package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.ThreadLocalObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class PropertyValue extends BaseType {
    private final PropertyIdentifier propertyIdentifier; // 0
    private final UnsignedInteger propertyArrayIndex;    // 1 optional
    private Encodable value;                             // 2
    private final UnsignedInteger priority;              // 3 optional
    
    public PropertyValue(PropertyIdentifier propertyIdentifier, Encodable value) {
        this(propertyIdentifier, null, value, null);
    }
    
    public PropertyValue(PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex, 
            Encodable value, UnsignedInteger priority) {
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
        this.value = value;
        this.priority = priority;
    }
    
    public UnsignedInteger getPriority() {
        return priority;
    }

    public UnsignedInteger getPropertyArrayIndex() {
        return propertyArrayIndex;
    }

    public PropertyIdentifier getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public Encodable getValue() {
        return value;
    }
    
    public void setValue(Encodable value) {
        this.value = value;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, propertyIdentifier, 0);
        writeOptional(queue, propertyArrayIndex, 1);
        writeEncodable(queue, value, 2);
        writeOptional(queue, priority, 3);
    }
    
    public PropertyValue(ByteQueue queue) throws BACnetException {
        propertyIdentifier = read(queue, PropertyIdentifier.class, 0);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 1);
        value = readEncodable(queue, ThreadLocalObjectType.get(), propertyIdentifier, propertyArrayIndex, 2);
        priority = readOptional(queue, UnsignedInteger.class, 3);
    }
    
    @Override
    public String toString() {
        return "PropertyValue(propertyIdentifier="+ propertyIdentifier +
        ", propertyArrayIndex="+ propertyArrayIndex +
        ", value="+ value +
        ", priority="+ priority +
        ")";
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((priority == null) ? 0 : priority.hashCode());
        result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final PropertyValue other = (PropertyValue) obj;
        if (priority == null) {
            if (other.priority != null)
                return false;
        }
        else if (!priority.equals(other.priority))
            return false;
        if (propertyArrayIndex == null) {
            if (other.propertyArrayIndex != null)
                return false;
        }
        else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
            return false;
        if (propertyIdentifier == null) {
            if (other.propertyIdentifier != null)
                return false;
        }
        else if (!propertyIdentifier.equals(other.propertyIdentifier))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
}
