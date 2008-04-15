package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class VtCloseRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 22;
    
    private SequenceOf<UnsignedInteger> listOfRemoteVTSessionIdentifiers;
    
    public VtCloseRequest(SequenceOf<UnsignedInteger> listOfRemoteVTSessionIdentifiers) {
        this.listOfRemoteVTSessionIdentifiers = listOfRemoteVTSessionIdentifiers;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, listOfRemoteVTSessionIdentifiers);
    }
    
    VtCloseRequest(ByteQueue queue) throws BACnetException {
        listOfRemoteVTSessionIdentifiers = readSequenceOf(queue, UnsignedInteger.class);
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfRemoteVTSessionIdentifiers == null) ? 0 : listOfRemoteVTSessionIdentifiers.hashCode());
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
        final VtCloseRequest other = (VtCloseRequest) obj;
        if (listOfRemoteVTSessionIdentifiers == null) {
            if (other.listOfRemoteVTSessionIdentifiers != null)
                return false;
        }
        else if (!listOfRemoteVTSessionIdentifiers.equals(other.listOfRemoteVTSessionIdentifiers))
            return false;
        return true;
    }
}
