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
 */
package com.serotonin.bacnet4j.service.unconfirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.Service;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.util.queue.ByteQueue;

abstract public class UnconfirmedRequestService extends Service {
    private static final long serialVersionUID = 8962921362279665295L;

    public static UnconfirmedRequestService createUnconfirmedRequestService(byte type, ByteQueue queue)
            throws BACnetException {
        if (type == IAmRequest.TYPE_ID)
            return new IAmRequest(queue);
        if (type == IHaveRequest.TYPE_ID)
            return new IHaveRequest(queue);
        if (type == UnconfirmedCovNotificationRequest.TYPE_ID)
            return new UnconfirmedCovNotificationRequest(queue);
        if (type == UnconfirmedEventNotificationRequest.TYPE_ID)
            return new UnconfirmedEventNotificationRequest(queue);
        if (type == UnconfirmedPrivateTransferRequest.TYPE_ID)
            return new UnconfirmedPrivateTransferRequest(queue);
        if (type == UnconfirmedTextMessageRequest.TYPE_ID)
            return new UnconfirmedTextMessageRequest(queue);
        if (type == TimeSynchronizationRequest.TYPE_ID)
            return new TimeSynchronizationRequest(queue);
        if (type == WhoHasRequest.TYPE_ID)
            return new WhoHasRequest(queue);
        if (type == WhoIsRequest.TYPE_ID)
            return new WhoIsRequest(queue);
        if (type == UTCTimeSynchronizationRequest.TYPE_ID)
            return new UTCTimeSynchronizationRequest(queue);

        throw new BACnetException("Unsupported unconfirmed service: " + (type & 0xff));
    }

    abstract public void handle(LocalDevice localDevice, Address from, Network network) throws BACnetException;
}
