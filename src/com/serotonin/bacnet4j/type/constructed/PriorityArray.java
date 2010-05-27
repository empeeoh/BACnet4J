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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.util.queue.ByteQueue;

public class PriorityArray extends SequenceOf<PriorityValue> {
    private static final long serialVersionUID = 8292702351986751796L;
    private static final int LENGTH = 16;

    public PriorityArray() {
        super(new ArrayList<PriorityValue>());
        ensureLength();
    }

    public PriorityArray(List<PriorityValue> priorityValues) {
        super(priorityValues);
        ensureLength();
    }

    public PriorityArray(ByteQueue queue, int contextId) throws BACnetException {
        super(queue, PriorityValue.class, contextId);
        ensureLength();
    }

    private void ensureLength() {
        while (getCount() < LENGTH)
            super.add(new PriorityValue(new Null()));
        while (getCount() > LENGTH)
            super.remove(getCount());
    }

    @Override
    public void set(int indexBase1, PriorityValue value) {
        if (indexBase1 < 1 || indexBase1 > LENGTH)
            throw new RuntimeException("Invalid priority value");
        if (value == null)
            value = new PriorityValue(new Null());
        super.set(indexBase1, value);
    }

    @Override
    public void add(PriorityValue value) {
        throw new RuntimeException("Use set method instead");
    }

    @Override
    public void remove(int indexBase1) {
        throw new RuntimeException("Use set method instead");
    }

    @Override
    public void remove(PriorityValue value) {
        throw new RuntimeException("Use set method instead");
    }

    @Override
    public void removeAll(PriorityValue value) {
        throw new RuntimeException("Use set method instead");
    }
}
