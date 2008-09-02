/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j;

import java.util.Arrays;

import com.serotonin.util.ArrayUtils;

/**
 * @author Matthew Lohbihler
 */
public class Network {
    private final int networkNumber;
    private final byte[] networkAddress;
    
    public Network(int networkNumber, byte[] networkAddress) {
        this.networkNumber = networkNumber;
        this.networkAddress = networkAddress;
    }
    
    public int getNetworkNumber() {
        return networkNumber;
    }
    public byte[] getNetworkAddress() {
        return networkAddress;
    }
    
    @Override
    public String toString() {
        return "Network(networkNumber="+ networkNumber +", networkAddress="+ ArrayUtils.toHexString(networkAddress) +")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(networkAddress);
        result = prime * result + networkNumber;
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
        final Network other = (Network) obj;
        if (!Arrays.equals(networkAddress, other.networkAddress))
            return false;
        if (networkNumber != other.networkNumber)
            return false;
        return true;
    }
}
