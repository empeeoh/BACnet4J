package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class VTCloseError extends BaseError {
    private SequenceOf<UnsignedInteger> listOfVTSessionIdentifiers;
    
    public VTCloseError(byte choice, BACnetError error, SequenceOf<UnsignedInteger> listOfVTSessionIdentifiers) {
        super(choice, error);
        this.listOfVTSessionIdentifiers = listOfVTSessionIdentifiers;
    }

    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        writeOptional(queue, listOfVTSessionIdentifiers, 1);
    }
    
    VTCloseError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        listOfVTSessionIdentifiers = readOptionalSequenceOf(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((listOfVTSessionIdentifiers == null) ? 0 : listOfVTSessionIdentifiers.hashCode());
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
        final VTCloseError other = (VTCloseError) obj;
        if (listOfVTSessionIdentifiers == null) {
            if (other.listOfVTSessionIdentifiers != null)
                return false;
        }
        else if (!listOfVTSessionIdentifiers.equals(other.listOfVTSessionIdentifiers))
            return false;
        return true;
    }
}
