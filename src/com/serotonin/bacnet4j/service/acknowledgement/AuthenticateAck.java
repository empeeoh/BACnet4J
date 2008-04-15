package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class AuthenticateAck extends AcknowledgementService {
    public static final byte TYPE_ID = 24;
    
    private UnsignedInteger modifiedRandomNumber;
    
    public AuthenticateAck(UnsignedInteger modifiedRandomNumber) {
        this.modifiedRandomNumber = modifiedRandomNumber;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, modifiedRandomNumber);
    }
    
    AuthenticateAck(ByteQueue queue) throws BACnetException {
        modifiedRandomNumber = read(queue, UnsignedInteger.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((modifiedRandomNumber == null) ? 0 : modifiedRandomNumber.hashCode());
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
        final AuthenticateAck other = (AuthenticateAck) obj;
        if (modifiedRandomNumber == null) {
            if (other.modifiedRandomNumber != null)
                return false;
        }
        else if (!modifiedRandomNumber.equals(other.modifiedRandomNumber))
            return false;
        return true;
    }
}
