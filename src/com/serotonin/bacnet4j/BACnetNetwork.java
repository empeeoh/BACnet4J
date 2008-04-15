package com.serotonin.bacnet4j;

import com.serotonin.io.messaging.DefaultExceptionListener;
import com.serotonin.io.messaging.MessagingConnectionListener;

abstract public class BACnetNetwork {
    private MessagingConnectionListener exceptionListener = new DefaultExceptionListener();

    public void setExceptionListener(MessagingConnectionListener exceptionListener) {
        if (exceptionListener == null)
            this.exceptionListener = new DefaultExceptionListener();
        else
            this.exceptionListener = exceptionListener;
    }
    
    public MessagingConnectionListener getExceptionListener() {
        return exceptionListener;
    }
}
