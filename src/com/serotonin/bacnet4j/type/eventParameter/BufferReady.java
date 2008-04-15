package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class BufferReady extends EventParameter {
    public static final byte TYPE_ID = 10;
    
    private UnsignedInteger notificationThreshold;
    private UnsignedInteger previousNotificationCount;
    
    public BufferReady(UnsignedInteger notificationThreshold, UnsignedInteger previousNotificationCount) {
        this.notificationThreshold = notificationThreshold;
        this.previousNotificationCount = previousNotificationCount;
    }

    protected void writeImpl(ByteQueue queue) {
        notificationThreshold.write(queue, 0);
        previousNotificationCount.write(queue, 1);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((notificationThreshold == null) ? 0 : notificationThreshold.hashCode());
        result = PRIME * result + ((previousNotificationCount == null) ? 0 : previousNotificationCount.hashCode());
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
        final BufferReady other = (BufferReady) obj;
        if (notificationThreshold == null) {
            if (other.notificationThreshold != null)
                return false;
        }
        else if (!notificationThreshold.equals(other.notificationThreshold))
            return false;
        if (previousNotificationCount == null) {
            if (other.previousNotificationCount != null)
                return false;
        }
        else if (!previousNotificationCount.equals(other.previousNotificationCount))
            return false;
        return true;
    }
}
