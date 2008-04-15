package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class Scale extends BaseType {
    private Choice scale;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Real.class);
        classes.add(SignedInteger.class);
    }
    
    public Scale(Real scale) {
        this.scale = new Choice(0, scale);
    }
    
    public Scale(SignedInteger scale) {
        this.scale = new Choice(1, scale);
    }

    public void write(ByteQueue queue) {
        write(queue, scale);
    }
    
    public Scale(ByteQueue queue) throws BACnetException {
        scale = new Choice(queue, classes);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((scale == null) ? 0 : scale.hashCode());
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
        final Scale other = (Scale) obj;
        if (scale == null) {
            if (other.scale != null)
                return false;
        }
        else if (!scale.equals(other.scale))
            return false;
        return true;
    }
}
