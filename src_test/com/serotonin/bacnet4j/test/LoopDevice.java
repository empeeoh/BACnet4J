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

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.BinaryPV;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.EventState;
import com.serotonin.bacnet4j.type.enumerated.EventType;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Reliability;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * 
 * software only device default local loop ;-)
 * 
 * @author mlohbihler
 * @author aploese
 */
public class LoopDevice implements Runnable {
    public static void main(String[] args) throws Exception {
        LoopDevice ld = new LoopDevice("127.0.0.255", IpNetwork.DEFAULT_PORT + 1);
        Thread.sleep(12000); // wait 2 min
        ld.doTerminate();
    }

    private boolean terminate;
    private IpNetwork network;
    private LocalDevice localDevice;
    private BACnetObject ai0;
    private BACnetObject ai1;
    private BACnetObject bi0;
    private BACnetObject bi1;
    private BACnetObject mso0;
    private BACnetObject ao0;

    public LoopDevice(String broadcastAddress, int port) throws BACnetServiceException, Exception {
        network = new IpNetwork(broadcastAddress, port);
        localDevice = new LocalDevice(1968, new Transport(network));
        try {
            localDevice.getEventHandler().addListener(new DeviceEventListener() {

                @Override
                public void listenerException(Throwable e) {
                    System.out.println("loopDevice listenerException");
                }

                @Override
                public void iAmReceived(RemoteDevice d) {
                    System.out.println("loopDevice iAmReceived");
                }

                @Override
                public boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv) {
                    System.out.println("loopDevice allowPropertyWrite");
                    return true;
                }

                @Override
                public void propertyWritten(BACnetObject obj, PropertyValue pv) {
                    System.out.println("loopDevice propertyWritten");
                }

                @Override
                public void iHaveReceived(RemoteDevice d, RemoteObject o) {
                    System.out.println("loopDevice iHaveReceived");
                }

                @Override
                public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier,
                        RemoteDevice initiatingDevice, ObjectIdentifier monitoredObjectIdentifier,
                        UnsignedInteger timeRemaining, SequenceOf<PropertyValue> listOfValues) {
                    System.out.println("loopDevice covNotificationReceived");
                }

                @Override
                public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice,
                        ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass,
                        UnsignedInteger priority, EventType eventType, CharacterString messageText,
                        NotifyType notifyType, Boolean ackRequired, EventState fromState, EventState toState,
                        NotificationParameters eventValues) {
                    System.out.println("loopDevice eventNotificationReceived");
                }

                @Override
                public void textMessageReceived(RemoteDevice textMessageSourceDevice, Choice messageClass,
                        MessagePriority messagePriority, CharacterString message) {
                    System.out.println("loopDevice textMessageReceived");
                }

                @Override
                public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber,
                        Encodable serviceParameters) {
                    System.out.println("loopDevice privateTransferReceived");
                }

                @Override
                public void reinitializeDevice(ReinitializedStateOfDevice reinitializedStateOfDevice) {
                    System.out.println("loopDevice reinitializeDevice");
                }

