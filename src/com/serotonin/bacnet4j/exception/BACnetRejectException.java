package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.enumerated.RejectReason;

public class BACnetRejectException extends BACnetException {
    private static final long serialVersionUID = -1;

    private RejectReason rejectReason;

    public BACnetRejectException(RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }
}
