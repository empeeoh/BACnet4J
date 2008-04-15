package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.Service;
import com.serotonin.util.queue.ByteQueue;

abstract public class AcknowledgementService extends Service {
    public static AcknowledgementService createAcknowledgementService(byte type, ByteQueue queue)
            throws BACnetException {
        
        if (type == GetAlarmSummaryAck.TYPE_ID) // 3
            return new GetAlarmSummaryAck(queue);
        if (type == GetEnrollmentSummaryAck.TYPE_ID) // 4
            return new GetEnrollmentSummaryAck(queue);
        if (type == AtomicReadFileAck.TYPE_ID) // 6
            return new AtomicReadFileAck(queue);
        if (type == AtomicWriteFileAck.TYPE_ID) // 7
            return new AtomicWriteFileAck(queue);
        if (type == CreateObjectAck.TYPE_ID) // 10
            return new CreateObjectAck(queue);
        if (type == ReadPropertyAck.TYPE_ID) // 12
            return new ReadPropertyAck(queue);
        if (type == ReadPropertyConditionalAck.TYPE_ID) // 13
            return new ReadPropertyConditionalAck(queue);
        if (type == ReadPropertyMultipleAck.TYPE_ID) // 14
            return new ReadPropertyMultipleAck(queue);
        if (type == ConfirmedPrivateTransferAck.TYPE_ID) // 18
            return new ConfirmedPrivateTransferAck(queue);
        if (type == VtOpenAck.TYPE_ID) // 21
            return new VtOpenAck(queue);
        if (type == VtDataAck.TYPE_ID) // 23
            return new VtDataAck(queue);
        if (type == AuthenticateAck.TYPE_ID) // 24
            return new AuthenticateAck(queue);
        if (type == ReadRangeAck.TYPE_ID) // 26
            return new ReadRangeAck(queue);
        if (type == GetEventInformationAck.TYPE_ID) // 29
            return new GetEventInformationAck(queue);
        
        throw new BACnetException("Unsupported service acknowledgement: "+ (type & 0xff));
    }
}
