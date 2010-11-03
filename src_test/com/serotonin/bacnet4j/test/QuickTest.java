/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.Network;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.base.BACnetUtils;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

/**
 * @author Matthew Lohbihler
 */
public class QuickTest {
    public static void main(String[] args) throws Exception {
        LocalDevice localDevice = new LocalDevice(1234, "192.168.0.255");
        try {
            localDevice.initialize();

            Network network = new Network(2001, "47");
            Address address = new Address(null, BACnetUtils.dottedStringToBytes("207.241.56.207"), 0xBAC0);
            RemoteDevice d = localDevice.findRemoteDevice(address, network, 3052);

            localDevice.getExtendedDeviceInformation(d);

            //localDevice.setMaxReadMultipleReferencesNonsegmented(09);
            long start = System.currentTimeMillis();
            @SuppressWarnings("unchecked")
            SequenceOf<Encodable> list = (SequenceOf<Encodable>) localDevice.sendReadPropertyAllowNull(d,
                    d.getObjectIdentifier(), PropertyIdentifier.objectList);
            System.out.println("Read " + list.getCount() + " values read in " + (System.currentTimeMillis() - start));

            System.out.println(list);
        }
        finally {
            localDevice.terminate();
        }
    }
}
