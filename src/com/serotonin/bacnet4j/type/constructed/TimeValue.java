package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.util.queue.ByteQueue;

public class TimeValue extends BaseType {
    private Time time;
    private Primitive value;
    
    public TimeValue(Time time, Primitive value) {
        this.time = time;
        this.value = value;
    }

    public void write(ByteQueue queue) {
        write(queue, time);
        write(queue, value);
    }
    
    public TimeValue(ByteQueue queue) throws BACnetException {
        time = read(queue, Time.class);
        value = read(queue, Primitive.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((time == null) ? 0 : time.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final TimeValue other = (TimeValue) obj;
        if (time == null) {
            if (other.time != null)
                return false;
        }
        else if (!time.equals(other.time))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
}
