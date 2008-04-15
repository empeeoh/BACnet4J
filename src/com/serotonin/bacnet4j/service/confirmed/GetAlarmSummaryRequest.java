package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.util.queue.ByteQueue;

public class GetAlarmSummaryRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 3;
    
    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    public GetAlarmSummaryRequest() {
        // no op
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        // no op
    }
    
    GetAlarmSummaryRequest(@SuppressWarnings("unused")ByteQueue queue) {
        // no op
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }
}
