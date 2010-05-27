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

public class LifeSafetyOperation extends Enumerated {
    private static final long serialVersionUID = -8453182789389518551L;
    public static final LifeSafetyOperation none = new LifeSafetyOperation(0);
    public static final LifeSafetyOperation silence = new LifeSafetyOperation(1);
    public static final LifeSafetyOperation silenceAudible = new LifeSafetyOperation(2);
    public static final LifeSafetyOperation silenceVisual = new LifeSafetyOperation(3);
    public static final LifeSafetyOperation reset = new LifeSafetyOperation(4);
    public static final LifeSafetyOperation resetAlarm = new LifeSafetyOperation(5);
    public static final LifeSafetyOperation resetFault = new LifeSafetyOperation(6);
    public static final LifeSafetyOperation unsilence = new LifeSafetyOperation(7);
    public static final LifeSafetyOperation unsilenceAudible = new LifeSafetyOperation(8);
    public static final LifeSafetyOperation unsilenceVisual = new LifeSafetyOperation(9);

    public LifeSafetyOperation(int value) {
        super(value);
    }

    public LifeSafetyOperation(ByteQueue queue) {
        super(queue);
    }
}
