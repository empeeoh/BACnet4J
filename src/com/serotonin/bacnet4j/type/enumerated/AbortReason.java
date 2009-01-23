package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class AbortReason extends Enumerated {
    public static final AbortReason other = new AbortReason(0);
    public static final AbortReason bufferOverflow = new AbortReason(1);
    public static final AbortReason invalidApduInThisState = new AbortReason(2);
    public static final AbortReason preemptedByHigherPriorityTask = new AbortReason(3);
    public static final AbortReason segmentationNotSupported = new AbortReason(4);

    public AbortReason(int value) {
        super(value);
    }
    
    public AbortReason(ByteQueue queue) {
        super(queue);
    }
    
    @Override
    public String toString() {
        int type = intValue();
        if (type == other.intValue())
            return "Other";
        if (type == bufferOverflow.intValue())
            return "Buffer overflow";
        if (type == invalidApduInThisState.intValue())
            return "Invalid APDU in this state";
        if (type == preemptedByHigherPriorityTask.intValue())
            return "Preempted by higher priority task";
        if (type == segmentationNotSupported.intValue())
            return "Segmentation not supported";
        return "Unknown abort reason("+ type +")";
    }
}
