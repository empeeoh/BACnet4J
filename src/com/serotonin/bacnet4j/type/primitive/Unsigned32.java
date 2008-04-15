package com.serotonin.bacnet4j.type.primitive;

import java.math.BigInteger;

import com.serotonin.util.queue.ByteQueue;

public class Unsigned32 extends UnsignedInteger {
    private static final long MAX = 0xffffffffl - 1;

    public Unsigned32(int value) {
        super(value);
    }
    
    public Unsigned32(BigInteger value) {
        super(value);
        if (value.longValue() > MAX)
            throw new IllegalArgumentException("Value cannot be greater than "+ MAX);
    }

    public Unsigned32(ByteQueue queue) {
        super(queue);
    }
}
