/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class AbortReason extends Enumerated {
    private static final long serialVersionUID = -5845112557505021907L;
    public static final AbortReason other = new AbortReason(0);
    public static final AbortReason bufferOverflow = new AbortReason(1);
    public static final AbortReason invalidApduInThisState = new AbortReason(2);
    public static final AbortReason preemptedByHigherPriorityTask = new AbortReason(3);
    public static final AbortReason segmentationNotSupported = new AbortReason(4);

    public static final AbortReason[] ALL = { other, bufferOverflow, invalidApduInThisState,
            preemptedByHigherPriorityTask, segmentationNotSupported, };

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
        return "Unknown abort reason(" + type + ")";
    }
}
