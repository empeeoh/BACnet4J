package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.ThreadLocalObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ReadAccessResult extends BaseType {
    private ObjectIdentifier objectIdentifier;
    private SequenceOf<Result> listOfResults;

    public ReadAccessResult(ObjectIdentifier objectIdentifier, SequenceOf<Result> listOfResults) {
        this.objectIdentifier = objectIdentifier;
        this.listOfResults = listOfResults;
    }

    public void write(ByteQueue queue) {
        write(queue, objectIdentifier, 0);
        writeOptional(queue, listOfResults, 1);
    }
    
    public String toString() {
        return "ReadAccessResult(oid="+ objectIdentifier +", results="+ listOfResults +")";
    }
    
    public SequenceOf<Result> getListOfResults() {
        return listOfResults;
    }

    public ObjectIdentifier getObjectIdentifier() {
        return objectIdentifier;
    }

    public ReadAccessResult(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class, 0);
        ThreadLocalObjectType.set(objectIdentifier.getObjectType());
        listOfResults = readOptionalSequenceOf(queue, Result.class, 1);
        ThreadLocalObjectType.remove();
    }
    
    public static class Result extends BaseType {
        private PropertyIdentifier propertyIdentifier;
        private UnsignedInteger propertyArrayIndex;
        private Choice readResult;
        
        public Result(PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex, Encodable readResult) {
            this.propertyIdentifier = propertyIdentifier;
            this.propertyArrayIndex = propertyArrayIndex;
            this.readResult = new Choice(4, readResult);
        }
        
        public Result(PropertyIdentifier propertyIdentifier, UnsignedInteger propertyArrayIndex, 
                BACnetError readResult) {
            this.propertyIdentifier = propertyIdentifier;
            this.propertyArrayIndex = propertyArrayIndex;
            this.readResult = new Choice(5, readResult);
        }
        
        public UnsignedInteger getPropertyArrayIndex() {
            return propertyArrayIndex;
        }

        public PropertyIdentifier getPropertyIdentifier() {
            return propertyIdentifier;
        }

        public Choice getReadResult() {
            return readResult;
        }

        public String toString() {
            return "Result(pid="+ propertyIdentifier 
                    +(propertyArrayIndex == null ? "" : ", pin="+ propertyArrayIndex) 
                    +", value="+ readResult +")";
        }

        public void write(ByteQueue queue) {
            write(queue, propertyIdentifier, 2);
            writeOptional(queue, propertyArrayIndex, 3);
            if (readResult.getContextId() == 4)
                writeEncodable(queue, readResult.getDatum(), 4);
            else
                write(queue, readResult.getDatum(), 5);
        }
        
        public Result(ByteQueue queue) throws BACnetException {
            propertyIdentifier = read(queue, PropertyIdentifier.class, 2);
            propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 3);
            int contextId = peekTagNumber(queue);
            if (contextId == 4)
                readResult = new Choice(4, readEncodable(queue, ThreadLocalObjectType.get(), propertyIdentifier, 4));
            else
                readResult = new Choice(5, read(queue, BACnetError.class, 5));
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
            result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
            result = PRIME * result + ((readResult == null) ? 0 : readResult.hashCode());
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
            final Result other = (Result) obj;
            if (propertyArrayIndex == null) {
                if (other.propertyArrayIndex != null)
                    return false;
            }
            else if (!propertyArrayIndex.equals(other.propertyArrayIndex))
                return false;
            if (propertyIdentifier == null) {
                if (other.propertyIdentifier != null)
                    return false;
            }
            else if (!propertyIdentifier.equals(other.propertyIdentifier))
                return false;
            if (readResult == null) {
                if (other.readResult != null)
                    return false;
            }
            else if (!readResult.equals(other.readResult))
                return false;
            return true;
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((listOfResults == null) ? 0 : listOfResults.hashCode());
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
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
        final ReadAccessResult other = (ReadAccessResult) obj;
        if (listOfResults == null) {
            if (other.listOfResults != null)
                return false;
        }
        else if (!listOfResults.equals(other.listOfResults))
            return false;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
        return true;
    }
}
