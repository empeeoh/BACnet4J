package com.serotonin.bacnet4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import com.serotonin.util.ArrayUtils;

public class UdpDump {
    public static void main(String args[]) throws Exception {
        int port = 0xbac0;
        DatagramSocket socket = new DatagramSocket(port);
        DatagramPacket packet; 
        
        // Broadcast whois
        byte[] data = new byte[] { (byte)0x81, 0xb, 0x0, 0x8, 0x1, 0x0, 0x10, 0x8 };
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
            
            System.out.println(packet.getAddress() +":"+ packet.getPort() +", length="+ packet.getLength());
            System.out.println(ArrayUtils.toHexString(packet.getData(), packet.getOffset(), packet.getLength()));
        }
    }
}
