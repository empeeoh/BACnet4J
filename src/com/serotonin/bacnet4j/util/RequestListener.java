package com.serotonin.bacnet4j.util;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public interface RequestListener {
    /**
     * Provides the current progress from 0 (not started, never actually sent) to 1 (finished and will not called again)
     * as well as an opportunity for the client to cancel the request. Other parameters represent the property that was
     * just received.
     * 
     * @param progress
     *            the current progress amount
     * @param oid
     *            the oid of the property that was received
     * @param pid
     *            the property id of the property that was received
     * @param pin
     *            the index of the property that was received
     * @param value
     *            the value of the property that was received
     * @return true if the request should be cancelled.
     */
    boolean requestProgress(double progress, ObjectIdentifier oid, PropertyIdentifier pid, UnsignedInteger pin,
            Encodable value);
}
