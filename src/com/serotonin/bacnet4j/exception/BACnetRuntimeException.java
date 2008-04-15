package com.serotonin.bacnet4j.exception;

public class BACnetRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1;
    
    public BACnetRuntimeException() {
        super();
    }

    public BACnetRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BACnetRuntimeException(String message) {
        super(message);
    }

    public BACnetRuntimeException(Throwable cause) {
        super(cause);
    }

}
