package com.serotonin.bacnet4j.npdu.mstp;

public interface FrameResponseListener {
    void response(Frame frame);

    void timeout();
}
