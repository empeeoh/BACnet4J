package com.serotonin.bacnet4j.type.constructed;

import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.util.queue.ByteQueue;

public class Choice extends BaseType {
    private int contextId;
    private Encodable datum;
    
    public Choice(int contextId, Encodable datum) {
        this.contextId = contextId;
        this.datum = datum;
    }

    public int getContextId() {
        return contextId;
    }

    public Encodable getDatum() {
        return datum;
    }

    public void write(ByteQueue queue) {
        write(queue, datum, contextId);
    }
    
    public Choice(ByteQueue queue, List<Class<? extends Encodable>> classes) throws BACnetException {
        read(queue, classes);
    }
    
    public Choice(ByteQueue queue, List<Class<? extends Encodable>> classes, int contextId) throws BACnetException {
        popStart(queue, contextId);
        read(queue, classes);
        popEnd(queue, contextId);
    }
    
    public void read(ByteQueue queue, List<Class<? extends Encodable>> classes) throws BACnetException {
        contextId = peekTagNumber(queue);
        datum = read(queue, classes.get(contextId), contextId);
    }

    public String toString() {
        return datum.toString();
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + contextId;
        result = PRIME * result + ((datum == null) ? 0 : datum.hashCode());
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
        final Choice other = (Choice) obj;
        if (contextId != other.contextId)
            return false;
        if (datum == null) {
            if (other.datum != null)
                return false;
        }
        else if (!datum.equals(other.datum))
            return false;
        return true;
    }
}
