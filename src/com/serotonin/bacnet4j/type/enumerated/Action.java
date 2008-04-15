package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class Action extends Enumerated {
    public static final Action direct = new Action(0);
    public static final Action reverse = new Action(1);

    public Action(int value) {
        super(value);
    }
    
    public Action(ByteQueue queue) {
        super(queue);
    }
}
