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
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.util.queue.ByteQueue;

public class WritePropertyMultipleError extends BaseError {
    private final ObjectPropertyReference firstFailedWriteAttempt;
    
    public WritePropertyMultipleError(byte choice, BACnetError error, ObjectPropertyReference firstFailedWriteAttempt) {
        super(choice, error);
        this.firstFailedWriteAttempt = firstFailedWriteAttempt;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        firstFailedWriteAttempt.write(queue, 1);
    }
    
    WritePropertyMultipleError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        firstFailedWriteAttempt = read(queue, ObjectPropertyReference.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((firstFailedWriteAttempt == null) ? 0 : firstFailedWriteAttempt.hashCode());
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
        final WritePropertyMultipleError other = (WritePropertyMultipleError) obj;
        if (firstFailedWriteAttempt == null) {
            if (other.firstFailedWriteAttempt != null)
                return false;
        }
        else if (!firstFailedWriteAttempt.equals(other.firstFailedWriteAttempt))
            return false;
        return true;
    }
}
