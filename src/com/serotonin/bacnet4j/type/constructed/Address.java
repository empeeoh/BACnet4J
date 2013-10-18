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
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.constructed;

import java.net.InetSocketAddress;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Unsigned16;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class Address extends BaseType {
    public static final int LOCAL_NETWORK = 0;
    public static final Address GLOBAL = new Address(new Unsigned16(0xFFFF), null);

    private static final long serialVersionUID = -3376358193474831753L;
    private final Unsigned16 networkNumber;
    private final OctetString macAddress;

    public Address(int networkNumber, byte[] macAddress) {
        this(new Unsigned16(networkNumber), new OctetString(macAddress));
    }

    public Address(int networkNumber, String dottedString) {
        this(new Unsigned16(networkNumber), new OctetString(dottedString));
    }

    public Address(OctetString macAddress) {
        this(LOCAL_NETWORK, macAddress);
    }

    public Address(int networkNumber, OctetString macAddress) {
        this(new Unsigned16(networkNumber), macAddress);
    }

    public Address(Unsigned16 networkNumber, OctetString macAddress) {
        this.networkNumber = networkNumber;
        this.macAddress = macAddress;
    }

    /**
     * Convenience constructor for MS/TP addresses local to this network.
     * 
     * @param station
     *            the station id
     */
    public Address(byte station) {
        this(LOCAL_NETWORK, station);
    }

    /**
     * Convenience constructor for MS/TP addresses remote to this network.
     * 
     * @param network
     * @param station
     */
    public Address(int networkNumber, byte station) {
        this.networkNumber = new Unsigned16(networkNumber);
        macAddress = new OctetString(new byte[] { station });
    }

    /**
     * Convenience constructor for IP addresses local to this network.
     * 
     * @param ipAddress
     * @param port
     */
    public Address(byte[] ipAddress, int port) {
        this(LOCAL_NETWORK, ipAddress, port);
    }

    /**
     * Convenience constructor for IP addresses remote to this network.
     * 
     * @param network
     * @param ipAddress
     * @param port
     */
    public Address(int networkNumber, byte[] ipAddress, int port) {
        this.networkNumber = new Unsigned16(networkNumber);

        byte[] ipMacAddress = new byte[ipAddress.length + 2];
        System.arraycopy(ipAddress, 0, ipMacAddress, 0, ipAddress.length);
        ipMacAddress[ipAddress.length] = (byte) (port >> 8);
        ipMacAddress[ipAddress.length + 1] = (byte) port;
        macAddress = new OctetString(ipMacAddress);
    }

    public Address(String host, int port) {
        this(LOCAL_NETWORK, host, port);
    }

    public Address(int networkNumber, String host, int port) {
        this(networkNumber, InetAddrCache.get(host, port));
    }

    public Address(InetSocketAddress addr) {
        this(LOCAL_NETWORK, addr.getAddress().getAddress(), addr.getPort());
    }

    public Address(int networkNumber, InetSocketAddress addr) {
        this(networkNumber, addr.getAddress().getAddress(), addr.getPort());
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

    public boolean isGlobal() {
        return networkNumber.intValue() == 0xFFFF;
    }

    //    //
    //    //
    //    // I/P convenience
    //    //
    //    public String getMacAddressDottedString() {
    //        return macAddress.getMacAddressDottedString();
    //    }
    //
    //    public InetAddress getInetAddress() {
    //        return macAddress.getInetAddress();
    //    }
    //
    //    public InetSocketAddress getInetSocketAddress() {
    //        return macAddress.getInetSocketAddress();
    //    }
    //
    //    public int getPort() {
    //        return macAddress.getPort();
    //    }
    //
    //    public String toIpString() {
    //        return macAddress.toIpString();
    //    }
    //
    //    public String toIpPortString() {
    //        return macAddress.toIpPortString();
    //    }
    //
    //    //
    //    //
    //    // MS/TP convenience
    //    //
    //    public byte getMstpAddress() {
    //        return macAddress.getBytes()[0];
    //    }
    //
    //    @Override
    //    public String toString() {
    //        return "Address(networkNumber=" + networkNumber + ", macAddress=" + macAddress + ")";
    //    }

    //
    //
    // General convenience
    //
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(macAddress.getDescription());
        if (networkNumber.intValue() != 0)
            sb.append('(').append(networkNumber).append(')');
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Address [networkNumber=" + networkNumber + ", macAddress=" + macAddress + "]";
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
