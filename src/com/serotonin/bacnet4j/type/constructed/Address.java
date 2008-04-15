package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.IpAddressUtils;
import com.serotonin.util.queue.ByteQueue;

public class Address extends BaseType {
    private final UnsignedInteger networkNumber;
    private final OctetString macAddress;
    
    public Address(UnsignedInteger networkNumber, OctetString macAddress) {
        this.networkNumber = networkNumber;
        this.macAddress = macAddress;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, networkNumber);
        write(queue, macAddress);
    }
    
    public Address(ByteQueue queue) throws BACnetException {
        networkNumber = read(queue, UnsignedInteger.class);
        macAddress = read(queue, OctetString.class);
    }
    
    public OctetString getMacAddress() {
        return macAddress;
    }

    public UnsignedInteger getNetworkNumber() {
        return networkNumber;
    }

    @Override
    public String toString() {
        return "Address(networkNumber="+ networkNumber +", macAddress="+ macAddress +")";
    }
    
    public String toIpString() {
        return IpAddressUtils.toIpString(macAddress.getBytes()) +":"+ networkNumber.intValue();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((macAddress == null) ? 0 : macAddress.hashCode());
        result = PRIME * result + ((networkNumber == null) ? 0 : networkNumber.hashCode());
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
        final Address other = (Address) obj;
        if (macAddress == null) {
            if (other.macAddress != null)
                return false;
        }
        else if (!macAddress.equals(other.macAddress))
            return false;
        if (networkNumber == null) {
            if (other.networkNumber != null)
                return false;
        }
        else if (!networkNumber.equals(other.networkNumber))
            return false;
        return true;
    }
}
