package com.serotonin.bacnet4j.apdu;

abstract public class AckAPDU extends APDU {
    /**
     * This parameter shall be the 'invokeID' contained in the confirmed service request being acknowledged. The same 
     * 'originalinvokeID' shall be used for all segments of a segmented acknowledgment.
     */
    protected byte originalInvokeId;

    public byte getOriginalInvokeId() {
        return originalInvokeId;
    }
    
    public boolean isServer() {
        return true;
    }
}
