package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.constructed.DeviceObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfLifeSafety extends EventParameter {
    public static final byte TYPE_ID = 8;
    
    private UnsignedInteger timeDelay;
    private SequenceOf<LifeSafetyState> listOfLifeSafetyAlarmValues;
    private SequenceOf<LifeSafetyState> listOfAlarmValues;
    private DeviceObjectPropertyReference modePropertyReference;
    
    public ChangeOfLifeSafety(UnsignedInteger timeDelay, SequenceOf<LifeSafetyState> listOfLifeSafetyAlarmValues, 
            SequenceOf<LifeSafetyState> listOfAlarmValues, DeviceObjectPropertyReference modePropertyReference) {
        this.timeDelay = timeDelay;
        this.listOfLifeSafetyAlarmValues = listOfLifeSafetyAlarmValues;
        this.listOfAlarmValues = listOfAlarmValues;
        this.modePropertyReference = modePropertyReference;
    }

    protected void writeImpl(ByteQueue queue) {
        timeDelay.write(queue, 0);
        listOfLifeSafetyAlarmValues.write(queue, 1);
        listOfAlarmValues.write(queue, 2);
        modePropertyReference.write(queue, 3);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfAlarmValues == null) ? 0 : listOfAlarmValues.hashCode());
        result = PRIME * result + ((listOfLifeSafetyAlarmValues == null) ? 0 : listOfLifeSafetyAlarmValues.hashCode());
        result = PRIME * result + ((modePropertyReference == null) ? 0 : modePropertyReference.hashCode());
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
        final ChangeOfLifeSafety other = (ChangeOfLifeSafety) obj;
        if (listOfAlarmValues == null) {
            if (other.listOfAlarmValues != null)
                return false;
        }
        else if (!listOfAlarmValues.equals(other.listOfAlarmValues))
            return false;
        if (listOfLifeSafetyAlarmValues == null) {
            if (other.listOfLifeSafetyAlarmValues != null)
                return false;
        }
        else if (!listOfLifeSafetyAlarmValues.equals(other.listOfLifeSafetyAlarmValues))
            return false;
        if (modePropertyReference == null) {
            if (other.modePropertyReference != null)
                return false;
        }
        else if (!modePropertyReference.equals(other.modePropertyReference))
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
