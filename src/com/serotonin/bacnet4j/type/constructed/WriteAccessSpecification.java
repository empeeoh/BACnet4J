package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.ThreadLocalObjectType;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class WriteAccessSpecification extends BaseType {
    private ObjectIdentifier objectIdentifier;
    private SequenceOf<PropertyValue> listOfProperties;
    
    public WriteAccessSpecification(ObjectIdentifier objectIdentifier, SequenceOf<PropertyValue> listOfProperties) {
        this.objectIdentifier = objectIdentifier;
        this.listOfProperties = listOfProperties;
    }

    public void write(ByteQueue queue) {
        write(queue, objectIdentifier, 0);
        write(queue, listOfProperties, 1);
    }
    
    public WriteAccessSpecification(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class, 0);
        ThreadLocalObjectType.set(objectIdentifier.getObjectType());
        listOfProperties = readSequenceOf(queue, PropertyValue.class, 1);
        ThreadLocalObjectType.remove();
    }

    public SequenceOf<PropertyValue> getListOfProperties() {
        return listOfProperties;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfProperties == null) ? 0 : listOfProperties.hashCode());
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
        final WriteAccessSpecification other = (WriteAccessSpecification) obj;
        if (listOfProperties == null) {
            if (other.listOfProperties != null)
                return false;
        }
        else if (!listOfProperties.equals(other.listOfProperties))
            return false;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
