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

    public RequestListenerUpdater(final RequestListener callback, 
    							  final PropertyValues propertyValues, 
    							  final int max) {
        this.callback = callback;
        this.propertyValues = propertyValues;
        this.max = max;
    }

    public void increment(final ObjectIdentifier oid, 
    					  final PropertyIdentifier pid, 
    					  final UnsignedInteger propindex, 
    					  final Encodable value) {
        current++;
        if (callback != null)
            cancelled = callback.requestProgress(((double) current) / max, oid, pid, 
            									 propindex, value);
        propertyValues.add(oid, pid, propindex, value);
    }

    public boolean cancelled() {
        return cancelled;
    }
}
