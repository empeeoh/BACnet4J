package com.serotonin.bacnet4j.type;

import java.util.Map;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.ObjectProperties;
import com.serotonin.bacnet4j.obj.PropertyTypeDefinition;
import com.serotonin.bacnet4j.service.VendorServiceKey;
import com.serotonin.bacnet4j.type.constructed.Sequence;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Primitive;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

abstract public class Encodable {
    abstract public void write(ByteQueue queue);
    abstract public void write(ByteQueue queue, int contextId);
    
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
                tagData.length = (BACnetUtils.toInt(queue.peek(peekIndex++)) << 8) |
                        BACnetUtils.toInt(queue.peek(peekIndex++));
            else if (tagData.length == 255)
                tagData.length = (BACnetUtils.toLong(queue.peek(peekIndex++)) << 24) | 
                        (BACnetUtils.toLong(queue.peek(peekIndex++)) << 16) | 
                        (BACnetUtils.toLong(queue.peek(peekIndex++)) << 8) | 
                        BACnetUtils.toLong(queue.peek(peekIndex++));
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
    
    protected static <T extends Encodable> T read(ByteQueue queue, Class<T> clazz) throws BACnetException {
        try {
            return clazz.getConstructor(new Class[] {ByteQueue.class}).newInstance(new Object[] {queue});
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
            throws BACnetException{
        return new SequenceOf<T>(queue, count, clazz);
    }
    
    protected static <T extends Encodable> SequenceOf<T> readSequenceOf(ByteQueue queue, Class<T> clazz, int contextId)
            throws BACnetException {
        popStart(queue, contextId);
        SequenceOf<T> result = new SequenceOf<T>(queue, clazz, contextId);
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
            ((Primitive)type).writeEncodable(queue, contextId);
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
        
        if (propertyArrayIndex != null && !def.isSequence())
            throw new BACnetErrorException(ErrorClass.property, ErrorCode.propertyIsNotAList);
        if (propertyArrayIndex == null && def.isSequence())
            return readSequenceOf(queue, def.getClazz(), contextId);
        
        return readWrapped(queue, def.getClazz(), contextId);
    }
    
//    protected static Encodable readEncodable(ByteQueue queue, ObjectType objectType, 
//            PropertyIdentifier propertyIdentifier, int contextId) throws BACnetException {
//        if (!matchNonEndTag(queue, contextId))
//            throw new BACnetErrorException(ErrorClass.property, ErrorCode.missingRequiredParameter);
//        
//        PropertyTypeDefinition def = ObjectProperties.getPropertyTypeDefinition(objectType, propertyIdentifier);
//        if (def == null)
//            return new AmbiguousValue(queue, contextId);
//        
//        if (def.isSequence())
//            return readSequenceOf(queue, def.getClazz(), contextId);
//        else
//            return readWrapped(queue, def.getClazz(), contextId);
//    }
    
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
    protected static Sequence readVendorSpecific(ByteQueue queue, UnsignedInteger vendorId, 
            UnsignedInteger serviceNumber, Map<VendorServiceKey, SequenceDefinition> resolutions, int contextId) 
            throws BACnetException {
        if (readStart(queue) != contextId)
            return null;
        VendorServiceKey key = new VendorServiceKey(vendorId, serviceNumber);
        SequenceDefinition def = resolutions.get(key);
        if (def == null)
            throw new BACnetErrorException(ErrorClass.device, ErrorCode.operationalProblem, 
                    "No sequence definition found for vendorId="+ vendorId +", serviceNumber"+ serviceNumber);
        popStart(queue, contextId);
        Sequence result = new Sequence(def, queue);
        popEnd(queue, contextId);
        return result;
    }
    
    
    private static <T extends Encodable> T readWrapped(ByteQueue queue, Class<T> clazz, int contextId) 
            throws BACnetException {
        popStart(queue, contextId);
        T result = read(queue, clazz);
        popEnd(queue, contextId);
        return result;
    }
}
