package com.serotonin.bacnet4j.discovery;

import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DeviceEventAdapter;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.mstp.MasterNode;
import com.serotonin.bacnet4j.npdu.mstp.MstpNetwork;
import com.serotonin.bacnet4j.npdu.mstp.MstpNode;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.transport.Transport;
import com.serotonin.bacnet4j.type.Encodable;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.RequestListener;
import com.serotonin.bacnet4j.util.RequestUtils;
import com.serotonin.io.serial.SerialParameters;
import com.serotonin.log.SimpleLog;

public class MstpObjectList {
    static final SimpleLog LOG = new SimpleLog();
    static LocalDevice localDevice;
    static boolean whoIsReceived;

    public static void main(String[] args) throws Exception {
        MstpNode.DEBUG = true;
        SerialParameters serialParams = new SerialParameters();
        serialParams.setCommPortId("COM4");
        serialParams.setBaudRate(38400);
        MasterNode node = new MasterNode(serialParams, (byte) 0, 2);
        MstpNetwork network = new MstpNetwork(node);
        Transport transport = new Transport(network);
        localDevice = new LocalDevice(1234, transport);
        localDevice.getEventHandler().addListener(new Listener());

        try {
            localDevice.initialize();
            LOG.out("Local device initialized");

            while (true) {
                if (!whoIsReceived) {
                    localDevice.sendGlobalBroadcast(new WhoIsRequest());
                    LOG.out("WhoIs sent");
                }
                Thread.sleep(3000);
            }
        }
        finally {
            localDevice.terminate();
        }
    }

    static class Listener extends DeviceEventAdapter {
        @Override
        public void iAmReceived(final RemoteDevice d) {
            if (!whoIsReceived) {
                whoIsReceived = true;
                LOG.out("IAm received from " + d);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getObjectList(d);
                        }
                        catch (BACnetException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    @SuppressWarnings("unchecked")
    static void getObjectList(RemoteDevice d) throws BACnetException {
        LOG.out("Getting extended information");
        RequestUtils.getExtendedDeviceInformation(localDevice, d);
        LOG.out("Got extended information");

        // Get the device's object list.
        LOG.out("Getting object list");
        List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(
                localDevice, d, d.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
        LOG.out("Got object list: " + oids.size());

        PropertyReferences refs = new PropertyReferences();
        for (ObjectIdentifier oid : oids)
            addPropertyReferences(refs, oid);

        LOG.out("Getting properties: " + refs.size());
        RequestUtils.readProperties(localDevice, d, refs, new RequestListener() {
            @Override
            public boolean requestProgress(double d, ObjectIdentifier oid, PropertyIdentifier pid,
                    UnsignedInteger unsignedinteger, Encodable encodable) {
                return false;
            }
        });
        LOG.out("Got properties");
    }

    static void addPropertyReferences(PropertyReferences refs, ObjectIdentifier oid) {
        refs.add(oid, PropertyIdentifier.objectName);

        ObjectType type = oid.getObjectType();
        if (ObjectType.accumulator.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
        else if (ObjectType.analogInput.equals(type) || ObjectType.analogOutput.equals(type)
                || ObjectType.analogValue.equals(type) || ObjectType.pulseConverter.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
        else if (ObjectType.binaryInput.equals(type) || ObjectType.binaryOutput.equals(type)
                || ObjectType.binaryValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.inactiveText);
            refs.add(oid, PropertyIdentifier.activeText);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
        else if (ObjectType.device.equals(type)) {
            refs.add(oid, PropertyIdentifier.modelName);
        }
        else if (ObjectType.lifeSafetyPoint.equals(type)) {
            refs.add(oid, PropertyIdentifier.units);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
        else if (ObjectType.loop.equals(type)) {
            refs.add(oid, PropertyIdentifier.outputUnits);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
        else if (ObjectType.multiStateInput.equals(type) || ObjectType.multiStateOutput.equals(type)
                || ObjectType.multiStateValue.equals(type)) {
            refs.add(oid, PropertyIdentifier.stateText);
            refs.add(oid, PropertyIdentifier.presentValue);
        }
    }
}
