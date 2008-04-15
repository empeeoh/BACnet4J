package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class CovSubscription extends BaseType {
    private RecipientProcess recipient;
    private ObjectPropertyReference monitoredPropertyReference;
    private Boolean issueConfirmedNotifications;
    private UnsignedInteger timeRemaining;
    private Real covIncrement;
    
    public CovSubscription(RecipientProcess recipient, ObjectPropertyReference monitoredPropertyReference, 
            Boolean issueConfirmedNotifications, UnsignedInteger timeRemaining, Real covIncrement) {
        this.recipient = recipient;
        this.monitoredPropertyReference = monitoredPropertyReference;
        this.issueConfirmedNotifications = issueConfirmedNotifications;
        this.timeRemaining = timeRemaining;
        this.covIncrement = covIncrement;
    }

    public void write(ByteQueue queue) {
        write(queue, recipient, 0);
        write(queue, monitoredPropertyReference, 1);
        write(queue, issueConfirmedNotifications, 2);
        write(queue, timeRemaining, 3);
        writeOptional(queue, covIncrement, 4);
    }
    
    public CovSubscription(ByteQueue queue) throws BACnetException {
        recipient = read(queue, RecipientProcess.class, 0);
        monitoredPropertyReference = read(queue, ObjectPropertyReference.class, 1);
        issueConfirmedNotifications = read(queue, Boolean.class, 2);
        timeRemaining = read(queue, UnsignedInteger.class, 3);
        covIncrement = readOptional(queue, Real.class, 4);
    }
}
