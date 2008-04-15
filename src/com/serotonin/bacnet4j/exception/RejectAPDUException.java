package com.serotonin.bacnet4j.exception;

import com.serotonin.bacnet4j.apdu.Reject;

public class RejectAPDUException extends BACnetException {
    private static final long serialVersionUID = -1;
    
    private Reject apdu;

    public RejectAPDUException(Reject apdu) {
        super(apdu.toString());
        this.apdu = apdu;
    }

    public Reject getApdu() {
        return apdu;
    }
}
