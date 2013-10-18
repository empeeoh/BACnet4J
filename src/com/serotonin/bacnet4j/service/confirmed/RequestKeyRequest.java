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
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

public class RequestKeyRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 6550270345275044608L;

    public static final byte TYPE_ID = 25;

    private final ObjectIdentifier requestingDeviceIdentifier;
    private final Address requestingDeviceAddress;
    private final ObjectIdentifier remoteDeviceIdentifier;
    private final Address remoteDeviceAddress;

    public RequestKeyRequest(ObjectIdentifier requestingDeviceIdentifier, Address requestingDeviceAddress,
            ObjectIdentifier remoteDeviceIdentifier, Address remoteDeviceAddress) {
        this.requestingDeviceIdentifier = requestingDeviceIdentifier;
        this.requestingDeviceAddress = requestingDeviceAddress;
        this.remoteDeviceIdentifier = remoteDeviceIdentifier;
        this.remoteDeviceAddress = remoteDeviceAddress;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, requestingDeviceIdentifier);
        write(queue, requestingDeviceAddress);
        write(queue, remoteDeviceIdentifier);
        write(queue, remoteDeviceAddress);
    }

    RequestKeyRequest(ByteQueue queue) throws BACnetException {
        requestingDeviceIdentifier = read(queue, ObjectIdentifier.class);
        requestingDeviceAddress = read(queue, Address.class);
        remoteDeviceIdentifier = read(queue, ObjectIdentifier.class);
        remoteDeviceAddress = read(queue, Address.class);
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((remoteDeviceAddress == null) ? 0 : remoteDeviceAddress.hashCode());
        result = PRIME * result + ((remoteDeviceIdentifier == null) ? 0 : remoteDeviceIdentifier.hashCode());
        result = PRIME * result + ((requestingDeviceAddress == null) ? 0 : requestingDeviceAddress.hashCode());
        result = PRIME * result + ((requestingDeviceIdentifier == null) ? 0 : requestingDeviceIdentifier.hashCode());
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
        final RequestKeyRequest other = (RequestKeyRequest) obj;
        if (remoteDeviceAddress == null) {
            if (other.remoteDeviceAddress != null)
                return false;
        }
        else if (!remoteDeviceAddress.equals(other.remoteDeviceAddress))
            return false;
        if (remoteDeviceIdentifier == null) {
            if (other.remoteDeviceIdentifier != null)
                return false;
        }
        else if (!remoteDeviceIdentifier.equals(other.remoteDeviceIdentifier))
            return false;
        if (requestingDeviceAddress == null) {
            if (other.requestingDeviceAddress != null)
                return false;
        }
        else if (!requestingDeviceAddress.equals(other.requestingDeviceAddress))
            return false;
        if (requestingDeviceIdentifier == null) {
            if (other.requestingDeviceIdentifier != null)
                return false;
        }
        else if (!requestingDeviceIdentifier.equals(other.requestingDeviceIdentifier))
            return false;
        return true;
    }
}
