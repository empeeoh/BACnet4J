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
package com.serotonin.bacnet4j.type.primitive;

import java.util.Arrays;

import com.serotonin.util.ArrayUtils;
import com.serotonin.util.queue.ByteQueue;

public class OctetString extends Primitive {
    private static final long serialVersionUID = -3557657941142811228L;

    public static final byte TYPE_ID = 6;

    private final byte[] value;

    public OctetString(byte[] value) {
        this.value = value;
    }

    public byte[] getBytes() {
        return value;
    }

    //
    // Reading and writing
    //
    public OctetString(ByteQueue queue) {
        int length = (int) readTag(queue);
        value = new byte[length];
        queue.pop(value);
    }

    @Override
    public void writeImpl(ByteQueue queue) {
        queue.push(value);
    }

    @Override
    public long getLength() {
        return value.length;
    }

    @Override
    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OctetString other = (OctetString) obj;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ArrayUtils.toHexString(value);
    }
}
