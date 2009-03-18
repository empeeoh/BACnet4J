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
package com.serotonin.bacnet4j.obj;

import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

public class PropertyTypeDefinition {
    private final ObjectType objectType;
    private final PropertyIdentifier propertyIdentifier;
    private final Class<? extends Encodable> clazz;
    private final boolean sequence;
    private boolean required;
    private final Encodable defaultValue;
    
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
