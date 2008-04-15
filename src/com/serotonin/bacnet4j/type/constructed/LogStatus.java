package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class LogStatus extends BitString {
    public LogStatus(boolean logDisabled, boolean bufferPurged) {
        super(new boolean[] {logDisabled, bufferPurged});
    }

    public LogStatus(ByteQueue queue) {
        super(queue);
    }

    public boolean isLogDisabled() {
        return getValue()[0];
    }
    
    public boolean isBufferPurged() {
        return getValue()[1];
    }
}
