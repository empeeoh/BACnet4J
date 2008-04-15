package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class Prescale extends BaseType {
    private UnsignedInteger multiplier;
    private UnsignedInteger moduloDivide;
    
    public Prescale(UnsignedInteger multiplier, UnsignedInteger moduloDivide) {
        this.multiplier = multiplier;
        this.moduloDivide = moduloDivide;
    }

    public void write(ByteQueue queue) {
        write(queue, multiplier, 0);
        write(queue, moduloDivide, 1);
    }
    
    public Prescale(ByteQueue queue) throws BACnetException {
        multiplier = read(queue, UnsignedInteger.class, 0);
        moduloDivide = read(queue, UnsignedInteger.class, 1);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((moduloDivide == null) ? 0 : moduloDivide.hashCode());
        result = PRIME * result + ((multiplier == null) ? 0 : multiplier.hashCode());
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
        final Prescale other = (Prescale) obj;
        if (moduloDivide == null) {
            if (other.moduloDivide != null)
                return false;
        }
        else if (!moduloDivide.equals(other.moduloDivide))
            return false;
        if (multiplier == null) {
            if (other.multiplier != null)
                return false;
        }
        else if (!multiplier.equals(other.multiplier))
            return false;
        return true;
    }
}
