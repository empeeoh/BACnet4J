package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.queue.ByteQueue;

public class PriorityArray extends SequenceOf<PriorityValue> {
    public PriorityArray() {
        super(new ArrayList<PriorityValue>());
    }
    
    public PriorityArray(List<PriorityValue> priorityValues) {
        super(priorityValues);
    }

    public PriorityArray(ByteQueue queue) throws BACnetException {
        super(queue, PriorityValue.class);
    }
}
