package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.util.queue.ByteQueue;

public class ClientCov extends BaseType {
    private Real realIncrement;
    private Null defaultIncrement;
    
    public ClientCov(Real realIncrement) {
        this.realIncrement = realIncrement;
    }
    
    public ClientCov(Null defaultIncrement) {
        this.defaultIncrement = defaultIncrement;
    }

    public void write(ByteQueue queue) {
        if (realIncrement != null)
            write(queue, realIncrement);
        else
            write(queue, defaultIncrement);
    }
    
    public ClientCov(ByteQueue queue) throws BACnetException {
        int tag = (queue.peek(0) & 0xff) >> 4;
        if (tag == Null.TYPE_ID)
            defaultIncrement = new Null(queue);
        else if (tag == Real.TYPE_ID)
            realIncrement = new Real(queue);
        else
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.invalidParameterDataType);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((defaultIncrement == null) ? 0 : defaultIncrement.hashCode());
        result = PRIME * result + ((realIncrement == null) ? 0 : realIncrement.hashCode());
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
        final ClientCov other = (ClientCov) obj;
        if (defaultIncrement == null) {
            if (other.defaultIncrement != null)
                return false;
        }
        else if (!defaultIncrement.equals(other.defaultIncrement))
            return false;
        if (realIncrement == null) {
            if (other.realIncrement != null)
                return false;
        }
        else if (!realIncrement.equals(other.realIncrement))
            return false;
        return true;
    }
}
