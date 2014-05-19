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
package com.serotonin.bacnet4j.enums;
/**
 * Per ASHRAE Standard 135-2012 p.713
 * 
 *
 */
public enum DayOfWeek {
    MONDAY     (1), 
    TUESDAY    (2), 
    WEDNESDAY  (3), 
    THURSDAY   (4), 
    FRIDAY     (5), 
    SATURDAY   (6), 
    SUNDAY     (7), 
    UNSPECIFIED(255);//i.e. X'FF' = any day of week

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
