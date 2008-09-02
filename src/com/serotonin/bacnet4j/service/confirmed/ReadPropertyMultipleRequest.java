package com.serotonin.bacnet4j.service.confirmed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyMultipleAck;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult.Result;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ReadPropertyMultipleRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 14;
    
    private final SequenceOf<ReadAccessSpecification> listOfReadAccessSpecs;
    
    public ReadPropertyMultipleRequest(SequenceOf<ReadAccessSpecification> listOfReadAccessSpecs) {
        this.listOfReadAccessSpecs = listOfReadAccessSpecs;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public void write(ByteQueue queue) {
        write(queue, listOfReadAccessSpecs);
    }
    
    ReadPropertyMultipleRequest(ByteQueue queue) throws BACnetException {
        listOfReadAccessSpecs = readSequenceOf(queue, ReadAccessSpecification.class);
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, Network network)
            throws BACnetException {
        BACnetObject obj;
        ObjectIdentifier oid;
        List<ReadAccessResult> readAccessResults = new ArrayList<ReadAccessResult>();
        List<Result> results;
        
        try {
            for (ReadAccessSpecification req : listOfReadAccessSpecs) {
                results = new ArrayList<Result>();
                oid = req.getObjectIdentifier();
                obj = localDevice.getObjectRequired(oid);
                
                for (PropertyReference propRef : req.getListOfPropertyReferences())
                    addProperty(obj, results, propRef.getPropertyIdentifier(), propRef.getPropertyArrayIndex(), true);
                
                readAccessResults.add(new ReadAccessResult(oid, new SequenceOf<Result>(results)));
            }
        }
        catch (BACnetServiceException e) {
            throw new BACnetErrorException(getChoiceId(), e);
        }
        
        return new ReadPropertyMultipleAck(new SequenceOf<ReadAccessResult>(readAccessResults));
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfReadAccessSpecs == null) ? 0 : listOfReadAccessSpecs.hashCode());
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
        final ReadPropertyMultipleRequest other = (ReadPropertyMultipleRequest) obj;
        if (listOfReadAccessSpecs == null) {
            if (other.listOfReadAccessSpecs != null)
                return false;
        }
        else if (!listOfReadAccessSpecs.equals(other.listOfReadAccessSpecs))
            return false;
        return true;
    }
    
    private void addProperty(BACnetObject obj, List<Result> results, PropertyIdentifier pid, UnsignedInteger pin,
            boolean valueRequired) {
        if (pid.intValue() == PropertyIdentifier.all.intValue()) {
            for (PropertyTypeDefinition def : ObjectProperties.getPropertyTypeDefinitions(obj.getId().getObjectType()))
                addProperty(obj, results, def.getPropertyIdentifier(), pin, def.isRequired());
        }
        else if (pid.intValue() == PropertyIdentifier.required.intValue()) {
            for (PropertyTypeDefinition def : ObjectProperties.getRequiredPropertyTypeDefinitions(
                    obj.getId().getObjectType()))
                addProperty(obj, results, def.getPropertyIdentifier(), pin, true);
        }
        else if (pid.intValue() == PropertyIdentifier.optional.intValue()) {
            for (PropertyTypeDefinition def : ObjectProperties.getOptionalPropertyTypeDefinitions(
                    obj.getId().getObjectType()))
                addProperty(obj, results, def.getPropertyIdentifier(), pin, false);
        }
        else {
            // Get the specified property.
            try {
                if (valueRequired)
                    results.add(new Result(pid, pin, obj.getPropertyRequired(pid, pin)));
                else {
                    Encodable prop = obj.getProperty(pid, pin);
                    if (prop != null)
                        results.add(new Result(pid, pin, prop));
                }
            }
            catch (BACnetServiceException e) {
                results.add(new Result(pid, pin, new BACnetError(e.getErrorClass(), e.getErrorCode())));
            }
        }
    }
}
