package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.util.queue.ByteQueue;

public class WeekNDay extends OctetString {
    public static class WeekOfMonth extends Enumerated {
        public static final WeekOfMonth days1to7 = new WeekOfMonth(1);
        public static final WeekOfMonth days8to14 = new WeekOfMonth(2);
        public static final WeekOfMonth days15to21 = new WeekOfMonth(3);
        public static final WeekOfMonth days22to28 = new WeekOfMonth(4);
        public static final WeekOfMonth days29to31 = new WeekOfMonth(5);
        public static final WeekOfMonth last7Days = new WeekOfMonth(6);
        public static final WeekOfMonth any = new WeekOfMonth(255);
        
        public static WeekOfMonth valueOf(byte b) {
            switch (b) {
            case 1 :
                return days1to7;
            case 2 :
                return days8to14;
            case 3 :
                return days15to21;
            case 4 :
                return days22to28;
            case 5 :
                return days29to31;
            case 6 :
                return last7Days;
            case (byte)255 :
                return any;
            }
            throw new IllegalArgumentException(Byte.toString(b));
        }

        private WeekOfMonth(int value) {
            super(value);
        }

        public WeekOfMonth(ByteQueue queue) {
            super(queue);
        }
    }
    
    public WeekNDay(Month month, WeekOfMonth weekOfMonth, DayOfWeek dayOfWeek) {
        super(new byte[] {month.getId(), weekOfMonth.byteValue(), dayOfWeek.getId()});
    }

    public Month getMonth() {
        return Month.valueOf(getBytes()[0]);
    }
    
    public WeekOfMonth getWeekOfMonth() {
        return WeekOfMonth.valueOf(getBytes()[1]);
    }
    
    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.valueOf(getBytes()[2]);
    }

    public WeekNDay(ByteQueue queue) {
        super(queue);
    }
}
