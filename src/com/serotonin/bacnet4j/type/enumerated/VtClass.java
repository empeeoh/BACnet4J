package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class VtClass extends Enumerated {
    public static final VtClass defaultTerminal = new VtClass(0);
    public static final VtClass ansi_x3_64 = new VtClass(1);
    public static final VtClass dec_vt52 = new VtClass(2);
    public static final VtClass dec_vt100 = new VtClass(3);
    public static final VtClass dec_vt220 = new VtClass(4);
    public static final VtClass hp_700_94 = new VtClass(5);
    public static final VtClass ibm_3130 = new VtClass(6);

    public VtClass(int value) {
        super(value);
    }
    
    public VtClass(ByteQueue queue) {
        super(queue);
    }
}
