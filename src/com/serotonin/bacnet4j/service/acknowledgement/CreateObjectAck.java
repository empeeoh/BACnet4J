package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class CreateObjectAck extends AcknowledgementService {
    public static final byte TYPE_ID = 10;
    
    private ObjectIdentifier objectIdentifier;
    
    public CreateObjectAck(ObjectIdentifier objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, objectIdentifier);
    }
    
    CreateObjectAck(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class);
    }
    
    public String toString() {
        return "CreateObjectAck("+ objectIdentifier +")";
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
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
        final CreateObjectAck other = (CreateObjectAck) obj;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
