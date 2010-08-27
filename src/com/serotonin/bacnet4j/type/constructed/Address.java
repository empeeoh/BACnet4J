/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.serotonin.bacnet4j.type.constructed;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.IpAddressUtils;
import com.serotonin.util.queue.ByteQueue;

public class Address extends BaseType {
    private static final long serialVersionUID = -3376358193474831753L;
    private final Unsigned16 networkNumber;
    private final OctetString macAddress;

    public Address(Unsigned16 networkNumber, OctetString macAddress) {
        this.networkNumber = networkNumber;
        this.macAddress = macAddress;
    }

    public Address(byte[] ipAddress, int port) {
        this(null, ipAddress, port);
    }

    public Address(Network network, byte[] ipAddress, int port) {
        if (network == null)
            networkNumber = new Unsigned16(0);
        else
            networkNumber = new Unsigned16(network.getNetworkNumber());

        byte[] ipMacAddress = new byte[ipAddress.length + 2];
        System.arraycopy(ipAddress, 0, ipMacAddress, 0, ipAddress.length);
        ipMacAddress[ipAddress.length] = (byte) (port >> 8);
        ipMacAddress[ipAddress.length + 1] = (byte) port;
        macAddress = new OctetString(ipMacAddress);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, networkNumber);
        write(queue, macAddress);
    }

    public Address(ByteQueue queue) throws BACnetException {
        networkNumber = read(queue, Unsigned16.class);
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
        return "Address(networkNumber=" + networkNumber + ", macAddress=" + macAddress + ")";
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByAddress(getIpBytes());
    }

    public int getPort() {
        byte[] b = macAddress.getBytes();
        if (b.length == 6)
            return ((b[4] & 0xff) << 8) | (b[5] & 0xff);
        return -1;
    }

    public String toIpString() {
        return IpAddressUtils.toIpString(getIpBytes());
    }

    public String toIpPortString() {
        return toIpString() + ":" + getPort();
    }

    private byte[] getIpBytes() {
        if (macAddress.getLength() == 4)
            return macAddress.getBytes();

        byte[] b = new byte[4];
        System.arraycopy(macAddress.getBytes(), 0, b, 0, 4);
        return b;
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
