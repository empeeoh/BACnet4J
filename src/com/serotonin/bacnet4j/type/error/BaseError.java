package com.serotonin.bacnet4j.type.error;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.util.queue.ByteQueue;

public class BaseError extends BaseType {
    public static BaseError createBaseError(ByteQueue queue) throws BACnetException {
        byte choice = queue.pop();
        
        switch (choice) {
        case 8 :
        case 9 :
            return new ChangeListError(choice, queue);
        case 10 :
            return new CreateObjectError(choice, queue);
        case 16 :
            return new WritePropertyMultipleError(choice, queue);
        case 18 :
            return new ConfirmedPrivateTransferError(choice, queue);
        case 22 :
            return new VTCloseError(choice, queue);
        }
        return new BaseError(choice, queue);
    }
    
    protected byte choice;
    protected BACnetError error;

    public BaseError(byte choice, BACnetError error) {
        this.choice = choice;
        this.error = error;
    }
    
    public void write(ByteQueue queue) {
        queue.push(choice);
        write(queue, error);
    }
    
    public BaseError(byte choice, ByteQueue queue) throws BACnetException {
        this.choice = choice;
        error = read(queue, BACnetError.class);
    }
    
    public BaseError(byte choice, ByteQueue queue, int contextId) throws BACnetException {
        this.choice = choice;
        error = read(queue, BACnetError.class, contextId);
    }
    
    public String toString() {
        return "choice="+ (choice & 0xff) +", "+ error;
    }
    
    public BACnetError getError() {
        return error;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + choice;
        result = PRIME * result + ((error == null) ? 0 : error.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BaseError other = (BaseError) obj;
        if (choice != other.choice)
            return false;
        if (error == null) {
            if (other.error != null)
                return false;
        }
        else if (!error.equals(other.error))
            return false;
        return true;
    }
}
