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
package com.serotonin.bacnet4j.test;

import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.RequestUtils;

/**
 * @author Matthew Lohbihler
 */
public class DiscoveryTest {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        IpNetwork network = new IpNetwork(IpNetwork.DEFAULT_BROADCAST_IP, IpNetwork.DEFAULT_PORT,
                IpNetwork.DEFAULT_BIND_IP, 1);

        // LocalDevice localDevice = new LocalDevice(1234, "192.168.0.255");
        LocalDevice localDevice = new LocalDevice(1234, new Transport(network));
        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();

        // Who is
        // InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("96.51.27.33"), 47808);
        // localDevice.sendUnconfirmed(addr, null, new WhoIsRequest());
        //        localDevice.sendBroadcast(network.getBroadcastAddress(2068), null, new WhoIsRequest());
        // localDevice.sendUnconfirmed(new Address(new UnsignedInteger(47808), new OctetString(new byte[] { (byte) 96,
        // (byte) 51, (byte) 24, (byte) 1 })), null, new WhoIsRequest());
        // RemoteDevice rd = new RemoteDevice(105, new Address(new UnsignedInteger(47808),
        // new OctetString(new byte[] {(byte)206, (byte)210, 100, (byte)134})), null);
        // rd.setSegmentationSupported(Segmentation.segmentedBoth);
        // rd.setMaxAPDULengthAccepted(1476);
        // localDevice.addRemoteDevice(rd);
        //        localDevice.sendLocalBroadcast(new WhoIsRequest());
        localDevice.sendGlobalBroadcast(new WhoIsRequest());

        // Wait a bit for responses to come in.
        Thread.sleep(1000);

        // Get extended information for all remote devices.
        for (RemoteDevice d : localDevice.getRemoteDevices()) {
            RequestUtils.getExtendedDeviceInformation(localDevice, d);
            List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                    localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();

            PropertyReferences refs = new PropertyReferences();
            for (ObjectIdentifier oid : oids)
                addPropertyReferences(refs, oid);

            PropertyValues pvs = RequestUtils.readProperties(localDevice, d, refs, null);
            // pvs.
            System.out.println(pvs);
            System.out.println(d);
        }

        // Wait a bit for responses to come in.
        Thread.sleep(200000);

        localDevice.terminate();
    }

    private static void addPropertyReferences(PropertyReferences refs, ObjectIdentifier oid) {
        refs.add(oid, PropertyIdentifier.objectName);

        ObjectType type = oid.getObjectType();
        if (ObjectType.accumulator.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.analogInput.equals(type) || ObjectType.analogOutput.equals(type)
                || ObjectType.analogValue.equals(type) || ObjectType.pulseConverter.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.binaryInput.equals(type) || ObjectType.binaryOutput.equals(type)
                || ObjectType.binaryValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.inactiveText);
            refs.add(oid, PropertyIdentifier.activeText);
        }
        else if (ObjectType.lifeSafetyPoint.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.loop.equals(type)) {
            refs.add(oid, PropertyIdentifier.outputUnits);
        }
        else if (ObjectType.multiStateInput.equals(type) || ObjectType.multiStateOutput.equals(type)
                || ObjectType.multiStateValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.stateText);
        }
        else
            return;

        refs.add(oid, PropertyIdentifier.presentValue);
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }
}
