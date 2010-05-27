/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class DoorValue extends Enumerated {
    private static final long serialVersionUID = -2200245400075159155L;
    public static final DoorValue lock = new DoorValue(0);
    public static final DoorValue unlock = new DoorValue(1);
    public static final DoorValue pulseUnlock = new DoorValue(2);
    public static final DoorValue extendedPulseUnlock = new DoorValue(3);

    public DoorValue(int value) {
        super(value);
    }

    public DoorValue(ByteQueue queue) {
        super(queue);
    }
}
