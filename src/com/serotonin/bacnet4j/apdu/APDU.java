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
 */
package com.serotonin.bacnet4j.apdu;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.IllegalPduTypeException;
import com.serotonin.util.queue.ByteQueue;

abstract public class APDU {
    public static APDU createAPDU(ByteQueue queue) throws BACnetException {
        // Get the first byte. The 4 high-order bits will tell us the type of PDU this is.
        byte type = queue.peek(0);
        type = (byte) ((type & 0xff) >> 4);

        if (type == ConfirmedRequest.TYPE_ID)
            return new ConfirmedRequest(queue);
        if (type == UnconfirmedRequest.TYPE_ID)
            return new UnconfirmedRequest(queue);
        if (type == SimpleACK.TYPE_ID)
            return new SimpleACK(queue);
        if (type == ComplexACK.TYPE_ID)
            return new ComplexACK(queue);
        if (type == SegmentACK.TYPE_ID)
            return new SegmentACK(queue);
        if (type == Error.TYPE_ID)
            return new Error(queue);
        if (type == Reject.TYPE_ID)
            return new Reject(queue);
        if (type == Abort.TYPE_ID)
            return new Abort(queue);
        throw new IllegalPduTypeException(Byte.toString(type));
    }

    abstract public byte getPduType();

    abstract public void write(ByteQueue queue);

    protected int getShiftedTypeId(byte typeId) {
        return typeId << 4;
    }

    abstract public boolean expectsReply();
}
