package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.util.queue.ByteQueue;

public class WritePropertyMultipleError extends BaseError {
    private ObjectPropertyReference firstFailedWriteAttempt;
    
    public WritePropertyMultipleError(byte choice, BACnetError error, ObjectPropertyReference firstFailedWriteAttempt) {
        super(choice, error);
        this.firstFailedWriteAttempt = firstFailedWriteAttempt;
    }

    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        firstFailedWriteAttempt.write(queue, 1);
    }
    
    WritePropertyMultipleError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        firstFailedWriteAttempt = read(queue, ObjectPropertyReference.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((firstFailedWriteAttempt == null) ? 0 : firstFailedWriteAttempt.hashCode());
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
        final WritePropertyMultipleError other = (WritePropertyMultipleError) obj;
        if (firstFailedWriteAttempt == null) {
            if (other.firstFailedWriteAttempt != null)
                return false;
        }
        else if (!firstFailedWriteAttempt.equals(other.firstFailedWriteAttempt))
            return false;
        return true;
    }
}
