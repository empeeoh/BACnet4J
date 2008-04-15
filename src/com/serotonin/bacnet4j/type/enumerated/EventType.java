package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.notificationParameters.BufferReady;
import com.serotonin.bacnet4j.type.notificationParameters.ChangeOfBitString;
import com.serotonin.bacnet4j.type.notificationParameters.ChangeOfLifeSafety;
import com.serotonin.bacnet4j.type.notificationParameters.ChangeOfState;
import com.serotonin.bacnet4j.type.notificationParameters.ChangeOfValue;
import com.serotonin.bacnet4j.type.notificationParameters.CommandFailure;
import com.serotonin.bacnet4j.type.notificationParameters.Extended;
import com.serotonin.bacnet4j.type.notificationParameters.FloatingLimit;
import com.serotonin.bacnet4j.type.notificationParameters.OutOfRange;
import com.serotonin.bacnet4j.type.notificationParameters.UnsignedRange;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class EventType extends Enumerated {
    public static final EventType changeOfBitstring = new EventType(ChangeOfBitString.TYPE_ID);
    public static final EventType changeOfState = new EventType(ChangeOfState.TYPE_ID);
    public static final EventType changeOfValue = new EventType(ChangeOfValue.TYPE_ID);
    public static final EventType commandFailure = new EventType(CommandFailure.TYPE_ID);
    public static final EventType floatingLimit = new EventType(FloatingLimit.TYPE_ID);
    public static final EventType outOfRange = new EventType(OutOfRange.TYPE_ID);
    public static final EventType changeOfLifeSafety = new EventType(ChangeOfLifeSafety.TYPE_ID);
    public static final EventType extended = new EventType(Extended.TYPE_ID);
    public static final EventType bufferReady = new EventType(BufferReady.TYPE_ID);
    public static final EventType unsignedRange = new EventType(UnsignedRange.TYPE_ID);

    public EventType(int value) {
        super(value);
    }
    
    public EventType(ByteQueue queue) {
        super(queue);
    }
}
