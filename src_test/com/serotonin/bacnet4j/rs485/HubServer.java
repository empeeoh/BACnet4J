package com.serotonin.bacnet4j.rs485;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

/**
 * Creates a hub that distributes all messages to all listeners except the sender.
 */
public class HubServer implements Runnable {
    private static final Logger LOG = Logger.getLogger(HubServer.class.toString());

    public static void main(String[] args) throws Exception {
        new HubServer();
    }

    final List<SocketHandler> handlers = new CopyOnWriteArrayList<SocketHandler>();

    public HubServer() throws Exception {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            // Use the main thread for the socket acceptor.
            ServerSocket acceptor = new ServerSocket(50505);

            while (true) {
                Socket socket = acceptor.accept();
                try {
                    new Thread(new SocketHandler(socket)).start();
                }
                catch (Exception e) {
                    System.out.println("Failed to create socket handler");
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeToListeners(byte[] b, int len, SocketHandler source) {
        //LOG.info("From: " + source.id + ", writing " + StreamUtils.dumpArray(b, 0, len));
        for (SocketHandler h : handlers) {
            if (h != source)
                h.write(b, len);
        }
    }

    class SocketHandler implements Runnable {
        private final Socket socket;
        final String id;
        private final InputStream in;
        private final OutputStream out;

        public SocketHandler(Socket socket) throws Exception {
            this.socket = socket;
            id = socket.getRemoteSocketAddress().toString();
            in = socket.getInputStream();
            out = socket.getOutputStream();
        }

        @Override
        public void run() {
            handlers.add(this);
            System.out.println("Handler count: " + handlers.size());

            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int readcount = in.read(buffer);
                    writeToListeners(buffer, readcount, this);
                }
            }
            catch (IOException e) {
                System.out.println("Error reading from socket");
            }
            finally {
                handlers.remove(this);
                System.out.println("Handler count: " + handlers.size());
            }
        }

        void write(byte[] b, int len) {
            try {
                out.write(b, 0, len);
                out.flush();
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
