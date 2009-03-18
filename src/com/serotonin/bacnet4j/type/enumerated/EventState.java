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

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class EventState extends Enumerated {
    public static final EventState normal = new EventState(0);
    public static final EventState fault = new EventState(1);
    public static final EventState offnormal = new EventState(2);
    public static final EventState highLimit = new EventState(3);
    public static final EventState lowLimit = new EventState(4);
    public static final EventState lifeSafetyAlarm = new EventState(5);

    public EventState(int value) {
        super(value);
    }
    
    public EventState(ByteQueue queue) {
        super(queue);
    }
    
    @Override
    public String toString() {
        if (intValue() == 0)
            return "normal";
        if (intValue() == 1)
            return "fault";
        if (intValue() == 2)
            return "off normal";
        if (intValue() == 3)
            return "high limit";
        if (intValue() == 4)
            return "low limit";
        if (intValue() == 5)
            return "life safety alarm";
        return "Unknown";
    }
}
