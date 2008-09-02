package com.serotonin.bacnet4j.service.unconfirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class WhoIsRequest extends UnconfirmedRequestService {
    public static final byte TYPE_ID = 8;
    
    private UnsignedInteger deviceInstanceRangeLowLimit;
    private UnsignedInteger deviceInstanceRangeHighLimit;
    
    public WhoIsRequest() {
        // no op
    }

    public WhoIsRequest(UnsignedInteger deviceInstanceRangeLowLimit, UnsignedInteger deviceInstanceRangeHighLimit) {
        this.deviceInstanceRangeLowLimit = deviceInstanceRangeLowLimit;
        this.deviceInstanceRangeHighLimit = deviceInstanceRangeHighLimit;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void handle(LocalDevice localDevice, Address from, Network network) throws BACnetException {
        BACnetObject local = localDevice.getConfiguration();
        
        // Check if we're in the device id range.
        if (deviceInstanceRangeLowLimit != null && local.getInstanceId() < deviceInstanceRangeLowLimit.intValue())
            return;
        
        if (deviceInstanceRangeHighLimit != null && local.getInstanceId() > deviceInstanceRangeHighLimit.intValue())
            return;
        
        // Return the result in a i am message.
        IAmRequest iam = localDevice.getIAm();
        localDevice.sendUnconfirmed(from, network, iam);
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, deviceInstanceRangeLowLimit, 0);
        writeOptional(queue, deviceInstanceRangeHighLimit, 1);
    }
    
    WhoIsRequest(ByteQueue queue) throws BACnetException {
        deviceInstanceRangeLowLimit = readOptional(queue, UnsignedInteger.class, 0);
        deviceInstanceRangeHighLimit = readOptional(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deviceInstanceRangeHighLimit == null) ? 0 : deviceInstanceRangeHighLimit.hashCode());
        result = PRIME * result + ((deviceInstanceRangeLowLimit == null) ? 0 : deviceInstanceRangeLowLimit.hashCode());
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
        final WhoIsRequest other = (WhoIsRequest) obj;
        if (deviceInstanceRangeHighLimit == null) {
            if (other.deviceInstanceRangeHighLimit != null)
                return false;
        }
        else if (!deviceInstanceRangeHighLimit.equals(other.deviceInstanceRangeHighLimit))
            return false;
        if (deviceInstanceRangeLowLimit == null) {
            if (other.deviceInstanceRangeLowLimit != null)
                return false;
        }
        else if (!deviceInstanceRangeLowLimit.equals(other.deviceInstanceRangeLowLimit))
            return false;
        return true;
    }
}
