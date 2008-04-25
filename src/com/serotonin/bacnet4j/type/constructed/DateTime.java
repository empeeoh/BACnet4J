package com.serotonin.bacnet4j.type.constructed;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.serotonin.util.queue.ByteQueue;

public class DateTime extends BaseType {
    private final Date date;
    private final Time time;
    
    public DateTime(Date date, Time time) {
        this.date = date;
        this.time = time;
    }
    
    @Override
    public void write(ByteQueue queue) {
        date.write(queue);
        time.write(queue);
    }
    
    public DateTime(ByteQueue queue) throws BACnetException {
        date = read(queue, Date.class);
        time = read(queue, Time.class);
    }
    
    public DateTime() {
        GregorianCalendar now = new GregorianCalendar();
        this.date = new Date(now);
        this.time = new Time(now);
    }
    
    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
    
    public long getTimeMillis() {
        GregorianCalendar gc = new GregorianCalendar(date.getYear(), date.getMonth().getId()-1, date.getDay(),
                time.getHour(), time.getMinute(), time.getSecond());
        gc.set(Calendar.MILLISECOND, time.getHundredth()*10);
        return gc.getTimeInMillis();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((date == null) ? 0 : date.hashCode());
        result = PRIME * result + ((time == null) ? 0 : time.hashCode());
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
        final DateTime other = (DateTime) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        }
        else if (!date.equals(other.date))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        }
        else if (!time.equals(other.time))
            return false;
        return true;
    }
}
