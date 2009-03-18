/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
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
