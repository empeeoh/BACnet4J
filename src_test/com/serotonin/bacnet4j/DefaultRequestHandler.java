package com.serotonin.bacnet4j;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.npdu.RequestHandler;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;

public class DefaultRequestHandler implements RequestHandler {
    public AcknowledgementService handleConfirmedRequest(Address from, byte invokeId, 
            ConfirmedRequestService serviceRequest) throws BACnetErrorException {
        System.out.println("Received confirmed service request "+ serviceRequest.getClass());
        throw new BACnetErrorException(ErrorClass.device, ErrorCode.deviceBusy);
    }

    public void handleUnconfirmedRequest(Address from, UnconfirmedRequestService serviceRequest) {
        System.out.println("Received unconfirmed service request "+ serviceRequest.getClass());
    }
}
