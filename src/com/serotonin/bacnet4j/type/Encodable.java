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
package com.serotonin.bacnet4j.type;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.event.ExceptionDispatch;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.ReflectionException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.eventParameter.EventParameter;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

abstract public class Encodable implements Serializable {
    private static final long serialVersionUID = -4378016931626697698L;

    abstract public void write(ByteQueue queue);

    abstract public void write(ByteQueue queue, int contextId);

    @Override
    public String toString() {
        return "Encodable(" + getClass().getName() + ")";
    }

    protected static void popTagData(ByteQueue queue, TagData tagData) {
        peekTagData(queue, tagData);
        queue.pop(tagData.tagLength);
    }

    protected static void peekTagData(ByteQueue queue, TagData tagData) {
        int peekIndex = 0;
        byte b = queue.peek(peekIndex++);
        tagData.tagNumber = (b & 0xff) >> 4;
        tagData.contextSpecific = (b & 8) != 0;
        tagData.length = (b & 7);

        if (tagData.tagNumber == 0xf)
            // Extended tag.
            tagData.tagNumber = BACnetUtils.toInt(queue.peek(peekIndex++));

        if (tagData.length == 5) {
            tagData.length = BACnetUtils.toInt(queue.peek(peekIndex++));
            if (tagData.length == 254)
                tagData.length = (BACnetUtils.toInt(queue.peek(peekIndex++)) << 8)
                        | BACnetUtils.toInt(queue.peek(peekIndex++));
            else if (tagData.length == 255)
                tagData.length = (BACnetUtils.toLong(queue.peek(peekIndex++)) << 24)
                        | (BACnetUtils.toLong(queue.peek(peekIndex++)) << 16)
                        | (BACnetUtils.toLong(queue.peek(peekIndex++)) << 8)
                        | BACnetUtils.toLong(queue.peek(peekIndex++));
        }

        tagData.tagLength = peekIndex;
    }

    protected static int peekTagNumber(ByteQueue queue) {
        if (queue.size() == 0)
            return -1;

        // Take a peek at the tag number.
        int tagNumber = (queue.peek(0) & 0xff) >> 4;
        if (tagNumber == 15)
            tagNumber = queue.peek(1) & 0xff;
        return tagNumber;
    }

    //
    // Write context tags for base types.
    protected void writeContextTag(ByteQueue queue, int contextId, boolean start) {
        if (contextId <= 14)
            queue.push((contextId << 4) | (start ? 0xe : 0xf));
        else {
            queue.push(start ? 0xfe : 0xff);
            queue.push(contextId);
        }
    }

    //
    // Read start tags.
    protected static int readStart(ByteQueue queue) {
        if (queue.size() == 0)
            return -1;

        int b = queue.peek(0) & 0xff;
        if ((b & 0xf) != 0xe)
            return -1;
        if ((b & 0xf0) == 0xf0)
            return queue.peek(1);
        return b >> 4;
    }

    protected static int popStart(ByteQueue queue) {
        int contextId = readStart(queue);
        if (contextId != -1) {
            queue.pop();
            if (contextId > 14)
                queue.pop();
        }
        return contextId;
    }

