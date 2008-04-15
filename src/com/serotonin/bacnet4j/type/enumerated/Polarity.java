package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class Polarity extends Enumerated {
    public static final Polarity normal = new Polarity(0);
    public static final Polarity reverse = new Polarity(1);

    public Polarity(int value) {
        super(value);
    }
    
    public Polarity(ByteQueue queue) {
        super(queue);
    }
}
