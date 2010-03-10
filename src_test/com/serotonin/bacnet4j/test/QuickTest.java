/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.constructed.ObjectPropertyReference;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;

/**
 * @author Matthew Lohbihler
 */
public class QuickTest {
    public static void main(String[] args) throws Exception {
        LocalDevice localDevice = new LocalDevice(1234, "192.168.0.255");
        try {
            localDevice.initialize();
            localDevice.sendBroadcast(2068, new WhoIsRequest(null, null));
            
            Thread.sleep(1000);
            
            RemoteDevice d = localDevice.getRemoteDevices().get(0);
            localDevice.getExtendedDeviceInformation(d);
            
            ObjectIdentifier oid = new ObjectIdentifier(ObjectType.binaryValue, 0);
            PropertyReferences refs = new PropertyReferences();
            refs.add(oid, PropertyIdentifier.all);
            
            PropertyValues pvs = localDevice.readProperties(d, refs);
            for (ObjectPropertyReference opr : pvs)
                System.out.println(pvs.getNoErrorCheck(opr));
            
//            Encodable value = new com.serotonin.bacnet4j.type.primitive.Boolean(true);
//            //Encodable value = BinaryPV.active;
//            WritePropertyRequest wpr = new WritePropertyRequest(oid, PropertyIdentifier.presentValue, null, value, null);
//            System.out.println(localDevice.send(d, wpr));
//            Thread.sleep(2000);
        }
        finally {
            localDevice.terminate();
        }
    }
}
