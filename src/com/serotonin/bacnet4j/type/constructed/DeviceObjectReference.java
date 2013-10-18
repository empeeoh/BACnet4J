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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.free.bacnet4j.util.ByteQueue;

public class DeviceObjectReference extends BaseType {
    private static final long serialVersionUID = -8598474203699924153L;
    private final ObjectIdentifier deviceIdentifier;
    private final ObjectIdentifier objectIdentifier;

    public DeviceObjectReference(ObjectIdentifier deviceIdentifier, ObjectIdentifier objectIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, deviceIdentifier, 0);
        write(queue, objectIdentifier, 1);
    }

    public DeviceObjectReference(ByteQueue queue) throws BACnetException {
        deviceIdentifier = readOptional(queue, ObjectIdentifier.class, 0);
        objectIdentifier = read(queue, ObjectIdentifier.class, 1);
    }

    public ObjectIdentifier getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deviceIdentifier == null) ? 0 : deviceIdentifier.hashCode());
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
        final DeviceObjectReference other = (DeviceObjectReference) obj;
        if (deviceIdentifier == null) {
            if (other.deviceIdentifier != null)
                return false;
        }
        else if (!deviceIdentifier.equals(other.deviceIdentifier))
            return false;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
