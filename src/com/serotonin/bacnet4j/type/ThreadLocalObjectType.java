package com.serotonin.bacnet4j.type;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;

public class ThreadLocalObjectType {
    private static ThreadLocal<ObjectType> objType = new ThreadLocal<ObjectType>();
    
    public static void set(ObjectType objectType) {
        if (objType.get() != null)
            throw new IllegalStateException("Stomping on another object type: old="+ 
                    objType.get() +", new="+ objectType);
        objType.set(objectType);
    }
    
    public static ObjectType get() {
        return objType.get();
    }
    
    public static void remove() {
        objType.remove();
    }
}
