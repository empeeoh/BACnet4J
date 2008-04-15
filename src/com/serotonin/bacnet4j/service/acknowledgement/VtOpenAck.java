package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class VtOpenAck extends AcknowledgementService {
    public static final byte TYPE_ID = 21;
    
    private UnsignedInteger remoteVTSessionIdentifier;
    
    public VtOpenAck(UnsignedInteger remoteVTSessionIdentifier) {
        this.remoteVTSessionIdentifier = remoteVTSessionIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, remoteVTSessionIdentifier);
    }
    
    VtOpenAck(ByteQueue queue) throws BACnetException {
        remoteVTSessionIdentifier = read(queue, UnsignedInteger.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((remoteVTSessionIdentifier == null) ? 0 : remoteVTSessionIdentifier.hashCode());
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
        final VtOpenAck other = (VtOpenAck) obj;
        if (remoteVTSessionIdentifier == null) {
            if (other.remoteVTSessionIdentifier != null)
                return false;
        }
        else if (!remoteVTSessionIdentifier.equals(other.remoteVTSessionIdentifier))
            return false;
        return true;
    }
}
