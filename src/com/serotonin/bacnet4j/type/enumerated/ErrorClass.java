package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class ErrorClass extends Enumerated {
    public static final ErrorClass device = new ErrorClass(0);
    public static final ErrorClass object = new ErrorClass(1);
    public static final ErrorClass property = new ErrorClass(2);
    public static final ErrorClass resources = new ErrorClass(3);
    public static final ErrorClass security = new ErrorClass(4);
    public static final ErrorClass services = new ErrorClass(5);
    public static final ErrorClass vt = new ErrorClass(6);

    public ErrorClass(int value) {
        super(value);
    }
    
    public ErrorClass(ByteQueue queue) {
        super(queue);
    }
    
    public String toString() {
        int type = intValue();
        if (type == device.intValue())
            return "Device";
        if (type == object.intValue())
            return "Object";
        if (type == property.intValue())
            return "Property";
        if (type == resources.intValue())
            return "Resources";
        if (type == security.intValue())
            return "Security";
        if (type == services.intValue())
            return "Services";
        if (type == vt.intValue())
            return "VT";
        return "Unknown: "+ type;
    }
}
