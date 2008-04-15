package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.WriteAccessSpecification;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.util.queue.ByteQueue;

public class WritePropertyMultipleRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 16;
    
    private SequenceOf<WriteAccessSpecification> listOfWriteAccessSpecifications;
    
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
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        BACnetObject obj;
        for (WriteAccessSpecification spec : listOfWriteAccessSpecifications) {
            obj = localDevice.getObject(spec.getObjectIdentifier());
            if (obj == null)
                throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.unknownObject);
            
            for (PropertyValue pv : spec.getListOfProperties()) {
                try {
                    if (localDevice.getEventHandler().checkAllowPropertyWrite(obj, pv)) {
                        obj.setProperty(pv);
                        localDevice.getEventHandler().propertyWritten(obj, pv);
                    }
                    else
                        throw new BACnetServiceException(ErrorClass.property, ErrorCode.writeAccessDenied);
                }
                catch (BACnetServiceException e) {
                    throw new BACnetErrorException(getChoiceId(), e);
                }
            }
        }
        
        return null;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfWriteAccessSpecifications == null) ? 0 : listOfWriteAccessSpecifications.hashCode());
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
