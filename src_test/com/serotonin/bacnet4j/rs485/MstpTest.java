package com.serotonin.bacnet4j.rs485;

import java.net.Socket;

import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.SlaveNode;

public class MstpTest {
    public static void main(String[] args) throws Exception {
        new MstpTest();
    }

    MstpTest() throws Exception {
        new HubServer();
        master((byte) 0, 3);
        master((byte) 1, 3);
        master((byte) 2, 3);
        master((byte) 3, 3);
        master((byte) 4, 3);
        master((byte) 10, 3);
        master((byte) 15, 3);
        master((byte) 20, 3);
        master((byte) 25, 3);
        master((byte) 30, 3);
        master((byte) 35, 3);
        master((byte) 40, 3);
        master((byte) 45, 3);
        master((byte) 50, 3);
        master((byte) 55, 3);
        master((byte) 70, 3);
        master((byte) 80, 3);
        master((byte) 110, 3);
        slave((byte) 5);
        slave((byte) 6);
        slave((byte) 7);
        slave((byte) 8);
        slave((byte) 9);
        slave((byte) 71);
        slave((byte) 72);
        slave((byte) 73);
    }

    void master(byte station, int retryCount) throws Exception {
        // Set up the socket
        Socket socket = new Socket("localhost", 50505);
        new MasterNode(socket.getInputStream(), socket.getOutputStream(), station, retryCount).initialize();
        Thread.sleep(1000);
    }

    void slave(byte station) throws Exception {
        // Set up the socket
        Socket socket = new Socket("localhost", 50505);
        new SlaveNode(socket.getInputStream(), socket.getOutputStream(), station).initialize();
        Thread.sleep(1000);
    }
}
