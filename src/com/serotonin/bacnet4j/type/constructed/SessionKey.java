/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.util.queue.ByteQueue;

public class SessionKey extends BaseType {
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
