package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfValue extends EventParameter {
    public static final byte TYPE_ID = 2;
    
    private UnsignedInteger timeDelay;
    private BitString bitmask;
    private Real referencedPropertyIncrement;
    
    public ChangeOfValue(UnsignedInteger timeDelay, BitString bitmask) {
        this.timeDelay = timeDelay;
        this.bitmask = bitmask;
    }

    public ChangeOfValue(UnsignedInteger timeDelay, Real referencedPropertyIncrement) {
        this.timeDelay = timeDelay;
        this.referencedPropertyIncrement = referencedPropertyIncrement;
    }

    protected void writeImpl(ByteQueue queue) {
        timeDelay.write(queue, 0);
        writeContextTag(queue, 1, true);
        if (bitmask != null)
            bitmask.write(queue, 0);
        else
            referencedPropertyIncrement.write(queue, 1);
        writeContextTag(queue, 1, false);
    }

    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((bitmask == null) ? 0 : bitmask.hashCode());
        result = PRIME * result + ((referencedPropertyIncrement == null) ? 0 : referencedPropertyIncrement.hashCode());
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
        final ChangeOfValue other = (ChangeOfValue) obj;
        if (bitmask == null) {
            if (other.bitmask != null)
                return false;
        }
        else if (!bitmask.equals(other.bitmask))
            return false;
        if (referencedPropertyIncrement == null) {
            if (other.referencedPropertyIncrement != null)
                return false;
        }
        else if (!referencedPropertyIncrement.equals(other.referencedPropertyIncrement))
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
