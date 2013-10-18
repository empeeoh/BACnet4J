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

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.Time;
import org.free.bacnet4j.util.ByteQueue;
/**
 * ASHRAE Standard 135-2012 Clause 21 p. 712 <br>
 * BACnetTimeValue ::= SEQUENCE { <br>
 * time Time, <br>
 * value ABSTRACT-SYNTAX.&Type -- any primitive datatype; complex types cannot be decoded <br>
 * }
 */
public class TimeValue extends BaseType {
    private static final long serialVersionUID = 6449737212397369712L;
    private final Time time;
    private final Primitive value;

    public TimeValue(Time time, Primitive value) {
        this.time = time;
        this.value = value;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, time);
        write(queue, value);
    }

    public TimeValue(ByteQueue queue) throws BACnetException {
        time = read(queue, Time.class);
        value = read(queue, Primitive.class);
    }

    public Time getTime() {
        return time;
    }

    public Primitive getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((time == null) ? 0 : time.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
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
        final TimeValue other = (TimeValue) obj;
        if (time == null) {
            if (other.time != null)
                return false;
        }
        else if (!time.equals(other.time))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }
}
