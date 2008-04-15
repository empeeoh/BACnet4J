package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class ProgramRequest extends Enumerated {
    public static final ProgramRequest ready = new ProgramRequest(0);
    public static final ProgramRequest load = new ProgramRequest(1);
    public static final ProgramRequest run = new ProgramRequest(2);
    public static final ProgramRequest halt = new ProgramRequest(3);
    public static final ProgramRequest restart = new ProgramRequest(4);
    public static final ProgramRequest unload = new ProgramRequest(5);

    public ProgramRequest(int value) {
        super(value);
    }
    
    public ProgramRequest(ByteQueue queue) {
        super(queue);
    }
}
