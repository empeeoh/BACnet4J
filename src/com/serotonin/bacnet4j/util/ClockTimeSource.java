package com.serotonin.bacnet4j.util;

public class ClockTimeSource implements TimeSource {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
