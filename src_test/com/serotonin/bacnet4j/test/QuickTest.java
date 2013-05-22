/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.event.DeviceEventListener;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.obj.BACnetObject;
import com.serotonin.bacnet4j.service.confirmed.ReinitializeDeviceRequest.ReinitializedStateOfDevice;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Choice;
import com.serotonin.bacnet4j.type.constructed.DateTime;
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
import com.serotonin.bacnet4j.util.RequestUtils;

/**
 * @author Matthew Lohbihler
 */
public class QuickTest {
    static IpNetwork network;
    static Transport transport;
    static LocalDevice localDevice;

    public static void main(String[] args) throws Exception {
        network = new IpNetwork("192.168.0.255");
        transport = new Transport(network);
        localDevice = new LocalDevice(1234, transport);

        try {
            localDevice.initialize();
            localDevice.getEventHandler().addListener(new Listener());

            sendWhoIs();

            while (true) {
                Thread.sleep(1000);
            }

            //            Network network = new Network(2001, "47");
            //            Address address = new Address(null, BACnetUtils.dottedStringToBytes("207.241.56.207"), 0xBAC0);
            //            RemoteDevice d = localDevice.findRemoteDevice(address, network, 3052);
            //
            //            localDevice.getExtendedDeviceInformation(d);
            //
            //            //localDevice.setMaxReadMultipleReferencesNonsegmented(09);
            //            long start = System.currentTimeMillis();
            //            @SuppressWarnings("unchecked")
            //            SequenceOf<Encodable> list = (SequenceOf<Encodable>) localDevice.sendReadPropertyAllowNull(d,
            //                    d.getObjectIdentifier(), PropertyIdentifier.objectList);
            //            System.out.println("Read " + list.getCount() + " values read in " + (System.currentTimeMillis() - start));
            //
            //            System.out.println(list);
        }
        finally {
            localDevice.terminate();
        }
    }

    static void sendWhoIs() throws BACnetException {
        Address bcastAddr = new Address(InetAddrCache.get(network.getBroadcastIp(), 0xBAC1));
        localDevice.sendBroadcast(bcastAddr, null, new WhoIsRequest());
    }

    static class Listener implements DeviceEventListener {
        @Override
        public void listenerException(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("iAmReceived: d=" + d);

            try {
                Encodable e = RequestUtils.sendReadPropertyAllowNull(localDevice, d, d.getObjectIdentifier(),
                        PropertyIdentifier.objectList);
                System.out.println(e);
            }
            catch (BACnetException e) {
                e.printStackTrace();
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#allowPropertyWrite(com.serotonin.bacnet4j.obj.BACnetObject,
         * com.serotonin.bacnet4j.type.constructed.PropertyValue)
         */
        @Override
        public boolean allowPropertyWrite(BACnetObject obj, PropertyValue pv) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#propertyWritten(com.serotonin.bacnet4j.obj.BACnetObject,
         * com.serotonin.bacnet4j.type.constructed.PropertyValue)
         */
        @Override
        public void propertyWritten(BACnetObject obj, PropertyValue pv) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see com.serotonin.bacnet4j.event.DeviceEventListener#iHaveReceived(com.serotonin.bacnet4j.RemoteDevice,
         * com.serotonin.bacnet4j.RemoteObject)
         */
        @Override
        public void iHaveReceived(RemoteDevice d, RemoteObject o) {
            System.out.println("iHaveReceived: d=" + d + ", o=" + o);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#covNotificationReceived(com.serotonin.bacnet4j.type.primitive
         * .UnsignedInteger, com.serotonin.bacnet4j.RemoteDevice,
         * com.serotonin.bacnet4j.type.primitive.ObjectIdentifier,
         * com.serotonin.bacnet4j.type.primitive.UnsignedInteger, com.serotonin.bacnet4j.type.constructed.SequenceOf)
         */
        @Override
        public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice,
                ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
                SequenceOf<PropertyValue> listOfValues) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#eventNotificationReceived(com.serotonin.bacnet4j.type.primitive
         * .UnsignedInteger, com.serotonin.bacnet4j.RemoteDevice,
         * com.serotonin.bacnet4j.type.primitive.ObjectIdentifier, com.serotonin.bacnet4j.type.constructed.TimeStamp,
         * com.serotonin.bacnet4j.type.primitive.UnsignedInteger, com.serotonin.bacnet4j.type.primitive.UnsignedInteger,
         * com.serotonin.bacnet4j.type.enumerated.EventType, com.serotonin.bacnet4j.type.primitive.CharacterString,
         * com.serotonin.bacnet4j.type.enumerated.NotifyType, com.serotonin.bacnet4j.type.primitive.Boolean,
         * com.serotonin.bacnet4j.type.enumerated.EventState, com.serotonin.bacnet4j.type.enumerated.EventState,
         * com.serotonin.bacnet4j.type.notificationParameters.NotificationParameters)
         */
        @Override
        public void eventNotificationReceived(UnsignedInteger processIdentifier, RemoteDevice initiatingDevice,
                ObjectIdentifier eventObjectIdentifier, TimeStamp timeStamp, UnsignedInteger notificationClass,
                UnsignedInteger priority, EventType eventType, CharacterString messageText, NotifyType notifyType,
                Boolean ackRequired, EventState fromState, EventState toState, NotificationParameters eventValues) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#textMessageReceived(com.serotonin.bacnet4j.RemoteDevice,
         * com.serotonin.bacnet4j.type.constructed.Choice, com.serotonin.bacnet4j.type.enumerated.MessagePriority,
         * com.serotonin.bacnet4j.type.primitive.CharacterString)
         */
        @Override
        public void textMessageReceived(RemoteDevice textMessageSourceDevice, Choice messageClass,
                MessagePriority messagePriority, CharacterString message) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#privateTransferReceived(com.serotonin.bacnet4j.type.primitive
         * .UnsignedInteger, com.serotonin.bacnet4j.type.primitive.UnsignedInteger,
         * com.serotonin.bacnet4j.type.Encodable)
         */
        @Override
        public void privateTransferReceived(UnsignedInteger vendorId, UnsignedInteger serviceNumber,
                Encodable serviceParameters) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#reinitializeDevice(com.serotonin.bacnet4j.service.confirmed
         * .ReinitializeDeviceRequest.ReinitializedStateOfDevice)
         */
        @Override
        public void reinitializeDevice(ReinitializedStateOfDevice reinitializedStateOfDevice) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.serotonin.bacnet4j.event.DeviceEventListener#synchronizeTime(com.serotonin.bacnet4j.type.constructed.
         * DateTime, boolean)
         */
        @Override
        public void synchronizeTime(DateTime dateTime, boolean utc) {
            // TODO Auto-generated method stub

        }
    }
}
