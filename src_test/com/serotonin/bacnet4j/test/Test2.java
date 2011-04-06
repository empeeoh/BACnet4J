package com.serotonin.bacnet4j.test;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DefaultDeviceEventListener;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;

public class Test2 {
    public static void main(String[] args) throws Exception {
        LocalDevice localDevice = new LocalDevice(1968, "255.255.255.255");
        //        LocalDevice localDevice = new LocalDevice(1968, "93.93.233.191");
        //        localDevice.setPort(47809);

        localDevice.getEventHandler().addListener(new Listener());
        localDevice.initialize();

        try {
            localDevice.sendBroadcast(new WhoIsRequest());
            Thread.sleep(2000);

            // Read
            // ReadPropertyRequest read = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.analogInput, 243),
            // PropertyIdentifier.presentValue);

            //        getObjectList(localDevice, "192.168.0.68", 0xBAC0, 101);
            getObjectList(localDevice, "192.168.0.68", 0xBAC0, 76058, new Network(2001, new byte[] { 0x3a }));

            //getObjectList(localDevice, "206.210.100.135", 0xBAC0, 1011);
            // getObjectList(localDevice, "10.174.1.128", 0xBAC0, 57);
            // getObjectList(localDevice, "10.174.1.132", 0xBAC0, 59);
            // getObjectList(localDevice, "10.174.1.137", 0xBAC0, 63);
        }
        finally {
            localDevice.terminate();
        }
    }

    private static void getObjectList(LocalDevice localDevice, String ip, int port, int deviceId) throws Exception {
        getObjectList(localDevice, ip, port, deviceId, null);
    }

    private static void getObjectList(LocalDevice localDevice, String ip, int port, int deviceId, Network network)
            throws Exception {
        InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName(ip), port);
        ReadPropertyRequest read = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.device, deviceId),
                PropertyIdentifier.objectList);
        ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(addr, network, 1476, Segmentation.segmentedBoth, read);

        System.out.println("IP: " + ip);
        SequenceOf<ObjectIdentifier> oids = (SequenceOf<ObjectIdentifier>) ack.getValue();
        for (ObjectIdentifier oid : oids)
            System.out.println("    " + oid);
    }

    static class Listener extends DefaultDeviceEventListener {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println("IAm received" + d);
        }
    }
}
