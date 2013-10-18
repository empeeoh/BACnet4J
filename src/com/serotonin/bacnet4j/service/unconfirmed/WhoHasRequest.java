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
package com.serotonin.bacnet4j.service.unconfirmed;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.BaseType;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class WhoHasRequest extends UnconfirmedRequestService {
    private static final long serialVersionUID = -3261764708955375488L;

    public static final byte TYPE_ID = 7;

    private static List<Class<? extends Encodable>> classes;
    static {
        classes = new ArrayList<Class<? extends Encodable>>();
        classes.add(Encodable.class);
        classes.add(Encodable.class);
        classes.add(ObjectIdentifier.class);
        classes.add(CharacterString.class);
    }

    private final Limits limits;
    private final Choice object;

    public WhoHasRequest(Limits limits, ObjectIdentifier identifier) {
        this.limits = limits;
        object = new Choice(2, identifier);
    }

    public WhoHasRequest(Limits limits, CharacterString name) {
        this.limits = limits;
        object = new Choice(3, name);
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public void handle(LocalDevice localDevice, Address from, OctetString linkService) throws BACnetException {
        // Check if we're in the device id range.
        if (limits != null) {
            int localId = localDevice.getConfiguration().getInstanceId();
            if (localId < limits.getDeviceInstanceRangeLowLimit().intValue()
                    || localId > limits.getDeviceInstanceRangeHighLimit().intValue())
                return;
        }

        // Check if we have the thing being looking for.
        BACnetObject result;
        if (object.getContextId() == 2) {
            ObjectIdentifier oid = (ObjectIdentifier) object.getDatum();
            result = localDevice.getObject(oid);
        }
        else if (object.getContextId() == 3) {
            String name = ((CharacterString) object.getDatum()).toString();
            result = localDevice.getObject(name);
        }
        else
            return;

        if (result != null) {
            // Return the result in an i have message.
            IHaveRequest response = new IHaveRequest(localDevice.getConfiguration().getId(), result.getId(),
                    result.getRawObjectName());
            localDevice.sendGlobalBroadcast(response);
        }
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, limits);
        write(queue, object);
    }

    public WhoHasRequest(ByteQueue queue) throws BACnetException {
        Limits l = new Limits(queue);
        limits = l.getDeviceInstanceRangeLowLimit() == null ? null : l;
        object = new Choice(queue, classes);
    }

    public static class Limits extends BaseType {
        private static final long serialVersionUID = -2736168226229323897L;
        private UnsignedInteger deviceInstanceRangeLowLimit;
        private UnsignedInteger deviceInstanceRangeHighLimit;

        @Override
        public void write(ByteQueue queue) {
            write(queue, deviceInstanceRangeLowLimit, 0);
            write(queue, deviceInstanceRangeHighLimit, 1);
        }

        Limits(ByteQueue queue) throws BACnetException {
            deviceInstanceRangeLowLimit = readOptional(queue, UnsignedInteger.class, 0);
            deviceInstanceRangeHighLimit = readOptional(queue, UnsignedInteger.class, 1);
        }

        public Limits(UnsignedInteger deviceInstanceRangeLowLimit, UnsignedInteger deviceInstanceRangeHighLimit) {
            if (deviceInstanceRangeLowLimit == null || deviceInstanceRangeHighLimit == null)
                throw new RuntimeException("Both the low and high limits must be set");
            this.deviceInstanceRangeLowLimit = deviceInstanceRangeLowLimit;
            this.deviceInstanceRangeHighLimit = deviceInstanceRangeHighLimit;
        }

        public UnsignedInteger getDeviceInstanceRangeLowLimit() {
            return deviceInstanceRangeLowLimit;
        }

        public void setDeviceInstanceRangeLowLimit(UnsignedInteger deviceInstanceRangeLowLimit) {
            this.deviceInstanceRangeLowLimit = deviceInstanceRangeLowLimit;
        }

        public UnsignedInteger getDeviceInstanceRangeHighLimit() {
            return deviceInstanceRangeHighLimit;
        }

        public void setDeviceInstanceRangeHighLimit(UnsignedInteger deviceInstanceRangeHighLimit) {
            this.deviceInstanceRangeHighLimit = deviceInstanceRangeHighLimit;
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((limits == null) ? 0 : limits.hashCode());
        result = PRIME * result + ((object == null) ? 0 : object.hashCode());
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
        final WhoHasRequest other = (WhoHasRequest) obj;
        if (limits == null) {
            if (other.limits != null)
                return false;
        }
        else if (!limits.equals(other.limits))
            return false;
        if (object == null) {
            if (other.object != null)
                return false;
        }
        else if (!object.equals(other.object))
            return false;
        return true;
    }
}
