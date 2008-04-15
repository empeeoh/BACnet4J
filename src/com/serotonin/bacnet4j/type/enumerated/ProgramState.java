package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class ProgramState extends Enumerated {
    public static final ProgramState idle = new ProgramState(0);
    public static final ProgramState loading = new ProgramState(1);
    public static final ProgramState running = new ProgramState(2);
    public static final ProgramState waiting = new ProgramState(3);
    public static final ProgramState halted = new ProgramState(4);
    public static final ProgramState unloading = new ProgramState(5);

    public ProgramState(int value) {
        super(value);
    }
    
    public ProgramState(ByteQueue queue) {
        super(queue);
    }
}
