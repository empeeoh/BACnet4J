package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.util.queue.ByteQueue;

public class SessionKey extends BaseType {
    private OctetString sessionKey;
    private Address peerAddress;
    
    public SessionKey(OctetString sessionKey, Address peerAddress) {
        this.sessionKey = sessionKey;
        this.peerAddress = peerAddress;
    }

    public void write(ByteQueue queue) {
        write(queue, sessionKey);
        write(queue, peerAddress);
    }
    
    public SessionKey(ByteQueue queue) throws BACnetException {
        sessionKey = read(queue, OctetString.class);
        peerAddress = read(queue, Address.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((peerAddress == null) ? 0 : peerAddress.hashCode());
        result = PRIME * result + ((sessionKey == null) ? 0 : sessionKey.hashCode());
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
        final SessionKey other = (SessionKey) obj;
        if (peerAddress == null) {
            if (other.peerAddress != null)
                return false;
        }
        else if (!peerAddress.equals(other.peerAddress))
            return false;
        if (sessionKey == null) {
            if (other.sessionKey != null)
                return false;
        }
        else if (!sessionKey.equals(other.sessionKey))
            return false;
        return true;
    }
}
