package com.serotonin.bacnet4j.type;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;

public class ThreadLocalObjectTypeStack {
    private static ThreadLocal<List<ObjectType>> objType = new ThreadLocal<List<ObjectType>>();

    public static void set(ObjectType objectType) {
        List<ObjectType> stack = objType.get();

        if (stack == null) {
            stack = new ArrayList<ObjectType>();
            objType.set(stack);
        }

        stack.add(objectType);
    }

    public static ObjectType get() {
        List<ObjectType> stack = objType.get();
        if (stack == null)
            return null;
        return stack.get(stack.size() - 1);
    }

    public static void remove() {
        List<ObjectType> stack = objType.get();
        if (stack == null)
            return;

        if (stack.size() <= 1)
            objType.remove();
        else
            stack.remove(stack.size() - 1);
    }
}
