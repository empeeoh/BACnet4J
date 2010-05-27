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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.util.queue.ByteQueue;

public class DeviceStatus extends Enumerated {
    private static final long serialVersionUID = -111489048861220863L;
    public static final DeviceStatus operational = new DeviceStatus(0);
    public static final DeviceStatus operationalReadOnly = new DeviceStatus(1);
    public static final DeviceStatus downloadRequired = new DeviceStatus(2);
    public static final DeviceStatus downloadInProgress = new DeviceStatus(3);
    public static final DeviceStatus nonOperational = new DeviceStatus(4);
    public static final DeviceStatus backupInProgress = new DeviceStatus(5);

    public DeviceStatus(int value) {
        super(value);
    }

    public DeviceStatus(ByteQueue queue) {
        super(queue);
    }
}
