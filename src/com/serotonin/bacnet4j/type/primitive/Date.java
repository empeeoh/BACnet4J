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
package com.serotonin.bacnet4j.type.primitive;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.util.queue.ByteQueue;

public class Date extends Primitive {
    private static final long serialVersionUID = -5981590660136837990L;

    public static final byte TYPE_ID = 10;

    private final int year;
    private final Month month;
    private final int day;
    private final DayOfWeek dayOfWeek;

    public Date(int year, Month month, int day, DayOfWeek dayOfWeek) {
        if (year > 1900)
            year -= 1900;
        else if (year == -1)
            year = 255;
        if (day == -1)
            day = 255;

        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
    }

    public Date() {
        this(new GregorianCalendar());
    }

    public Date(GregorianCalendar now) {
        this.year = now.get(Calendar.YEAR) - 1900;
        this.month = Month.valueOf((byte) (now.get(Calendar.MONTH) + 1));
        this.day = now.get(Calendar.DATE);
        this.dayOfWeek = DayOfWeek.valueOf((byte) (((now.get(Calendar.DAY_OF_WEEK) + 5) % 7) + 1));
    }

    public boolean isYearUnspecified() {
        return year == 255;
    }

    public int getYear() {
        return year;
    }

    public int getCenturyYear() {
        return year + 1900;
    }

    public Month getMonth() {
        return month;
    }

    public boolean isLastDayOfMonth() {
        return day == 32;
    }

    public boolean isDayUnspecified() {
        return day == 255;
    }

    public int getDay() {
        return day;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    //
    // Reading and writing
    //
    public Date(ByteQueue queue) {
        readTag(queue);
        year = queue.popU1B();
        month = Month.valueOf(queue.pop());
        day = queue.popU1B();
        dayOfWeek = DayOfWeek.valueOf(queue.pop());
    }

    @Override
    public void writeImpl(ByteQueue queue) {
        queue.push(year);
        queue.push(month.getId());
        queue.push((byte) day);
        queue.push(dayOfWeek.getId());
    }

    @Override
    protected long getLength() {
        return 4;
    }

    @Override
    protected byte getTypeId() {
        return TYPE_ID;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + day;
        result = PRIME * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
        result = PRIME * result + ((month == null) ? 0 : month.hashCode());
        result = PRIME * result + year;
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
        final Date other = (Date) obj;
        if (day != other.day)
            return false;
        if (dayOfWeek == null) {
            if (other.dayOfWeek != null)
                return false;
        }
        else if (!dayOfWeek.equals(other.dayOfWeek))
            return false;
        if (month == null) {
            if (other.month != null)
                return false;
        }
        else if (!month.equals(other.month))
            return false;
        if (year != other.year)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return dayOfWeek + " " + month + " " + day + ", " + year;
    }
}
