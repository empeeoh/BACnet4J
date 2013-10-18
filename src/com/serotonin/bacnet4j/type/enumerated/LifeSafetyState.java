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

public class LifeSafetyState extends Enumerated {
    private static final long serialVersionUID = 2161598068039518923L;
    public static final LifeSafetyState quiet = new LifeSafetyState(0);
    public static final LifeSafetyState preAlarm = new LifeSafetyState(1);
    public static final LifeSafetyState alarm = new LifeSafetyState(2);
    public static final LifeSafetyState fault = new LifeSafetyState(3);
    public static final LifeSafetyState faultPreAlarm = new LifeSafetyState(4);
    public static final LifeSafetyState faultAlarm = new LifeSafetyState(5);
    public static final LifeSafetyState notReady = new LifeSafetyState(6);
    public static final LifeSafetyState active = new LifeSafetyState(7);
    public static final LifeSafetyState tamper = new LifeSafetyState(8);
    public static final LifeSafetyState testAlarm = new LifeSafetyState(9);
    public static final LifeSafetyState testActive = new LifeSafetyState(10);
    public static final LifeSafetyState testFault = new LifeSafetyState(11);
    public static final LifeSafetyState testFaultAlarm = new LifeSafetyState(12);
    public static final LifeSafetyState holdup = new LifeSafetyState(13);
    public static final LifeSafetyState duress = new LifeSafetyState(14);
    public static final LifeSafetyState tamperAlarm = new LifeSafetyState(15);
    public static final LifeSafetyState abnormal = new LifeSafetyState(16);
    public static final LifeSafetyState emergencyPower = new LifeSafetyState(17);
    public static final LifeSafetyState delayed = new LifeSafetyState(18);
    public static final LifeSafetyState blocked = new LifeSafetyState(19);
    public static final LifeSafetyState localAlarm = new LifeSafetyState(20);
    public static final LifeSafetyState generalAlarm = new LifeSafetyState(21);
    public static final LifeSafetyState supervisory = new LifeSafetyState(22);
    public static final LifeSafetyState testSupervisory = new LifeSafetyState(23);

    public static final LifeSafetyState[] ALL = { quiet, preAlarm, alarm, fault, faultPreAlarm, faultAlarm, notReady,
            active, tamper, testAlarm, testActive, testFault, testFaultAlarm, holdup, duress, tamperAlarm, abnormal,
            emergencyPower, delayed, blocked, localAlarm, generalAlarm, supervisory, testSupervisory, };

    public LifeSafetyState(int value) {
        super(value);
    }

    public LifeSafetyState(ByteQueue queue) {
        super(queue);
    }

    @Override
    public String toString() {
        int type = intValue();
        if (type == quiet.intValue())
            return "Quiet";
        if (type == preAlarm.intValue())
            return "Pre-alarm";
        if (type == alarm.intValue())
            return "Alarm";
        if (type == fault.intValue())
            return "Fault";
        if (type == faultPreAlarm.intValue())
            return "Fault pre-alarm";
        if (type == faultAlarm.intValue())
            return "Fault alarm";
        if (type == notReady.intValue())
            return "Not ready";
        if (type == active.intValue())
            return "Active";
        if (type == tamper.intValue())
            return "Tamper";
        if (type == testAlarm.intValue())
            return "Test alarm";
        if (type == testActive.intValue())
            return "Test active";
        if (type == testFault.intValue())
            return "Test fault";
        if (type == testFaultAlarm.intValue())
            return "Test fault alarm";
        if (type == holdup.intValue())
            return "Holdup";
        if (type == duress.intValue())
            return "Duress";
        if (type == tamperAlarm.intValue())
            return "Tamper alarm";
        if (type == abnormal.intValue())
            return "Abnormal";
        if (type == emergencyPower.intValue())
            return "Emergency power";
        if (type == delayed.intValue())
            return "Delayed";
        if (type == blocked.intValue())
            return "Blocked";
        if (type == localAlarm.intValue())
            return "Local alarm";
        if (type == generalAlarm.intValue())
            return "General alarm";
        if (type == supervisory.intValue())
            return "Supervisory";
        if (type == testSupervisory.intValue())
            return "Test supervisory";

        return "Unknown: " + type;
    }

}
