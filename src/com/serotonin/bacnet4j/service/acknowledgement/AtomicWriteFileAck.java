package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class AtomicWriteFileAck extends AcknowledgementService {
    public static final byte TYPE_ID = 7;
    
    private boolean recordAccess;
    private SignedInteger fileStart;
    
    public AtomicWriteFileAck(boolean recordAccess, SignedInteger fileStart) {
        this.recordAccess = recordAccess;
        this.fileStart = fileStart;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public void write(ByteQueue queue) {
        write(queue, fileStart, recordAccess ? 1 : 0);
    }
    
    AtomicWriteFileAck(ByteQueue queue) throws BACnetException {
        recordAccess = peekTagNumber(queue) == 1;
        fileStart = read(queue, SignedInteger.class, recordAccess ? 1 : 0);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fileStart == null) ? 0 : fileStart.hashCode());
        result = PRIME * result + (recordAccess ? 1231 : 1237);
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
        final AtomicWriteFileAck other = (AtomicWriteFileAck) obj;
        if (fileStart == null) {
            if (other.fileStart != null)
                return false;
        }
        else if (!fileStart.equals(other.fileStart))
            return false;
        if (recordAccess != other.recordAccess)
            return false;
        return true;
    }
}
