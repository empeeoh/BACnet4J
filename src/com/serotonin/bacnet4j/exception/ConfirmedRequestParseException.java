package com.serotonin.bacnet4j.exception;

public class ConfirmedRequestParseException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private int originalInvokeId;

    public ConfirmedRequestParseException(int originalInvokeId) {
        this.originalInvokeId = originalInvokeId;
    }

    public ConfirmedRequestParseException(int originalInvokeId, Throwable cause) {
        super(cause);
        this.originalInvokeId = originalInvokeId;
    }

    public int getOriginalInvokeId() {
        return originalInvokeId;
    }
}
