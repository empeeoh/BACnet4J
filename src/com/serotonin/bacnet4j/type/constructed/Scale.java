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
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class Scale extends BaseType {
    private static final long serialVersionUID = 5351842576726308269L;

    private final Choice scale;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Real.class);
        classes.add(SignedInteger.class);
    }

    public Scale(Real scale) {
        this.scale = new Choice(0, scale);
    }

    public Scale(SignedInteger scale) {
        this.scale = new Choice(1, scale);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, scale);
    }

    public Scale(ByteQueue queue) throws BACnetException {
        scale = new Choice(queue, classes);
    }

    public boolean isReal() {
        return scale.getContextId() == 0;
    }

    public Real getReal() {
        return (Real) scale.getDatum();
    }

    public SignedInteger getSignedInteger() {
        return (SignedInteger) scale.getDatum();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((scale == null) ? 0 : scale.hashCode());
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
        final Scale other = (Scale) obj;
        if (scale == null) {
            if (other.scale != null)
                return false;
        }
        else if (!scale.equals(other.scale))
            return false;
        return true;
    }
}
