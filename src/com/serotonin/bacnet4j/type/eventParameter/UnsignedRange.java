package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class UnsignedRange extends EventParameter {
    public static final byte TYPE_ID = 11;
    
    private UnsignedInteger timeDelay;
    private UnsignedInteger lowLimit;
    private UnsignedInteger highLimit;
    
    public UnsignedRange(UnsignedInteger timeDelay, UnsignedInteger lowLimit, UnsignedInteger highLimit) {
        this.timeDelay = timeDelay;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    protected void writeImpl(ByteQueue queue) {
        timeDelay.write(queue, 0);
        lowLimit.write(queue, 1);
        highLimit.write(queue, 2);
    }
    
    protected int getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((highLimit == null) ? 0 : highLimit.hashCode());
        result = PRIME * result + ((lowLimit == null) ? 0 : lowLimit.hashCode());
        result = PRIME * result + ((timeDelay == null) ? 0 : timeDelay.hashCode());
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
        final UnsignedRange other = (UnsignedRange) obj;
        if (highLimit == null) {
            if (other.highLimit != null)
                return false;
        }
        else if (!highLimit.equals(other.highLimit))
            return false;
        if (lowLimit == null) {
            if (other.lowLimit != null)
                return false;
        }
        else if (!lowLimit.equals(other.lowLimit))
            return false;
        if (timeDelay == null) {
            if (other.timeDelay != null)
                return false;
        }
        else if (!timeDelay.equals(other.timeDelay))
            return false;
        return true;
    }
}
