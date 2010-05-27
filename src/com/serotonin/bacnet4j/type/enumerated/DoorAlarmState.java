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
public class DoorAlarmState extends Enumerated {
    private static final long serialVersionUID = -4016268027739544828L;
    public static final DoorAlarmState normal = new DoorAlarmState(0);
    public static final DoorAlarmState alarm = new DoorAlarmState(1);
    public static final DoorAlarmState doorOpenTooLong = new DoorAlarmState(2);
    public static final DoorAlarmState forcedOpen = new DoorAlarmState(3);
    public static final DoorAlarmState tamper = new DoorAlarmState(4);
    public static final DoorAlarmState doorFault = new DoorAlarmState(5);
    public static final DoorAlarmState lockDown = new DoorAlarmState(6);
    public static final DoorAlarmState freeAccess = new DoorAlarmState(7);
    public static final DoorAlarmState egressOpen = new DoorAlarmState(8);

    public DoorAlarmState(int value) {
        super(value);
    }

    public DoorAlarmState(ByteQueue queue) {
        super(queue);
    }
}
