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

import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class EventTransitionBits extends BitString {
    public EventTransitionBits(boolean toOffnormal, boolean toFault, boolean toNormal) {
        super(new boolean[] {toOffnormal, toFault, toNormal});
    }

    public EventTransitionBits(ByteQueue queue) {
        super(queue);
    }

    public boolean isToOffnormal() {
        return getValue()[0];
    }
    
    public boolean isToFault() {
        return getValue()[1];
    }
    
    public boolean isToNormal() {
        return getValue()[2];
    }
    
    public boolean contains(EventState toState) {
        if (toState.equals(EventState.normal) && isToNormal())
            return true;
        
        if (toState.equals(EventState.fault) && isToFault())
            return true;
        
        // All other event states are considered off-normal
        return isToOffnormal();
    }
}
