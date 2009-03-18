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

public class AbortReason extends Enumerated {
    public static final AbortReason other = new AbortReason(0);
    public static final AbortReason bufferOverflow = new AbortReason(1);
    public static final AbortReason invalidApduInThisState = new AbortReason(2);
    public static final AbortReason preemptedByHigherPriorityTask = new AbortReason(3);
    public static final AbortReason segmentationNotSupported = new AbortReason(4);

    public AbortReason(int value) {
        super(value);
    }
    
    public AbortReason(ByteQueue queue) {
        super(queue);
    }
    
    @Override
    public String toString() {
        int type = intValue();
        if (type == other.intValue())
            return "Other";
        if (type == bufferOverflow.intValue())
            return "Buffer overflow";
        if (type == invalidApduInThisState.intValue())
            return "Invalid APDU in this state";
        if (type == preemptedByHigherPriorityTask.intValue())
            return "Preempted by higher priority task";
        if (type == segmentationNotSupported.intValue())
            return "Segmentation not supported";
        return "Unknown abort reason("+ type +")";
    }
}
