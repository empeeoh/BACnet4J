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

public class ResultFlags extends BitString {
    private static final long serialVersionUID = 7657134249555371620L;

    public ResultFlags(boolean firstItem, boolean lastItem, boolean moreItems) {
        super(new boolean[] { firstItem, lastItem, moreItems });
    }

    public ResultFlags(ByteQueue queue) {
        super(queue);
    }

    public boolean isFirstItem() {
        return getValue()[0];
    }

    public boolean isLastItem() {
        return getValue()[1];
    }

    public boolean isMoreItems() {
        return getValue()[2];
    }
}
