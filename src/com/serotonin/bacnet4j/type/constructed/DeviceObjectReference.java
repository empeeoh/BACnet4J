package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.util.queue.ByteQueue;

public class DeviceObjectReference extends BaseType {
    private ObjectIdentifier deviceIdentifier;
    private ObjectIdentifier objectIdentifier;
    
    public DeviceObjectReference(ObjectIdentifier deviceIdentifier, ObjectIdentifier objectIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
        this.objectIdentifier = objectIdentifier;
    }
    
    public void write(ByteQueue queue) {
        writeOptional(queue, deviceIdentifier, 0);
        write(queue, objectIdentifier, 1);
    }
    
    public DeviceObjectReference(ByteQueue queue) throws BACnetException {
        deviceIdentifier = readOptional(queue, ObjectIdentifier.class, 0);
        objectIdentifier = read(queue, ObjectIdentifier.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((deviceIdentifier == null) ? 0 : deviceIdentifier.hashCode());
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
        final DeviceObjectReference other = (DeviceObjectReference) obj;
        if (deviceIdentifier == null) {
            if (other.deviceIdentifier != null)
                return false;
        }
        else if (!deviceIdentifier.equals(other.deviceIdentifier))
            return false;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
