package com.serotonin.bacnet4j.npdu;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.constructed.Address;

public interface RequestHandler {
    AcknowledgementService handleConfirmedRequest(Address from, byte invokeId, ConfirmedRequestService serviceRequest)
            throws BACnetException;
    void handleUnconfirmedRequest(Address from, UnconfirmedRequestService serviceRequest);
}
