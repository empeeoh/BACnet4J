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

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class VtOpenAck extends AcknowledgementService {
    private static final long serialVersionUID = 6959632953423207763L;

    public static final byte TYPE_ID = 21;

    private final UnsignedInteger remoteVTSessionIdentifier;

    public VtOpenAck(UnsignedInteger remoteVTSessionIdentifier) {
        this.remoteVTSessionIdentifier = remoteVTSessionIdentifier;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, remoteVTSessionIdentifier);
    }

    VtOpenAck(ByteQueue queue) throws BACnetException {
        remoteVTSessionIdentifier = read(queue, UnsignedInteger.class);
    }

    public UnsignedInteger getRemoteVTSessionIdentifier() {
        return remoteVTSessionIdentifier;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((remoteVTSessionIdentifier == null) ? 0 : remoteVTSessionIdentifier.hashCode());
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
        final VtOpenAck other = (VtOpenAck) obj;
        if (remoteVTSessionIdentifier == null) {
            if (other.remoteVTSessionIdentifier != null)
                return false;
        }
        else if (!remoteVTSessionIdentifier.equals(other.remoteVTSessionIdentifier))
            return false;
        return true;
    }
}
