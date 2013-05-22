package com.serotonin.bacnet4j.discovery;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.primitive.OctetString;

public class WhoIs {
    public static void main(String[] args) throws Exception {
        IpNetwork network = new IpNetwork();
        Transport transport = new Transport(network);
        LocalDevice localDevice = new LocalDevice(45677, transport);

        try {
            localDevice.initialize();

            // CBM
            //            RemoteDevice r = localDevice.findRemoteDevice(new Address(36, (byte) 1), new OctetString("89.101.141.54"),
            //                    121);

            // CBT
            RemoteDevice r = localDevice.findRemoteDevice(new Address(36, (byte) 2), new OctetString("89.101.141.54"),
                    122);

            // CBR
            //            RemoteDevice r = localDevice.findRemoteDevice(new Address("89.101.141.54", IpNetwork.DEFAULT_PORT), null,
            //                    123);

            System.out.println(r);
        }
        finally {
            localDevice.terminate();
        }
    }
}
