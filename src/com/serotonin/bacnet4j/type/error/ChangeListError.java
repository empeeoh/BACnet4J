package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeListError extends BaseError {
    private UnsignedInteger firstFailedElementNumber;
    
    public ChangeListError(byte choice, BACnetError error, UnsignedInteger firstFailedElementNumber) {
        super(choice, error);
        this.firstFailedElementNumber = firstFailedElementNumber;
    }

    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        write(queue, firstFailedElementNumber, 1);
    }
    
    ChangeListError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        firstFailedElementNumber = read(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((firstFailedElementNumber == null) ? 0 : firstFailedElementNumber.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ChangeListError other = (ChangeListError) obj;
        if (firstFailedElementNumber == null) {
            if (other.firstFailedElementNumber != null)
                return false;
        }
        else if (!firstFailedElementNumber.equals(other.firstFailedElementNumber))
            return false;
        return true;
    }
}
