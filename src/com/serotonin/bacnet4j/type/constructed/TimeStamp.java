package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class TimeStamp extends BaseType {
    private final Choice choice;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Time.class);
        classes.add(UnsignedInteger.class);
        classes.add(DateTime.class);
    }
    
    public TimeStamp(Time time) {
        choice = new Choice(0, time);
    }
    
    public TimeStamp(UnsignedInteger sequenceNumber) {
        choice = new Choice(1, sequenceNumber);
    }
    
    public TimeStamp(DateTime dateTime) {
        choice = new Choice(2, dateTime);
    }
    
    @Override
    public void write(ByteQueue queue) {
        write(queue, choice);
    }
    
    public TimeStamp(ByteQueue queue) throws BACnetException {
        choice = new Choice(queue, classes);
    }
    
    public boolean isSequenceNumber() {
        return choice.getContextId() == 1;
    }
    
    public UnsignedInteger getSequenceNumber() {
        return (UnsignedInteger)choice.getDatum();
    }

    public boolean isDateTime() {
        return choice.getContextId() == 2;
    }
    
    public DateTime getDateTime() {
        return (DateTime)choice.getDatum();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((choice == null) ? 0 : choice.hashCode());
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
        final TimeStamp other = (TimeStamp) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        }
        else if (!choice.equals(other.choice))
            return false;
        return true;
    }

}
