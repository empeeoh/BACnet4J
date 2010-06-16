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
package com.serotonin.bacnet4j.enums;

public enum DayOfWeek {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7), UNSPECIFIED(255);

    private byte id;

    DayOfWeek(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }

    public static DayOfWeek valueOf(byte id) {
        if (id == MONDAY.id)
            return MONDAY;
        if (id == TUESDAY.id)
            return TUESDAY;
        if (id == WEDNESDAY.id)
            return WEDNESDAY;
        if (id == THURSDAY.id)
            return THURSDAY;
        if (id == FRIDAY.id)
            return FRIDAY;
        if (id == SATURDAY.id)
            return SATURDAY;
        if (id == SUNDAY.id)
            return SUNDAY;
        return UNSPECIFIED;
    }
}
