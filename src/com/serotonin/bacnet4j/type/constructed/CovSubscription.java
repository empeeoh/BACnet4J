/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class CovSubscription extends BaseType {
    private final RecipientProcess recipient;
    private final ObjectPropertyReference monitoredPropertyReference;
    private final Boolean issueConfirmedNotifications;
    private final UnsignedInteger timeRemaining;
    private final Real covIncrement;
    
    public CovSubscription(RecipientProcess recipient, ObjectPropertyReference monitoredPropertyReference, 
            Boolean issueConfirmedNotifications, UnsignedInteger timeRemaining, Real covIncrement) {
        this.recipient = recipient;
        this.monitoredPropertyReference = monitoredPropertyReference;
        this.issueConfirmedNotifications = issueConfirmedNotifications;
        this.timeRemaining = timeRemaining;
        this.covIncrement = covIncrement;
    }

    @Override
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
