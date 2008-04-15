package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class FileAccessMethod extends Enumerated {
    public static final FileAccessMethod recordAccess = new FileAccessMethod(0);
    public static final FileAccessMethod streamAccess = new FileAccessMethod(1);

    public FileAccessMethod(int value) {
        super(value);
    }
    
    public FileAccessMethod(ByteQueue queue) {
        super(queue);
    }
}
