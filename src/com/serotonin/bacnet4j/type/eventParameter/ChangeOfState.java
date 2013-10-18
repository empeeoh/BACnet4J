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
package com.serotonin.bacnet4j.type.eventParameter;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.PropertyStates;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class ChangeOfState extends EventParameter {
    private static final long serialVersionUID = -2481408388822644602L;

    public static final byte TYPE_ID = 1;

    private final UnsignedInteger timeDelay;
    private final SequenceOf<PropertyStates> listOfValues;

    public ChangeOfState(UnsignedInteger timeDelay, SequenceOf<PropertyStates> listOfValues) {
        this.timeDelay = timeDelay;
        this.listOfValues = listOfValues;
    }

    public ChangeOfState(ByteQueue queue) throws BACnetException {
        timeDelay = read(queue, UnsignedInteger.class, 0);
        listOfValues = readSequenceOf(queue, PropertyStates.class, 1);
    }

    @Override
    protected void writeImpl(ByteQueue queue) {
        write(queue, timeDelay, 0);
        write(queue, listOfValues, 1);
    }

    @Override
    protected int getTypeId() {
        return TYPE_ID;
    }

    public UnsignedInteger getTimeDelay() {
        return timeDelay;
    }

    public SequenceOf<PropertyStates> getListOfValues() {
        return listOfValues;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfValues == null) ? 0 : listOfValues.hashCode());
        result = PRIME * result + ((timeDelay == null) ? 0 : timeDelay.hashCode());
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
        final ChangeOfState other = (ChangeOfState) obj;
        if (listOfValues == null) {
            if (other.listOfValues != null)
                return false;
        }
        else if (!listOfValues.equals(other.listOfValues))
            return false;
        if (timeDelay == null) {
            if (other.timeDelay != null)
                return false;
        }
        else if (!timeDelay.equals(other.timeDelay))
            return false;
        return true;
    }
}
