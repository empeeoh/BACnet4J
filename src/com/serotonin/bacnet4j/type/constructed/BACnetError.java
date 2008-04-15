package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.util.queue.ByteQueue;

public class BACnetError extends BaseType {
    private ErrorClass errorClass;
    private ErrorCode errorCode;
    
    public BACnetError(ErrorClass errorClass, ErrorCode errorCode) {
        this.errorClass = errorClass;
        this.errorCode = errorCode;
    }
    
    public BACnetError(BACnetServiceException e) {
        this.errorClass = e.getErrorClass();
        this.errorCode = e.getErrorCode();
    }
    
    public void write(ByteQueue queue) {
        write(queue, errorClass);
        write(queue, errorCode);
    }


    public BACnetError(ByteQueue queue) throws BACnetException {
        errorClass = read(queue, ErrorClass.class);
        errorCode = read(queue, ErrorCode.class);
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

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((errorClass == null) ? 0 : errorClass.hashCode());
        result = PRIME * result + ((errorCode == null) ? 0 : errorCode.hashCode());
        return result;
    }
    
    public String toString() {
        return "errorClass="+ errorClass +", errorCode="+ errorCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BACnetError other = (BACnetError) obj;
        if (errorClass == null) {
            if (other.errorClass != null)
                return false;
        }
        else if (!errorClass.equals(other.errorClass))
            return false;
        if (errorCode == null) {
            if (other.errorCode != null)
                return false;
        }
        else if (!errorCode.equals(other.errorCode))
            return false;
        return true;
    }
}
