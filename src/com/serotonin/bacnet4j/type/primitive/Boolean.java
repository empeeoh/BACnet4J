package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.util.queue.ByteQueue;

public class Boolean extends Primitive {
    public static final byte TYPE_ID = 1;
    
    protected boolean value;
    
    public Boolean(boolean value) {
        this.value = value;
    }
    
    public boolean booleanValue() {
        return value;
    }
    
    public Boolean(ByteQueue queue) {
        long length = readTag(queue);
        if (contextSpecific)
            value = queue.pop() == 1;
        else
            value = length == 1;
    }
    
    public void writeImpl(ByteQueue queue) {
        if (contextSpecific)
            queue.push((byte)(value ? 1 : 0));
    }

    @Override
    protected long getLength() {
        if (contextSpecific)
            return 1;
        return (byte)(value ? 1 : 0);
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (value ? 1231 : 1237);
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
        final Boolean other = (Boolean) obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    public String toString() {
        return java.lang.Boolean.toString(value);
    }
}
