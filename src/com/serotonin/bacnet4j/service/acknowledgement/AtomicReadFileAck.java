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
package com.serotonin.bacnet4j.service.acknowledgement;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.SignedInteger;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class AtomicReadFileAck extends AcknowledgementService {
    private static final long serialVersionUID = 7850183659621947037L;

    public static final byte TYPE_ID = 6;

    private final Boolean endOfFile;
    private final SignedInteger fileStartPosition;
    private final OctetString fileData;
    private final UnsignedInteger returnedRecordCount;
    private final SequenceOf<OctetString> fileRecordData;

    public AtomicReadFileAck(Boolean endOfFile, SignedInteger fileStartPosition, OctetString fileData) {
        super();
        this.endOfFile = endOfFile;
        this.fileStartPosition = fileStartPosition;
        this.fileData = fileData;
        returnedRecordCount = null;
        fileRecordData = null;
    }

    public AtomicReadFileAck(Boolean endOfFile, SignedInteger fileStartPosition, UnsignedInteger returnedRecordCount,
            SequenceOf<OctetString> fileRecordData) {
        super();
        this.endOfFile = endOfFile;
        this.fileStartPosition = fileStartPosition;
        fileData = null;
        this.returnedRecordCount = returnedRecordCount;
        this.fileRecordData = fileRecordData;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, endOfFile);
        if (fileData != null) {
            writeContextTag(queue, 0, true);
            write(queue, fileStartPosition);
            write(queue, fileData);
            writeContextTag(queue, 0, false);
        }
        else {
            writeContextTag(queue, 1, true);
            write(queue, fileStartPosition);
            write(queue, returnedRecordCount);
            write(queue, fileRecordData);
            writeContextTag(queue, 1, false);
        }
    }

    AtomicReadFileAck(ByteQueue queue) throws BACnetException {
        endOfFile = read(queue, Boolean.class);
        if (popStart(queue) == 0) {
            fileStartPosition = read(queue, SignedInteger.class);
            fileData = read(queue, OctetString.class);
            returnedRecordCount = null;
            fileRecordData = null;
            popEnd(queue, 0);
        }
        else {
            fileStartPosition = read(queue, SignedInteger.class);
            returnedRecordCount = read(queue, UnsignedInteger.class);
            fileData = null;
            List<OctetString> records = new ArrayList<OctetString>();
            for (int i = 0; i < returnedRecordCount.intValue(); i++)
                records.add(read(queue, OctetString.class));
            fileRecordData = new SequenceOf<OctetString>(records);
            popEnd(queue, 1);
        }
    }

    public Boolean getEndOfFile() {
        return endOfFile;
    }

    public SignedInteger getFileStartPosition() {
        return fileStartPosition;
    }

    public OctetString getFileData() {
        return fileData;
    }

    public UnsignedInteger getReturnedRecordCount() {
        return returnedRecordCount;
    }

    public SequenceOf<OctetString> getFileRecordData() {
        return fileRecordData;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((endOfFile == null) ? 0 : endOfFile.hashCode());
        result = PRIME * result + ((fileData == null) ? 0 : fileData.hashCode());
        result = PRIME * result + ((fileRecordData == null) ? 0 : fileRecordData.hashCode());
        result = PRIME * result + ((fileStartPosition == null) ? 0 : fileStartPosition.hashCode());
        result = PRIME * result + ((returnedRecordCount == null) ? 0 : returnedRecordCount.hashCode());
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
        final AtomicReadFileAck other = (AtomicReadFileAck) obj;
        if (endOfFile == null) {
            if (other.endOfFile != null)
                return false;
        }
        else if (!endOfFile.equals(other.endOfFile))
            return false;
        if (fileData == null) {
            if (other.fileData != null)
                return false;
        }
        else if (!fileData.equals(other.fileData))
            return false;
        if (fileRecordData == null) {
            if (other.fileRecordData != null)
                return false;
        }
        else if (!fileRecordData.equals(other.fileRecordData))
            return false;
        if (fileStartPosition == null) {
            if (other.fileStartPosition != null)
                return false;
        }
        else if (!fileStartPosition.equals(other.fileStartPosition))
            return false;
        if (returnedRecordCount == null) {
            if (other.returnedRecordCount != null)
                return false;
        }
        else if (!returnedRecordCount.equals(other.returnedRecordCount))
            return false;
        return true;
    }
}
