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

public class RejectReason extends Enumerated {
    private static final long serialVersionUID = 3672606740809550085L;
    public static final RejectReason other = new RejectReason(0);
    public static final RejectReason bufferOverflow = new RejectReason(1);
    public static final RejectReason inconsistentParameters = new RejectReason(2);
    public static final RejectReason invalidParameterDataType = new RejectReason(3);
    public static final RejectReason invalidTag = new RejectReason(4);
    public static final RejectReason missingRequiredParameter = new RejectReason(5);
    public static final RejectReason parameterOutOfRange = new RejectReason(6);
    public static final RejectReason tooManyArguments = new RejectReason(7);
    public static final RejectReason undefinedEnumeration = new RejectReason(8);
    public static final RejectReason unrecognizedService = new RejectReason(9);

    public RejectReason(int value) {
        super(value);
    }

    public RejectReason(ByteQueue queue) {
        super(queue);
    }
}
