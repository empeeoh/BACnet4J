package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.error.BaseError;

public class BACnetErrorException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private BaseError error;
    
    public BACnetErrorException(byte choice, ErrorClass errorClass, ErrorCode errorCode) {
        error = new BaseError(choice, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(byte choice, BACnetServiceException e) {
        error = new BaseError(choice, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }
    
    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode) {
        error = new BaseError((byte)127, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(BACnetServiceException e) {
        super(e.getMessage());
        error = new BaseError((byte)127, new BACnetError(e.getErrorClass(), e.getErrorCode()));
    }
    
    public BACnetErrorException(ErrorClass errorClass, ErrorCode errorCode, String message) {
        super(message);
        error = new BaseError((byte)127, new BACnetError(errorClass, errorCode));
    }
    
    public BACnetErrorException(BaseError error) {
        this.error = error;
    }

    public BaseError getError() {
        return error;
    }
}
