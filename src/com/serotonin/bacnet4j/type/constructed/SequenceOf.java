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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.ObjectUtils;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import org.free.bacnet4j.util.ByteQueue;

public class SequenceOf<E extends Encodable> extends BaseType implements Iterable<E> {
    private static final long serialVersionUID = -5118339248636778224L;
    private final List<E> values;

    public SequenceOf() {
        values = new ArrayList<E>();
    }

    public SequenceOf(List<E> values) {
        this.values = values;
    }

    @Override
    public void write(ByteQueue queue) {
        for (Encodable value : values)
            value.write(queue);
    }

    public SequenceOf(ByteQueue queue, Class<E> clazz) throws BACnetException {
        values = new ArrayList<E>();
        while (peekTagNumber(queue) != -1)
            values.add(read(queue, clazz));
    }

    public SequenceOf(ByteQueue queue, int count, Class<E> clazz) throws BACnetException {
        values = new ArrayList<E>();
        while (count-- > 0)
            values.add(read(queue, clazz));
    }

    public SequenceOf(ByteQueue queue, Class<E> clazz, int contextId) throws BACnetException {
        values = new ArrayList<E>();
        while (readEnd(queue) != contextId)
            values.add(read(queue, clazz));
    }

    public E get(int indexBase1) {
        return values.get(indexBase1 - 1);
    }

    public int getCount() {
        return values.size();
    }

    public void set(int indexBase1, E value) {
        int index = indexBase1 - 1;
        while (values.size() <= index)
            values.add(null);
        values.set(index, value);
    }

    public void add(E value) {
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) == null) {
                values.set(i, value);
                return;
            }
        }
        values.add(value);
    }

    public void remove(int indexBase1) {
        int index = indexBase1 - 1;
        if (index < values.size())
            values.remove(index);
        // values.set(index, null);
        // Trim null values at the end.
        // while (!values.isEmpty() && values.get(values.size() - 1) == null)
        // values.remove(values.size() - 1);
    }

    public void remove(E value) {
        if (value == null)
            return;

        for (int i = 0; i < values.size(); i++) {
            if (ObjectUtils.equals(values.get(i), value)) {
                remove(i + 1);
                break;
            }
        }
    }

    public void removeAll(E value) {
        for (ListIterator<E> it = values.listIterator(); it.hasNext();) {
            E e = it.next();
            if (ObjectUtils.equals(e, value))
                it.remove();
        }
    }

    public boolean contains(E value) {
        for (E e : values) {
            if (ObjectUtils.equals(e, value))
                return true;
        }
        return false;
    }

    public Iterator<E> iterator() {
        return values.iterator();
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public List<E> getValues() {
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
        final SequenceOf<?> other = (SequenceOf<?>) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        }
        else if (!values.equals(other.values))
            return false;
        return true;
    }
}
