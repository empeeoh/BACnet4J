package com.serotonin.bacnet4j.apdu;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.util.queue.ByteQueue;

public interface Segmentable {
    byte getInvokeId();
    boolean isSegmentedMessage();
    boolean isMoreFollows();
    int getSequenceNumber();
    int getProposedWindowSize();
    void appendServiceData(ByteQueue segmentable);
    void parseServiceData() throws BACnetException;
    ByteQueue getServiceData();
    APDU clone(boolean moreFollows, int sequenceNumber, int actualSegWindow, ByteQueue data);
}
