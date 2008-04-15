package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class Segmentation extends Enumerated {
    public static final Segmentation segmentedBoth = new Segmentation(0);
    public static final Segmentation segmentedTransmit = new Segmentation(1);
    public static final Segmentation segmentedReceive = new Segmentation(2);
    public static final Segmentation noSegmentation = new Segmentation(3);

    public Segmentation(int value) {
        super(value);
    }
    
    public Segmentation(ByteQueue queue) {
        super(queue);
    }
    
    public String toString() {
        int type = intValue();
        if (type == segmentedBoth.intValue())
            return "both";
        if (type == segmentedTransmit.intValue())
            return "transmit";
        if (type == segmentedReceive.intValue())
            return "receive";
        if (type == noSegmentation.intValue())
            return "none";
        return "Unknown: "+ type;
    }
}
