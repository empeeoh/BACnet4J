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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;

public class SessionKey extends BaseType {
    private static final long serialVersionUID = 2276895536699919255L;
    private final OctetString sessionKey;
    private final Address peerAddress;

    public SessionKey(OctetString sessionKey, Address peerAddress) {
        this.sessionKey = sessionKey;
        this.peerAddress = peerAddress;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, sessionKey);
        write(queue, peerAddress);
    }

    public SessionKey(ByteQueue queue) throws BACnetException {
        sessionKey = read(queue, OctetString.class);
        peerAddress = read(queue, Address.class);
    }

    public OctetString getSessionKey() {
        return sessionKey;
    }

    public Address getPeerAddress() {
        return peerAddress;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((peerAddress == null) ? 0 : peerAddress.hashCode());
        result = PRIME * result + ((sessionKey == null) ? 0 : sessionKey.hashCode());
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
        final SessionKey other = (SessionKey) obj;
        if (peerAddress == null) {
            if (other.peerAddress != null)
                return false;
        }
        else if (!peerAddress.equals(other.peerAddress))
            return false;
        if (sessionKey == null) {
            if (other.sessionKey != null)
                return false;
        }
        else if (!sessionKey.equals(other.sessionKey))
            return false;
        return true;
    }
}
