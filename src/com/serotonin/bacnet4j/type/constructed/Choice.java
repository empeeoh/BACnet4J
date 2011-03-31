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

import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.util.queue.ByteQueue;

public class Choice extends BaseType {
    private static final long serialVersionUID = 7942157718147383894L;
    private int contextId;
    private Encodable datum;

    public Choice(int contextId, Encodable datum) {
        this.contextId = contextId;
        this.datum = datum;
    }

    public int getContextId() {
        return contextId;
    }

    public Encodable getDatum() {
        return datum;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, datum, contextId);
    }

    public Choice(ByteQueue queue, List<Class<? extends Encodable>> classes) throws BACnetException {
        read(queue, classes);
    }

    public Choice(ByteQueue queue, List<Class<? extends Encodable>> classes, int contextId) throws BACnetException {
        popStart(queue, contextId);
        read(queue, classes);
        popEnd(queue, contextId);
    }

    public void read(ByteQueue queue, List<Class<? extends Encodable>> classes) throws BACnetException {
        contextId = peekTagNumber(queue);
        datum = read(queue, classes.get(contextId), contextId);
    }

    @Override
    public String toString() {
        return datum.toString();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + contextId;
        result = PRIME * result + ((datum == null) ? 0 : datum.hashCode());
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
        final Choice other = (Choice) obj;
        if (contextId != other.contextId)
            return false;
        if (datum == null) {
            if (other.datum != null)
                return false;
        }
        else if (!datum.equals(other.datum))
            return false;
        return true;
    }
}
