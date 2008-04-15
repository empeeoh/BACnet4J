package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.util.queue.ByteQueue;

public class Unsigned16 extends UnsignedInteger {
    private static final int MAX = 0xffff - 1;
    
    public Unsigned16(int value) {
        super(value);
        if (value > MAX)
            throw new IllegalArgumentException("Value cannot be greater than "+ MAX);
    }

    public Unsigned16(ByteQueue queue) {
        super(queue);
    }
}
