package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.util.queue.ByteQueue;

abstract public class BaseType extends Encodable {
    public void write(ByteQueue queue, int contextId) {
        // Write a start tag
        writeContextTag(queue, contextId, true);
        write(queue);
        // Write an end tag
        writeContextTag(queue, contextId, false);
    }
}
