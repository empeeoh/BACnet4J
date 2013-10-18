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
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import org.free.bacnet4j.util.ByteQueue;

public class UnconfirmedRequest extends APDU {
    private static final long serialVersionUID = 1606568334137370062L;

    public static final byte TYPE_ID = 1;

    /**
     * This parameter shall contain the parameters of the specific service that is being requested, encoded according to
     * the rules of 20.2. These parameters are defined in the individual service descriptions in this standard and are
     * represented in Clause 21 in accordance with the rules of ASN.1.
     */
    private final UnconfirmedRequestService service;

    public UnconfirmedRequest(UnconfirmedRequestService service) {
        this.service = service;
    }

    @Override
    public byte getPduType() {
        return TYPE_ID;
    }

    public UnconfirmedRequestService getService() {
        return service;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(getShiftedTypeId(TYPE_ID));
        queue.push(service.getChoiceId());
        service.write(queue);
    }

    public UnconfirmedRequest(ServicesSupported services, ByteQueue queue) throws BACnetException {
        queue.pop();
        byte choiceId = queue.pop();
        service = UnconfirmedRequestService.createUnconfirmedRequestService(services, choiceId, queue);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((service == null) ? 0 : service.hashCode());
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
        final UnconfirmedRequest other = (UnconfirmedRequest) obj;
        if (service == null) {
            if (other.service != null)
                return false;
        }
        else if (!service.equals(other.service))
            return false;
        return true;
    }

    @Override
    public boolean expectsReply() {
        return false;
    }
}
