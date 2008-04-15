package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class MessagePriority extends Enumerated {
    public static final MessagePriority normal = new MessagePriority(0);
    public static final MessagePriority urgent = new MessagePriority(1);

    public MessagePriority(int value) {
        super(value);
    }
    
    public MessagePriority(ByteQueue queue) {
        super(queue);
    }
}
