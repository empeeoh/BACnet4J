package com.serotonin.bacnet4j.type.primitive;

import java.util.Arrays;

import com.serotonin.util.ArrayUtils;
import com.serotonin.util.queue.ByteQueue;

public class OctetString extends Primitive {
    public static final byte TYPE_ID = 6;
    
    private final byte[] value;
    
    public OctetString(byte[] value) {
        this.value = value;
    }
    
    public byte[] getBytes() {
        return value;
    }
    
    //
    // Reading and writing
    //
    public OctetString(ByteQueue queue) {
        int length = (int)readTag(queue);
        value = new byte[length];
        queue.pop(value);
    }
    
    @Override
    public void writeImpl(ByteQueue queue) {
        queue.push(value);
    }

    @Override
    protected long getLength() {
        return value.length;
    }

    @Override
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
        final OctetString other = (OctetString) obj;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ArrayUtils.toHexString(value);
    }
}
