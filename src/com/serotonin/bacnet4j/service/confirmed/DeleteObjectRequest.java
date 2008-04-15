package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class DeleteObjectRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 11;
    
    private ObjectIdentifier objectIdentifier;
    
    public DeleteObjectRequest(ObjectIdentifier objectIdentifier) {
        this.objectIdentifier = objectIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetErrorException {
        try {
            localDevice.removeObject(objectIdentifier);
        }
        catch (BACnetServiceException e) {
            throw new BACnetErrorException(getChoiceId(), e);
        }
            
        // Returning null sends a simple ack.
        return null;
    }
    
    @Override
    public void write(ByteQueue queue) {
        write(queue, objectIdentifier);
    }
    
    DeleteObjectRequest(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
        final DeleteObjectRequest other = (DeleteObjectRequest) obj;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
