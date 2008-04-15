package com.serotonin.bacnet4j.obj;

import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

public class ObjectPropertyIdentifier {
    private ObjectType objectType;
    private PropertyIdentifier propertyIdentifier;
    
    public ObjectPropertyIdentifier(ObjectType objectType, PropertyIdentifier propertyIdentifier) {
        this.objectType = objectType;
        this.propertyIdentifier = propertyIdentifier;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((objectType == null) ? 0 : objectType.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
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
        final ObjectPropertyIdentifier other = (ObjectPropertyIdentifier) obj;
        if (objectType == null) {
            if (other.objectType != null)
                return false;
        }
        else if (!objectType.equals(other.objectType))
            return false;
        if (propertyIdentifier == null) {
            if (other.propertyIdentifier != null)
                return false;
        }
        else if (!propertyIdentifier.equals(other.propertyIdentifier))
            return false;
        return true;
    }
}
