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

import com.serotonin.bacnet4j.type.primitive.BitString;
import org.free.bacnet4j.util.ByteQueue;

public class DaysOfWeek extends BitString {
    private static final long serialVersionUID = -7381461753680751900L;

    public DaysOfWeek() {
        super(new boolean[7]);
    }

    public DaysOfWeek(boolean defaultValue) {
        super(7, defaultValue);
    }

    public DaysOfWeek(ByteQueue queue) {
        super(queue);
    }

    public boolean contains(int day) {
        return getValue()[day];
    }

    public boolean isMonday() {
        return getValue()[0];
    }

    public void setMonday(boolean monday) {
        getValue()[0] = monday;
    }

    public boolean isTuesday() {
        return getValue()[1];
    }

    public void setTuesday(boolean tuesday) {
        getValue()[1] = tuesday;
    }

    public boolean isWednesday() {
        return getValue()[2];
    }

    public void setWednesday(boolean wednesday) {
        getValue()[2] = wednesday;
    }

    public boolean isThursday() {
        return getValue()[3];
    }

    public void setThursday(boolean thursday) {
        getValue()[3] = thursday;
    }

    public boolean isFriday() {
        return getValue()[4];
    }

    public void setFriday(boolean friday) {
        getValue()[4] = friday;
    }

    public boolean isSaturday() {
        return getValue()[5];
    }

    public void setSaturday(boolean saturday) {
        getValue()[5] = saturday;
    }

    public boolean isSunday() {
        return getValue()[6];
    }

    public void setSunday(boolean sunday) {
        getValue()[6] = sunday;
    }
}
