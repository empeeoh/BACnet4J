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
package com.serotonin.bacnet4j.type.primitive;

import com.serotonin.util.queue.ByteQueue;

public class Unsigned16 extends UnsignedInteger {
    private static final long serialVersionUID = -4615555609013008931L;
    private static final int MAX = 0xffff - 1;

    public Unsigned16(int value) {
        super(value);
        if (value > MAX)
            throw new IllegalArgumentException("Value cannot be greater than " + MAX);
    }

    public Unsigned16(ByteQueue queue) {
        super(queue);
    }
}
