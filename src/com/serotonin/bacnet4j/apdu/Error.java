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
package com.serotonin.bacnet4j.apdu;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.error.BaseError;
import org.free.bacnet4j.util.ByteQueue;

/**
 * The BACnet-Error-PDU is used to convey the information contained in a service response primitive ('Result(-)') that
 * indicates the reason why a previous confirmed service request failed in its entirety.
 * 
 * @author mlohbihler
 */
public class Error extends AckAPDU {
    private static final long serialVersionUID = 1062847933272895140L;

    public static final byte TYPE_ID = 5;

    /**
     * This parameter, of type BACnet-Error, indicates the reason the indicated service request could not be carried
     * out. This parameter shall be encoded according to the rules of 20.2.
     */
    private final BaseError error;

    public Error(byte originalInvokeId, BaseError error) {
        this.originalInvokeId = originalInvokeId;
        this.error = error;
    }

    @Override
    public byte getPduType() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(originalInvokeId);
        error.write(queue);
    }

    Error(ByteQueue queue) throws BACnetException {
        queue.pop(); // Ignore the first byte. No news there.
        originalInvokeId = queue.pop();
        error = BaseError.createBaseError(queue);
    }

    @Override
    public String toString() {
        return "ErrorAPDU(" + error + ")";
    }

    public BaseError getError() {
        return error;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((error == null) ? 0 : error.hashCode());
        result = PRIME * result + originalInvokeId;
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
        final Error other = (Error) obj;
        if (error == null) {
            if (other.error != null)
                return false;
        }
        else if (!error.equals(other.error))
            return false;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
