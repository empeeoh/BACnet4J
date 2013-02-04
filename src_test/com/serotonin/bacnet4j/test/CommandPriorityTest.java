/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.RemoteObject;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.Null;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

/**
 * @author Matthew Lohbihler
 */
public class CommandPriorityTest {
    private static IpNetwork network;
    private static LocalDevice localDevice;
    private static RemoteDevice remoteDevice;
    private static RemoteObject remoteObject;

    public static void main(String[] args) throws Exception {
        String objectName = "Command Priority Test";
        network = new IpNetwork();
        localDevice = new LocalDevice(1234, new Transport(network));
        localDevice.initialize();

        // Who is
        localDevice.sendBroadcast(network.getBroadcastAddress(2068), null, new WhoIsRequest());
        Thread.sleep(1000);

        // Who has
        localDevice.sendBroadcast(network.getBroadcastAddress(2068), null, new WhoHasRequest(null, new CharacterString(
                objectName)));

        // Wait a bit for responses to come in.
        Thread.sleep(1000);

        // Find the device
        remoteDevice = localDevice.getRemoteDevices().get(0);
        //        localDevice.getExtendedDeviceInformation(remoteDevice);
        //        List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)localDevice.sendReadPropertyAllowNull(
        //                remoteDevice, remoteDevice.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
        //        for ()
        //        remoteDevice.setObject(new RemoteObject())

        // Find the object.
        for (RemoteObject o : remoteDevice.getObjects()) {
            if (o.getObjectName().equals(objectName)) {
                remoteObject = o;
                break;
            }
        }

        getPresentValue();
        getPriorityArray();

        setPresentValue(new Real(16), 16);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Real(15), 15);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Null(), 16);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Real(16), 16);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Real(10), 10);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Null(), 10);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Null(), 15);
        getPresentValue();
        getPriorityArray();

        setPresentValue(new Null(), 16);
        getPresentValue();
        getPriorityArray();

        localDevice.terminate();
    }

    private static void getPresentValue() throws Exception {
        ReadPropertyRequest rpr = new ReadPropertyRequest(remoteObject.getObjectIdentifier(),
                PropertyIdentifier.presentValue);
        ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, rpr);
        System.out.println("Present value: " + ack.getValue());
    }

    private static void getPriorityArray() throws Exception {
        ReadPropertyRequest rpr = new ReadPropertyRequest(remoteObject.getObjectIdentifier(),
                PropertyIdentifier.priorityArray);
        ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, rpr);
        System.out.println("Priority array: " + ack.getValue());
    }

    private static void setPresentValue(Encodable value, int priority) throws Exception {
        WritePropertyRequest wpr = new WritePropertyRequest(remoteObject.getObjectIdentifier(),
                PropertyIdentifier.presentValue, null, value, new UnsignedInteger(priority));
        localDevice.send(remoteDevice, wpr);
    }
}
