package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.apdu.Abort;

public class SegmentedMessageAbortedException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private Abort abort;
    
    public SegmentedMessageAbortedException(Abort abort) {
        this.abort = abort;
    }

    public Abort getAbort() {
        return abort;
    }
}
