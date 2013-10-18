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
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class LogRecord extends BaseType {
    private static final long serialVersionUID = -158114439196293884L;
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(LogStatus.class);
        classes.add(Boolean.class);
        classes.add(Real.class);
        classes.add(Enumerated.class);
        classes.add(UnsignedInteger.class);
        classes.add(SignedInteger.class);
        classes.add(BitString.class);
        classes.add(Null.class);
        classes.add(BACnetError.class);
        classes.add(Real.class);
        classes.add(Encodable.class);
    }

    private final DateTime timestamp;
    private Choice choice;
    private final StatusFlags statusFlags;

    private LogRecord(DateTime timestamp, StatusFlags statusFlags) {
        this.timestamp = timestamp;
        this.statusFlags = statusFlags;
    }

    public LogRecord(DateTime timestamp, LogStatus datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(0, datum);
    }

    public LogRecord(DateTime timestamp, Boolean datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(1, datum);
    }

    public LogRecord(DateTime timestamp, boolean timeChange, Real datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(timeChange ? 9 : 2, datum);
    }

    public LogRecord(DateTime timestamp, Enumerated datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(3, datum);
    }

    public LogRecord(DateTime timestamp, UnsignedInteger datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(4, datum);
    }

    public LogRecord(DateTime timestamp, SignedInteger datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(5, datum);
    }

    public LogRecord(DateTime timestamp, BitString datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(6, datum);
    }

    public LogRecord(DateTime timestamp, Null datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(7, datum);
    }

    public LogRecord(DateTime timestamp, BACnetError datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(8, datum);
    }

    public LogRecord(DateTime timestamp, Encodable datum, StatusFlags statusFlags) {
        this(timestamp, statusFlags);
        choice = new Choice(10, datum);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, timestamp, 0);
        write(queue, choice, 1);
        writeOptional(queue, statusFlags, 2);
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public StatusFlags getStatusFlags() {
        return statusFlags;
    }

    public LogStatus getLogStatus() {
        return (LogStatus) choice.getDatum();
    }

    public Boolean getBoolean() {
        return (Boolean) choice.getDatum();
    }

    public Real getReal() {
        return (Real) choice.getDatum();
    }

    public Real getTimeChange() {
        return (Real) choice.getDatum();
    }

    public Enumerated getEnumerated() {
        return (Enumerated) choice.getDatum();
    }

    public UnsignedInteger getUnsignedInteger() {
        return (UnsignedInteger) choice.getDatum();
    }

    public SignedInteger getSignedInteger() {
        return (SignedInteger) choice.getDatum();
    }

    public BitString getBitString() {
        return (BitString) choice.getDatum();
    }

    public Null getNull() {
        return (Null) choice.getDatum();
    }

    public BACnetError getBACnetError() {
        return (BACnetError) choice.getDatum();
    }

    public Encodable getEncodable() {
        return choice.getDatum();
    }

    public int getChoiceType() {
        return choice.getContextId();
    }

    public LogRecord(ByteQueue queue) throws BACnetException {
        timestamp = read(queue, DateTime.class, 0);
        choice = new Choice(queue, classes, 1);
        statusFlags = readOptional(queue, StatusFlags.class, 2);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((choice == null) ? 0 : choice.hashCode());
        result = PRIME * result + ((statusFlags == null) ? 0 : statusFlags.hashCode());
        result = PRIME * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
        final LogRecord other = (LogRecord) obj;
        if (choice == null) {
            if (other.choice != null)
                return false;
        }
        else if (!choice.equals(other.choice))
            return false;
        if (statusFlags == null) {
            if (other.statusFlags != null)
                return false;
        }
        else if (!statusFlags.equals(other.statusFlags))
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
