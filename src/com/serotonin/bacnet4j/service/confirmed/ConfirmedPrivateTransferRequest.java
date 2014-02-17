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

import java.util.HashMap;
import java.util.Map;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ConfirmedPrivateTransferRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 621779506703151368L;

    public static final Map<VendorServiceKey, SequenceDefinition> vendorServiceResolutions = new HashMap<VendorServiceKey, SequenceDefinition>();

    public static final byte TYPE_ID = 18;

    private final UnsignedInteger vendorId;
    private final UnsignedInteger serviceNumber;
    private final Encodable serviceParameters;

    public ConfirmedPrivateTransferRequest(final UnsignedInteger vendorId,
    									   final UnsignedInteger serviceNumber,
    									   final Encodable serviceParameters) {
        this.vendorId = vendorId;
        this.serviceNumber = serviceNumber;
        this.serviceParameters = serviceParameters;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService) {
        localDevice.getEventHandler().firePrivateTransfer(vendorId, serviceNumber, serviceParameters);
        return null;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, vendorId, 0);
        write(queue, serviceNumber, 1);
        writeOptional(queue, serviceParameters, 2);
    }

    ConfirmedPrivateTransferRequest(ByteQueue queue) throws BACnetException {
        vendorId = read(queue, UnsignedInteger.class, 0);
        serviceNumber = read(queue, UnsignedInteger.class, 1);
        serviceParameters = readVendorSpecific(queue, vendorId, serviceNumber, vendorServiceResolutions, 2);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((serviceNumber == null) ? 0 : serviceNumber.hashCode());
        result = PRIME * result + ((serviceParameters == null) ? 0 : serviceParameters.hashCode());
        result = PRIME * result + ((vendorId == null) ? 0 : vendorId.hashCode());
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
        final ConfirmedPrivateTransferRequest other = (ConfirmedPrivateTransferRequest) obj;
        if (serviceNumber == null) {
            if (other.serviceNumber != null)
                return false;
        }
        else if (!serviceNumber.equals(other.serviceNumber))
            return false;
        if (serviceParameters == null) {
            if (other.serviceParameters != null)
                return false;
        }
        else if (!serviceParameters.equals(other.serviceParameters))
            return false;
        if (vendorId == null) {
            if (other.vendorId != null)
                return false;
        }
        else if (!vendorId.equals(other.vendorId))
            return false;
        return true;
    }
}
