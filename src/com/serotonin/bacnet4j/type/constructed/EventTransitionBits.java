package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class EventTransitionBits extends BitString {
    public EventTransitionBits(boolean toOffnormal, boolean toFault, boolean toNormal) {
        super(new boolean[] {toOffnormal, toFault, toNormal});
    }

    public EventTransitionBits(ByteQueue queue) {
        super(queue);
    }

    public boolean isToOffnormal() {
        return getValue()[0];
    }
    
    public boolean isToFault() {
        return getValue()[1];
    }
    
    public boolean isToNormal() {
        return getValue()[2];
    }
    
    public boolean contains(EventState toState) {
        if (toState.equals(EventState.normal) && isToNormal())
            return true;
        
        if (toState.equals(EventState.fault) && isToFault())
            return true;
        
        // All other event states are considered off-normal
        return isToOffnormal();
    }
}
