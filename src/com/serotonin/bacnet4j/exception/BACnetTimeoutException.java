package com.serotonin.bacnet4j.exception;

public class BACnetTimeoutException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    public BACnetTimeoutException() {
        super();
    }

    public BACnetTimeoutException(String message) {
        super(message);
    }
}
