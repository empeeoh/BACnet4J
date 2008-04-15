package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class LimitEnable extends BitString {
    public LimitEnable(boolean lowLimitEnable, boolean highLimitEnable) {
        super(new boolean[] {lowLimitEnable, highLimitEnable});
    }

    public LimitEnable(ByteQueue queue) {
        super(queue);
    }

    public boolean isLowLimitEnable() {
        return getValue()[0];
    }
    
    public boolean isHighLimitEnable() {
        return getValue()[1];
    }
}
