package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class PropertyReference extends BaseType {
    private PropertyIdentifier propertyIdentifier;
    private UnsignedInteger propertyArrayIndex;
    
    public PropertyReference(PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex) {
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
    }
    
    public PropertyReference(PropertyIdentifier propertyIdentifier) {
        this.propertyIdentifier = propertyIdentifier;
    }
    
    public UnsignedInteger getPropertyArrayIndex() {
        return propertyArrayIndex;
    }

    public PropertyIdentifier getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public void write(ByteQueue queue) {
        write(queue, propertyIdentifier, 0);
        writeOptional(queue, propertyArrayIndex, 1);
    }
    
    public PropertyReference(ByteQueue queue) throws BACnetException {
        propertyIdentifier = read(queue, PropertyIdentifier.class, 0);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
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
        final PropertyReference other = (PropertyReference) obj;
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
        return true;
    }
}
