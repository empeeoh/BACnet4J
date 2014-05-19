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
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.BackupState;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class AtomicReadFileRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = -279843621668191530L;

    public static final byte TYPE_ID = 6;

    private final ObjectIdentifier fileIdentifier;
    private final boolean recordAccess;
    private final SignedInteger fileStartPosition;
    private final UnsignedInteger requestedCount;

    public AtomicReadFileRequest(ObjectIdentifier fileIdentifier, boolean recordAccess,
            SignedInteger fileStartPosition, UnsignedInteger requestedCount) {
        this.fileIdentifier = fileIdentifier;
        this.recordAccess = recordAccess;
        this.fileStartPosition = fileStartPosition;
        this.requestedCount = requestedCount;
    }

    public AtomicReadFileRequest(ObjectIdentifier fileIdentifier, boolean recordAccess, int start, int length) {
        this(fileIdentifier, recordAccess, new SignedInteger(start), new UnsignedInteger(length));
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        AtomicReadFileAck response;

        BACnetObject obj;
        FileObject file;
        try {
            // Find the file.
            obj = localDevice.getObjectRequired(fileIdentifier);
            if (!(obj instanceof FileObject)) {
                System.out.println("File access request on an object that is not a file");
                throw new BACnetServiceException(ErrorClass.object, ErrorCode.rejectInconsistentParameters);
            }

            // Check for status (backup/restore)
            BackupState bsOld = (BackupState) localDevice.getConfiguration().getProperty(
                    PropertyIdentifier.backupAndRestoreState);
            if (bsOld.intValue() == BackupState.preparingForBackup.intValue()
                    || bsOld.intValue() == BackupState.preparingForRestore.intValue())
                // Send error: device configuration in progress as response
                throw new BACnetServiceException(ErrorClass.device, ErrorCode.configurationInProgress);

            file = (FileObject) obj;

            // Validation.
            FileAccessMethod fileAccessMethod = (FileAccessMethod) file
                    .getProperty(PropertyIdentifier.fileAccessMethod);
            if (recordAccess && fileAccessMethod.equals(FileAccessMethod.streamAccess) || !recordAccess
                    && fileAccessMethod.equals(FileAccessMethod.recordAccess))
                throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileAccessMethod);
        }
        catch (BACnetServiceException e) {
            throw new BACnetErrorException(getChoiceId(), e);
        }

        if (recordAccess)
            throw new NotImplementedException();

        long start = fileStartPosition.longValue();
        long length = requestedCount.longValue();

        /*
         * throw an exception when the following conditions are met - start is a negative number - start exceeds the
         * length of the file object
         */
        if (start < 0 || start > file.length())
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.invalidFileStartPosition);

        try {
            response = new AtomicReadFileAck(new Boolean(file.length() <= start + length), fileStartPosition,
                    file.readData(start, length));
        }
        catch (IOException e) {
            throw new BACnetErrorException(getChoiceId(), ErrorClass.object, ErrorCode.fileAccessDenied);
        }

        return response;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, fileIdentifier);
        writeContextTag(queue, recordAccess ? 1 : 0, true);
        write(queue, fileStartPosition);
        write(queue, requestedCount);
        writeContextTag(queue, recordAccess ? 1 : 0, false);
    }

    AtomicReadFileRequest(ByteQueue queue) throws BACnetException {
        fileIdentifier = read(queue, ObjectIdentifier.class);
        recordAccess = popStart(queue) == 1;
        fileStartPosition = read(queue, SignedInteger.class);
        requestedCount = read(queue, UnsignedInteger.class);
        popEnd(queue, recordAccess ? 1 : 0);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((fileIdentifier == null) ? 0 : fileIdentifier.hashCode());
        result = PRIME * result + ((fileStartPosition == null) ? 0 : fileStartPosition.hashCode());
        result = PRIME * result + (recordAccess ? 1231 : 1237);
        result = PRIME * result + ((requestedCount == null) ? 0 : requestedCount.hashCode());
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
        final AtomicReadFileRequest other = (AtomicReadFileRequest) obj;
        if (fileIdentifier == null) {
            if (other.fileIdentifier != null)
                return false;
        }
        else if (!fileIdentifier.equals(other.fileIdentifier))
            return false;
        if (fileStartPosition == null) {
            if (other.fileStartPosition != null)
                return false;
        }
        else if (!fileStartPosition.equals(other.fileStartPosition))
            return false;
        if (recordAccess != other.recordAccess)
            return false;
        if (requestedCount == null) {
            if (other.requestedCount != null)
                return false;
        }
        else if (!requestedCount.equals(other.requestedCount))
            return false;
        return true;
    }
}
