package com.serotonin.bacnet4j.apdu;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.error.BaseError;
import com.serotonin.util.queue.ByteQueue;

/**
 * The BACnet-Error-PDU is used to convey the information contained in a service response primitive ('Result(-)') that 
 * indicates the reason why a previous confirmed service request failed in its entirety.
 * @author mlohbihler
 */
public class Error extends AckAPDU {
    public static final byte TYPE_ID = 5;

    /**
     * This parameter, of type BACnet-Error, indicates the reason the indicated service request could not be carried 
     * out. This parameter shall be encoded according to the rules of 20.2.
     */
    private BaseError error;
    
    public Error(byte originalInvokeId, BaseError error) {
        this.originalInvokeId = originalInvokeId;
        this.error = error;
    }

    public byte getPduType() {
        return TYPE_ID;
    }

    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(originalInvokeId);
        error.write(queue);
    }
    
    Error(ByteQueue queue) throws BACnetException {
        queue.pop(); // Ignore the first byte. No news there.
        originalInvokeId = queue.pop();
        error = BaseError.createBaseError(queue);
    }
    
    public String toString() {
        return "ErrorAPDU("+ error +")";
    }
    
    public BaseError getError() {
        return error;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((error == null) ? 0 : error.hashCode());
        result = PRIME * result + originalInvokeId;
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
        final Error other = (Error) obj;
        if (error == null) {
            if (other.error != null)
                return false;
        }
        else if (!error.equals(other.error))
            return false;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
