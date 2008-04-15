package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class DeviceStatus extends Enumerated {
    public static final DeviceStatus operational = new DeviceStatus(0);
    public static final DeviceStatus operationalReadOnly = new DeviceStatus(1);
    public static final DeviceStatus downloadRequired = new DeviceStatus(2);
    public static final DeviceStatus downloadInProgress = new DeviceStatus(3);
    public static final DeviceStatus nonOperational = new DeviceStatus(4);
    public static final DeviceStatus backupInProgress = new DeviceStatus(5);

    public DeviceStatus(int value) {
        super(value);
    }
    
    public DeviceStatus(ByteQueue queue) {
        super(queue);
    }
}
