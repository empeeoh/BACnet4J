package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.util.queue.ByteQueue;

public class FloatingLimit extends NotificationParameters {
    public static final byte TYPE_ID = 4;
  
    private Real referenceValue;
    private StatusFlags statusFlags;
    private Real setpointValue;
    private Real errorLimit;
    
    public FloatingLimit(Real referenceValue, StatusFlags statusFlags, Real setpointValue, Real errorLimit) {
        this.referenceValue = referenceValue;
        this.statusFlags = statusFlags;
        this.setpointValue = setpointValue;
        this.errorLimit = errorLimit;
    }
    
    protected void writeImpl(ByteQueue queue) {
        write(queue, referenceValue, 0);
        write(queue, statusFlags, 0);
        write(queue, setpointValue, 0);
        write(queue, errorLimit, 0);
    }
    
    public FloatingLimit(ByteQueue queue) throws BACnetException {
        referenceValue = read(queue, Real.class, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
        setpointValue = read(queue, Real.class, 2);
        errorLimit = read(queue, Real.class, 3);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((errorLimit == null) ? 0 : errorLimit.hashCode());
        result = PRIME * result + ((referenceValue == null) ? 0 : referenceValue.hashCode());
        result = PRIME * result + ((setpointValue == null) ? 0 : setpointValue.hashCode());
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
        final FloatingLimit other = (FloatingLimit) obj;
        if (errorLimit == null) {
            if (other.errorLimit != null)
                return false;
        }
        else if (!errorLimit.equals(other.errorLimit))
            return false;
        if (referenceValue == null) {
            if (other.referenceValue != null)
                return false;
        }
        else if (!referenceValue.equals(other.referenceValue))
            return false;
        if (setpointValue == null) {
            if (other.setpointValue != null)
                return false;
        }
        else if (!setpointValue.equals(other.setpointValue))
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
