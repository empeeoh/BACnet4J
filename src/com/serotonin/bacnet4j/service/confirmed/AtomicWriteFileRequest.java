package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class AtomicWriteFileRequest extends ConfirmedRequestService {
    public static final byte TYPE_ID = 7;
    
    private ObjectIdentifier fileIdentifier;
    private SignedInteger fileStart;
    private OctetString fileData;
    private UnsignedInteger recordCount;
    private SequenceOf<OctetString> fileRecordData;
    
    public AtomicWriteFileRequest(ObjectIdentifier fileIdentifier, SignedInteger fileStart, 
            OctetString fileData) {
        super();
        this.fileIdentifier = fileIdentifier;
        this.fileStart = fileStart;
        this.fileData = fileData;
    }

    public AtomicWriteFileRequest(ObjectIdentifier fileIdentifier, SignedInteger fileStart, 
            UnsignedInteger recordCount, SequenceOf<OctetString> fileRecordData) {
        super();
        this.fileIdentifier = fileIdentifier;
        this.fileStart = fileStart;
        this.recordCount = recordCount;
        this.fileRecordData = fileRecordData;
    }
    
    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }
    
    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from) throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, fileIdentifier);
        if (fileData != null) {
            writeContextTag(queue, 0, true);
            write(queue, fileStart);
            write(queue, fileData);
            writeContextTag(queue, 0, false);
        }
        else {
            writeContextTag(queue, 1, true);
            write(queue, fileStart);
            write(queue, recordCount);
            write(queue, fileRecordData);
            writeContextTag(queue, 1, false);
        }
    }
    
    AtomicWriteFileRequest(ByteQueue queue) throws BACnetException {
        fileIdentifier = read(queue, ObjectIdentifier.class);
        if (popStart(queue) == 0) {
            fileStart = read(queue, SignedInteger.class);
            fileData = read(queue, OctetString.class);
            popEnd(queue, 0);
        }
        else {
            fileStart = read(queue, SignedInteger.class);
            recordCount = read(queue, UnsignedInteger.class);
            fileRecordData = readSequenceOf(queue, recordCount.intValue(), OctetString.class);
            popEnd(queue, 1);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fileData == null) ? 0 : fileData.hashCode());
        result = PRIME * result + ((fileIdentifier == null) ? 0 : fileIdentifier.hashCode());
        result = PRIME * result + ((fileRecordData == null) ? 0 : fileRecordData.hashCode());
        result = PRIME * result + ((fileStart == null) ? 0 : fileStart.hashCode());
        result = PRIME * result + ((recordCount == null) ? 0 : recordCount.hashCode());
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
        final AtomicWriteFileRequest other = (AtomicWriteFileRequest) obj;
        if (fileData == null) {
            if (other.fileData != null)
                return false;
        }
        else if (!fileData.equals(other.fileData))
            return false;
        if (fileIdentifier == null) {
            if (other.fileIdentifier != null)
                return false;
        }
        else if (!fileIdentifier.equals(other.fileIdentifier))
            return false;
        if (fileRecordData == null) {
            if (other.fileRecordData != null)
                return false;
        }
        else if (!fileRecordData.equals(other.fileRecordData))
            return false;
        if (fileStart == null) {
            if (other.fileStart != null)
                return false;
        }
        else if (!fileStart.equals(other.fileStart))
            return false;
        if (recordCount == null) {
            if (other.recordCount != null)
                return false;
        }
        else if (!recordCount.equals(other.recordCount))
            return false;
        return true;
    }
}
