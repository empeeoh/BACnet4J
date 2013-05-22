package com.serotonin.bacnet4j.test;

import java.util.Map;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;

public class Test3 {
    public static void main(String[] args) throws Exception {
        IpNetwork network = new IpNetwork();
        Transport transport = new Transport(network);
        LocalDevice ld = new LocalDevice(1, transport);
        ld.initialize();

        RemoteDevice rd = ld.findRemoteDevice(new Address("192.168.0.68", 0xbac0), null, 101);

        Map<PropertyIdentifier, Encodable> values = RequestUtils.getProperties(ld, rd, null,
                PropertyIdentifier.objectName, PropertyIdentifier.vendorName, PropertyIdentifier.modelName,
                PropertyIdentifier.description, PropertyIdentifier.location, PropertyIdentifier.objectList);

        System.out.println(values);

        ld.terminate();
    }
}
