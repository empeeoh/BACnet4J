package com.serotonin.bacnet4j.type.constructed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.util.queue.ByteQueue;

public class SequenceOf<E extends Encodable> extends BaseType implements Iterable<E> {
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
        values.add(value);
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
