package com.serotonin.bacnet4j.type.primitive;

import java.math.BigInteger;

import com.serotonin.util.queue.ByteQueue;

public class Enumerated extends UnsignedInteger {
    public static final byte TYPE_ID = 9;
    
    public Enumerated(int value) {
        super(value);
    }
    
    public Enumerated(BigInteger value) {
        super(value);
    }
    
    public byte byteValue() {
        return (byte)intValue();
    }
    
    public boolean equals(Enumerated that) {
        return intValue() == that.intValue();
    }
    
    //
    // Reading and writing
    //
    public Enumerated(ByteQueue queue) {
        super(queue);
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }
}
