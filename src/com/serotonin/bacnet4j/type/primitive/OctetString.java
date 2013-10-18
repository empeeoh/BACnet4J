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
package com.serotonin.bacnet4j.type.primitive;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import org.free.bacnet4j.util.ArrayUtils;
import org.free.bacnet4j.util.IpAddressUtils;
import org.free.bacnet4j.util.ByteQueue;

public class OctetString extends Primitive {
    private static final long serialVersionUID = -3557657941142811228L;

    public static final byte TYPE_ID = 6;

    private final byte[] value;

    public OctetString(byte[] value) {
        this.value = value;
    }

    public OctetString(String dottedString) {
        this(dottedString, IpNetwork.DEFAULT_PORT);
    }

    public OctetString(String dottedString, int defaultPort) {
        dottedString = dottedString.trim();
        int colon = dottedString.indexOf(":");
        if (colon == -1) {
            byte[] b = BACnetUtils.dottedStringToBytes(dottedString);
            if (b.length == 4)
                value = toBytes(b, defaultPort);
            else
                value = b;
        }
        else {
            byte[] ip = BACnetUtils.dottedStringToBytes(dottedString.substring(0, colon));
            int port = Integer.parseInt(dottedString.substring(colon + 1));
            value = toBytes(ip, port);
        }
    }

    /**
     * Convenience constructor for MS/TP addresses local to this network.
     * 
     * @param station
     *            the station id
     */
    public OctetString(byte station) {
        value = new byte[] { station };
    }

    /**
     * Convenience constructor for IP addresses local to this network.
     * 
     * @param ipAddress
     * @param port
     */
    public OctetString(byte[] ipAddress, int port) {
        value = toBytes(ipAddress, port);
    }

    public OctetString(InetSocketAddress addr) {
        this(addr.getAddress().getAddress(), addr.getPort());
    }

    public byte[] getBytes() {
        return value;
    }

    private static byte[] toBytes(byte[] ipAddress, int port) {
        if (ipAddress.length != 4)
            throw new IllegalArgumentException("IP address must have 4 parts, not " + ipAddress.length);

        byte[] b = new byte[6];
        System.arraycopy(ipAddress, 0, b, 0, ipAddress.length);
        b[ipAddress.length] = (byte) (port >> 8);
        b[ipAddress.length + 1] = (byte) port;
        return b;
    }

    //
    //
    // I/P convenience
    //
    public String getMacAddressDottedString() {
        return BACnetUtils.bytesToDottedString(value);
    }

    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByAddress(getIpBytes());
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public InetSocketAddress getInetSocketAddress() {
        return InetAddrCache.get(getInetAddress(), getPort());
    }

    public int getPort() {
        if (value.length == 6)
            return ((value[4] & 0xff) << 8) | (value[5] & 0xff);
        return -1;
    }

    public String toIpString() {
        return IpAddressUtils.toIpString(getIpBytes());
    }

    public String toIpPortString() {
        return toIpString() + ":" + getPort();
    }

    public byte[] getIpBytes() {
        if (value.length == 4)
            return value;

        byte[] b = new byte[4];
        System.arraycopy(value, 0, b, 0, 4);
        return b;
    }

    //
    //
    // MS/TP convenience
    //
    public byte getMstpAddress() {
        return value[0];
    }

    //
    // Reading and writing
    //
    public OctetString(ByteQueue queue) {
        int length = (int) readTag(queue);
        value = new byte[length];
        queue.pop(value);
    }

    @Override
    public void writeImpl(ByteQueue queue) {
        queue.push(value);
    }

    @Override
    public long getLength() {
        return value.length;
    }

    @Override
    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(value);
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
        final OctetString other = (OctetString) obj;
        if (!Arrays.equals(value, other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ArrayUtils.toHexString(value);
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (value.length == 1)
            // Assume an MS/TP address
            sb.append(getMstpAddress() & 0xff);
        else if (value.length == 6)
            // Assume an I/P address
            sb.append(toIpPortString());
        else
            sb.append(toString());
        return sb.toString();
    }
}
