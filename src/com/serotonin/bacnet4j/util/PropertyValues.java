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
package com.serotonin.bacnet4j.util;

import java.io.Serializable;
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

public class PropertyValues implements Iterable<ObjectPropertyReference>, Serializable {
    private static final long serialVersionUID = 5880275533969236369L;

    private final Map<ObjectPropertyReference, Encodable> values = new HashMap<>();

    public void add(final ObjectIdentifier oid, final PropertyIdentifier pid, 
    				final UnsignedInteger propArrayIndex, final Encodable value) {
        values.put(new ObjectPropertyReference(oid, pid, propArrayIndex), value);
    }

    public Encodable getNoErrorCheck(final ObjectPropertyReference opr) {
        return values.get(opr);
    }

    public Encodable get(final ObjectPropertyReference opr) throws PropertyValueException {
        final Encodable e = getNoErrorCheck(opr);
        if (e instanceof BACnetError)
            throw new PropertyValueException((BACnetError) e);
        return e;
    }

    public Encodable getNoErrorCheck(final ObjectIdentifier oid, final PropertyReference ref) {
        return getNoErrorCheck(new ObjectPropertyReference(oid, 
        												   ref.getPropertyIdentifier(),
        												   ref.getPropertyArrayIndex()));
    }

    public Encodable getNoErrorCheck(final ObjectIdentifier oid, final PropertyIdentifier pid) {
        return getNoErrorCheck(new ObjectPropertyReference(oid, pid));
    }

    public Encodable get(final ObjectIdentifier oid, final PropertyIdentifier pid) throws PropertyValueException {
        return get(new ObjectPropertyReference(oid, pid));
    }

    public Encodable getNullOnError(final ObjectIdentifier oid, final PropertyIdentifier pid) {
        return getNullOnError(getNoErrorCheck(new ObjectPropertyReference(oid, pid)));
    }

    public String getString(final ObjectIdentifier oid, final PropertyIdentifier pid) {
        return getString(getNoErrorCheck(oid, pid));
    }

    public String getString(ObjectIdentifier oid, PropertyIdentifier pid, String defaultValue) {
        return getString(getNoErrorCheck(oid, pid), defaultValue);
    }

    @Override
    public Iterator<ObjectPropertyReference> iterator() {
        return values.keySet().iterator();
    }

    public static String getString(Encodable value) {
        if (value == null)
            return null;
        return value.toString();
    }

    public static String getString(Encodable value, String defaultValue) {
        if (value == null || value instanceof BACnetError)
            return defaultValue;
        return value.toString();
    }

    public static Encodable getNullOnError(Encodable value) {
        if (value instanceof BACnetError)
            return null;
        return value;
    }
}
