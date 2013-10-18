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
package com.serotonin.bacnet4j.service.confirmed;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.NotImplementedException;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Enumerated;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import org.free.bacnet4j.util.ByteQueue;

public class DeviceCommunicationControlRequest extends ConfirmedRequestService {
    private static final long serialVersionUID = 5975917456558517142L;

    public static final byte TYPE_ID = 17;

    private final UnsignedInteger timeDuration;
    private final EnableDisable enableDisable;
    private final CharacterString password;

    public DeviceCommunicationControlRequest(UnsignedInteger timeDuration, EnableDisable enableDisable,
            CharacterString password) {
        super();
        this.timeDuration = timeDuration;
        this.enableDisable = enableDisable;
        this.password = password;
    }

    @Override
    public byte getChoiceId() {
        return TYPE_ID;
    }

    @Override
    public AcknowledgementService handle(LocalDevice localDevice, Address from, OctetString linkService)
            throws BACnetException {
        throw new NotImplementedException();
    }

    @Override
    public void write(ByteQueue queue) {
        writeOptional(queue, timeDuration, 0);
        write(queue, enableDisable, 1);
        writeOptional(queue, password, 2);
    }

    DeviceCommunicationControlRequest(ByteQueue queue) throws BACnetException {
        timeDuration = readOptional(queue, UnsignedInteger.class, 0);
        enableDisable = read(queue, EnableDisable.class, 1);
        password = readOptional(queue, CharacterString.class, 2);
    }

    public static class EnableDisable extends Enumerated {
        private static final long serialVersionUID = -697975876817917708L;
        public static final EnableDisable enable = new EnableDisable(0);
        public static final EnableDisable disable = new EnableDisable(1);
        public static final EnableDisable disableInitiation = new EnableDisable(2);

        private EnableDisable(int value) {
            super(value);
        }

        public EnableDisable(ByteQueue queue) {
            super(queue);
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((enableDisable == null) ? 0 : enableDisable.hashCode());
        result = PRIME * result + ((password == null) ? 0 : password.hashCode());
        result = PRIME * result + ((timeDuration == null) ? 0 : timeDuration.hashCode());
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
        final DeviceCommunicationControlRequest other = (DeviceCommunicationControlRequest) obj;
        if (enableDisable == null) {
            if (other.enableDisable != null)
                return false;
        }
        else if (!enableDisable.equals(other.enableDisable))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        }
        else if (!password.equals(other.password))
            return false;
        if (timeDuration == null) {
            if (other.timeDuration != null)
                return false;
        }
        else if (!timeDuration.equals(other.timeDuration))
            return false;
        return true;
    }
}
