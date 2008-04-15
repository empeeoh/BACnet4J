package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.util.queue.ByteQueue;

public class ReadPropertyConditionalAck extends AcknowledgementService {
    public static final byte TYPE_ID = 13;
    
    private SequenceOf<ReadAccessResult> listOfReadAccessResults;
    
    public ReadPropertyConditionalAck(SequenceOf<ReadAccessResult> listOfReadAccessResults) {
        this.listOfReadAccessResults = listOfReadAccessResults;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public void write(ByteQueue queue) {
        write(queue, listOfReadAccessResults);
    }
    
    ReadPropertyConditionalAck(ByteQueue queue) throws BACnetException {
        listOfReadAccessResults = readSequenceOf(queue, ReadAccessResult.class);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfReadAccessResults == null) ? 0 : listOfReadAccessResults.hashCode());
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
        final ReadPropertyConditionalAck other = (ReadPropertyConditionalAck) obj;
        if (listOfReadAccessResults == null) {
            if (other.listOfReadAccessResults != null)
                return false;
        }
        else if (!listOfReadAccessResults.equals(other.listOfReadAccessResults))
            return false;
        return true;
    }
}
