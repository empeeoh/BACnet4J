package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.apdu.Abort;

public class AbortAPDUException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private Abort apdu;

    public AbortAPDUException(Abort apdu) {
        super(apdu.toString());
        this.apdu = apdu;
    }

    public Abort getApdu() {
        return apdu;
    }
}
