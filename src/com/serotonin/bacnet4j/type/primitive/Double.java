package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.util.queue.ByteQueue;

public class Double extends Primitive {
    public static final byte TYPE_ID = 5;
    
    private double value;
    
    public Double(double value) {
        this.value = value;
    }
    
    public double doubleValue() {
        return value;
    }
    
    //
    // Reading and writing
    //
    public Double(ByteQueue queue) {
        readTag(queue);
        value = java.lang.Double.longBitsToDouble(BACnetUtils.popLong(queue));
    }
    
    public void writeImpl(ByteQueue queue) {
        BACnetUtils.pushLong(queue, java.lang.Double.doubleToLongBits(value));
    }

    protected long getLength() {
        return 8;
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        long temp;
        temp = java.lang.Double.doubleToLongBits(value);
        result = PRIME * result + (int) (temp ^ (temp >>> 32));
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
        final Double other = (Double) obj;
        if (java.lang.Double.doubleToLongBits(value) != java.lang.Double.doubleToLongBits(other.value))
            return false;
        return true;
    }
    
    public String toString() {
        return java.lang.Double.toString(value);
    }
}
