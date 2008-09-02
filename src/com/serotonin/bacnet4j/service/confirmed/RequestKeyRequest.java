package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class RequestKeyRequest extends ConfirmedRequestService {
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
    public AcknowledgementService handle(LocalDevice localDevice, Address from, Network network)
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
