package com.serotonin.bacnet4j.exception;

public class BACnetException extends Exception {
    private static final long serialVersionUID = -1;
    
    public BACnetException() {
        super();
    }

    public BACnetException(String message, Throwable cause) {
        super(message, cause);
    }

    public BACnetException(String message) {
        super(message);
    }

    public BACnetException(Throwable cause) {
        super(cause);
    }
}
