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

import org.free.bacnet4j.util.ByteQueue;

public class SimpleACK extends AckAPDU {
    private static final long serialVersionUID = -4585349985375271438L;

    public static final byte TYPE_ID = 2;

    /**
     * This parameter shall contain the value of the BACnetConfirmedServiceChoice corresponding to the service contained
     * in the previous BACnet-Confirmed-Service-Request that has resulted in this acknowledgment.
     */
    private final int serviceAckChoice;

    public SimpleACK(byte originalInvokeId, int serviceAckChoice) {
        this.originalInvokeId = originalInvokeId;
        this.serviceAckChoice = serviceAckChoice;
    }

    @Override
    public byte getPduType() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(originalInvokeId);
        queue.push(serviceAckChoice);
    }

    public SimpleACK(ByteQueue queue) {
        queue.pop(); // no news here
        originalInvokeId = queue.pop();
        serviceAckChoice = queue.popU1B();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + originalInvokeId;
        result = PRIME * result + serviceAckChoice;
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
        final SimpleACK other = (SimpleACK) obj;
        if (originalInvokeId != other.originalInvokeId)
            return false;
        if (serviceAckChoice != other.serviceAckChoice)
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