                @Override
                public void synchronizeTime(DateTime dateTime, boolean utc) {
                    System.out.println("loopDevice synchronizeTime");
                }
            });

            // for valid property values with valid datatypes see com.serotonin.bacnet4j.obj.ObjectProperties and ther
            // look for the big static block at the end;
            // properties of device object
            localDevice.getConfiguration().setProperty(PropertyIdentifier.modelName,
                    new CharacterString("BACnet4J LoopDevice"));

            // Set up a few objects.
            ai0 = new BACnetObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.analogInput));

            // mandatory properties
            ai0.setProperty(PropertyIdentifier.objectName, new CharacterString("G1-RLT03-TM-01")); // this is a cryptic
            // encoded name of a
            // temp sensor from a
            // drawing... (ahm.
            // actually taken
            // from a book ;-))
            ai0.setProperty(PropertyIdentifier.presentValue, new Real(11));
            ai0.setProperty(PropertyIdentifier.outOfService, new Boolean(false));
            ai0.setProperty(PropertyIdentifier.units, EngineeringUnits.degreesCelsius);

            // some optional properties
            ai0.setProperty(PropertyIdentifier.description, new CharacterString("temperature"));
            ai0.setProperty(PropertyIdentifier.deviceType, new CharacterString("random values"));
            ai0.setProperty(PropertyIdentifier.reliability, Reliability.noFaultDetected);
            ai0.setProperty(PropertyIdentifier.updateInterval, new UnsignedInteger(10));

            ai0.setProperty(PropertyIdentifier.minPresValue, new Real(-70));
            ai0.setProperty(PropertyIdentifier.maxPresValue, new Real(120));
            ai0.setProperty(PropertyIdentifier.resolution, new Real((float) 0.1));
            ai0.setProperty(PropertyIdentifier.profileName, new CharacterString("funny reader"));

            localDevice.addObject(ai0);

            ai1 = new BACnetObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.analogInput));
            ai1.setProperty(PropertyIdentifier.units, EngineeringUnits.percentObscurationPerFoot);
            localDevice.addObject(ai1);

            bi0 = new BACnetObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.binaryInput));
            localDevice.addObject(bi0);
            bi0.setProperty(PropertyIdentifier.objectName, new CharacterString("Off and on"));
            bi0.setProperty(PropertyIdentifier.inactiveText, new CharacterString("Off"));
            bi0.setProperty(PropertyIdentifier.activeText, new CharacterString("On"));

            bi1 = new BACnetObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.binaryInput));
            localDevice.addObject(bi1);
            bi1.setProperty(PropertyIdentifier.objectName, new CharacterString("Good and bad"));
            bi1.setProperty(PropertyIdentifier.inactiveText, new CharacterString("Bad"));
            bi1.setProperty(PropertyIdentifier.activeText, new CharacterString("Good"));

            mso0 = new BACnetObject(localDevice,
                    localDevice.getNextInstanceObjectIdentifier(ObjectType.multiStateOutput));
            mso0.setProperty(PropertyIdentifier.objectName, new CharacterString("Vegetable"));
            mso0.setProperty(PropertyIdentifier.numberOfStates, new UnsignedInteger(4));
            mso0.setProperty(PropertyIdentifier.stateText, 1, new CharacterString("Tomato"));
            mso0.setProperty(PropertyIdentifier.stateText, 2, new CharacterString("Potato"));
            mso0.setProperty(PropertyIdentifier.stateText, 3, new CharacterString("Onion"));
            mso0.setProperty(PropertyIdentifier.stateText, 4, new CharacterString("Broccoli"));
            mso0.setProperty(PropertyIdentifier.presentValue, new UnsignedInteger(1));
            localDevice.addObject(mso0);

            ao0 = new BACnetObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.analogOutput));
            ao0.setProperty(PropertyIdentifier.objectName, new CharacterString("Settable analog"));
            localDevice.addObject(ao0);

            // Start the local device.
            localDevice.initialize();
            new Thread(this).start();
        }
        catch (RuntimeException e) {
            System.out.println("Ex in LoopDevice() ");
            e.printStackTrace();
            localDevice.terminate();
            localDevice = null;
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("LoopDevice start changing values" + this);

            // Let it go...
            float ai0value = 0;
            float ai1value = 0;
            boolean bi0value = false;
            boolean bi1value = false;

            getMso0().setProperty(PropertyIdentifier.presentValue, new UnsignedInteger(2));
            while (!isTerminate()) {
                System.out.print("Change values of LoopDevice " + this);

                // Update the values in the objects.
                ai0.setProperty(PropertyIdentifier.presentValue, new Real(ai0value));
                ai1.setProperty(PropertyIdentifier.presentValue, new Real(ai1value));
                bi0.setProperty(PropertyIdentifier.presentValue, bi0value ? BinaryPV.active : BinaryPV.inactive);
                bi1.setProperty(PropertyIdentifier.presentValue, bi1value ? BinaryPV.active : BinaryPV.inactive);

                synchronized (this) {
                    wait(1000); // 1 second or notified (faster exit then stupid wait for 1 second)
                }
            }
            System.out.println("Close LoopDevive " + this);
        }
        catch (Exception ex) {
            // no op
        }
        localDevice.terminate();
        localDevice = null;
    }

    @Override
    protected void finalize() {
        if (localDevice != null) {
            localDevice.terminate();
            localDevice = null;
        }
    }

    /**
     * @return the terminate
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * @param terminate
     *            the terminate to set
     */
    public void doTerminate() {
        terminate = true;
        synchronized (this) {
            notifyAll(); // we may wait for this in run() ...
        }
    }

    /**
     * @return the broadcastAddress
     */
    public String getBroadcastAddress() {
        return network.getBroadcastIp();
    }

    /**
     * @return the port
     */
    public int getPort() {
        return network.getPort();
    }

    /**
     * @return the localDevice
     */
    public LocalDevice getLocalDevice() {
        return localDevice;
    }

    /**
     * @return the ai0
     */
    public BACnetObject getAi0() {
        return ai0;
    }

    /**
     * @return the ai1
     */
    public BACnetObject getAi1() {
        return ai1;
    }

    /**
     * @return the bi0
     */
    public BACnetObject getBi0() {
        return bi0;
    }

    /**
     * @return the bi1
     */
    public BACnetObject getBi1() {
        return bi1;
    }

    /**
     * @return the mso0
     */
    public BACnetObject getMso0() {
        return mso0;
    }

    /**
     * @return the ao0
     */
    public BACnetObject getAo0() {
        return ao0;
    }
}