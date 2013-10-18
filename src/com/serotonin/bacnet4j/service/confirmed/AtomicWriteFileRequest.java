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
package com.serotonin.bacnet4j.service.confirmed;

import java.io.IOException;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.obj.FileObject;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicWriteFileAck;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class AtomicWriteFileRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 2426317127743066428L;

    public static final byte TYPE_ID = 7;

    private final ObjectIdentifier fileIdentifier;
    private SignedInteger fileStart;
    private OctetString fileData;
    private UnsignedInteger recordCount;
    private SequenceOf<OctetString> fileRecordData;

    public AtomicWriteFileRequest(ObjectIdentifier fileIdentifier, SignedInteger fileStart, OctetString fileData) {
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
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        AtomicWriteFileAck response;

        BACnetObject obj;
        FileObject file;
        try {
            // Find the file.
            obj = localDevice.getObjectRequired(fileIdentifier);
            if (!(obj instanceof FileObject))
                throw new BACnetServiceException(ErrorClass.object, ErrorCode.rejectInconsistentParameters);
            file = (FileObject) obj;

            // Validation.
            FileAccessMethod fileAccessMethod = (FileAccessMethod) file
                    .getProperty(PropertyIdentifier.fileAccessMethod);
            if (fileData == null && fileAccessMethod.equals(FileAccessMethod.streamAccess) || fileData != null
                    && fileAccessMethod.equals(FileAccessMethod.recordAccess))
                throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileAccessMethod);
        }
        catch (BACnetServiceException e) {
            throw new BACnetErrorException(getChoiceId(), e);
        }

        if (fileData == null) {
            throw new NotImplementedException();
        }

        long start = fileStart.longValue();

        if (start > file.length())
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileStartPosition);

        try {
            file.writeData(start, fileData);
            response = new AtomicWriteFileAck(fileData == null, fileStart);
        }
        catch (IOException e) {
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.fileAccessDenied);
        }

        return response;
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
