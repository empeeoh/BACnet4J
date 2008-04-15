package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.util.queue.ByteQueue;

public class ComplexEventType extends NotificationParameters {
    public static final byte TYPE_ID = 6;
    
    private SequenceOf<PropertyValue> values;

    public ComplexEventType(SequenceOf<PropertyValue> values) {
        this.values = values;
    }
    
    protected void writeImpl(ByteQueue queue) {
        write(queue, values);
    }
    
    public ComplexEventType(ByteQueue queue) throws BACnetException {
        values = readSequenceOf(queue, PropertyValue.class);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((values == null) ? 0 : values.hashCode());
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
        final ComplexEventType other = (ComplexEventType) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        }
        else if (!values.equals(other.values))
            return false;
        return true;
    }
}
