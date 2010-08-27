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
package com.serotonin.bacnet4j.event;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class DefaultExceptionListener implements ExceptionListener {
    public void unimplementedVendorService(UnsignedInteger vendorId, UnsignedInteger serviceNumber, ByteQueue queue) {
        System.out.println("Received unimplemented vendor service: vendor id=" + vendorId + ", service number="
                + serviceNumber + ", bytes (with context id)=" + queue);
    }

    public void receivedException(Exception e) {
        e.printStackTrace();
    }

    public void receivedThrowable(Throwable t) {
        t.printStackTrace();
    }
}
