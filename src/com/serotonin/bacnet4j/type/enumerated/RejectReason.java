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
import org.free.bacnet4j.util.ByteQueue;

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

    public static final RejectReason[] ALL = { other, bufferOverflow, inconsistentParameters, invalidParameterDataType,
            invalidTag, missingRequiredParameter, parameterOutOfRange, tooManyArguments, undefinedEnumeration,
            unrecognizedService, };

    public RejectReason(int value) {
        super(value);
    }

    public RejectReason(ByteQueue queue) {
        super(queue);
    }
}
