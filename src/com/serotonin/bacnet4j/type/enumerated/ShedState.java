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
public class ShedState extends Enumerated {
    public static final ShedState shedInactive = new ShedState(0);
    public static final ShedState shedRequestPending = new ShedState(1);
    public static final ShedState shedCompliant = new ShedState(2);
    public static final ShedState shedNonCompliant = new ShedState(3);

    public ShedState(int value) {
        super(value);
    }
    
    public ShedState(ByteQueue queue) {
        super(queue);
    }
}
