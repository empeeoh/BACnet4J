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

import com.serotonin.bacnet4j.enums.DayOfWeek;
import com.serotonin.bacnet4j.enums.Month;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import org.free.bacnet4j.util.ByteQueue;
/**
 * ASHRAE Standard 135-2012 Clause 21 p. 713<br>
 * BACnetWeekNDay ::= OCTET STRING (SIZE (3))<br>
 * -- first octet  month(1..14)   1 = January<br>
 * --                            13 = odd months<br>
 * --							 14 = even months<br>
 * --							255 = X'FF' = any month<br>
 * -- second octet weekOfMonth where: 1 = days numbered 1-7<br>
 * -- 								  2 = days numbered 8-14<br>
 * -- 								  3 = days numbered 15-21<br>
 * --								  4 = days numbered 22-28<br>
 * --								  5 = days numbered 29-31<br>
 * --								  6 = last 7 days of this month<br> 
 * --								255 =X'FF' = any week of this month<br>
 * -- third octet dayOfWeek where: 1 = Monday<br>
 * -- 							   7 = Sunday<br>
 * --							 255 = X'FF' = any day of week<br>
 */
public class WeekNDay extends OctetString {
    private static final long serialVersionUID = -2836161294089567458L;

    public static class WeekOfMonth extends Enumerated {
        private static final long serialVersionUID = 1951617360223950570L;
        public static final WeekOfMonth days1to7 = new WeekOfMonth(1);
        public static final WeekOfMonth days8to14 = new WeekOfMonth(2);
        public static final WeekOfMonth days15to21 = new WeekOfMonth(3);
        public static final WeekOfMonth days22to28 = new WeekOfMonth(4);
        public static final WeekOfMonth days29to31 = new WeekOfMonth(5);
        public static final WeekOfMonth last7Days = new WeekOfMonth(6);
        public static final WeekOfMonth any = new WeekOfMonth(255);

        public static WeekOfMonth valueOf(byte b) {
            switch (b) {
            case 1:
                return days1to7;
            case 2:
                return days8to14;
            case 3:
                return days15to21;
            case 4:
                return days22to28;
            case 5:
                return days29to31;
            case 6:
                return last7Days;
            default:
                return any;
            }
        }

        private WeekOfMonth(int value) {
            super(value);
        }

        public WeekOfMonth(ByteQueue queue) {
            super(queue);
        }
    }

    public WeekNDay(Month month, WeekOfMonth weekOfMonth, DayOfWeek dayOfWeek) {
        super(new byte[] { month.getId(), weekOfMonth.byteValue(), dayOfWeek.getId() });
    }

    public Month getMonth() {
        return Month.valueOf(getBytes()[0]);
    }

    public WeekOfMonth getWeekOfMonth() {
        return WeekOfMonth.valueOf(getBytes()[1]);
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.valueOf(getBytes()[2]);
    }

    public WeekNDay(ByteQueue queue) {
        super(queue);
    }
}
