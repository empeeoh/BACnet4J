package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class ResultFlags extends BitString {
    public ResultFlags(boolean firstItem, boolean lastItem, boolean moreItems) {
        super(new boolean[] {firstItem, lastItem, moreItems});
    }

    public ResultFlags(ByteQueue queue) {
        super(queue);
    }

    public boolean isFirstItem() {
        return getValue()[0];
    }
    
    public boolean isLastItem() {
        return getValue()[1];
    }
    
    public boolean isMoreItems() {
        return getValue()[2];
    }
}
