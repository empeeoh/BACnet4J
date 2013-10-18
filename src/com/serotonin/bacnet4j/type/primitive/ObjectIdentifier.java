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

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import org.free.bacnet4j.util.ByteQueue;

public class ObjectIdentifier extends Primitive {
    private static final long serialVersionUID = 4171263406246161971L;

    public static final byte TYPE_ID = 12;

    private ObjectType objectType;
    private int instanceNumber;

    public ObjectIdentifier(ObjectType objectType, int instanceNumber) {
        setValues(objectType, instanceNumber);
    }

    private void setValues(ObjectType objectType, int instanceNumber) {
        if (instanceNumber < 0 || instanceNumber > 0x3FFFFF)
            throw new IllegalArgumentException("Illegal instance number: " + instanceNumber);

        this.objectType = objectType;
        this.instanceNumber = instanceNumber;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public int getInstanceNumber() {
        return instanceNumber;
    }

    @Override
    public String toString() {
        return objectType.toString() + " " + instanceNumber;
    }

    //
    // Reading and writing
    //
    public ObjectIdentifier(ByteQueue queue) {
        readTag(queue);

        int objectType = queue.popU1B() << 2;
        int i = queue.popU1B();
        objectType |= i >> 6;

        this.objectType = new ObjectType(objectType);

        instanceNumber = (i & 0x3f) << 16;
        instanceNumber |= queue.popU1B() << 8;
        instanceNumber |= queue.popU1B();
    }

    @Override
    public void writeImpl(ByteQueue queue) {
        int objectType = this.objectType.intValue();
        queue.push(objectType >> 2);
        queue.push(((objectType & 3) << 6) | (instanceNumber >> 16));
        queue.push(instanceNumber >> 8);
        queue.push(instanceNumber);
    }

    @Override
    protected long getLength() {
        return 4;
    }

    @Override
    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + instanceNumber;
        result = PRIME * result + ((objectType == null) ? 0 : objectType.hashCode());
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
        final ObjectIdentifier other = (ObjectIdentifier) obj;
        if (instanceNumber != other.instanceNumber)
            return false;
        if (objectType == null) {
            if (other.objectType != null)
                return false;
        }
        else if (!objectType.equals(other.objectType))
            return false;
        return true;
    }
}
