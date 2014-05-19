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

import java.util.HashMap;
import java.util.Map;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ConfirmedPrivateTransferError extends BaseError {
    private static final long serialVersionUID = -4736829685989649711L;

    public static final Map<VendorServiceKey, SequenceDefinition> vendorServiceResolutions = new HashMap<VendorServiceKey, SequenceDefinition>();

    private final UnsignedInteger vendorId;
    private final UnsignedInteger serviceNumber;
    private final Encodable errorParameters;

    public ConfirmedPrivateTransferError(byte choice, BACnetError error, UnsignedInteger vendorId,
            UnsignedInteger serviceNumber, BaseType errorParameters) {
        super(choice, error);
        this.vendorId = vendorId;
        this.serviceNumber = serviceNumber;
        this.errorParameters = errorParameters;
    }

    @Override
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error, 0);
        write(queue, vendorId, 1);
        write(queue, serviceNumber, 2);
        writeOptional(queue, errorParameters, 3);
    }

    ConfirmedPrivateTransferError(byte choice, ByteQueue queue) throws BACnetException {
        super(choice, queue, 0);
        vendorId = read(queue, UnsignedInteger.class, 1);
        serviceNumber = read(queue, UnsignedInteger.class, 2);
        errorParameters = readVendorSpecific(queue, vendorId, serviceNumber, vendorServiceResolutions, 3);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + ((errorParameters == null) ? 0 : errorParameters.hashCode());
        result = PRIME * result + ((serviceNumber == null) ? 0 : serviceNumber.hashCode());
        result = PRIME * result + ((vendorId == null) ? 0 : vendorId.hashCode());
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
        final ConfirmedPrivateTransferError other = (ConfirmedPrivateTransferError) obj;
        if (errorParameters == null) {
            if (other.errorParameters != null)
                return false;
        }
        else if (!errorParameters.equals(other.errorParameters))
            return false;
        if (serviceNumber == null) {
            if (other.serviceNumber != null)
                return false;
        }
        else if (!serviceNumber.equals(other.serviceNumber))
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
