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
package com.serotonin.bacnet4j;

import java.io.Serializable;
import java.util.Arrays;

import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.util.ArrayUtils;

/**
 * @author Matthew Lohbihler
 */
public class Network implements Serializable {
    private static final long serialVersionUID = -8228723394333966565L;
    private final int networkNumber;
    private final byte[] networkAddress;

    public Network(int networkNumber, String networkAddress) {
        this.networkNumber = networkNumber;
        this.networkAddress = BACnetUtils.dottedStringToBytes(networkAddress);
    }

    public Network(int networkNumber, byte[] networkAddress) {
        this.networkNumber = networkNumber;
        this.networkAddress = networkAddress;
    }

    public int getNetworkNumber() {
        return networkNumber;
    }

    public byte[] getNetworkAddress() {
        return networkAddress;
    }

    public String getNetworkAddressDottedString() {
        return BACnetUtils.bytesToDottedString(networkAddress);
    }

    @Override
    public String toString() {
        return "Network(networkNumber=" + networkNumber + ", networkAddress=" + ArrayUtils.toHexString(networkAddress)
                + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(networkAddress);
        result = prime * result + networkNumber;
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
        final Network other = (Network) obj;
        if (!Arrays.equals(networkAddress, other.networkAddress))
            return false;
        if (networkNumber != other.networkNumber)
            return false;
        return true;
    }
}
