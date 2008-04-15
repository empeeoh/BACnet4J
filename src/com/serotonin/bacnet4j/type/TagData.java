package com.serotonin.bacnet4j.type;

public class TagData {
    public int tagNumber;
    public boolean contextSpecific;
    public long length;
    public int tagLength;
    
    public int getTotalLength() {
        return (int)(length + tagLength);
    }
    
    public boolean isStartTag() {
        return contextSpecific && ((length & 6) == 6);
    }
    
    public boolean isStartTag(int contextId) {
        return isStartTag() && tagNumber == contextId;
    }
    
    public boolean isEndTag() {
        return contextSpecific && ((length & 7) == 7);
    }
    
    public boolean isEndTag(int contextId) {
        return isEndTag() && tagNumber == contextId;
    }
}
