package com.serotonin.bacnet4j.type.notificationParameters;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfValue extends NotificationParameters {
    public static final byte TYPE_ID = 2;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(BitString.class);
        classes.add(Real.class);
    }
    
    private Choice newValue;
    private StatusFlags statusFlags;
    
    public ChangeOfValue(BitString newValue, StatusFlags statusFlags) {
        this.newValue = new Choice(0, newValue);
        this.statusFlags = statusFlags;
    }

    public ChangeOfValue(Real newValue, StatusFlags statusFlags) {
        this.newValue = new Choice(1, newValue);
        this.statusFlags = statusFlags;
    }

    protected void writeImpl(ByteQueue queue) {
        write(queue, newValue, 0);
        write(queue, statusFlags, 1);
    }

    public ChangeOfValue(ByteQueue queue) throws BACnetException {
        popStart(queue, 0);
        newValue = new Choice(queue, classes);
        popEnd(queue, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((newValue == null) ? 0 : newValue.hashCode());
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
        final ChangeOfValue other = (ChangeOfValue) obj;
        if (newValue == null) {
            if (other.newValue != null)
                return false;
        }
        else if (!newValue.equals(other.newValue))
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
