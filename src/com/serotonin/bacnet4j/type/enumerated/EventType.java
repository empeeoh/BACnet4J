/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
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
