package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.util.queue.ByteQueue;

public class Null extends Primitive {
    public static final byte TYPE_ID = 0;
    
    public Null() {
        // no op
    }
    
    public Null(ByteQueue queue) {
        readTag(queue);
    }
    
    public void writeImpl(ByteQueue queue) {
        // no op
    }

    protected long getLength() {
        return 0;
    }

    protected byte getTypeId() {
        return TYPE_ID;
    }
    
    public boolean equals() {
        return true;
    }
    
    public String toString() {
        return "Null";
    }
}
