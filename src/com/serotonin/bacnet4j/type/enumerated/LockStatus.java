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
public class LockStatus extends Enumerated {
    private static final long serialVersionUID = -1433958074950622510L;
    public static final LockStatus locked = new LockStatus(0);
    public static final LockStatus unlocked = new LockStatus(1);
    public static final LockStatus fault = new LockStatus(2);
    public static final LockStatus unknown = new LockStatus(3);

    public LockStatus(int value) {
        super(value);
    }

    public LockStatus(ByteQueue queue) {
        super(queue);
    }
}
