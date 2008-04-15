package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class RejectReason extends Enumerated {
    public static final RejectReason other = new RejectReason(0);
    public static final RejectReason bufferOverflow = new RejectReason(1);
    public static final RejectReason inconsistentParameters = new RejectReason(2);
    public static final RejectReason invalidParameterDataType = new RejectReason(3);
    public static final RejectReason invalidTag = new RejectReason(4);
    public static final RejectReason missingRequiredParameter = new RejectReason(5);
    public static final RejectReason parameterOutOfRange = new RejectReason(6);
    public static final RejectReason tooManyArguments = new RejectReason(7);
    public static final RejectReason undefinedEnumeration = new RejectReason(8);
    public static final RejectReason unrecognizedService = new RejectReason(9);

    public RejectReason(int value) {
        super(value);
    }
    
    public RejectReason(ByteQueue queue) {
        super(queue);
    }
}
