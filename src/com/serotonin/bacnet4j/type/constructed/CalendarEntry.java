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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Date;
import org.free.bacnet4j.util.ByteQueue;
/**
 * ASHRAE Standard 135-2012 Clause 21 p. 667<br> 
 * BACnetCalendarEntry ::= CHOICE {<br>
 *		date 						[0] Date,<br>
 *		dateRange 		 			[1] BACnetDateRange,<br>
 *		weekNDay 					[2] BACnetWeekNDay <br>
 *	}
 */
public class CalendarEntry extends BaseType {
    private static final long serialVersionUID = -4210434764578714766L;
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Date.class);
        classes.add(DateRange.class);
        classes.add(WeekNDay.class);
    }

    private final Choice entry;

    public CalendarEntry(final Date date) {
        entry = new Choice(0, date);
    }

    public CalendarEntry(final DateRange dateRange) {
        entry = new Choice(1, dateRange);
    }

    public CalendarEntry(final WeekNDay weekNDay) {
        entry = new Choice(2, weekNDay);
    }

    @Override
    public void write(final ByteQueue queue) {
        write(queue, entry);
    }

    public CalendarEntry(final ByteQueue queue) throws BACnetException {
        entry = new Choice(queue, classes);
    }

    public boolean isDate() {
        return entry.getContextId() == 0;
    }

    public boolean isDateRange() {
        return entry.getContextId() == 1;
    }

    public boolean isWeekNDay() {
        return entry.getContextId() == 2;
    }

    public Date getDate() {
        return (Date) entry.getDatum();
    }

    public DateRange getDateRange() {
        return (DateRange) entry.getDatum();
    }

    public WeekNDay getWeekNDay() {
        return (WeekNDay) entry.getDatum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entry == null) ? 0 : entry.hashCode());
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
        final CalendarEntry other = (CalendarEntry) obj;
        if (entry == null) {
            if (other.entry != null)
                return false;
        }
        else if (!entry.equals(other.entry))
            return false;
        return true;
    }
}