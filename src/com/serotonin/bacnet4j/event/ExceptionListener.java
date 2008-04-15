package com.serotonin.bacnet4j.event;

public interface ExceptionListener {
    void receivedException(Exception e);
    void receivedThrowable(Throwable t);
}
