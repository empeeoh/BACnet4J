package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class ReadAccessSpecification extends BaseType {
    private ObjectIdentifier objectIdentifier;
    private SequenceOf<PropertyReference> listOfPropertyReferences;
    
    public ReadAccessSpecification(ObjectIdentifier objectIdentifier, 
            SequenceOf<PropertyReference> listOfPropertyReferences) {
        this.objectIdentifier = objectIdentifier;
        this.listOfPropertyReferences = listOfPropertyReferences;
    }

    public ReadAccessSpecification(ObjectIdentifier objectIdentifier, PropertyIdentifier pid) { 
        this.objectIdentifier = objectIdentifier;
        List<PropertyReference> refs = new ArrayList<PropertyReference>(1);
        refs.add(new PropertyReference(pid, null));
        this.listOfPropertyReferences = new SequenceOf<PropertyReference>(refs);
    }

    public SequenceOf<PropertyReference> getListOfPropertyReferences() {
        return listOfPropertyReferences;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public void write(ByteQueue queue) {
        write(queue, objectIdentifier, 0);
        write(queue, listOfPropertyReferences, 1);
    }
    
    public ReadAccessSpecification(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class, 0);
        listOfPropertyReferences = readSequenceOf(queue, PropertyReference.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfPropertyReferences == null) ? 0 : listOfPropertyReferences.hashCode());
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
        final ReadAccessSpecification other = (ReadAccessSpecification) obj;
        if (listOfPropertyReferences == null) {
            if (other.listOfPropertyReferences != null)
                return false;
        }
        else if (!listOfPropertyReferences.equals(other.listOfPropertyReferences))
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
