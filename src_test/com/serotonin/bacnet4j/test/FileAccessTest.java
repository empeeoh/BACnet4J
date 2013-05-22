/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.service.acknowledgement.AtomicReadFileAck;
import com.serotonin.bacnet4j.service.confirmed.AtomicReadFileRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.util.RequestUtils;

/**
 * Use with SlaveDeviceTest
 * 
 * @author Matthew Lohbihler
 */
public class FileAccessTest {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        IpNetwork network = new IpNetwork("192.168.0.255");
        LocalDevice localDevice = new LocalDevice(1234, new Transport(network));
        try {
            localDevice.initialize();
            localDevice.sendBroadcast(network.getBroadcastAddress(2068), null, new WhoIsRequest(null, null));

            Thread.sleep(1000);

            RemoteDevice fileDev = null;
            ObjectIdentifier file = null;
            for (RemoteDevice d : localDevice.getRemoteDevices()) {
                RequestUtils.getExtendedDeviceInformation(localDevice, d);
                List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                        localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();

                for (ObjectIdentifier oid : oids) {
                    if (oid.getObjectType().equals(ObjectType.file)) {
                        fileDev = d;
                        file = oid;
                        break;
                    }
                }
            }

            AtomicReadFileRequest request = new AtomicReadFileRequest(file, false, 0, 716);
            AtomicReadFileAck response = (AtomicReadFileAck) localDevice.send(fileDev, request);

            System.out.println("eof: " + response.getEndOfFile());
            System.out.println("start: " + response.getFileStartPosition());
            System.out.println("data: " + new String(response.getFileData().getBytes()));
            System.out.println("length: " + response.getFileData().getBytes().length);
        }
        finally {
            localDevice.terminate();
        }
    }
}
