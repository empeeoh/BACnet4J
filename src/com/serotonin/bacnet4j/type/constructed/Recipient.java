package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class Recipient extends BaseType {
    private final Choice choice;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(ObjectIdentifier.class);
        classes.add(Address.class);
    }
    
    public Recipient(ObjectIdentifier device) {
        choice = new Choice(0, device);
    }
    
    public Recipient(Address address) {
        choice = new Choice(1, address);
    }
    
    public boolean isObjectIdentifier() {
        return choice.getContextId() == 0;
    }
    
    public ObjectIdentifier getObjectIdentifier() {
        return (ObjectIdentifier)choice.getDatum();
    }

    public boolean isAddress() {
        return choice.getContextId() == 1;
    }
    
    public Address getAddress() {
        return (Address)choice.getDatum();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, choice);
    }
    
    public Recipient(ByteQueue queue) throws BACnetException {
        choice = new Choice(queue, classes);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((choice == null) ? 0 : choice.hashCode());
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
        final Recipient other = (Recipient) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        }
        else if (!choice.equals(other.choice))
            return false;
        return true;
    }
}
