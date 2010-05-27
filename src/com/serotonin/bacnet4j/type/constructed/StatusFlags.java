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

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class StatusFlags extends BitString {
    private static final long serialVersionUID = 2553458399968003127L;

    public StatusFlags(boolean inAlarm, boolean fault, boolean overridden, boolean outOfService) {
        super(new boolean[] { inAlarm, fault, overridden, outOfService });
    }

    public StatusFlags(ByteQueue queue) {
        super(queue);
    }

    public boolean isInAlarm() {
        return getValue()[0];
    }

    public boolean isFault() {
        return getValue()[1];
    }

    public boolean isOverridden() {
        return getValue()[2];
    }

    public boolean isOutOfService() {
        return getValue()[3];
    }
}
