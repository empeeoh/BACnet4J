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
package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.DeviceStatus;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyMode;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyState;
import com.serotonin.bacnet4j.type.enumerated.Polarity;
import com.serotonin.bacnet4j.type.enumerated.ProgramError;
import com.serotonin.bacnet4j.type.enumerated.ProgramRequest;
import com.serotonin.bacnet4j.type.enumerated.ProgramState;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class PropertyStates extends BaseType {
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Boolean.class);
        classes.add(BinaryPV.class);
        classes.add(EventType.class);
        classes.add(Polarity.class);
        classes.add(ProgramRequest.class);
        classes.add(ProgramState.class);
        classes.add(ProgramError.class);
        classes.add(Reliability.class);
        classes.add(EventState.class);
        classes.add(DeviceStatus.class);
        classes.add(EngineeringUnits.class);
        classes.add(UnsignedInteger.class);
        classes.add(LifeSafetyMode.class);
        classes.add(LifeSafetyState.class);
    }
    
    public interface Types {
        int BOOLEAN = 0;
        int BINARY_PV = 1;
        int EVENT_TYPE = 2;
        int POLARITY = 3;
        int PROGRAM_REQUEST = 4;
        int PROGRAM_STATE = 5;
        int PROGRAM_ERROR = 6;
        int RELIABILITY = 7;
        int EVENT_STATE = 8;
        int DEVICE_STATUS = 9;
        int ENGINEERING_UNITS = 10;
        int UNSIGNED = 11;
        int LIFE_SAFETY_MODE = 12;
        int LIFE_SAFETY_STATE = 13;
    }
    
    private final Choice state;
    
    public PropertyStates(int type, BaseType state) {
        this.state = new Choice(type, state);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, state);
    }
    
    public PropertyStates(ByteQueue queue) throws BACnetException {
        state = new Choice(queue, classes);
    }
}
