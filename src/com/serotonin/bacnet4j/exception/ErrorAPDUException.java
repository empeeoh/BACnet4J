package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.apdu.Error;
import com.serotonin.bacnet4j.type.constructed.BACnetError;

public class ErrorAPDUException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private Error apdu;

    public ErrorAPDUException(Error apdu) {
        super(apdu.toString());
        this.apdu = apdu;
    }

    public Error getApdu() {
        return apdu;
    }
    
    public BACnetError getBACnetError() {
        return apdu.getError().getError();
    }
}
