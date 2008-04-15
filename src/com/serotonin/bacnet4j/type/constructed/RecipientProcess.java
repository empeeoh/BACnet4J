package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class RecipientProcess extends BaseType {
    private Recipient recipient;
    private UnsignedInteger processIdentifier;
    
    public RecipientProcess(Recipient recipient, UnsignedInteger processIdentifier) {
        this.recipient = recipient;
        this.processIdentifier = processIdentifier;
    }

    public void write(ByteQueue queue) {
        write(queue, recipient, 0);
        write(queue, processIdentifier, 1);
    }
    
    public RecipientProcess(ByteQueue queue) throws BACnetException {
        recipient = read(queue, Recipient.class, 0);
        processIdentifier = read(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((processIdentifier == null) ? 0 : processIdentifier.hashCode());
        result = PRIME * result + ((recipient == null) ? 0 : recipient.hashCode());
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
        final RecipientProcess other = (RecipientProcess) obj;
        if (processIdentifier == null) {
            if (other.processIdentifier != null)
                return false;
        }
        else if (!processIdentifier.equals(other.processIdentifier))
            return false;
        if (recipient == null) {
            if (other.recipient != null)
                return false;
        }
        else if (!recipient.equals(other.recipient))
            return false;
        return true;
    }
}
