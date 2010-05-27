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
public class DoorSecuredStatus extends Enumerated {
    private static final long serialVersionUID = 7337105893343734773L;
    public static final DoorSecuredStatus secured = new DoorSecuredStatus(0);
    public static final DoorSecuredStatus unsecured = new DoorSecuredStatus(1);
    public static final DoorSecuredStatus unknown = new DoorSecuredStatus(2);

    public DoorSecuredStatus(int value) {
        super(value);
    }

    public DoorSecuredStatus(ByteQueue queue) {
        super(queue);
    }
}