    protected static void popStart(ByteQueue queue, int contextId) throws BACnetErrorException {
        if (popStart(queue) != contextId)
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);
    }

    //
    // Read end tags.
    protected static int readEnd(ByteQueue queue) {
        if (queue.size() == 0)
            return -1;
        int b = queue.peek(0) & 0xff;
        if ((b & 0xf) != 0xf)
            return -1;
        if ((b & 0xf0) == 0xf0)
            return queue.peek(1);
        return b >> 4;
    }

    protected static void popEnd(ByteQueue queue, int contextId) throws BACnetErrorException {
        if (readEnd(queue) != contextId)
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);
        queue.pop();
        if (contextId > 14)
            queue.pop();
    }

    private static boolean matchContextId(ByteQueue queue, int contextId) {
        return peekTagNumber(queue) == contextId;
    }

    protected static boolean matchStartTag(ByteQueue queue, int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) == 0xe;
    }

    protected static boolean matchEndTag(ByteQueue queue, int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) == 0xf;
    }

    protected static boolean matchNonEndTag(ByteQueue queue, int contextId) {
        return matchContextId(queue, contextId) && (queue.peek(0) & 0xf) != 0xf;
    }

    //
    // Basic read and write. Pretty trivial.
    protected static void write(ByteQueue queue, Encodable type) {
        type.write(queue);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Encodable> T read(ByteQueue queue, Class<T> clazz) throws BACnetException {
        if (clazz == Primitive.class)
            return (T) Primitive.createPrimitive(queue);

        try {
            return clazz.getConstructor(new Class[] { ByteQueue.class }).newInstance(new Object[] { queue });
        }
        catch (NoSuchMethodException e) {
            // Check if this is an EventParameter
            if (clazz == EventParameter.class)
                return (T) EventParameter.createEventParameter(queue);
            throw new BACnetException(e);
        }
        catch (InvocationTargetException e) {
            // Check if there is a wrapped BACnet exception
            if (e.getCause() instanceof BACnetException)
                throw (BACnetException) e.getCause();
            throw new ReflectionException(e);
        }
        catch (Exception e) {
            throw new BACnetException(e);
        }
    }

    //
    // Read and write with context id.
    protected static <T extends Encodable> T read(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        if (!matchNonEndTag(queue, contextId))
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);

        if (Primitive.class.isAssignableFrom(clazz))
            return read(queue, clazz);
        return readWrapped(queue, clazz, contextId);
    }

    protected static void write(ByteQueue queue, Encodable type, int contextId) {
        type.write(queue, contextId);
    }

    //
    // Optional read and write.
    protected static void writeOptional(ByteQueue queue, Encodable type) {
        if (type == null)
            return;
        write(queue, type);
    }

    protected static void writeOptional(ByteQueue queue, Encodable type, int contextId) {
        if (type == null)
            return;
        write(queue, type, contextId);
    }

    protected static <T extends Encodable> T readOptional(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        if (!matchNonEndTag(queue, contextId))
            return null;
        return read(queue, clazz, contextId);
    }

    //
    // Read lists
    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(ByteQueue queue, Class<T> clazz)
            throws BACnetException {
        return new SequenceOf<T>(queue, clazz);
    }

    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(ByteQueue queue, int count, Class<T> clazz)
            throws BACnetException {
        return new SequenceOf<T>(queue, count, clazz);
    }

    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        popStart(queue, contextId);
        SequenceOf<T> result = new SequenceOf<T>(queue, clazz, contextId);
        popEnd(queue, contextId);
        return result;
    }

    protected static <T extends Encodable> T readSequenceType(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        popStart(queue, contextId);
        T result;
        try {
            result = clazz.getConstructor(new Class[] { ByteQueue.class, Integer.TYPE }).newInstance(
                    new Object[] { queue, contextId });
        }
        catch (Exception e) {
            throw new BACnetException(e);
        }
        popEnd(queue, contextId);
        return result;
    }

    protected static SequenceOf<Choice> readSequenceOfChoice(ByteQueue queue, List<Class<? extends Encodable>> classes,
            int contextId) throws BACnetException {
        popStart(queue, contextId);
        SequenceOf<Choice> result = new SequenceOf<Choice>();
        while (readEnd(queue) != contextId)
            result.add(new Choice(queue, classes));
        popEnd(queue, contextId);
        return result;
    }

    protected static <T extends Encodable> SequenceOf<T> readOptionalSequenceOf(ByteQueue queue, Class<T> clazz,
            int contextId) throws BACnetException {
        if (readStart(queue) != contextId)
            return null;
        return readSequenceOf(queue, clazz, contextId);
    }

    // Read and write encodable
    protected static void writeEncodable(ByteQueue queue, Encodable type, int contextId) {
        if (Primitive.class.isAssignableFrom(type.getClass()))
            ((Primitive) type).writeEncodable(queue, contextId);
        else
            type.write(queue, contextId);
    }

    protected static Encodable readEncodable(ByteQueue queue, ObjectType objectType,
            PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex, int contextId)
            throws BACnetException {
        // A property array index of 0 indicates a request for the length of an array.
        if (propertyArrayIndex != null && propertyArrayIndex.intValue() == 0)
            return readWrapped(queue, UnsignedInteger.class, contextId);

        if (!matchNonEndTag(queue, contextId))
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);

        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinition(objectType, propertyIdentifier);
        if (def == null)
            return new AmbiguousValue(queue, contextId);

        if (ObjectProperties.isCommandable(objectType, propertyIdentifier)) {
            // If the object is commandable, it could be set to Null, so we need to treat it as ambiguous.
            AmbiguousValue amb = new AmbiguousValue(queue, contextId);

            if (amb.isNull())
                return new Null();

            // Try converting to the definition value.
            return amb.convertTo(def.getClazz());
        }

        if (propertyArrayIndex != null) {
            if (!def.isSequence() && !SequenceOf.class.isAssignableFrom(def.getClazz()))
                throw new BACnetErrorException(ErrorClass.property, ErrorCode.propertyIsNotAList);
            if (SequenceOf.class.isAssignableFrom(def.getClazz()))
                return readWrapped(queue, def.getInnerType(), contextId);
        }
        else {
            if (def.isSequence())
                return readSequenceOf(queue, def.getClazz(), contextId);
            if (SequenceOf.class.isAssignableFrom(def.getClazz()))
                return readSequenceType(queue, def.getClazz(), contextId);
        }

        return readWrapped(queue, def.getClazz(), contextId);
    }

    protected static Encodable readOptionalEncodable(ByteQueue queue, ObjectType objectType,
            PropertyIdentifier propertyIdentifier, int contextId) throws BACnetException {
        if (readStart(queue) != contextId)
            return null;
        return readEncodable(queue, objectType, propertyIdentifier, null, contextId);
    }

    protected static SequenceOf<? extends Encodable> readSequenceOfEncodable(ByteQueue queue, ObjectType objectType,
            PropertyIdentifier propertyIdentifier, int contextId) throws BACnetException {
        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinition(objectType, propertyIdentifier);
        if (def == null)
            return readSequenceOf(queue, AmbiguousValue.class, contextId);
        return readSequenceOf(queue, def.getClazz(), contextId);
    }

    // Read vendor-specific
    protected static Sequence readVendorSpecific(final ByteQueue queue, 
    											 final UnsignedInteger vendorId,
    											 final UnsignedInteger serviceNumber,
    											 final Map<VendorServiceKey, SequenceDefinition> resolutions, 
    											 final int contextId)
            throws BACnetException {
        if (readStart(queue) != contextId)
            return null;

        VendorServiceKey key = new VendorServiceKey(vendorId, serviceNumber);
        SequenceDefinition def = resolutions.get(key);
        if (def == null) {
            ExceptionDispatch.fireUnimplementedVendorService(vendorId, serviceNumber, queue);
            return null;
        }

        return new Sequence(def, queue, contextId);
    }

    private static <T extends Encodable> T readWrapped(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        popStart(queue, contextId);
        T result = read(queue, clazz);
        popEnd(queue, contextId);
        return result;
    }
}
