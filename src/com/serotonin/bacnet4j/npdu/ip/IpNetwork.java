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
package com.serotonin.bacnet4j.npdu.ip;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.serotonin.bacnet4j.BACnetNetwork;
import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.ArrayUtils;
import com.serotonin.util.queue.ByteQueue;

public class IpNetwork extends BACnetNetwork {
    private static final int DEFAULT_PORT = 0xbac0;
    private static final int MESSAGE_LENGTH = 1476;

    private int port = DEFAULT_PORT;

    // Runtime fields.
    private DatagramSocket socket;

    public IpNetwork() {
        // no op
    }

    public IpNetwork(int port) {
        this.port = port;
    }

    public void init() throws BACnetException {
        try {
            socket = new DatagramSocket(port);
            // TODO get the timeout from somewhere else
            socket.setSoTimeout(5000);
        }
        catch (SocketException e) {
            throw new BACnetException(e);
        }
    }

    public void destroy() {
        socket.close();
    }

    public void send(APDU apdu, String host) throws BACnetException {
        send(apdu, host, DEFAULT_PORT);
    }

    public void send(APDU apdu, String host, int port) throws BACnetException {
        ByteQueue queue = new ByteQueue();

        // BACnet virtual link layer detail
        queue.push(0x81); // BACnet/IP
        queue.push(0xa); // Original-Unicast-NPDU
        queue.push(0x0); // Length MSB
        queue.push(0x11); // Length LSB

        // Protocol version, always 1.
        queue.push((byte) 1);

        // Only confirmed requests expect a response
        if (apdu instanceof ConfirmedRequest)
            queue.push((byte) 4);
        else
            queue.push((byte) 0);

        apdu.write(queue);

        byte[] data = queue.popAll();

        try {
            // TODO get the retries from somewhere else
            int attempts = 2 + 1;

            while (true) {
                // Send the request.
                sendImpl(data, host, port);

                // Recieve the response.
                try {
                    // TODO
                    // ipResponse = receiveImpl();
                    receiveImpl();
                }
                catch (SocketTimeoutException e) {
                    attempts--;
                    if (attempts > 0)
                        // Try again.
                        continue;

                    throw new BACnetException(e);
                }

                // We got the response
                break;
            }

            // return ipResponse.getModbusResponse();
        }
        catch (IOException e) {
            throw new BACnetException(e);
        }
    }

    private void sendImpl(byte[] data, String host, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
        socket.send(packet);
    }

    private void receiveImpl() throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[MESSAGE_LENGTH], MESSAGE_LENGTH);
        socket.receive(packet);

        // TODO We could verify that the packet was received from the same address to which the request was sent,
        // but let's not bother with that yet.

        System.out.println(packet.getAddress() + ":" + packet.getPort());
        System.out.println(ArrayUtils.toHexString(packet.getData(), packet.getOffset(), packet.getLength()));

        // ByteQueue queue = new ByteQueue(packet.getData(), 0, packet.getLength());
        // System.out.println(queue);
    }
}
