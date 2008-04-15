package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.util.queue.ByteQueue;

public class DateRange extends BaseType {
    private Date startDate;
    private Date endDate;
    
    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void write(ByteQueue queue) {
        write(queue, startDate);
        write(queue, endDate);
    }
    
    public DateRange(ByteQueue queue) throws BACnetException {
        startDate = read(queue, Date.class);
        endDate = read(queue, Date.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = PRIME * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        final DateRange other = (DateRange) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        }
        else if (!endDate.equals(other.endDate))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }
}
