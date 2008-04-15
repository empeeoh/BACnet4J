package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class DaysOfWeek extends BitString {
    public DaysOfWeek() {
        super(new boolean[7]);
    }
    
    public DaysOfWeek(boolean defaultValue) {
        super(7, defaultValue);
    }

    public DaysOfWeek(ByteQueue queue) {
        super(queue);
    }
    
    public boolean contains(int day) {
        return getValue()[day];
    }

    public boolean isMonday() {
        return getValue()[0];
    }

    public void setMonday(boolean monday) {
        getValue()[0] = monday;
    }

    public boolean isTuesday() {
        return getValue()[1];
    }

    public void setTuesday(boolean tuesday) {
        getValue()[1] = tuesday;
    }

    public boolean isWednesday() {
        return getValue()[2];
    }

    public void setWednesday(boolean wednesday) {
        getValue()[2] = wednesday;
    }

    public boolean isThursday() {
        return getValue()[3];
    }

    public void setThursday(boolean thursday) {
        getValue()[3] = thursday;
    }

    public boolean isFriday() {
        return getValue()[4];
    }

    public void setFriday(boolean friday) {
        getValue()[4] = friday;
    }

    public boolean isSaturday() {
        return getValue()[5];
    }

    public void setSaturday(boolean saturday) {
        getValue()[5] = saturday;
    }

    public boolean isSunday() {
        return getValue()[6];
    }

    public void setSunday(boolean sunday) {
        getValue()[6] = sunday;
    }
}
