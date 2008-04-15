package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.VtClass;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class VtOpenRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 21;
    
    private VtClass vtClass;
    private UnsignedInteger localVTSessionIdentifier;
    
    public VtOpenRequest(VtClass vtClass, UnsignedInteger localVTSessionIdentifier) {
        this.vtClass = vtClass;
        this.localVTSessionIdentifier = localVTSessionIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, vtClass);
        write(queue, localVTSessionIdentifier);
    }
    
    VtOpenRequest(ByteQueue queue) throws BACnetException {
        vtClass = read(queue, VtClass.class);
        localVTSessionIdentifier = read(queue, UnsignedInteger.class);
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((localVTSessionIdentifier == null) ? 0 : localVTSessionIdentifier.hashCode());
        result = PRIME * result + ((vtClass == null) ? 0 : vtClass.hashCode());
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
        final VtOpenRequest other = (VtOpenRequest) obj;
        if (localVTSessionIdentifier == null) {
            if (other.localVTSessionIdentifier != null)
                return false;
        }
        else if (!localVTSessionIdentifier.equals(other.localVTSessionIdentifier))
            return false;
        if (vtClass == null) {
            if (other.vtClass != null)
                return false;
        }
        else if (!vtClass.equals(other.vtClass))
            return false;
        return true;
    }
}
