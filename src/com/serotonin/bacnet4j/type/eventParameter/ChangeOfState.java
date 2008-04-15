package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.constructed.PropertyStates;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfState extends EventParameter {
    public static final byte TYPE_ID = 1;
    
    private UnsignedInteger timeDelay;
    private SequenceOf<PropertyStates> listOfValues;
    
    public ChangeOfState(UnsignedInteger timeDelay, SequenceOf<PropertyStates> listOfValues) {
        this.timeDelay = timeDelay;
        this.listOfValues = listOfValues;
    }

    protected void writeImpl(ByteQueue queue) {
        timeDelay.write(queue, 0);
        listOfValues.write(queue, 1);
    }

    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfValues == null) ? 0 : listOfValues.hashCode());
        result = PRIME * result + ((timeDelay == null) ? 0 : timeDelay.hashCode());
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
        if (listOfValues == null) {
            if (other.listOfValues != null)
                return false;
        }
        else if (!listOfValues.equals(other.listOfValues))
            return false;
        if (timeDelay == null) {
            if (other.timeDelay != null)
                return false;
        }
        else if (!timeDelay.equals(other.timeDelay))
            return false;
        return true;
    }
}
