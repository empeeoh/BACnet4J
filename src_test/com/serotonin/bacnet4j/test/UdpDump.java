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
package com.serotonin.bacnet4j.test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class UdpDump {
    public static void main(String args[]) throws Exception {
        int port = 0xbac0;
        DatagramSocket socket = new DatagramSocket(port);
        DatagramPacket packet; 
        
        // Broadcast whois
        //byte[] data = new byte[] { (byte)0x81, 0xb, 0x0, 0x8, 0x1, 0x0, 0x10, 0x8 };
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.1.255", 47808));
        
        // Network broadcast whois.
        byte[] data = new byte[] { (byte)0x81, 0xb, 0x0, 0xc, 0x1, 0x20, (byte)0xff, (byte)0xff, 0x0, (byte)0xff, 0x10, 0x8 };
        packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.1.255", 47808));

        // Non-broadcast whois
        //byte[] data = new byte[] { (byte)0x81, 0xa, 0x0, 0x8, 0x1, 0x0, 0x10, 0x8 };
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.1.255", 47808));
        
        // Non-broadcast whois from device # 105-105
        //byte[] data = new byte[] { (byte)0x81, 0xa, 0x0, 0xc, 0x1, 0x0, 0x10, 0x8 , 0x9, 0x69, 0x19, 0x69 };
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.1.255", 47808));
        
        // Read property services supported
        //byte[] data = new byte[] { (byte)0x81, 0xa, 0x0, 0x11, 0x1, 0x4, 0x2, 0x75, 0x0, 0xc, 0xc, 0x2, 0x0, 0x0, 0x69 , 0x19, 0x61 };
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.1.53", 47808));
         
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.0.6", 47808));
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("255.255.255.255", 47808));
        //packet = new DatagramPacket(data, data.length, new InetSocketAddress("192.168.0.255", 47808));
        socket.send(packet);
        
        // Listen
        while (true) {
            packet = new DatagramPacket(new byte[1497], 1497);
            socket.receive(packet);
            
            //System.out.println(packet.getAddress() +":"+ packet.getPort() +", length="+ packet.getLength());
            //System.out.println(ArrayUtils.toHexString(packet.getData(), packet.getOffset(), packet.getLength()));
            System.out.println(new String(packet.getData()));
        }
    }
}
