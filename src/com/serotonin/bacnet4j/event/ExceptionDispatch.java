package com.serotonin.bacnet4j.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ExceptionDispatch {
    private static final List<ExceptionListener> listeners = new CopyOnWriteArrayList<ExceptionListener>();
    private static final ExceptionListener defaultExceptionListener = new DefaultExceptionListener();

    static {
        listeners.add(defaultExceptionListener);
    }

    public static void addListener(ExceptionListener l) {
        listeners.add(l);
    }

    public static void removeListener(ExceptionListener l) {
        listeners.remove(l);
    }

    public void removeDefaultExceptionListener() {
        listeners.remove(defaultExceptionListener);
    }

    public static void fireUnimplementedVendorService(UnsignedInteger vendorId, UnsignedInteger serviceNumber,
            ByteQueue queue) {
        for (ExceptionListener l : listeners)
            l.unimplementedVendorService(vendorId, serviceNumber, queue);
    }

    public static void fireReceivedException(Exception e) {
        for (ExceptionListener l : listeners)
            l.receivedException(e);
    }

    public static void fireReceivedThrowable(Throwable t) {
        for (ExceptionListener l : listeners)
            l.receivedThrowable(t);
    }
}
