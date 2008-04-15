package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.util.queue.ByteQueue;

public class CalendarEntry extends BaseType {
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Date.class);
        classes.add(DateRange.class);
        classes.add(WeekNDay.class);
    }
    
    private Choice entry;
    
    public CalendarEntry(Date date) {
        entry = new Choice(0, date);
    }
    
    public CalendarEntry(DateRange dateRange) {
        entry = new Choice(1, dateRange);
    }
    
    public CalendarEntry(WeekNDay weekNDay) {
        entry = new Choice(2, weekNDay);
    }

    public void write(ByteQueue queue) {
        write(queue, entry);
    }
    
    public CalendarEntry(ByteQueue queue) throws BACnetException {
        entry = new Choice(queue, classes);
    }
}
