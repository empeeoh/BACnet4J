package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.util.queue.ByteQueue;

abstract public class EventParameter extends BaseType {
    final public void write(ByteQueue queue) {
        writeContextTag(queue, getTypeId(), true);
        writeImpl(queue);
        writeContextTag(queue, getTypeId(), false);
    }
    
    abstract protected int getTypeId();
    abstract protected void writeImpl(ByteQueue queue);
}
