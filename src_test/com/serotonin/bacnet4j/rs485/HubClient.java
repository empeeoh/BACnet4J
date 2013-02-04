package com.serotonin.bacnet4j.rs485;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class HubClient {
    public static void main(String[] args) throws Exception {
        new HubClient();
    }

    Socket socket;

    HubClient() throws Exception {
        // Set up the socket
        socket = new Socket("localhost", 50505);
        new Thread(new MessageReader()).start();
        OutputStream out = socket.getOutputStream();

        // Write the occasional message.
        Random random = new Random();

        byte[] msg = new byte[random.nextInt(2000)];
        random.nextBytes(msg);
        System.out.println("Message length=" + msg.length);

        try {
            while (true) {
                Thread.sleep(random.nextInt(3000));
                out.write(msg);
            }
        }
        finally {
            socket.close();
        }
    }

    class MessageReader implements Runnable {
        private final InputStream in;

        public MessageReader() throws Exception {
            in = socket.getInputStream();
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];

                while (true) {
                    int readcount = in.read(buffer);
                    System.out.println("Read " + readcount + " bytes");
                }
            }
            catch (IOException e) {
                try {
                    socket.close();
                }
                catch (IOException e1) {
                    System.out.println("Error closing socket");
                }
            }
        }
    }
}
