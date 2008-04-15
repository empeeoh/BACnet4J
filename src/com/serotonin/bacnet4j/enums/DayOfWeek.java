package com.serotonin.bacnet4j.enums;

public enum DayOfWeek {
    MONDAY      (1),
    TUESDAY     (2),
    WEDNESDAY   (3),
    THURSDAY    (4),
    FRIDAY      (5),
    SATURDAY    (6),
    SUNDAY      (7),
    UNSPECIFIED (255);
    
    private byte id;
    
    DayOfWeek(int id) {
        this.id = (byte)id;
    }
    
    public byte getId() {
        return id;
    }
    
    public static DayOfWeek valueOf(byte id) {
        if (id == MONDAY.id)
            return MONDAY;
        if (id == TUESDAY.id)
            return TUESDAY;
        if (id == WEDNESDAY.id)
            return WEDNESDAY;
        if (id == THURSDAY.id)
            return THURSDAY;
        if (id == FRIDAY.id)
            return FRIDAY;
        if (id == SATURDAY.id)
            return SATURDAY;
        if (id == SUNDAY.id)
            return SUNDAY;
        if (id == UNSPECIFIED.id)
            return UNSPECIFIED;
            
        throw new IllegalArgumentException("Unknown id: "+ id);
    }
}
