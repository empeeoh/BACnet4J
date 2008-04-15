package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class AddressBinding extends BaseType {
    private ObjectIdentifier deviceObjectIdentifier;
    private Address deviceAddress;
    
    public AddressBinding(ObjectIdentifier deviceObjectIdentifier, Address deviceAddress) {
        this.deviceObjectIdentifier = deviceObjectIdentifier;
        this.deviceAddress = deviceAddress;
    }

    public void write(ByteQueue queue) {
        write(queue, deviceObjectIdentifier);
        write(queue, deviceAddress);
    }
    
    public AddressBinding(ByteQueue queue) throws BACnetException {
        deviceObjectIdentifier = read(queue, ObjectIdentifier.class);
        deviceAddress = read(queue, Address.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deviceAddress == null) ? 0 : deviceAddress.hashCode());
        result = PRIME * result + ((deviceObjectIdentifier == null) ? 0 : deviceObjectIdentifier.hashCode());
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
        final AddressBinding other = (AddressBinding) obj;
        if (deviceAddress == null) {
            if (other.deviceAddress != null)
                return false;
        }
        else if (!deviceAddress.equals(other.deviceAddress))
            return false;
        if (deviceObjectIdentifier == null) {
            if (other.deviceObjectIdentifier != null)
                return false;
        }
        else if (!deviceObjectIdentifier.equals(other.deviceObjectIdentifier))
            return false;
        return true;
    }
}
