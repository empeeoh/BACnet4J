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

import java.io.File;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.obj.FileObject;
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
import com.serotonin.bacnet4j.type.enumerated.FileAccessMethod;
import com.serotonin.bacnet4j.type.enumerated.MessagePriority;
import com.serotonin.bacnet4j.type.enumerated.NotifyType;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class SlaveDeviceTest {
    public static void main(String[] args) throws Exception {
        LocalDevice localDevice = new LocalDevice(1969, new Transport(new IpNetwork("192.168.0.255", 2068)));
        localDevice.getConfiguration().setProperty(PropertyIdentifier.objectName,
                new CharacterString("BACnet4J slave device test"));
        localDevice.getEventHandler().addListener(new Listener());
        // localDevice.getConfiguration().setProperty(PropertyIdentifier.segmentationSupported,
        // Segmentation.noSegmentation);

        // Set up a few objects.
        BACnetObject ai0 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.analogInput));
        ai0.setProperty(PropertyIdentifier.units, EngineeringUnits.centimeters);
        // Set the COV threshold/increment which is the value at which COV notifications will be triggered
        ai0.setProperty(PropertyIdentifier.covIncrement, new Real(0.2f));
        localDevice.addObject(ai0);

        BACnetObject ai1 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.analogInput));
        ai1.setProperty(PropertyIdentifier.units, EngineeringUnits.percentObscurationPerFoot);
        // Set the COV threshold/increment which is the value at which COV notifications will be triggered
        ai1.setProperty(PropertyIdentifier.covIncrement, new Real(1));
        localDevice.addObject(ai1);

        BACnetObject bi0 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.binaryInput));
        localDevice.addObject(bi0);
        bi0.setProperty(PropertyIdentifier.objectName, new CharacterString("Off and on"));
        bi0.setProperty(PropertyIdentifier.inactiveText, new CharacterString("Off"));
        bi0.setProperty(PropertyIdentifier.activeText, new CharacterString("On"));

        BACnetObject bi1 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.binaryInput));
        localDevice.addObject(bi1);
        bi1.setProperty(PropertyIdentifier.objectName, new CharacterString("Good and bad"));
        bi1.setProperty(PropertyIdentifier.inactiveText, new CharacterString("Bad"));
        bi1.setProperty(PropertyIdentifier.activeText, new CharacterString("Good"));

        BACnetObject mso0 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.multiStateOutput));
        mso0.setProperty(PropertyIdentifier.objectName, new CharacterString("Vegetable"));
        mso0.setProperty(PropertyIdentifier.numberOfStates, new UnsignedInteger(4));
        mso0.setProperty(PropertyIdentifier.stateText, 1, new CharacterString("Tomato"));
        mso0.setProperty(PropertyIdentifier.stateText, 2, new CharacterString("Potato"));
        mso0.setProperty(PropertyIdentifier.stateText, 3, new CharacterString("Onion"));
        mso0.setProperty(PropertyIdentifier.stateText, 4, new CharacterString("Broccoli"));
        mso0.setProperty(PropertyIdentifier.presentValue, new UnsignedInteger(1));
        localDevice.addObject(mso0);

        BACnetObject ao0 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.analogOutput));
        ao0.setProperty(PropertyIdentifier.objectName, new CharacterString("Settable analog"));
        localDevice.addObject(ao0);

        BACnetObject av0 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.analogValue));
        av0.setProperty(PropertyIdentifier.objectName, new CharacterString("Command Priority Test"));
        av0.setProperty(PropertyIdentifier.relinquishDefault, new Real(3.1415F));
        localDevice.addObject(av0);

        FileObject file0 = new FileObject(localDevice, localDevice.getNextInstanceObjectIdentifier(ObjectType.file),
                new File("testFile.txt"), FileAccessMethod.streamAccess);
        file0.setProperty(PropertyIdentifier.fileType, new CharacterString("aTestFile"));
        file0.setProperty(PropertyIdentifier.archive, new Boolean(false));
        localDevice.addObject(file0);

        BACnetObject bv1 = new BACnetObject(localDevice,
                localDevice.getNextInstanceObjectIdentifier(ObjectType.binaryValue));
        bv1.setProperty(PropertyIdentifier.objectName, new CharacterString("A binary value"));
        bv1.setProperty(PropertyIdentifier.inactiveText, new CharacterString("Down"));
        bv1.setProperty(PropertyIdentifier.activeText, new CharacterString("Up"));
        localDevice.addObject(bv1);

        // Start the local device.
        localDevice.initialize();

        // Send an iam.
        localDevice.sendGlobalBroadcast(localDevice.getIAm());

        // Let it go...
        float ai0value = 0;
        float ai1value = 0;
        boolean bi0value = false;
        boolean bi1value = false;

        Thread.sleep(10000);

        mso0.setProperty(PropertyIdentifier.presentValue, new UnsignedInteger(2));
        while (true) {
            // Change the values.
            ai0value += 0.1;
            ai1value += 0.7;
            bi0value = !bi0value;
            bi1value = !bi1value;

            // Update the values in the objects.
            ai0.setProperty(PropertyIdentifier.presentValue, new Real(ai0value));
            ai1.setProperty(PropertyIdentifier.presentValue, new Real(ai1value));
            bi0.setProperty(PropertyIdentifier.presentValue, bi0value ? BinaryPV.active : BinaryPV.inactive);
            bi1.setProperty(PropertyIdentifier.presentValue, bi1value ? BinaryPV.active : BinaryPV.inactive);

            Thread.sleep(2500);
        }
    }

    static class Listener implements DeviceEventListener {
        @Override
        public void listenerException(Throwable e) {
            // no op
        }

        @Override
        public void iAmReceived(RemoteDevice d) {
            // no op
        }

        @Override
        public boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv) {
            return true;
        }

        @Override
        public void propertyWritten(BACnetObject obj, PropertyValue pv) {
            System.out.println("Wrote " + pv + " to " + obj);
        }

        @Override
        public void iHaveReceived(RemoteDevice d, RemoteObject o) {
            // no op
        }

        @Override
        public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice,
                ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
                SequenceOf<PropertyValue> listOfValues) {
            // no op
        }

        @Override
        public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice,
                ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass,
                UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType,
                Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues) {
            // no op
        }

        @Override
        public void textMessageReceived(RemoteDevice textMessageSourceDevice, Choice messageClass,
                MessagePriority messagePriority, CharacterString message) {
            // no op
        }

        @Override
        public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber,
                Encodable serviceParameters) {
            // no op
        }

        @Override
        public void reinitializeDevice(ReinitializedStateOfDevice reinitializedStateOfDevice) {
            // no op
        }

        @Override
        public void synchronizeTime(DateTime dateTime, boolean utc) {
            // no op
        }
    }
}
