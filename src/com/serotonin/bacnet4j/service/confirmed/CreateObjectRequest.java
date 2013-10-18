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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.CreateObjectAck;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.ThreadLocalObjectTypeStack;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.error.CreateObjectError;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class CreateObjectRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = -610206284148696878L;

    public static final byte TYPE_ID = 10;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(ObjectType.class);
        classes.add(ObjectIdentifier.class);
    }

    private final Choice objectSpecifier;
    private final SequenceOf<PropertyValue> listOfInitialValues;

    public CreateObjectRequest(ObjectType objectType, SequenceOf<PropertyValue> listOfInitialValues) {
        objectSpecifier = new Choice(0, objectType);
        this.listOfInitialValues = listOfInitialValues;
    }

    public CreateObjectRequest(ObjectIdentifier objectIdentifier, SequenceOf<PropertyValue> listOfInitialValues) {
        objectSpecifier = new Choice(1, objectIdentifier);
        this.listOfInitialValues = listOfInitialValues;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetErrorException {
        final ObjectIdentifier id;
        if (objectSpecifier.getContextId() == 0) {
            ObjectType type = (ObjectType) objectSpecifier.getDatum();
            id = localDevice.getNextInstanceObjectIdentifier(type);
        }
        else
            id = (ObjectIdentifier) objectSpecifier.getDatum();

        BACnetObject obj = new BACnetObject(localDevice, id);

        if (listOfInitialValues != null) {
            for (int i = 0; i < listOfInitialValues.getCount(); i++) {
                PropertyValue pv = listOfInitialValues.get(i + 1);
                try {
                    obj.setProperty(pv);
                }
                catch (BACnetServiceException e) {
                    throw new BACnetErrorException(new CreateObjectError(getChoiceId(), e, new UnsignedInteger(i + 1)));
                }
            }
        }

        try {
            localDevice.addObject(obj);
        }
        catch (BACnetServiceException e) {
            throw new BACnetErrorException(new CreateObjectError(getChoiceId(), e, null));
        }

        // Return a create object ack.
        return new CreateObjectAck(id);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, objectSpecifier, 0);
        writeOptional(queue, listOfInitialValues, 1);
    }

    CreateObjectRequest(ByteQueue queue) throws BACnetException {
        popStart(queue, 0);
        objectSpecifier = new Choice(queue, classes);
        popEnd(queue, 0);

        try {
            if (objectSpecifier.getContextId() == 0)
                ThreadLocalObjectTypeStack.set((ObjectType) objectSpecifier.getDatum());
            else
                ThreadLocalObjectTypeStack.set(((ObjectIdentifier) objectSpecifier.getDatum()).getObjectType());
            listOfInitialValues = readOptionalSequenceOf(queue, PropertyValue.class, 1);
        }
        finally {
            ThreadLocalObjectTypeStack.remove();
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfInitialValues == null) ? 0 : listOfInitialValues.hashCode());
        result = PRIME * result + ((objectSpecifier == null) ? 0 : objectSpecifier.hashCode());
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
        final CreateObjectRequest other = (CreateObjectRequest) obj;
        if (listOfInitialValues == null) {
            if (other.listOfInitialValues != null)
                return false;
        }
        else if (!listOfInitialValues.equals(other.listOfInitialValues))
            return false;
        if (objectSpecifier == null) {
            if (other.objectSpecifier != null)
                return false;
        }
        else if (!objectSpecifier.equals(other.objectSpecifier))
            return false;
        return true;
    }
}
