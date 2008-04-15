package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.PropertyStates;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfState extends NotificationParameters {
    public static final byte TYPE_ID = 1;
    
    private PropertyStates newState;
    private StatusFlags statusFlags;
    
    public ChangeOfState(PropertyStates newState, StatusFlags statusFlags) {
        this.newState = newState;
        this.statusFlags = statusFlags;
    }

    protected void writeImpl(ByteQueue queue) {
        write(queue, newState, 0);
        write(queue, statusFlags, 1);
    }

    public ChangeOfState(ByteQueue queue) throws BACnetException {
        newState = read(queue, PropertyStates.class, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((newState == null) ? 0 : newState.hashCode());
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
        final ChangeOfState other = (ChangeOfState) obj;
        if (newState == null) {
            if (other.newState != null)
                return false;
        }
        else if (!newState.equals(other.newState))
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
