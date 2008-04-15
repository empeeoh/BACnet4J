package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class BinaryPV extends Enumerated {
    public static final BinaryPV inactive = new BinaryPV(0);
    public static final BinaryPV active = new BinaryPV(1);

    public BinaryPV(int value) {
        super(value);
    }
    
    public BinaryPV(ByteQueue queue) {
        super(queue);
    }
}
