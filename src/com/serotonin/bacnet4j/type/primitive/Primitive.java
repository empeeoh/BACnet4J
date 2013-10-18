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
package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import org.free.bacnet4j.util.ByteQueue;

abstract public class Primitive extends Encodable {
    private static final long serialVersionUID = 611651273642455709L;

    public static Primitive createPrimitive(ByteQueue queue) throws BACnetErrorException {
        // Get the first byte. The 4 high-order bits will tell us what the data type is.
        byte type = queue.peek(0);
        type = (byte) ((type & 0xff) >> 4);
        return createPrimitive(type, queue);
    }

    public static Primitive createPrimitive(ByteQueue queue, int contextId, int typeId) throws BACnetErrorException {
        int tagNumber = peekTagNumber(queue);

        // Check if the tag number matches the context id. If they match, then create the context-specific parameter,
        // otherwise return null.
        if (tagNumber != contextId)
            return null;

        return createPrimitive(typeId, queue);
    }

    private static Primitive createPrimitive(int typeId, ByteQueue queue) throws BACnetErrorException {
        if (typeId == Null.TYPE_ID)
            return new Null(queue);
        if (typeId == Boolean.TYPE_ID)
            return new Boolean(queue);
        if (typeId == UnsignedInteger.TYPE_ID)
            return new UnsignedInteger(queue);
        if (typeId == SignedInteger.TYPE_ID)
            return new SignedInteger(queue);
        if (typeId == Real.TYPE_ID)
            return new Real(queue);
        if (typeId == Double.TYPE_ID)
            return new Double(queue);
        if (typeId == OctetString.TYPE_ID)
            return new OctetString(queue);
        if (typeId == CharacterString.TYPE_ID)
            return new CharacterString(queue);
        if (typeId == BitString.TYPE_ID)
            return new BitString(queue);
        if (typeId == Enumerated.TYPE_ID)
            return new Enumerated(queue);
        if (typeId == Date.TYPE_ID)
            return new Date(queue);
        if (typeId == Time.TYPE_ID)
            return new Time(queue);
        if (typeId == ObjectIdentifier.TYPE_ID)
            return new ObjectIdentifier(queue);

        throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidParameterDataType);
    }

    /**
     * This field is maintained specifically for boolean types, since their encoding differs depending on whether the
     * type is context specific or not.
     */
    protected boolean contextSpecific;

    @Override
    final public void write(ByteQueue queue) {
        writeTag(queue, getTypeId(), false, getLength());
        writeImpl(queue);
    }

    @Override
    final public void write(ByteQueue queue, int contextId) {
        contextSpecific = true;
        writeTag(queue, contextId, true, getLength());
        writeImpl(queue);
    }

    final public void writeEncodable(ByteQueue queue, int contextId) {
        writeContextTag(queue, contextId, true);
        write(queue);
        writeContextTag(queue, contextId, false);
    }

    abstract protected void writeImpl(ByteQueue queue);

    abstract protected long getLength();

    abstract protected byte getTypeId();

    private void writeTag(ByteQueue queue, int tagNumber, boolean classTag, long length) {
        int classValue = classTag ? 8 : 0;

        if (length < 0 || length > 0x100000000l)
            throw new IllegalArgumentException("Invalid length: " + length);

        boolean extendedTag = tagNumber > 14;

        if (length < 5) {
            if (extendedTag) {
                queue.push(0xf0 | classValue | length);
                queue.push(tagNumber);
            }
            else
                queue.push((tagNumber << 4) | classValue | length);
        }
        else {
            if (extendedTag) {
                queue.push(0xf5 | classValue);
                queue.push(tagNumber);
            }
            else
                queue.push((tagNumber << 4) | classValue | 0x5);

            if (length < 254)
                queue.push(length);
            else if (length < 65536) {
                queue.push(254);
                BACnetUtils.pushShort(queue, length);
            }
            else {
                queue.push(255);
                BACnetUtils.pushInt(queue, length);
            }
        }
    }

    protected long readTag(ByteQueue queue) {
        byte b = queue.pop();
        int tagNumber = (b & 0xff) >> 4;
        contextSpecific = (b & 8) != 0;
        long length = (b & 7);

        if (tagNumber == 0xf)
            // Extended tag.
            tagNumber = queue.popU1B();

        if (length == 5) {
            length = queue.popU1B();
            if (length == 254)
                length = queue.popU2B();
            else if (length == 255)
                length = queue.popU4B();
        }

        return length;
    }
}
