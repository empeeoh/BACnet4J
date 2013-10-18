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
package com.serotonin.bacnet4j.type.enumerated;

import com.serotonin.bacnet4j.type.primitive.Enumerated;
import org.free.bacnet4j.util.ByteQueue;

public class LifeSafetyMode extends Enumerated {
    private static final long serialVersionUID = -4939675355903263402L;
    public static final LifeSafetyMode off = new LifeSafetyMode(0);
    public static final LifeSafetyMode on = new LifeSafetyMode(1);
    public static final LifeSafetyMode test = new LifeSafetyMode(2);
    public static final LifeSafetyMode manned = new LifeSafetyMode(3);
    public static final LifeSafetyMode unmanned = new LifeSafetyMode(4);
    public static final LifeSafetyMode armed = new LifeSafetyMode(5);
    public static final LifeSafetyMode disarmed = new LifeSafetyMode(6);
    public static final LifeSafetyMode prearmed = new LifeSafetyMode(7);
    public static final LifeSafetyMode slow = new LifeSafetyMode(8);
    public static final LifeSafetyMode fast = new LifeSafetyMode(9);
    public static final LifeSafetyMode disconnected = new LifeSafetyMode(10);
    public static final LifeSafetyMode enabled = new LifeSafetyMode(11);
    public static final LifeSafetyMode disabled = new LifeSafetyMode(12);
    public static final LifeSafetyMode automaticReleaseDisabled = new LifeSafetyMode(13);
    public static final LifeSafetyMode defaultMode = new LifeSafetyMode(14);

    public static final LifeSafetyMode[] ALL = { off, on, test, manned, unmanned, armed, disarmed, prearmed, slow,
            fast, disconnected, enabled, disabled, automaticReleaseDisabled, defaultMode, };

    public LifeSafetyMode(int value) {
        super(value);
    }

    public LifeSafetyMode(ByteQueue queue) {
        super(queue);
    }
}
