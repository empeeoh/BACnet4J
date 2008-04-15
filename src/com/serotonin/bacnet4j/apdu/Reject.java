package com.serotonin.bacnet4j.apdu;

import com.serotonin.bacnet4j.type.enumerated.RejectReason;
import com.serotonin.util.queue.ByteQueue;

/**
 * The BACnet-Reject-PDU is used to reject a received confirmed request PDU based on syntactical flaws or other 
 * protocol errors that prevent the PDU from being interpreted or the requested service from being provided. Only 
 * confirmed request PDUs may be rejected.
 */
public class Reject extends AckAPDU {
    public static final byte TYPE_ID = 6;

    /**
     * This parameter, of type BACnetRejectReason, contains the reason the PDU with the indicated 'invokeID' is being 
     * rejected.
     */
    private RejectReason rejectReason;
    
    public Reject(byte originalInvokeId, RejectReason rejectReason) {
        this.originalInvokeId = originalInvokeId;
        this.rejectReason = rejectReason;
    }
    
    public byte getPduType() {
        return TYPE_ID;
    }

    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(originalInvokeId);
        queue.push(rejectReason.byteValue());
    }
    
    Reject(ByteQueue queue) {
        queue.pop(); // Ignore the first byte. No news there.
        originalInvokeId = queue.pop();
        rejectReason = new RejectReason(queue.popU1B());
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + originalInvokeId;
        result = PRIME * result + ((rejectReason == null) ? 0 : rejectReason.hashCode());
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
        final Reject other = (Reject) obj;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        if (rejectReason == null) {
            if (other.rejectReason != null)
                return false;
        }
        else if (!rejectReason.equals(other.rejectReason))
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
