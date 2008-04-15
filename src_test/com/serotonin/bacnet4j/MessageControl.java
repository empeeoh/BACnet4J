package com.serotonin.bacnet4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;

public class MessageControl extends Thread {
    // Config
    private int port;
    
    // Runtime
    private byte nextMessageId;
    private DatagramSocket socket;
    private HashMap<Byte, WaitingRoomMember> waitingRoom = new HashMap<Byte, WaitingRoomMember>();
    
    public MessageControl(int port) {
        this.port = port;
    }
    
    public void init() throws Exception {
        socket = new DatagramSocket(port);
        start();
    }
    
    public void destroy() {
        if (socket != null)
            socket.close();
    }
    
    
    // For sending
    public byte sendConfirmed(int port, byte value) throws Exception {
        long start = System.currentTimeMillis();
        
        byte id = nextMessageId++;
        
        Message request = new Message(MessageType.CONFIRMED_REQUEST, id, value);
        WaitingRoomMember wrm = new WaitingRoomMember();
        
        try {
            synchronized (wrm) {
                synchronized (waitingRoom) {
                    if (waitingRoom.get(id) != null)
                        throw new BACnetRuntimeException("Waiting room too crowded");
                    
                    waitingRoom.put(id, wrm);
                }
                send(request, InetAddress.getByName("localhost"), port);
                wrm.wait(30000);
            }
        }
        catch (InterruptedException e) {}
        
        synchronized (waitingRoom) {
            waitingRoom.remove(id);
        }
        
        Message response = wrm.getResponse();
        if (response == null)
            throw new BACnetException("Timeout while waiting for response for id "+ id);
        
        System.out.println("Took "+ (System.currentTimeMillis() - start) +"ms to send and receive");
        return response.getValue();
    }
    
    public void sendUnconfirmed(int port, byte value) throws Exception {
        Message m = new Message(MessageType.UNCONFIRMED_REQUEST, nextMessageId++, value);
        send(m, InetAddress.getByName("localhost"), port);
    }
    
    private void send(Message m, InetAddress address, int port) throws IOException {
        byte[] data = m.getData();
        DatagramPacket p = new DatagramPacket(data, data.length);
        p.setAddress(address);
        p.setPort(port);
        socket.send(p);
    }
    
    // For receiving
    public void run() {
        while (!socket.isClosed()) {
            DatagramPacket p = new DatagramPacket(new byte[10], 10);
            try {
                socket.receive(p);
            }
            catch (IOException e) {
                continue;
            }
            
            Message m = new Message(p.getData());
            if (m.getMessageType() == MessageType.CONFIRMED_REQUEST) {
                System.out.println("Received confirmed request: id="+ m.getMessageId() +", value="+ m.getValue());
                
                // Wait for a sec before sending response
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e1) {}
                
                Message response = new Message(MessageType.RESPONSE, m.getMessageId(), (byte)(m.getValue() + 1));
                try {
                    send(response, p.getAddress(), p.getPort());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (m.getMessageType() == MessageType.UNCONFIRMED_REQUEST)
                System.out.println("Received unconfirmed request: id="+ m.getMessageId() +", value="+ m.getValue());
            else if (m.getMessageType() == MessageType.RESPONSE) {
                System.out.println("Received response: id="+ m.getMessageId() +", value="+ m.getValue());
                synchronized (waitingRoom) {
                    WaitingRoomMember wrm = waitingRoom.get(m.getMessageId());
                    if (wrm == null)
                        System.out.println("No waiter for response id: "+ m.getMessageId());
                    else
                        wrm.setResponse(m);
                }
            }
        }
        System.out.println("Message control ended: port="+ port);
    }
    
    
    
    
    
    
    public interface MessageType {
        byte CONFIRMED_REQUEST = 0;
        byte UNCONFIRMED_REQUEST = 1;
        byte RESPONSE = 2;
    }
    
    public static class Message {
        private byte messageType;
        private byte messageId;
        private byte value;
        
        public Message(byte messageType, byte messageId, byte value) {
            this.messageType = messageType;
            this.messageId = messageId;
            this.value = value;
        }
        
        public Message(byte[] data) {
            messageType = data[0];
            messageId = data[1];
            value = data[2];
        }
        
        public byte[] getData() {
            return new byte[] {messageType, messageId, value};
        }

        public byte getMessageId() {
            return messageId;
        }

        public byte getMessageType() {
            return messageType;
        }

        public byte getValue() {
            return value;
        }
    }
    
    static class WaitingRoomMember {
        private Message response;
        
        void setResponse(Message response) {
            this.response = response;
            synchronized (this) {
                notify();
            }
        }
        
        Message getResponse() {
            return response;
        }
    }
}
