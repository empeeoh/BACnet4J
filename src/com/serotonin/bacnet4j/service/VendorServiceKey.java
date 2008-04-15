package com.serotonin.bacnet4j.service;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class VendorServiceKey {
    private UnsignedInteger vendorId;
    private UnsignedInteger serviceNumber;
    
    public VendorServiceKey(UnsignedInteger vendorId, UnsignedInteger serviceNumber) {
        this.vendorId = vendorId;
        this.serviceNumber = serviceNumber;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((serviceNumber == null) ? 0 : serviceNumber.hashCode());
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
        final VendorServiceKey other = (VendorServiceKey) obj;
        if (serviceNumber == null) {
            if (other.serviceNumber != null)
                return false;
        }
        else if (!serviceNumber.equals(other.serviceNumber))
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
