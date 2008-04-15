package com.serotonin.bacnet4j.exception;

public class IllegalPduTypeException extends BACnetRuntimeException {
    private static final long serialVersionUID = -1;
    
    public IllegalPduTypeException(String message) {
        super(message);
    }
}
