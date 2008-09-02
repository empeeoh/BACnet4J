package com.serotonin.bacnet4j.service.confirmed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ReadRangeRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 26;
    
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Encodable.class);
        classes.add(Encodable.class);
        classes.add(Encodable.class);
        classes.add(ByPosition.class);
        classes.add(Encodable.class);
        classes.add(Encodable.class);
        classes.add(BySequenceNumber.class);
        classes.add(ByTime.class);
    }
    private final ObjectIdentifier objectIdentifier;
    private final PropertyIdentifier propertyIdentifier;
    private final UnsignedInteger propertyArrayIndex;
    private Choice range;
    
    private ReadRangeRequest(ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, 
            UnsignedInteger propertyArrayIndex) {
        this.objectIdentifier = objectIdentifier;
        this.propertyIdentifier = propertyIdentifier;
        this.propertyArrayIndex = propertyArrayIndex;
    }

    public ReadRangeRequest(ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, 
            UnsignedInteger propertyArrayIndex, ByPosition range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex);
        this.range = new Choice(3, range);
    }

    public ReadRangeRequest(ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, 
            UnsignedInteger propertyArrayIndex, BySequenceNumber range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex);
        this.range = new Choice(6, range);
    }

    public ReadRangeRequest(ObjectIdentifier objectIdentifier, PropertyIdentifier propertyIdentifier, 
            UnsignedInteger propertyArrayIndex, ByTime range) {
        this(objectIdentifier, propertyIdentifier, propertyArrayIndex);
        this.range = new Choice(7, range);
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, Network network)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, objectIdentifier, 0);
        write(queue, propertyIdentifier, 1);
        writeOptional(queue, propertyArrayIndex, 2);
        writeOptional(queue, range);
    }
    
    ReadRangeRequest(ByteQueue queue) throws BACnetException {
        objectIdentifier = read(queue, ObjectIdentifier.class, 0);
        propertyIdentifier = read(queue, PropertyIdentifier.class, 1);
        propertyArrayIndex = readOptional(queue, UnsignedInteger.class, 2);
        if (peekTagNumber(queue) != -1)
            range = new Choice(queue, classes);
    }
    
    abstract public static class Range extends BaseType {
        protected SignedInteger count;

        public Range(SignedInteger count) {
            this.count = count;
        }

        Range() {
        }
    }
    
    public static class ByPosition extends Range {
        private final UnsignedInteger referenceIndex;

        public ByPosition(UnsignedInteger referenceIndex, SignedInteger count) {
            super(count);
            this.referenceIndex = referenceIndex;
        }

        @Override
        public void write(ByteQueue queue) {
            write(queue, referenceIndex);
            write(queue, count);
        }
        
        ByPosition(ByteQueue queue) throws BACnetException {
            referenceIndex = read(queue, UnsignedInteger.class);
            count = read(queue, SignedInteger.class);
        }
    }
    
    public static class BySequenceNumber extends Range {
        private final UnsignedInteger referenceIndex;

        public BySequenceNumber(UnsignedInteger referenceIndex, SignedInteger count) {
            super(count);
            this.referenceIndex = referenceIndex;
        }

        @Override
        public void write(ByteQueue queue) {
            write(queue, referenceIndex);
            write(queue, count);
        }
        
        BySequenceNumber(ByteQueue queue) throws BACnetException {
            referenceIndex = read(queue, UnsignedInteger.class);
            count = read(queue, SignedInteger.class);
        }
    }
    
    public static class ByTime extends Range {
        private final DateTime referenceTime;

        public ByTime(DateTime referenceTime, SignedInteger count) {
            super(count);
            this.referenceTime = referenceTime;
        }

        @Override
        public void write(ByteQueue queue) {
            write(queue, referenceTime);
            write(queue, count);
        }
        
        ByTime(ByteQueue queue) throws BACnetException {
            referenceTime = read(queue, DateTime.class);
            count = read(queue, SignedInteger.class);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((objectIdentifier == null) ? 0 : objectIdentifier.hashCode());
        result = PRIME * result + ((propertyArrayIndex == null) ? 0 : propertyArrayIndex.hashCode());
        result = PRIME * result + ((propertyIdentifier == null) ? 0 : propertyIdentifier.hashCode());
        result = PRIME * result + ((range == null) ? 0 : range.hashCode());
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
        final ReadRangeRequest other = (ReadRangeRequest) obj;
        if (objectIdentifier == null) {
            if (other.objectIdentifier != null)
                return false;
        }
        else if (!objectIdentifier.equals(other.objectIdentifier))
            return false;
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
        if (range == null) {
            if (other.range != null)
                return false;
        }
        else if (!range.equals(other.range))
            return false;
        return true;
    }
}
