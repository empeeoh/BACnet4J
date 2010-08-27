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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.type.primitive.BitString;
import com.serotonin.util.queue.ByteQueue;

public class LimitEnable extends BitString {
    private static final long serialVersionUID = -8754983228968085042L;

    public LimitEnable(boolean lowLimitEnable, boolean highLimitEnable) {
        super(new boolean[] { lowLimitEnable, highLimitEnable });
    }

    public LimitEnable(ByteQueue queue) {
        super(queue);
    }

    public boolean isLowLimitEnable() {
        return getValue()[0];
    }

    public boolean isHighLimitEnable() {
        return getValue()[1];
    }
}
