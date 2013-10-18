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
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.WriteAccessSpecification;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.WritePropertyMultipleError;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

public class WritePropertyMultipleRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 4702397545138383955L;

    public static final byte TYPE_ID = 16;

    private final SequenceOf<WriteAccessSpecification> listOfWriteAccessSpecifications;

    public WritePropertyMultipleRequest(SequenceOf<WriteAccessSpecification> listOfWriteAccessSpecifications) {
        this.listOfWriteAccessSpecifications = listOfWriteAccessSpecifications;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, listOfWriteAccessSpecifications);
    }

    WritePropertyMultipleRequest(ByteQueue queue) throws BACnetException {
        listOfWriteAccessSpecifications = readSequenceOf(queue, WriteAccessSpecification.class);
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        BACnetObject obj;
        for (WriteAccessSpecification spec : listOfWriteAccessSpecifications) {
            obj = localDevice.getObject(spec.getObjectIdentifier());
            if (obj == null)
                throw createException(ErrorClass.property, ErrorCode.writeAccessDenied, spec, null);

            for (PropertyValue pv : spec.getListOfProperties()) {
                try {
                    if (localDevice.getEventHandler().checkAllowPropertyWrite(obj, pv)) {
                        obj.setProperty(pv);
                        localDevice.getEventHandler().propertyWritten(obj, pv);
                    }
                    else
                        throw createException(ErrorClass.property, ErrorCode.writeAccessDenied, spec, pv);
                }
                catch (BACnetServiceException e) {
                    throw createException(e.getErrorClass(), e.getErrorCode(), spec, pv);
                }
            }
        }

        return null;
    }

    private BACnetErrorException createException(ErrorClass errorClass, ErrorCode errorCode,
            WriteAccessSpecification spec, PropertyValue pv) {
        if (pv == null)
            pv = spec.getListOfProperties().get(1);
        return new BACnetErrorException(new WritePropertyMultipleError(getChoiceId(), new BACnetError(errorClass,
                errorCode), new ObjectPropertyReference(spec.getObjectIdentifier(), pv.getPropertyIdentifier(),
                pv.getPropertyArrayIndex())));
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result
                + ((listOfWriteAccessSpecifications == null) ? 0 : listOfWriteAccessSpecifications.hashCode());
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
        final WritePropertyMultipleRequest other = (WritePropertyMultipleRequest) obj;
        if (listOfWriteAccessSpecifications == null) {
            if (other.listOfWriteAccessSpecifications != null)
                return false;
        }
        else if (!listOfWriteAccessSpecifications.equals(other.listOfWriteAccessSpecifications))
            return false;
        return true;
    }
}
