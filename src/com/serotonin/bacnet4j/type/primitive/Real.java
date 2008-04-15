package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.util.queue.ByteQueue;

public class Real extends Primitive {
    public static final byte TYPE_ID = 4;
    
    private float value;
    
    public Real(float value) {
        this.value = value;
    }
    
    public float floatValue() {
        return value;
    }
    
    //
    // Reading and writing
    //
    public Real(ByteQueue queue) {
        readTag(queue);
        value = Float.intBitsToFloat(BACnetUtils.popInt(queue));
    }
    
    public void writeImpl(ByteQueue queue) {
        BACnetUtils.pushInt(queue, Float.floatToIntBits(value));
    }

    protected long getLength() {
        return 4;
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Float.floatToIntBits(value);
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
        final Real other = (Real) obj;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        return true;
    }
    
    public String toString() {
        return Float.toString(value);
    }
}
