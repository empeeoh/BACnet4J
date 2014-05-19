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
package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class VTCloseError extends BaseError {
    private static final long serialVersionUID = 7817379391677615141L;
    private final SequenceOf<UnsignedInteger> listOfVTSessionIdentifiers;

    public VTCloseError(byte choice, BACnetError error, SequenceOf<UnsignedInteger> listOfVTSessionIdentifiers) {
        super(choice, error);
        this.listOfVTSessionIdentifiers = listOfVTSessionIdentifiers;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        writeOptional(queue, listOfVTSessionIdentifiers, 1);
    }

    VTCloseError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        listOfVTSessionIdentifiers = readOptionalSequenceOf(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((listOfVTSessionIdentifiers == null) ? 0 : listOfVTSessionIdentifiers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VTCloseError other = (VTCloseError) obj;
        if (listOfVTSessionIdentifiers == null) {
            if (other.listOfVTSessionIdentifiers != null)
                return false;
        }
        else if (!listOfVTSessionIdentifiers.equals(other.listOfVTSessionIdentifiers))
            return false;
        return true;
    }
}
