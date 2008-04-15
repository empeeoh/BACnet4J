package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class StatusFlags extends BitString {
    public StatusFlags(boolean inAlarm, boolean fault, boolean overridden, boolean outOfService) {
        super(new boolean[] {inAlarm, fault, overridden, outOfService});
    }
    
    public StatusFlags(ByteQueue queue) {
        super(queue);
    }

    public boolean isInAlarm() {
        return getValue()[0];
    }
    
    public boolean isFault() {
        return getValue()[1];
    }
    
    public boolean isOverridden() {
        return getValue()[2];
    }
    
    public boolean isOutOfService() {
        return getValue()[3];
    }
}
