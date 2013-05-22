package com.serotonin.bacnet4j.util;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class RequestListenerUpdater {
    private final RequestListener callback;
    private final PropertyValues propertyValues;
    private final int max;
    private int current;
    private boolean cancelled;

    public RequestListenerUpdater(RequestListener callback, PropertyValues propertyValues, int max) {
        this.callback = callback;
        this.propertyValues = propertyValues;
        this.max = max;
    }

    public void increment(ObjectIdentifier oid, PropertyIdentifier pid, UnsignedInteger pin, Encodable value) {
        current++;
        if (callback != null)
            cancelled = callback.requestProgress(((double) current) / max, oid, pid, pin, value);
        propertyValues.add(oid, pid, pin, value);
    }

    public boolean cancelled() {
        return cancelled;
    }
}
