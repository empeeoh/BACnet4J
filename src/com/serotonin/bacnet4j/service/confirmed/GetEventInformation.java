package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class GetEventInformation extends ConfirmedRequestService {
    public static final byte TYPE_ID = 29;
    
    private final ObjectIdentifier lastReceivedObjectIdentifier;
    
    public GetEventInformation(ObjectIdentifier lastReceivedObjectIdentifier) {
        this.lastReceivedObjectIdentifier = lastReceivedObjectIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, Network network)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, lastReceivedObjectIdentifier, 0);
    }
    
    GetEventInformation(ByteQueue queue) throws BACnetException {
        lastReceivedObjectIdentifier = readOptional(queue, ObjectIdentifier.class, 0);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((lastReceivedObjectIdentifier == null) ? 0 : lastReceivedObjectIdentifier.hashCode());
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
        final GetEventInformation other = (GetEventInformation) obj;
        if (lastReceivedObjectIdentifier == null) {
            if (other.lastReceivedObjectIdentifier != null)
                return false;
        }
        else if (!lastReceivedObjectIdentifier.equals(other.lastReceivedObjectIdentifier))
            return false;
        return true;
    }
}
