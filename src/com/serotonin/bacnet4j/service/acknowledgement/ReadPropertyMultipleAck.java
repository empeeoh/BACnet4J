package com.serotonin.bacnet4j.service.acknowledgement;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.ReadAccessResult;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.util.queue.ByteQueue;

public class ReadPropertyMultipleAck extends AcknowledgementService {
    public static final byte TYPE_ID = 14;
    
    private SequenceOf<ReadAccessResult> listOfReadAccessResults;
    
    public ReadPropertyMultipleAck(SequenceOf<ReadAccessResult> listOfReadAccessResults) {
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
    
    ReadPropertyMultipleAck(ByteQueue queue) throws BACnetException {
        listOfReadAccessResults = readSequenceOf(queue, ReadAccessResult.class);
    }

    @Override
    public String toString() {
        return "ReadPropertyMultipleAck("+ listOfReadAccessResults +")";
    }

    public SequenceOf<ReadAccessResult> getListOfReadAccessResults() {
        return listOfReadAccessResults;
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
        final ReadPropertyMultipleAck other = (ReadPropertyMultipleAck) obj;
        if (listOfReadAccessResults == null) {
            if (other.listOfReadAccessResults != null)
                return false;
        }
        else if (!listOfReadAccessResults.equals(other.listOfReadAccessResults))
            return false;
        return true;
    }
}
