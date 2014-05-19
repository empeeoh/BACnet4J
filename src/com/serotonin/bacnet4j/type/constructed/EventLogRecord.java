/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * When signing a commercial license with Serotonin Software Technologies Inc.,
 * the following extension to GPL is made. A special exception to the GPL is 
 * included to allow you to distribute a combined work that includes BAcnet4J 
 * without being obliged to provide the source code for any proprietary components.
 */
package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedEventNotificationRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Real;
import org.free.bacnet4j.util.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class EventLogRecord extends BaseType {
    private static final long serialVersionUID = 7506599418976133752L;
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(LogStatus.class);
        classes.add(ConfirmedEventNotificationRequest.class);
        classes.add(Real.class);
    }

    private final DateTime timestamp;
    private final Choice choice;

    public EventLogRecord(DateTime timestamp, LogStatus datum) {
        this.timestamp = timestamp;
        choice = new Choice(0, datum);
    }

    public EventLogRecord(DateTime timestamp, ConfirmedEventNotificationRequest datum) {
        this.timestamp = timestamp;
        choice = new Choice(1, datum);
    }

    public EventLogRecord(DateTime timestamp, Real datum) {
        this.timestamp = timestamp;
        choice = new Choice(2, datum);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, timestamp, 0);
        write(queue, choice, 1);
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public LogStatus getLogStatus() {
        return (LogStatus) choice.getDatum();
    }

    public ConfirmedEventNotificationRequest getConfirmedEventNotificationRequest() {
        return (ConfirmedEventNotificationRequest) choice.getDatum();
    }

    public Real getReal() {
        return (Real) choice.getDatum();
    }

    public int getChoiceType() {
        return choice.getContextId();
    }

    public EventLogRecord(ByteQueue queue) throws BACnetException {
        timestamp = read(queue, DateTime.class, 0);
        choice = new Choice(queue, classes, 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((choice == null) ? 0 : choice.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final EventLogRecord other = (EventLogRecord) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        }
        else if (!choice.equals(other.choice))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        }
        else if (!timestamp.equals(other.timestamp))
            return false;
        return true;
    }
}
