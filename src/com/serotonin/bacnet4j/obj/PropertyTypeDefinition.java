package com.serotonin.bacnet4j.obj;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

public class PropertyTypeDefinition {
    private ObjectType objectType;
    private PropertyIdentifier propertyIdentifier;
    private Class<? extends Encodable> clazz;
    private boolean sequence;
    private boolean required;
    private Encodable defaultValue;
    
    PropertyTypeDefinition(ObjectType objectType, PropertyIdentifier propertyIdentifier, 
            Class<? extends Encodable> clazz, boolean sequence, boolean required, Encodable defaultValue) {
        this.objectType = objectType;
        this.propertyIdentifier = propertyIdentifier;
        this.clazz = clazz;
        this.sequence = sequence;
        this.required = required;
        this.defaultValue = defaultValue;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public PropertyIdentifier getPropertyIdentifier() {
        return propertyIdentifier;
    }

    public Class<? extends Encodable> getClazz() {
        return clazz;
    }

    public boolean isSequence() {
        return sequence;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isOptional() {
        return !required;
    }

    public Encodable getDefaultValue() {
        return defaultValue;
    }
}
