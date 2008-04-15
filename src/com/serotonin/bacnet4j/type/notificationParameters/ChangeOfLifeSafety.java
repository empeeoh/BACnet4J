package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyMode;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfLifeSafety extends NotificationParameters {
    public static final byte TYPE_ID = 8;
  
    private LifeSafetyState newState;
    private LifeSafetyMode newMode;
    private StatusFlags statusFlags;
    private LifeSafetyOperation operationExpected;
    
    public ChangeOfLifeSafety(LifeSafetyState newState, LifeSafetyMode newMode, StatusFlags statusFlags, LifeSafetyOperation operationExpected) {
        this.newState = newState;
        this.newMode = newMode;
        this.statusFlags = statusFlags;
        this.operationExpected = operationExpected;
    }
    
    protected void writeImpl(ByteQueue queue) {
        write(queue, newState, 0);
        write(queue, statusFlags, 1);
        write(queue, newMode, 2);
        write(queue, operationExpected, 3);
    }
    
    public ChangeOfLifeSafety(ByteQueue queue) throws BACnetException {
        newState = read(queue, LifeSafetyState.class, 0);
        newMode = read(queue, LifeSafetyMode.class, 1);
        statusFlags = read(queue, StatusFlags.class, 2);
        operationExpected = read(queue, LifeSafetyOperation.class, 3);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((newMode == null) ? 0 : newMode.hashCode());
        result = PRIME * result + ((newState == null) ? 0 : newState.hashCode());
        result = PRIME * result + ((operationExpected == null) ? 0 : operationExpected.hashCode());
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
        final ChangeOfLifeSafety other = (ChangeOfLifeSafety) obj;
        if (newMode == null) {
            if (other.newMode != null)
                return false;
        }
        else if (!newMode.equals(other.newMode))
            return false;
        if (newState == null) {
            if (other.newState != null)
                return false;
        }
        else if (!newState.equals(other.newState))
            return false;
        if (operationExpected == null) {
            if (other.operationExpected != null)
                return false;
        }
        else if (!operationExpected.equals(other.operationExpected))
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
