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
package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import org.free.bacnet4j.util.ByteQueue;

abstract public class EventParameter extends BaseType {
    private static final long serialVersionUID = -8202182792179896645L;

    public static EventParameter createEventParameter(ByteQueue queue) throws BACnetException {
        // Get the first byte. It will tell us what the parameter type is.
        int type = popStart(queue);

        EventParameter result;
        if (type == ChangeOfBitString.TYPE_ID) // 0
            result = new ChangeOfBitString(queue);
        else if (type == ChangeOfState.TYPE_ID) // 1
            result = new ChangeOfState(queue);
        else if (type == ChangeOfValue.TYPE_ID) // 2
            result = new ChangeOfValue(queue);
        else if (type == CommandFailure.TYPE_ID) // 3
            result = new CommandFailure(queue);
        else if (type == FloatingLimit.TYPE_ID) // 4
            result = new FloatingLimit(queue);
        else if (type == OutOfRange.TYPE_ID) // 5
            result = new OutOfRange(queue);
        else if (type == ChangeOfLifeSafety.TYPE_ID) // 8
            result = new ChangeOfLifeSafety(queue);
        else if (type == Extended.TYPE_ID) // 9
            result = new Extended(queue);
        else if (type == BufferReady.TYPE_ID) // 10
            result = new BufferReady(queue);
        else if (type == UnsignedRange.TYPE_ID) // 11
            result = new UnsignedRange(queue);
        else
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidParameterDataType);

        popEnd(queue, type);
        return result;
    }

    @Override
    final public void write(ByteQueue queue) {
        writeContextTag(queue, getTypeId(), true);
        writeImpl(queue);
        writeContextTag(queue, getTypeId(), false);
    }

    abstract protected int getTypeId();

    abstract protected void writeImpl(ByteQueue queue);
}
