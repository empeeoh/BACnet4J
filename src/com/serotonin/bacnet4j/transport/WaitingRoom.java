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
package com.serotonin.bacnet4j.transport;

import java.util.HashMap;
import java.util.LinkedList;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.Abort;
import com.serotonin.bacnet4j.apdu.AckAPDU;
import com.serotonin.bacnet4j.apdu.ConfirmedRequest;
import com.serotonin.bacnet4j.apdu.Segmentable;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetRuntimeException;
import com.serotonin.bacnet4j.exception.BACnetTimeoutException;
import com.serotonin.bacnet4j.exception.SegmentedMessageAbortedException;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;

public class WaitingRoom {
    private final HashMap<WaitingRoomKey, Member> waitHere = new HashMap<WaitingRoomKey, Member>();
    private byte nextInvokeId;

    synchronized public WaitingRoomKey enterClient(Address address, OctetString linkService) {
        Member member = new Member();
        WaitingRoomKey key;

        // Loop until we find a key that is available.
        int attempts = 256;
        while (true) {
            // We set the server value in the key to true so that it matches with the message from the server.
            key = new WaitingRoomKey(address, linkService, nextInvokeId++, true);

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

    public WaitingRoomKey enterServer(Address address, OctetString linkService, byte id) {
        // We set the server value in the key to false so that it matches with the message from the client.
        WaitingRoomKey key = new WaitingRoomKey(address, linkService, id, false);
        Member member = new Member();

        synchronized (waitHere) {
            if (waitHere.get(key) != null)
                throw new BACnetRuntimeException("Cannot enter a server into the waiting room. key=" + key);
            waitHere.put(key, member);
        }

        return key;
    }

    public AckAPDU getAck(WaitingRoomKey key, long timeout, boolean throwTimeout) throws BACnetException {
        return (AckAPDU) getAPDU(key, timeout, throwTimeout);
    }

    public ConfirmedRequest getRequest(WaitingRoomKey key, long timeout, boolean throwTimeout) throws BACnetException {
        return (ConfirmedRequest) getAPDU(key, timeout, throwTimeout);
    }

    public Segmentable getSegmentable(WaitingRoomKey key, long timeout, boolean throwTimeout) throws BACnetException {
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

    public APDU getAPDU(WaitingRoomKey key, long timeout, boolean throwTimeout) throws BACnetException {
        Member member = getMember(key);
        APDU apdu = member.getAPDU(timeout);
        if (apdu == null && throwTimeout)
            throw new BACnetTimeoutException("Timeout while waiting for APDU id " + key.getInvokeId());
        return apdu;
    }

    public void leave(WaitingRoomKey key) {
        synchronized (waitHere) {
            waitHere.remove(key);
        }
    }

    public void notifyMember(Address address, OctetString linkService, byte id, boolean isFromServer, APDU apdu)
            throws BACnetException {
        WaitingRoomKey key = new WaitingRoomKey(address, linkService, id, isFromServer);
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
        throw new BACnetException("No waiting recipient for message: address=" + address + ", id=" + (id & 0xff)
                + ", isFromServer=" + isFromServer + ", message=" + apdu);
    }

    private Member getMember(WaitingRoomKey key) {
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
    static class Member {
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
}
