package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.util.queue.ByteQueue;

public class OutOfRange extends NotificationParameters {
    public static final byte TYPE_ID = 5;
    
    private Real exceedingValue;
    private StatusFlags statusFlags;
    private Real deadband;
    private Real exceedingLimit;
    
    public OutOfRange(Real exceedingValue, StatusFlags statusFlags, Real deadband, Real exceedingLimit) {
        this.exceedingValue = exceedingValue;
        this.statusFlags = statusFlags;
        this.deadband = deadband;
        this.exceedingLimit = exceedingLimit;
    }
    
    protected void writeImpl(ByteQueue queue) {
        write(queue, exceedingValue, 0);
        write(queue, statusFlags, 1);
        write(queue, deadband, 2);
        write(queue, exceedingLimit, 3);
    }
    
    public OutOfRange(ByteQueue queue) throws BACnetException {
        exceedingValue = read(queue, Real.class, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
        deadband = read(queue, Real.class, 2);
        exceedingLimit = read(queue, Real.class, 3);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deadband == null) ? 0 : deadband.hashCode());
        result = PRIME * result + ((exceedingLimit == null) ? 0 : exceedingLimit.hashCode());
        result = PRIME * result + ((exceedingValue == null) ? 0 : exceedingValue.hashCode());
        result = PRIME * result + ((statusFlags == null) ? 0 : statusFlags.hashCode());
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
        final OutOfRange other = (OutOfRange) obj;
        if (deadband == null) {
            if (other.deadband != null)
                return false;
        }
        else if (!deadband.equals(other.deadband))
            return false;
        if (exceedingLimit == null) {
            if (other.exceedingLimit != null)
                return false;
        }
        else if (!exceedingLimit.equals(other.exceedingLimit))
            return false;
        if (exceedingValue == null) {
            if (other.exceedingValue != null)
                return false;
        }
        else if (!exceedingValue.equals(other.exceedingValue))
            return false;
        if (statusFlags == null) {
            if (other.statusFlags != null)
                return false;
        }
        else if (!statusFlags.equals(other.statusFlags))
            return false;
        return true;
    }
}
