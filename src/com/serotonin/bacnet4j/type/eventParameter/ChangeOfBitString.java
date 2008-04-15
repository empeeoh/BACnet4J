package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeOfBitString extends EventParameter {
    public static final byte TYPE_ID = 0;
    
    private UnsignedInteger timeDelay;
    private BitString bitMask;
    private SequenceOf<BitString> listOfBitstringValues;
    
    public ChangeOfBitString(UnsignedInteger timeDelay, BitString bitMask,
            SequenceOf<BitString> listOfBitstringValues) {
        this.timeDelay = timeDelay;
        this.bitMask = bitMask;
        this.listOfBitstringValues = listOfBitstringValues;
    }

    protected void writeImpl(ByteQueue queue) {
        timeDelay.write(queue, 0);
        bitMask.write(queue, 1);
        listOfBitstringValues.write(queue, 2);
    }

    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((bitMask == null) ? 0 : bitMask.hashCode());
        result = PRIME * result + ((listOfBitstringValues == null) ? 0 : listOfBitstringValues.hashCode());
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
        final ChangeOfBitString other = (ChangeOfBitString) obj;
        if (bitMask == null) {
            if (other.bitMask != null)
                return false;
        }
        else if (!bitMask.equals(other.bitMask))
            return false;
        if (listOfBitstringValues == null) {
            if (other.listOfBitstringValues != null)
                return false;
        }
        else if (!listOfBitstringValues.equals(other.listOfBitstringValues))
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
