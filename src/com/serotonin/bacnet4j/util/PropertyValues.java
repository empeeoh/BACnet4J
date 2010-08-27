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
 */
package com.serotonin.bacnet4j.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.serotonin.bacnet4j.exception.PropertyValueException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.BACnetError;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class PropertyValues implements Iterable<ObjectPropertyReference> {
    private final Map<ObjectPropertyReference, Encodable> values = new HashMap<ObjectPropertyReference, Encodable>();

    public void add(ObjectIdentifier oid, PropertyIdentifier pid, UnsignedInteger pin, Encodable value) {
        values.put(new ObjectPropertyReference(oid, pid, pin), value);
    }

    public Encodable getNoErrorCheck(ObjectPropertyReference opr) {
        return values.get(opr);
    }

    public Encodable get(ObjectPropertyReference opr) throws PropertyValueException {
        Encodable e = getNoErrorCheck(opr);

        if (e instanceof BACnetError)
            throw new PropertyValueException((BACnetError) e);

        return e;
    }

    public Encodable getNoErrorCheck(ObjectIdentifier oid, PropertyReference ref) {
        return getNoErrorCheck(new ObjectPropertyReference(oid, ref.getPropertyIdentifier(),
                ref.getPropertyArrayIndex()));
    }

    public Encodable getNoErrorCheck(ObjectIdentifier oid, PropertyIdentifier pid) {
        return getNoErrorCheck(new ObjectPropertyReference(oid, pid));
    }

    public Encodable get(ObjectIdentifier oid, PropertyIdentifier pid) throws PropertyValueException {
        return get(new ObjectPropertyReference(oid, pid));
    }

    public Encodable getNullOnError(ObjectIdentifier oid, PropertyIdentifier pid) {
        Encodable e = getNoErrorCheck(new ObjectPropertyReference(oid, pid));

        if (e instanceof BACnetError)
            return null;

        return e;
    }

    public String getString(ObjectIdentifier oid, PropertyIdentifier pid) {
        return getNoErrorCheck(oid, pid).toString();
    }

    public String getString(ObjectIdentifier oid, PropertyIdentifier pid, String defaultValue) {
        Encodable value = getNoErrorCheck(oid, pid);
        if (value == null || value instanceof BACnetError)
            return defaultValue;
        return value.toString();
    }

    public Iterator<ObjectPropertyReference> iterator() {
        return values.keySet().iterator();
    }
}
