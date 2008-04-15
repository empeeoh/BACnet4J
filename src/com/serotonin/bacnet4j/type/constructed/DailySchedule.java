package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.queue.ByteQueue;

public class DailySchedule extends BaseType {
    private SequenceOf<TimeValue> daySchedule;

    public DailySchedule(SequenceOf<TimeValue> daySchedule) {
        this.daySchedule = daySchedule;
    }

    public void write(ByteQueue queue) {
        write(queue, daySchedule, 0);
    }
    
    public DailySchedule(ByteQueue queue) throws BACnetException {
        daySchedule = readSequenceOf(queue, TimeValue.class, 0);
    }
}
