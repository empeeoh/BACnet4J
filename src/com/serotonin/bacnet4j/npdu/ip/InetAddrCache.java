package com.serotonin.bacnet4j.npdu.ip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class InetAddrCache {
    private static final Map<InetAddress, Map<Integer, InetSocketAddress>> socketCache = new HashMap<InetAddress, Map<Integer, InetSocketAddress>>();

    public static InetSocketAddress get(String host, int port) {
        try {
            return get(InetAddress.getByName(host), port);
        }
        catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * InetSocketAddress cache, because instantiation can take up to 10 seconds on Android.
     * ??? Should there be a means of purging this?
     * 
     * @param addr
     * @param port
     * @return
     */
    public static InetSocketAddress get(InetAddress addr, int port) {
        Map<Integer, InetSocketAddress> ports = socketCache.get(addr);
        if (ports == null) {
            synchronized (socketCache) {
                ports = socketCache.get(addr);
                if (ports == null) {
                    ports = new HashMap<Integer, InetSocketAddress>();
                    socketCache.put(addr, ports);
                }
            }
        }

        InetSocketAddress socket = ports.get(port);
        if (socket == null) {
            synchronized (ports) {
                socket = ports.get(port);
                if (socket == null) {
                    socket = new InetSocketAddress(addr, port);
                    ports.put(port, socket);
                }
            }
        }

        return socket;
    }
}
