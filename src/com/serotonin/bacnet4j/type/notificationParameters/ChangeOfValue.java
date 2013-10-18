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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.StatusFlags;
import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.bacnet4j.type.primitive.Real;
import org.free.bacnet4j.util.ByteQueue;

public class ChangeOfValue extends NotificationParameters {
    private static final long serialVersionUID = -5744561310526587892L;

    public static final byte TYPE_ID = 2;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(BitString.class);
        classes.add(Real.class);
    }

    private final Choice newValue;
    private final StatusFlags statusFlags;

    public ChangeOfValue(BitString newValue, StatusFlags statusFlags) {
        this.newValue = new Choice(0, newValue);
        this.statusFlags = statusFlags;
    }

    public ChangeOfValue(Real newValue, StatusFlags statusFlags) {
        this.newValue = new Choice(1, newValue);
        this.statusFlags = statusFlags;
    }

    @Override
    protected void writeImpl(ByteQueue queue) {
        write(queue, newValue, 0);
        write(queue, statusFlags, 1);
    }

    public ChangeOfValue(ByteQueue queue) throws BACnetException {
        newValue = new Choice(queue, classes, 0);
        statusFlags = read(queue, StatusFlags.class, 1);
    }

    @Override
    protected int getTypeId() {
        return TYPE_ID;
    }

    public Choice getNewValue() {
        return newValue;
    }

    public StatusFlags getStatusFlags() {
        return statusFlags;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((newValue == null) ? 0 : newValue.hashCode());
        result = PRIME * result + ((statusFlags == null) ? 0 : statusFlags.hashCode());
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
        final ChangeOfValue other = (ChangeOfValue) obj;
        if (newValue == null) {
            if (other.newValue != null)
                return false;
        }
        else if (!newValue.equals(other.newValue))
            return false;
        if (statusFlags == null) {
            if (other.statusFlags != null)
                return false;
        }
        else if (!statusFlags.equals(other.statusFlags))
            return false;
        return true;
    }
}
