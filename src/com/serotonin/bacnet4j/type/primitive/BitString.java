package com.serotonin.bacnet4j.type.primitive;

import java.util.Arrays;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.util.queue.ByteQueue;

public class BitString extends Primitive {
    public static final byte TYPE_ID = 8;
    
    private boolean[] value;
    
    public BitString(boolean[] value) {
        this.value = value;
    }
    
    public BitString(int size, boolean defaultValue) {
        value = new boolean[size];
        if (defaultValue) {
            for (int i=0; i<size; i++)
                value[i] = true;
        }
    }
    
    public boolean[] getValue() {
        return value;
    }
    
    public void setAll(boolean value) {
        boolean[] values = getValue();
        for (int i=0; i<values.length; i++)
            values[i] = value;
    }
    
    //
    // Reading and writing
    //
    public BitString(ByteQueue queue) {
        int length = (int)readTag(queue) - 1;
        int remainder = queue.popU1B();
        
        if (length == 0)
            value = new boolean[0];
        else {
            byte[] data = new byte[length];
            queue.pop(data);
            value = BACnetUtils.convertToBooleans(data, length * 8 - remainder);
        }
    }
    
    public void writeImpl(ByteQueue queue) {
        if (value.length == 0)
            queue.push((byte)0);
        else {
            queue.push((byte)(8 - value.length % 8));
            queue.push(BACnetUtils.convertToBytes(value));
        }
    }

    protected long getLength() {
        if (value.length == 0)
            return 1;
        return (value.length - 1) / 8 + 2;
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(value);
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
        final BitString other = (BitString) obj;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
    }
    
    public String toString() {
        return Arrays.toString(value);
    }
}
