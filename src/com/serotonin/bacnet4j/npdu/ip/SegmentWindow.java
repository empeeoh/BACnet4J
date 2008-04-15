package com.serotonin.bacnet4j.npdu.ip;

import com.serotonin.bacnet4j.apdu.Segmentable;

public class SegmentWindow {
    private int firstSequenceId;
    private Segmentable[] segments;
    
    public SegmentWindow(int windowSize, int firstSequenceId) {
        this.firstSequenceId = firstSequenceId;
        segments = new Segmentable[windowSize];
    }
    
    public Segmentable getSegment(int sequenceId) {
        return segments[sequenceId - firstSequenceId];
    }
    
    public void setSegment(Segmentable segment) {
        segments[segment.getSequenceNumber() - firstSequenceId] = segment;
    }
    
    public boolean fitsInWindow(Segmentable segment) {
        int index = segment.getSequenceNumber() - firstSequenceId;
        if (index < 0 || index >= segments.length)
            return false;
        return true;
    }
    
    public boolean isEmpty() {
        for (int i=0; i<segments.length; i++) {
            if (segments[i] != null)
                return false;
        }
        return true;
    }
    
    public boolean isFull() {
        for (int i=0; i<segments.length; i++) {
            if (segments[i] == null)
                return false;
        }
        return true;
    }
    
    public void clear(int firstSequenceId) {
        this.firstSequenceId = firstSequenceId;
        for (int i=0; i<segments.length; i++)
            segments[i] = null;
    }
    
    public boolean isLastSegment(int sequenceId) {
        return sequenceId == segments.length + firstSequenceId - 1;
    }
}
