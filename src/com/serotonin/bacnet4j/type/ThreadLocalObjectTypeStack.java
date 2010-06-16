package com.serotonin.bacnet4j.type;

import java.util.ArrayDeque;
import java.util.Deque;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;

public class ThreadLocalObjectTypeStack {
    private static ThreadLocal<Deque<ObjectType>> objType = new ThreadLocal<Deque<ObjectType>>();

    public static void set(ObjectType objectType) {
        Deque<ObjectType> deque = objType.get();

        if (deque == null) {
            deque = new ArrayDeque<ObjectType>();
            objType.set(deque);
        }

        deque.push(objectType);
    }

    public static ObjectType get() {
        Deque<ObjectType> deque = objType.get();
        if (deque == null)
            return null;
        return deque.peek();
    }

    public static void remove() {
        Deque<ObjectType> deque = objType.get();
        if (deque == null)
            return;

        deque.pop();

        if (deque.isEmpty())
            objType.remove();
    }
}
