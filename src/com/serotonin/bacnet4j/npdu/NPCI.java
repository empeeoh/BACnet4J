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
 */
package com.serotonin.bacnet4j.npdu;

import java.math.BigInteger;

import com.serotonin.util.queue.ByteQueue;

/**
 * Network layer protocol control information. This class currently only implements the reading of information.
 * 
 * @author mlohbihler
 */
public class NPCI {
    private final int version;
    private final BigInteger control;
    private int destinationNetwork;
    private int destinationLength;
    private byte[] destinationAddress;
    private int sourceNetwork;
    private int sourceLength;
    private byte[] sourceAddress;
    private int hopCount;
    private int messageType;
    private int vendorId;

    public NPCI(ByteQueue queue) {
        version = queue.popU1B();
        control = BigInteger.valueOf(queue.popU1B());

        if (control.testBit(5)) {
            destinationNetwork = queue.popU2B();
            destinationLength = queue.popU1B();
            if (destinationLength > 0) {
                destinationAddress = new byte[destinationLength];
                queue.pop(destinationAddress);
            }
        }

        if (control.testBit(3)) {
            sourceNetwork = queue.popU2B();
            sourceLength = queue.popU1B();
            sourceAddress = new byte[sourceLength];
            queue.pop(sourceAddress);
        }

        if (control.testBit(5))
            hopCount = queue.popU1B();

        if (control.testBit(7)) {
            messageType = queue.popU1B();
            if (messageType >= 80)
                vendorId = queue.popU2B();
        }
    }

    public boolean hasDestinationInfo() {
        return control.testBit(5);
    }

    public boolean isDestinationBroadcast() {
        return destinationLength == 0;
    }

    public boolean hasSourceInfo() {
        return control.testBit(3);
    }

    public boolean isExpectingReply() {
        return control.testBit(2);
    }

    public boolean isNetworkMessage() {
        return control.testBit(7);
    }

    public boolean isVendorSpecificNetworkMessage() {
        return isNetworkMessage() && messageType >= 80;
    }

    public int getNetworkPriority() {
        return (control.testBit(1) ? 2 : 0) | (control.testBit(0) ? 1 : 0);
    }

    public byte[] getDestinationAddress() {
        return destinationAddress;
    }

    public int getDestinationLength() {
        return destinationLength;
    }

    public int getDestinationNetwork() {
        return destinationNetwork;
    }

    public int getHopCount() {
        return hopCount;
    }

    public int getMessageType() {
        return messageType;
    }

    public byte[] getSourceAddress() {
        return sourceAddress;
    }

    public int getSourceLength() {
        return sourceLength;
    }

    public int getSourceNetwork() {
        return sourceNetwork;
    }

    public int getVendorId() {
        return vendorId;
    }

    public int getVersion() {
        return version;
    }
    //    
    // public static void main(String[] args) {
    // byte[] b1 = {(byte)0x81,(byte)0xb,(byte)0x0,(byte)0x18};
    // byte[] b2 = {(byte)0x1,(byte)0x8,(byte)0x0,(byte)0x64,(byte)0x1,
    // (byte)0x2,(byte)0x10,(byte)0x0,(byte)0xc4,(byte)0x2,(byte)0x0,(byte)0x7,(byte)0xd0,(byte)0x22,
    // (byte)0x1,(byte)0xe0,(byte)0x91,(byte)0x0,(byte)0x21,(byte)0x23};
    // ByteQueue q = new ByteQueue(b2);
    // new NPCI(q);
    //
    // }
}
