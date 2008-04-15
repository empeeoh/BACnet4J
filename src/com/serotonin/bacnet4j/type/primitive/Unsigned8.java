package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.util.queue.ByteQueue;

public class Unsigned8 extends UnsignedInteger {
    private static final int MAX = 0xff - 1;
    
    public Unsigned8(int value) {
        super(value);
        if (value > MAX)
            throw new IllegalArgumentException("Value cannot be greater than "+ MAX);
    }

    public Unsigned8(ByteQueue queue) {
        super(queue);
    }
}
