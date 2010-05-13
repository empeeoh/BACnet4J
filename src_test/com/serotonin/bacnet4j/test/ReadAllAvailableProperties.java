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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;

/**
 * Discovers and devices and print all properties of all objects found. this is done by using PropertyIdentifier.all so
 * the Device will send all propertys that are set. if you want poll all PropertyId {@link ReadPropertyRangeTest}.
 * 
 * @author Matthew Lohbihler
 * @author Arne Plöse
 */
public class ReadAllAvailableProperties {
    public static String BROADCAST_ADDRESS = "127.0.0.255";
    private LoopDevice loopDevice;
    private final LocalDevice localDevice;
    // remote devices found
    final List<RemoteDevice> remoteDevices = new ArrayList<RemoteDevice>();

    public ReadAllAvailableProperties(String broadcastAddress, int port) throws IOException {
        localDevice = new LocalDevice(1234, broadcastAddress);
        localDevice.setPort(port);
        localDevice.getEventHandler().addListener(new DeviceEventListener() {

            public void listenerException(Throwable e) {
                System.out.println("DiscoveryTest listenerException");
            }

            public void iAmReceived(RemoteDevice d) {
                System.out.println("DiscoveryTest iAmReceived");
                remoteDevices.add(d);
                synchronized (ReadAllAvailableProperties.this) {
                    ReadAllAvailableProperties.this.notifyAll();
                }
            }

            public boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv) {
                System.out.println("DiscoveryTest allowPropertyWrite");
                return true;
            }

            public void propertyWritten(BACnetObject obj, PropertyValue pv) {
                System.out.println("DiscoveryTest propertyWritten");
            }

            public void iHaveReceived(RemoteDevice d, RemoteObject o) {
                System.out.println("DiscoveryTest iHaveReceived");
            }

            public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier,
                    RemoteDevice initiatingDevice, ObjectIdentifier monitoredObjectIdentifier,
                    UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {
                System.out.println("DiscoveryTest covNotificationReceived");
            }

            public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice,
                    ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass,
                    UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType,
                    Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues) {
                System.out.println("DiscoveryTest eventNotificationReceived");
            }

            public void textMessageReceived(RemoteDevice textMessageSourceDevice, Choice messageClass,
                    MessagePriority messagePriority, CharacterString message) {
                System.out.println("DiscoveryTest textMessageReceived");
            }

            public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber,
                    Encodable serviceParameters) {
                System.out.println("DiscoveryTest privateTransferReceived");
            }

            public void reinitializeDevice(ReinitializedStateOfDevice reinitializedStateOfDevice) {
                System.out.println("DiscoveryTest reinitializeDevice");
            }

            @Override
            public void synchronizeTime(DateTime dateTime, boolean utc) {
                System.out.println("DiscoveryTest synchronizeTime");
            }
        });
        localDevice.initialize();

    }

    /**
     * Send a WhoIs request and wait for the first to answer
     * 
     * @throws java.lang.Exception
     */
    public void doDiscover() throws Exception {
        // Who is
        System.out.println("Send Broadcast WhoIsRequest() ");
        // Send the broadcast to the correct port of the LoopDevice !!!
        localDevice.sendBroadcast(loopDevice.getPort(), new WhoIsRequest(null, null));

        // wait for notification in iAmReceived() Timeout 2 sec
        synchronized (this) {
            final long start = System.currentTimeMillis();
            this.wait(2000);
            System.out.println(" waited for iAmReceived: " + (System.currentTimeMillis() - start) + " ms");
        }

        // An other way to get to the list of devices
        // return localDevice.getRemoteDevices();
    }

    @SuppressWarnings("unchecked")
    private void printDevices() throws BACnetException {
        for (RemoteDevice d : remoteDevices) {

            localDevice.getExtendedDeviceInformation(d);

            List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) localDevice.sendReadPropertyAllowNull(d, d
                    .getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();

            PropertyReferences refs = new PropertyReferences();
            // add the property references of the "device object" to the list
            refs.add(d.getObjectIdentifier(), PropertyIdentifier.all);

            // and now from all objects under the device object >> ai0, ai1,bi0,bi1...
            for (ObjectIdentifier oid : oids) {
                refs.add(oid, PropertyIdentifier.all);
            }

            System.out.println("Start read properties");
            final long start = System.currentTimeMillis();

            PropertyValues pvs = localDevice.readProperties(d, refs);
            System.out.println(String.format("Properties read done in %d ms", System.currentTimeMillis() - start));
            printObject(d.getObjectIdentifier(), pvs);
            for (ObjectIdentifier oid : oids) {
                printObject(oid, pvs);
            }

        }

        System.out.println("Remote devices done...");
    }

    private void printObject(ObjectIdentifier oid, PropertyValues pvs) {
        System.out.println(String.format("\t%s", oid));
        for (ObjectPropertyReference opr : pvs) {
            if (oid.equals(opr.getObjectIdentifier())) {
                System.out.println(String.format("\t\t%s = %s", opr.getPropertyIdentifier().toString(), pvs
                        .getNoErrorCheck(opr)));
            }

        }
    }

    /**
     * Note same Broadcast address, but different ports!!!
     * 
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        ReadAllAvailableProperties dt = new ReadAllAvailableProperties(BROADCAST_ADDRESS, LocalDevice.DEFAULT_PORT);
        try {
            dt.setLoopDevice(new LoopDevice(BROADCAST_ADDRESS, LocalDevice.DEFAULT_PORT + 1));
        }
        catch (RuntimeException e) {
            dt.localDevice.terminate();
            throw e;
        }
        try {
            dt.doDiscover();
            dt.printDevices();
        }
        finally {
            dt.localDevice.terminate();
            System.out.println("Cleanup loopDevice");
            dt.getLoopDevice().doTerminate();
        }
    }

    /**
     * @return the loopDevice
     */
    public LoopDevice getLoopDevice() {
        return loopDevice;
    }

    /**
     * @param loopDevice
     *            the loopDevice to set
     */
    public void setLoopDevice(LoopDevice loopDevice) {
        this.loopDevice = loopDevice;
    }
}