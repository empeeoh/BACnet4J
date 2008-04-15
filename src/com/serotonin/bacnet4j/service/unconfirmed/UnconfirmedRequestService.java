package com.serotonin.bacnet4j.service.unconfirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.Service;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.util.queue.ByteQueue;

abstract public class UnconfirmedRequestService extends Service {
    public static UnconfirmedRequestService createUnconfirmedRequestService(byte type, ByteQueue queue)
            throws BACnetException {
        if (type == IAmRequest.TYPE_ID)
            return new IAmRequest(queue);
        if (type == IHaveRequest.TYPE_ID)
            return new IHaveRequest(queue);
        if (type == UnconfirmedCovNotificationRequest.TYPE_ID)
            return new UnconfirmedCovNotificationRequest(queue);
        if (type == UnconfirmedEventNotificationRequest.TYPE_ID)
            return new UnconfirmedEventNotificationRequest(queue);
        if (type == UnconfirmedPrivateTransferRequest.TYPE_ID)
            return new UnconfirmedPrivateTransferRequest(queue);
        if (type == UnconfirmedTextMessageRequest.TYPE_ID)
            return new UnconfirmedTextMessageRequest(queue);
        if (type == TimeSynchronizationRequest.TYPE_ID)
            return new TimeSynchronizationRequest(queue);
        if (type == WhoHasRequest.TYPE_ID)
            return new WhoHasRequest(queue);
        if (type == WhoIsRequest.TYPE_ID)
            return new WhoIsRequest(queue);
        if (type == UTCTimeSynchronizationRequest.TYPE_ID)
            return new UTCTimeSynchronizationRequest(queue);
        
        throw new BACnetException("Unsupported unconfirmed service: "+ (type & 0xff));
    }
    
    abstract public void handle(LocalDevice localDevice, Address from) throws BACnetException;
}
