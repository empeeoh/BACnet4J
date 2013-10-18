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
package com.serotonin.bacnet4j.type.constructed;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.primitive.Date;
import org.free.bacnet4j.util.ByteQueue;
/**
 * ASHRAE Standard 135-2012 Clause 21 p. 667<br> 
 * BACnetDateRange ::= SEQUENCE { -- see {@link com.serotonin.bacnet4j.type.primitive.Date} 
 * Clause 20.2.12 p.631 for restrictions <br>
 * startDate Date,<br>
 * endDate Date }<br>
 *
 */
public class DateRange extends BaseType {
    private static final long serialVersionUID = 7219491325251523667L;
    private final Date startDate;
    private final Date endDate;

    public DateRange(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, startDate);
        write(queue, endDate);
    }

    public DateRange(ByteQueue queue) throws BACnetException {
        startDate = read(queue, Date.class);
        endDate = read(queue, Date.class);
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = PRIME * result + ((startDate == null) ? 0 : startDate.hashCode());
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
        final DateRange other = (DateRange) obj;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        }
        else if (!endDate.equals(other.endDate))
            return false;
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        }
        else if (!startDate.equals(other.startDate))
            return false;
        return true;
    }
}
