package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class UnsignedRange extends NotificationParameters {
    public static final byte TYPE_ID = 11;
    
    private UnsignedInteger exceedingValue;
    private StatusFlags statusFlags;
    private UnsignedInteger exceedingLimit;
    
    public UnsignedRange(UnsignedInteger exceedingValue, StatusFlags statusFlags, UnsignedInteger exceedingLimit) {
        this.exceedingValue = exceedingValue;
        this.statusFlags = statusFlags;
        this.exceedingLimit = exceedingLimit;
    }
    
    protected void writeImpl(ByteQueue queue) {
        write(queue, exceedingValue, 0);
        write(queue, statusFlags, 1);
        write(queue, exceedingLimit, 2);
    }
    
    public UnsignedRange(ByteQueue queue) throws BACnetException {
        exceedingValue = read(queue, UnsignedInteger.class, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
        exceedingLimit = read(queue, UnsignedInteger.class, 2);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
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
        final UnsignedRange other = (UnsignedRange) obj;
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
