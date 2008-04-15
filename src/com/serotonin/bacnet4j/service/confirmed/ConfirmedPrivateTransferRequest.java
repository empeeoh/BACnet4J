package com.serotonin.bacnet4j.service.confirmed;

import java.util.HashMap;
import java.util.Map;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.SequenceDefinition;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ConfirmedPrivateTransferRequest extends ConfirmedRequestService {
    public static final Map<VendorServiceKey, SequenceDefinition> vendorServiceResolutions = 
            new HashMap<VendorServiceKey, SequenceDefinition>();
    
    public static final byte TYPE_ID = 18;
    
    private UnsignedInteger vendorId;
    private UnsignedInteger serviceNumber;
    private Encodable serviceParameters;
    
    public ConfirmedPrivateTransferRequest(UnsignedInteger vendorId, UnsignedInteger serviceNumber, 
            Encodable serviceParameters) {
        this.vendorId = vendorId;
        this.serviceNumber = serviceNumber;
        this.serviceParameters = serviceParameters;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        localDevice.getEventHandler().firePrivateTransfer(vendorId, serviceNumber, serviceParameters);
        return null;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, vendorId, 0);
        write(queue, serviceNumber, 1);
        writeOptional(queue, serviceParameters, 2);
    }
    
    ConfirmedPrivateTransferRequest(ByteQueue queue) throws BACnetException {
        vendorId = read(queue, UnsignedInteger.class, 0);
        serviceNumber = read(queue, UnsignedInteger.class, 1);
        serviceParameters = readVendorSpecific(queue, vendorId, serviceNumber, vendorServiceResolutions, 2);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((serviceNumber == null) ? 0 : serviceNumber.hashCode());
        result = PRIME * result + ((serviceParameters == null) ? 0 : serviceParameters.hashCode());
        result = PRIME * result + ((vendorId == null) ? 0 : vendorId.hashCode());
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
        final ConfirmedPrivateTransferRequest other = (ConfirmedPrivateTransferRequest) obj;
        if (serviceNumber == null) {
            if (other.serviceNumber != null)
                return false;
        }
        else if (!serviceNumber.equals(other.serviceNumber))
            return false;
        if (serviceParameters == null) {
            if (other.serviceParameters != null)
                return false;
        }
        else if (!serviceParameters.equals(other.serviceParameters))
            return false;
        if (vendorId == null) {
            if (other.vendorId != null)
                return false;
        }
        else if (!vendorId.equals(other.vendorId))
            return false;
        return true;
    }
}
