package com.serotonin.bacnet4j;

import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class RemoteObject {
    private ObjectIdentifier oid;
    private String objectName;
    
    public RemoteObject(ObjectIdentifier oid) {
        this.oid = oid;
    }
    
    public ObjectIdentifier getObjectIdentifier() {
        return oid;
    }
    
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectName() {
        return objectName;
    }
    
    public String toString() {
        return oid.toString();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((oid == null) ? 0 : oid.hashCode());
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
        final RemoteObject other = (RemoteObject) obj;
        if (oid == null) {
            if (other.oid != null)
                return false;
        }
        else if (!oid.equals(other.oid))
            return false;
        return true;
    }
}
