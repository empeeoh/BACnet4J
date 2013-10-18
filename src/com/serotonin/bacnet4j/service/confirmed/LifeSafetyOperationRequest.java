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
package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.LifeSafetyOperation;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class LifeSafetyOperationRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = -4852452672635267599L;

    public static final byte TYPE_ID = 27;

    private final UnsignedInteger requestingProcessIdentifier;
    private final CharacterString requestingSource;
    private final LifeSafetyOperation request;
    private final ObjectIdentifier objectIdentifier;

    public LifeSafetyOperationRequest(UnsignedInteger requestingProcessIdentifier, CharacterString requestingSource,
            LifeSafetyOperation request, ObjectIdentifier objectIdentifier) {
        this.requestingProcessIdentifier = requestingProcessIdentifier;
        this.requestingSource = requestingSource;
        this.request = request;
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, requestingProcessIdentifier, 0);
        write(queue, requestingSource, 1);
        write(queue, request, 2);
        writeOptional(queue, objectIdentifier, 3);
    }

    LifeSafetyOperationRequest(ByteQueue queue) throws BACnetException {
        requestingProcessIdentifier = read(queue, UnsignedInteger.class, 0);
        requestingSource = read(queue, CharacterString.class, 1);
        request = read(queue, LifeSafetyOperation.class, 2);
        objectIdentifier = readOptional(queue, ObjectIdentifier.class, 3);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
        result = PRIME * result + ((request == null) ? 0 : request.hashCode());
        result = PRIME * result + ((requestingProcessIdentifier == null) ? 0 : requestingProcessIdentifier.hashCode());
        result = PRIME * result + ((requestingSource == null) ? 0 : requestingSource.hashCode());
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
        final LifeSafetyOperationRequest other = (LifeSafetyOperationRequest) obj;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        if (request == null) {
            if (other.request != null)
                return false;
        }
        else if (!request.equals(other.request))
            return false;
        if (requestingProcessIdentifier == null) {
            if (other.requestingProcessIdentifier != null)
                return false;
        }
        else if (!requestingProcessIdentifier.equals(other.requestingProcessIdentifier))
            return false;
        if (requestingSource == null) {
            if (other.requestingSource != null)
                return false;
        }
        else if (!requestingSource.equals(other.requestingSource))
            return false;
        return true;
    }
}
