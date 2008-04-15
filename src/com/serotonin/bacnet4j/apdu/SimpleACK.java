package com.serotonin.bacnet4j.apdu;

import com.serotonin.util.queue.ByteQueue;

public class SimpleACK extends AckAPDU {
    public static final byte TYPE_ID = 2;

    /**
     * This parameter shall contain the value of the BACnetConfirmedServiceChoice corresponding to the service 
     * contained in the previous BACnet-Confirmed-Service-Request that has resulted in this acknowledgment.
     */
    private int serviceAckChoice;
    
    public SimpleACK(byte originalInvokeId, int serviceAckChoice) {
        this.originalInvokeId = originalInvokeId;
        this.serviceAckChoice = serviceAckChoice;
    }
    
    public byte getPduType() {
        return TYPE_ID;
    }

    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(originalInvokeId);
        queue.push(serviceAckChoice);
    }
    
    public SimpleACK(ByteQueue queue) {
        queue.pop(); // no news here
        originalInvokeId = queue.pop();
        serviceAckChoice = queue.popU1B();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + originalInvokeId;
        result = PRIME * result + serviceAckChoice;
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
        final SimpleACK other = (SimpleACK) obj;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        if (serviceAckChoice != other.serviceAckChoice)
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
