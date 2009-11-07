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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
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
    
    public Address(Network network, byte[] ipAddress, int port) {
        if (network == null)
            networkNumber = new Unsigned16(0);
        else
            networkNumber = new Unsigned16(network.getNetworkNumber());
        
        byte[] ipMacAddress = new byte[ipAddress.length + 2];
        System.arraycopy(ipAddress, 0, ipMacAddress, 0, ipAddress.length);
        ipMacAddress[ipAddress.length] = (byte)(port >> 8);
        ipMacAddress[ipAddress.length + 1] = (byte)port;
        macAddress = new OctetString (ipMacAddress);
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
