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
import org.free.bacnet4j.util.ByteQueue;

public class SetpointReference extends BaseType {
    private static final long serialVersionUID = 6454996310502957318L;
    private final ObjectPropertyReference setpointReference;

    public SetpointReference(ObjectPropertyReference setpointReference) {
        this.setpointReference = setpointReference;
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, setpointReference, 0);
    }

    public SetpointReference(ByteQueue queue) throws BACnetException {
        setpointReference = readOptional(queue, ObjectPropertyReference.class, 0);
    }

    public ObjectPropertyReference getSetpointReference() {
        return setpointReference;
    }

    @Override
    public String toString() {
        return "SetpointReference(setpointReference=" + setpointReference + ")";
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((setpointReference == null) ? 0 : setpointReference.hashCode());
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
        final SetpointReference other = (SetpointReference) obj;
        if (setpointReference == null) {
            if (other.setpointReference != null)
                return false;
        }
        else if (!setpointReference.equals(other.setpointReference))
            return false;
        return true;
    }
}
