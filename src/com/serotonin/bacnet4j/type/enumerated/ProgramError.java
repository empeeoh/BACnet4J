package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class ProgramError extends Enumerated {
    public static final ProgramError normal = new ProgramError(0);
    public static final ProgramError loadFailed = new ProgramError(1);
    public static final ProgramError internal = new ProgramError(2);
    public static final ProgramError program = new ProgramError(3);
    public static final ProgramError other = new ProgramError(4);

    public ProgramError(int value) {
        super(value);
    }
    
    public ProgramError(ByteQueue queue) {
        super(queue);
    }
}
