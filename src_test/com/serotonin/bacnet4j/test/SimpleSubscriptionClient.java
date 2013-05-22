package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.confirmed.SubscribeCOVRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;

public class SimpleSubscriptionClient {
    public static void main(String[] args) throws Exception {
        IpNetwork network = new IpNetwork();
        LocalDevice localDevice = new LocalDevice(1234, new Transport(network));
        RemoteDevice d = null;
        ObjectIdentifier oid = new ObjectIdentifier(ObjectType.binaryInput, 0);
        ObjectIdentifier aoid = new ObjectIdentifier(ObjectType.analogInput, 0);
        try {
            localDevice.initialize();
            localDevice.getEventHandler().addListener(new Listener());
            localDevice.sendBroadcast(new Address(InetAddrCache.get("255.255.255.255", 2068)), null,
                    localDevice.getIAm());
            d = localDevice
                    .findRemoteDevice(new Address(new byte[] { (byte) 192, (byte) 168, 0, 2 }, 2068), null, 1968);

            // Subscribe to binary 0
            SubscribeCOVRequest req = new SubscribeCOVRequest(new UnsignedInteger(0), oid, new Boolean(true),
                    new UnsignedInteger(0));
            localDevice.send(d, req);

            // Also subscribe to analog 0
            req = new SubscribeCOVRequest(new UnsignedInteger(1), aoid, new Boolean(true), new UnsignedInteger(0));
            localDevice.send(d, req);

            Thread.sleep(22000);
        }
        finally {
            if (d != null)
                // Unsubscribe
                localDevice.send(d, new SubscribeCOVRequest(new UnsignedInteger(0), oid, null, null));
            localDevice.send(d, new SubscribeCOVRequest(new UnsignedInteger(1), aoid, null, null));
            localDevice.terminate();
        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void covNotificationReceived(UnsignedInteger subscriberProcessIdentifier, RemoteDevice initiatingDevice,
                ObjectIdentifier monitoredObjectIdentifier, UnsignedInteger timeRemaining,
                SequenceOf<PropertyValue> listOfValues) {
            System.out.println("Received COV notification: " + listOfValues);
        }
    }
}
