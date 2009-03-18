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

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.primitive.Date;
import com.serotonin.util.queue.ByteQueue;

public class CalendarEntry extends BaseType {
    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Date.class);
        classes.add(DateRange.class);
        classes.add(WeekNDay.class);
    }
    
    private final Choice entry;
    
    public CalendarEntry(Date date) {
        entry = new Choice(0, date);
    }
    
    public CalendarEntry(DateRange dateRange) {
        entry = new Choice(1, dateRange);
    }
    
    public CalendarEntry(WeekNDay weekNDay) {
        entry = new Choice(2, weekNDay);
    }

    @Override
    public void write(ByteQueue queue) {
        write(queue, entry);
    }
    
    public CalendarEntry(ByteQueue queue) throws BACnetException {
        entry = new Choice(queue, classes);
    }
}
