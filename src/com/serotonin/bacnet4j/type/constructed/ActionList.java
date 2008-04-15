package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.queue.ByteQueue;

public class ActionList extends BaseType {
    private SequenceOf<ActionCommand> action;

    public ActionList(SequenceOf<ActionCommand> action) {
        this.action = action;
    }

    public void write(ByteQueue queue) {
        write(queue, action, 0);
    }
    
    public ActionList(ByteQueue queue) throws BACnetException {
        action = readSequenceOf(queue, ActionCommand.class, 0);
    }
}
