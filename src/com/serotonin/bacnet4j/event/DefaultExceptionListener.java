package com.serotonin.bacnet4j.event;

public class DefaultExceptionListener implements ExceptionListener {
    public void receivedException(Exception e) {
        e.printStackTrace();
    }
    
    public void receivedThrowable(Throwable t) {
        t.printStackTrace();
    }
}
