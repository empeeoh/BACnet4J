package com.serotonin.bacnet4j.npdu.mstp;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Using a different convention for instance naming here to see how it feels.
 */
public enum FrameType {
    token(0), //
    pollForMaster(1), //
    replyToPollForMaster(2), //
    testRequest(3), //
    testResponse(4), //
    bacnetDataExpectingReply(5), //
    bacnetDataNotExpectingReply(6), //
    replyPostponed(7), //
    ;

    public final byte id;

    private FrameType(int id) {
        this.id = (byte) id;
    }

    public static FrameType forId(byte b) {
        for (FrameType e : values()) {
            if (e.id == b)
                return e;
        }
        return null;
    }

    public boolean oneOf(FrameType... types) {
        return ArrayUtils.contains(types, this);
    }
}
