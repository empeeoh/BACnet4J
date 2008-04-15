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
import com.serotonin.bacnet4j.type.ThreadLocalObjectType;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.error.CreateObjectError;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class CreateObjectRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 10;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(ObjectType.class);
        classes.add(ObjectIdentifier.class);
    }
    
    private Choice objectSpecifier;
    private SequenceOf<PropertyValue> listOfInitialValues;
    
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
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetErrorException {
        ObjectIdentifier id;
        if (objectSpecifier.getContextId() == 0) {
            ObjectType type = (ObjectType)objectSpecifier.getDatum();
            id = localDevice.getNextInstanceObjectIdentifier(type);
        }
        else
            id = (ObjectIdentifier)objectSpecifier.getDatum();
        
        BACnetObject obj = new BACnetObject(localDevice, id);
        
        if (listOfInitialValues != null) {
            for (int i=0; i<listOfInitialValues.getCount(); i++) {
                PropertyValue pv = listOfInitialValues.get(i+1);
                try {
                    obj.setProperty(pv);
                }
                catch (BACnetServiceException e) {
                    throw new BACnetErrorException(new CreateObjectError(getChoiceId(), e, new UnsignedInteger(i+1)));
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
        
        if (objectSpecifier.getContextId() == 0)
            ThreadLocalObjectType.set((ObjectType)objectSpecifier.getDatum());
        else
            ThreadLocalObjectType.set(((ObjectIdentifier)objectSpecifier.getDatum()).getObjectType());
        listOfInitialValues = readOptionalSequenceOf(queue, PropertyValue.class, 1);
        ThreadLocalObjectType.remove();
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
