/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.RequestUtils;

/**
 * Tests what happens when a request for an object list causes an overflow abort.
 * 
 * @author Matthew Lohbihler
 */
public class OverflowTest {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        LocalDevice localDevice = new LocalDevice(1234, new Transport(new IpNetwork()));
        localDevice.initialize();

        // Who is
        localDevice.sendGlobalBroadcast(new WhoIsRequest());

        // Wait a bit for responses to come in.
        Thread.sleep(1000);

        // Get extended information for all remote devices.
        for (RemoteDevice d : localDevice.getRemoteDevices()) {
            RequestUtils.getExtendedDeviceInformation(localDevice, d);

            List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                    localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
            System.out.println("Object count: " + oids.size());

            PropertyReferences refs = new PropertyReferences();
            for (ObjectIdentifier oid : oids)
                addPropertyReferences(refs, oid);

            RequestUtils.readProperties(localDevice, d, refs, null);
            System.out.println(d);
        }

        // Wait a bit for responses to come in.
        Thread.sleep(2000);

        localDevice.terminate();
    }

    private static void addPropertyReferences(PropertyReferences refs, ObjectIdentifier oid) {
        refs.add(oid, PropertyIdentifier.objectName);

        ObjectType type = oid.getObjectType();
        if (ObjectType.accumulator.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.analogInput.equals(type) || ObjectType.analogOutput.equals(type)
                || ObjectType.analogValue.equals(type) || ObjectType.pulseConverter.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.binaryInput.equals(type) || ObjectType.binaryOutput.equals(type)
                || ObjectType.binaryValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.inactiveText);
            refs.add(oid, PropertyIdentifier.activeText);
        }
        else if (ObjectType.lifeSafetyPoint.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
        }
        else if (ObjectType.loop.equals(type)) {
            refs.add(oid, PropertyIdentifier.outputUnits);
        }
        else if (ObjectType.multiStateInput.equals(type) || ObjectType.multiStateOutput.equals(type)
                || ObjectType.multiStateValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.stateText);
        }
        else
            return;

        refs.add(oid, PropertyIdentifier.presentValue);
    }
}
