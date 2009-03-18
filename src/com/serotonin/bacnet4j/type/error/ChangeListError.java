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
package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ChangeListError extends BaseError {
    private final UnsignedInteger firstFailedElementNumber;
    
    public ChangeListError(byte choice, BACnetError error, UnsignedInteger firstFailedElementNumber) {
        super(choice, error);
        this.firstFailedElementNumber = firstFailedElementNumber;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        write(queue, firstFailedElementNumber, 1);
    }
    
    ChangeListError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        firstFailedElementNumber = read(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((firstFailedElementNumber == null) ? 0 : firstFailedElementNumber.hashCode());
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
        final ChangeListError other = (ChangeListError) obj;
        if (firstFailedElementNumber == null) {
            if (other.firstFailedElementNumber != null)
                return false;
        }
        else if (!firstFailedElementNumber.equals(other.firstFailedElementNumber))
            return false;
        return true;
    }
}
