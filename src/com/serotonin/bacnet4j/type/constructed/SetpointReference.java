package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.queue.ByteQueue;

public class SetpointReference extends BaseType {
    private ObjectPropertyReference setpointReference;

    public SetpointReference(ObjectPropertyReference setpointReference) {
        this.setpointReference = setpointReference;
    }

    public void write(ByteQueue queue) {
        writeOptional(queue, setpointReference);
    }
    
    public SetpointReference(ByteQueue queue) throws BACnetException {
        setpointReference = read(queue, ObjectPropertyReference.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((setpointReference == null) ? 0 : setpointReference.hashCode());
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
        final SetpointReference other = (SetpointReference) obj;
        if (setpointReference == null) {
            if (other.setpointReference != null)
                return false;
        }
        else if (!setpointReference.equals(other.setpointReference))
            return false;
        return true;
    }
}
