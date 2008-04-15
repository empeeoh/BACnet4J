package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class LifeSafetyOperation extends Enumerated {
    public static final LifeSafetyOperation none = new LifeSafetyOperation(0);
    public static final LifeSafetyOperation silence = new LifeSafetyOperation(1);
    public static final LifeSafetyOperation silenceAudible = new LifeSafetyOperation(2);
    public static final LifeSafetyOperation silenceVisual = new LifeSafetyOperation(3);
    public static final LifeSafetyOperation reset = new LifeSafetyOperation(4);
    public static final LifeSafetyOperation resetAlarm = new LifeSafetyOperation(5);
    public static final LifeSafetyOperation resetFault = new LifeSafetyOperation(6);
    public static final LifeSafetyOperation unsilence = new LifeSafetyOperation(7);
    public static final LifeSafetyOperation unsilenceAudible = new LifeSafetyOperation(8);
    public static final LifeSafetyOperation unsilenceVisual = new LifeSafetyOperation(9);

    public LifeSafetyOperation(int value) {
        super(value);
    }
    
    public LifeSafetyOperation(ByteQueue queue) {
        super(queue);
    }
}
