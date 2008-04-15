package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class EventState extends Enumerated {
    public static final EventState normal = new EventState(0);
    public static final EventState fault = new EventState(1);
    public static final EventState offnormal = new EventState(2);
    public static final EventState highLimit = new EventState(3);
    public static final EventState lowLimit = new EventState(4);
    public static final EventState lifeSafetyAlarm = new EventState(5);

    public EventState(int value) {
        super(value);
    }
    
    public EventState(ByteQueue queue) {
        super(queue);
    }
}
