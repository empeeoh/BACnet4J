package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;

public class BACnetServiceException extends Exception {
    private static final long serialVersionUID = -1;
    
    private ErrorClass errorClass;
    private ErrorCode errorCode;
    
    public BACnetServiceException(ErrorClass errorClass, ErrorCode errorCode) {
        this.errorClass = errorClass;
        this.errorCode = errorCode;
    }

    public BACnetServiceException(ErrorClass errorClass, ErrorCode errorCode, String message) {
        super(message);
        this.errorClass = errorClass;
        this.errorCode = errorCode;
    }

    public ErrorClass getErrorClass() {
        return errorClass;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public boolean equals(ErrorClass errorClass, ErrorCode errorCode) {
        return this.errorClass.equals(errorClass) && this.errorCode.equals(errorCode);
    }

    public String getMessage() {
        String message = "class="+ errorClass +", code="+ errorCode;
        String userDesc = super.getMessage();
        if (userDesc != null)
            message += ", message="+ userDesc;
        return message;
    }
}
