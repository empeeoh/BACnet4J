package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class NotifyType extends Enumerated {
    public static final NotifyType alarm = new NotifyType(0);
    public static final NotifyType event = new NotifyType(1);
    public static final NotifyType ackNotification = new NotifyType(2);

    public NotifyType(int value) {
        super(value);
    }
    
    public NotifyType(ByteQueue queue) {
        super(queue);
    }
}
