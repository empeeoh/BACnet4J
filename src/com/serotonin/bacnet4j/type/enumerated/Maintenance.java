package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class Maintenance extends Enumerated {
    public static final Maintenance none = new Maintenance(0);
    public static final Maintenance periodicTest = new Maintenance(1);
    public static final Maintenance needServiceOperational = new Maintenance(2);
    public static final Maintenance needServiceInoperative = new Maintenance(3);

    public Maintenance(int value) {
        super(value);
    }
    
    public Maintenance(ByteQueue queue) {
        super(queue);
    }
}
