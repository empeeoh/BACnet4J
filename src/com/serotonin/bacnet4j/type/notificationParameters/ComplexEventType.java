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
package com.serotonin.bacnet4j.type.notificationParameters;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import org.free.bacnet4j.util.ByteQueue;

public class ComplexEventType extends NotificationParameters {
    private static final long serialVersionUID = -5125532863892322124L;

    public static final byte TYPE_ID = 6;

    private final SequenceOf<PropertyValue> values;

    public ComplexEventType(SequenceOf<PropertyValue> values) {
        this.values = values;
    }

    @Override
    protected void writeImpl(ByteQueue queue) {
        write(queue, values);
    }

    public ComplexEventType(ByteQueue queue) throws BACnetException {
        values = readSequenceOf(queue, PropertyValue.class);
    }

    @Override
    protected int getTypeId() {
        return TYPE_ID;
    }

    public SequenceOf<PropertyValue> getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((values == null) ? 0 : values.hashCode());
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
        final ComplexEventType other = (ComplexEventType) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        }
        else if (!values.equals(other.values))
            return false;
        return true;
    }
}
