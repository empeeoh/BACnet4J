package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class VtSession extends BaseType {
    private UnsignedInteger localVtSessionId;
    private UnsignedInteger remoteVtSessionId;
    private Address remoteVtAddress;
    
    public VtSession(UnsignedInteger localVtSessionId, UnsignedInteger remoteVtSessionId, Address remoteVtAddress) {
        this.localVtSessionId = localVtSessionId;
        this.remoteVtSessionId = remoteVtSessionId;
        this.remoteVtAddress = remoteVtAddress;
    }

    public void write(ByteQueue queue) {
        write(queue, localVtSessionId);
        write(queue, remoteVtSessionId);
        write(queue, remoteVtAddress);
    }

    public VtSession(ByteQueue queue) throws BACnetException {
        localVtSessionId = read(queue, UnsignedInteger.class);
        remoteVtSessionId = read(queue, UnsignedInteger.class);
        remoteVtAddress = read(queue, Address.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((localVtSessionId == null) ? 0 : localVtSessionId.hashCode());
        result = PRIME * result + ((remoteVtAddress == null) ? 0 : remoteVtAddress.hashCode());
        result = PRIME * result + ((remoteVtSessionId == null) ? 0 : remoteVtSessionId.hashCode());
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
        final VtSession other = (VtSession) obj;
        if (localVtSessionId == null) {
            if (other.localVtSessionId != null)
                return false;
        }
        else if (!localVtSessionId.equals(other.localVtSessionId))
            return false;
        if (remoteVtAddress == null) {
            if (other.remoteVtAddress != null)
                return false;
        }
        else if (!remoteVtAddress.equals(other.remoteVtAddress))
            return false;
        if (remoteVtSessionId == null) {
            if (other.remoteVtSessionId != null)
                return false;
        }
        else if (!remoteVtSessionId.equals(other.remoteVtSessionId))
            return false;
        return true;
    }
}
