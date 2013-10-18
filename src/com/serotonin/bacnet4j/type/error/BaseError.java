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
import com.serotonin.bacnet4j.type.constructed.BaseType;
import org.free.bacnet4j.util.ByteQueue;

public class BaseError extends BaseType {
    private static final long serialVersionUID = 8363160647986011176L;

    public static BaseError createBaseError(ByteQueue queue) throws BACnetException {
        byte choice = queue.pop();

        switch (choice) {
        case 8:
        case 9:
            return new ChangeListError(choice, queue);
        case 10:
            return new CreateObjectError(choice, queue);
        case 16:
            return new WritePropertyMultipleError(choice, queue);
        case 18:
            return new ConfirmedPrivateTransferError(choice, queue);
        case 22:
            return new VTCloseError(choice, queue);
        }
        return new BaseError(choice, queue);
    }

    protected byte choice;
    protected BACnetError error;

    public BaseError(byte choice, BACnetError error) {
        this.choice = choice;
        this.error = error;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error);
    }

    public BaseError(byte choice, ByteQueue queue) throws BACnetException {
        this.choice = choice;
        error = read(queue, BACnetError.class);
    }

    public BaseError(byte choice, ByteQueue queue, int contextId) throws BACnetException {
        this.choice = choice;
        error = read(queue, BACnetError.class, contextId);
    }

    @Override
    public String toString() {
        return "choice=" + (choice & 0xff) + ", " + error;
    }

    public BACnetError getError() {
        return error;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + choice;
        result = PRIME * result + ((error == null) ? 0 : error.hashCode());
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
        final BaseError other = (BaseError) obj;
        if (choice != other.choice)
            return false;
        if (error == null) {
            if (other.error != null)
                return false;
        }
        else if (!error.equals(other.error))
            return false;
        return true;
    }
}
