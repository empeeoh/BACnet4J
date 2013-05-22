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
package com.serotonin.bacnet4j.npdu.ip;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.exception.SegmentedMessageAbortedException;
import com.serotonin.bacnet4j.type.constructed.Address;

public class WaitingRoom {
    private final HashMap<Key, Member> waitHere = new HashMap<Key, Member>();
    private byte nextInvokeId;

    synchronized public Key enterClient(InetSocketAddress peer, Address linkService) {
        Member member = new Member();
        Key key;

        // Loop until we find a key that is available.
        int attempts = 256;
        while (true) {
            // We set the server value in the key to true so that it matches with the message from the server.
            key = new Key(peer, linkService, nextInvokeId++, true);

            synchronized (waitHere) {
                if (waitHere.get(key) != null) {
                    // Key collision. Try again unless we've tried too many times.
                    if (--attempts > 0)
                        continue;
                    throw new BACnetRuntimeException("Cannot enter a client into the waiting room. key=" + key);
                }

                // Found a good id. Use it and exit.
                waitHere.put(key, member);
                break;
            }
        }

        return key;
    }

    public Key enterServer(InetSocketAddress peer, Address linkService, byte id) {
        // We set the server value in the key to false so that it matches with the message from the client.
        Key key = new Key(peer, linkService, id, false);
        Member member = new Member();

        synchronized (waitHere) {
            if (waitHere.get(key) != null)
                throw new BACnetRuntimeException("Cannot enter a server into the waiting room. key=" + key);
            waitHere.put(key, member);
        }

        return key;
    }

    public AckAPDU getAck(Key key, long timeout, boolean throwTimeout) throws BACnetException {
        return (AckAPDU) getAPDU(key, timeout, throwTimeout);
    }

    public ConfirmedRequest getRequest(Key key, long timeout, boolean throwTimeout) throws BACnetException {
        return (ConfirmedRequest) getAPDU(key, timeout, throwTimeout);
    }

    public Segmentable getSegmentable(Key key, long timeout, boolean throwTimeout) throws BACnetException {
        APDU apdu = getAPDU(key, timeout, throwTimeout);
        if (apdu instanceof Abort)
            throw new SegmentedMessageAbortedException((Abort) apdu);
        try {
            return (Segmentable) apdu;
        }
        catch (ClassCastException e) {
            throw new BACnetException("Receiving an APDU of type " + apdu.getClass()
                    + " when expecting a Segmentable for key " + key);
        }
    }

    public APDU getAPDU(Key key, long timeout, boolean throwTimeout) throws BACnetException {
        Member member = getMember(key);
        APDU apdu = member.getAPDU(timeout);
        if (apdu == null && throwTimeout)
            throw new BACnetTimeoutException("Timeout while waiting for APDU id " + key.getInvokeId());
        return apdu;
    }

    public void leave(Key key) {
        synchronized (waitHere) {
            waitHere.remove(key);
        }
    }

    public void notifyMember(InetSocketAddress peer, Address linkService, byte id, boolean isFromServer, APDU apdu)
            throws BACnetException {
        System.out.println("Received APDU (1): " + apdu);
        if (apdu != null && apdu instanceof ComplexACK)
            ((ComplexACK) apdu).parseServiceData();
        System.out.println("Received APDU: " + apdu);
        Key key = new Key(peer, linkService, id, isFromServer);
        Member member = getMember(key);
        if (member != null) {
            member.setAPDU(apdu);
            return;
        }

        // The member may not have gotten around to listening for a message yet, so enter a retry loop to
        // make sure that this message gets to where it's supposed to go if there is somewhere to go.
        int attempts = 5;
        long sleep = 50;
        while (attempts > 0) {
            member = getMember(key);
            if (member != null) {
                member.setAPDU(apdu);
                return;
            }

            attempts--;
            try {
                Thread.sleep(sleep);
            }
            catch (InterruptedException e) {
                // no op
            }
        }
        throw new BACnetException("No waiting recipient for message: peer=" + peer + ", id=" + (id & 0xff)
                + ", isFromServer=" + isFromServer + ", message=" + apdu);
    }

    private Member getMember(Key key) {
        synchronized (waitHere) {
            return waitHere.get(key);
        }
    }

    /**
     * This class is used by network message controllers to manage the blocking of threads sending confirmed messages.
     * The instance itself serves as a monitor upon which the sending thread can wait (with a timeout). When a response
     * is received, the message controller can set it in here, automatically notifying the sending thread that the
     * response is available.
     * 
     * @author mlohbihler
     */
    class Member {
        private final LinkedList<APDU> apdus = new LinkedList<APDU>();

        synchronized void setAPDU(APDU apdu) {
            apdus.add(apdu);
            notify();
        }

        synchronized APDU getAPDU(long timeout) {
            // Check if there is an APDU available now.
            APDU result = apdus.poll();
            if (result != null)
                return result;

            // If not, wait the timeout and then check again.
            waitNoThrow(timeout);
            return apdus.poll();
        }

        private void waitNoThrow(long timeout) {
            try {
                super.wait(timeout);
            }
            catch (InterruptedException e) {
                // Ignore
            }
        }
    }

    public class Key {
        private final InetSocketAddress peer;
        private final Address linkService;
        private final byte invokeId;
        private final boolean fromServer;

        public Key(InetSocketAddress peer, Address linkService, byte invokeId, boolean fromServer) {
            this.peer = peer;
            this.linkService = linkService;
            this.invokeId = invokeId;
            this.fromServer = fromServer;
        }

        public InetSocketAddress getPeer() {
            return peer;
        }

        public Address getLinkService() {
            return linkService;
        }

        public byte getInvokeId() {
            return invokeId;
        }

        public boolean isFromServer() {
            return fromServer;
        }

        @Override
        public String toString() {
            return "Key(peer=" + peer + ", linkService=" + linkService + ", invokeId=" + invokeId + ", fromServer="
                    + fromServer + ")";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (fromServer ? 1231 : 1237);
            result = prime * result + ((linkService == null) ? 0 : linkService.hashCode());
            result = prime * result + ((peer == null) ? 0 : peer.hashCode());
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
            final Key other = (Key) obj;
            if (fromServer != other.fromServer)
                return false;
            if (invokeId != other.invokeId)
                return false;
            if (linkService == null) {
                if (other.linkService != null)
                    return false;
            }
            else if (!linkService.equals(other.linkService))
                return false;
            if (peer == null) {
                if (other.peer != null)
                    return false;
            }
            else if (!peer.equals(other.peer))
                return false;
            return true;
        }
    }
}
