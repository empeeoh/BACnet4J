/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2009 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 */
package com.serotonin.bacnet4j.test;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.npdu.ip.InetAddrCache;
import com.serotonin.bacnet4j.service.acknowledgement.AcknowledgementService;
import com.serotonin.bacnet4j.service.acknowledgement.CreateObjectAck;
import com.serotonin.bacnet4j.service.confirmed.ConfirmedRequestService;
import com.serotonin.bacnet4j.service.confirmed.CreateObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.DeleteObjectRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionCriteria;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionCriteria.RelationSpecifier;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyConditionalRequest.ObjectSelectionCriteria.SelectionLogic;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyMultipleRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.bacnet4j.type.constructed.Address;
import com.serotonin.bacnet4j.type.constructed.Destination;
import com.serotonin.bacnet4j.type.constructed.EventTransitionBits;
import com.serotonin.bacnet4j.type.constructed.PropertyReference;
import com.serotonin.bacnet4j.type.constructed.PropertyValue;
import com.serotonin.bacnet4j.type.constructed.ReadAccessSpecification;
import com.serotonin.bacnet4j.type.constructed.Recipient;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.constructed.WriteAccessSpecification;
import com.serotonin.bacnet4j.type.enumerated.EngineeringUnits;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.Boolean;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.OctetString;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.bacnet4j.util.RequestUtils;

public class Test {
    public static void main(String[] args) {
        //        new Address(0, "192.168.0.70:47808");
        new OctetString("192.168.2.3:47808", 47808);
    }

    //    public static void main(String[] args) throws Exception {
    //        LocalDevice localDevice = null;
    //        int instanceNumber = 1;
    //        BACnetObject notif = new BACnetObject(localDevice, new ObjectIdentifier(ObjectType.notificationClass,
    //                instanceNumber));
    //
    //        RemoteDevice remoteDevice = null;
    //
    //        ReadPropertyRequest req = new ReadPropertyRequest(new ObjectIdentifier(ObjectType.analogOutput, 1234),
    //                PropertyIdentifier.priorityArray);
    //        ReadPropertyAck ack = (ReadPropertyAck) localDevice.send(remoteDevice, req);
    //        PriorityArray priorityArray = (PriorityArray) ack.getValue();
    //        int priority = priorityArray.get(16).getIntegerValue().intValue();
    //    }

    // public static void main(String[] args) throws Exception {
    // Enumeration<NetworkInterface> netInter = NetworkInterface.getNetworkInterfaces();
    // int n = 0;
    //
    // while (netInter.hasMoreElements()) {
    // NetworkInterface ni = netInter.nextElement();
    //
    // System.out.println("NetworkInterface " + n++ + ": " + ni.getDisplayName());
    //
    // for (InetAddress iaddress : Collections.list(ni.getInetAddresses())) {
    // System.out.println("CanonicalHostName: " + iaddress.getCanonicalHostName());
    //
    // System.out.println("IP: " + iaddress.getHostAddress());
    //
    // System.out.println("Loopback? " + iaddress.isLoopbackAddress());
    // System.out.println("SiteLocal? " + iaddress.isSiteLocalAddress());
    // System.out.println();
    //
    // // if (!iaddress.isLoopbackAddress() && iaddress.isSiteLocalAddress()) {
    // // System.out.println(iaddress.getAddress());
    // // return;
    // // // return iaddress.getAddress();
    // // }
    // }
    // }
    //
    // System.out.println(InetAddress.getLocalHost().getAddress());
    // }

    // public static void main(String[] args) throws Exception {
    // LocalDevice localDevice = new LocalDevice(1, "255.255.255.255", "localhost");
    // try {
    // localDevice.initialize();
    // localDevice.sendBroadcast(47808, new WhoIsRequest(null, null));
    //
    // Thread.sleep(5000);
    //
    // RemoteDevice d = localDevice.getRemoteDevices().get(0);
    //
    // ObjectIdentifier oid = new ObjectIdentifier(ObjectType.multiStateOutput, 0);
    // PropertyReferences refs = new PropertyReferences();
    // refs.add(oid, PropertyIdentifier.all);
    //
    // PropertyValues pvs = localDevice.readProperties(d, refs);
    // for (ObjectPropertyReference opr : pvs)
    // System.out.println("" + opr.getPropertyIdentifier() + ": " + pvs.getNoErrorCheck(opr));
    // Thread.sleep(2000);
    // }
    // finally {
    // localDevice.terminate();
    // }
    // }

    // public static void main(String[] args) throws Exception {
    // LocalDevice d = new LocalDevice(1234, "192.168.0.255");
    // //d.setPort(0xbac0);
    // d.initialize();
    // long start = System.currentTimeMillis();
    // try {
    // test3(d);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // System.out.println("Total test time: "+ (System.currentTimeMillis() - start) +"ms");
    // Thread.sleep(2000);
    // d.terminate();
    // }

    public static void test3(LocalDevice d) throws Exception {
        RemoteDevice rd = new RemoteDevice(105, new Address(new byte[] { (byte) 206, (byte) 210, 100, (byte) 134 },
                47808), null);
        rd.setSegmentationSupported(Segmentation.noSegmentation);
        rd.setMaxAPDULengthAccepted(1476);

        d.addRemoteDevice(rd);
        RequestUtils.getExtendedDeviceInformation(d, rd);

        // List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>)d.sendReadPropertyAllowNull(
        // rd, rd.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();
        //        
        // PropertyReferences refs = new PropertyReferences();
        // for (ObjectIdentifier oid : oids)
        // addPropertyReferences(refs, oid);
        //      
        // d.readProperties(rd, refs);
    }

    public static void test(LocalDevice d) throws Exception {
        // Who is
        d.sendGlobalBroadcast(new WhoIsRequest(null, null));

        AcknowledgementService result;

        // Create object
        List<PropertyValue> values = new ArrayList<PropertyValue>();
        values.add(new PropertyValue(PropertyIdentifier.objectName, new CharacterString("A cool analog input")));
        CharacterString cs = new CharacterString(description);
        values.add(new PropertyValue(PropertyIdentifier.description, cs));
        values.add(new PropertyValue(PropertyIdentifier.presentValue, new Real(155.2f)));
        values.add(new PropertyValue(PropertyIdentifier.units, EngineeringUnits.centimetersOfMercury));
        result = send(d, new CreateObjectRequest(ObjectType.analogInput, new SequenceOf<PropertyValue>(values)));
        System.out.println(result);
        result = send(d, new CreateObjectRequest(ObjectType.analogInput, new SequenceOf<PropertyValue>(values)));
        System.out.println(result);
        ObjectIdentifier created = ((CreateObjectAck) result).getObjectIdentifier();
        ObjectIdentifier device = new ObjectIdentifier(ObjectType.device, 1234);
        ObjectIdentifier deviceIndirect = new ObjectIdentifier(ObjectType.device, 0x3FFFFF);

        System.out.println(send(d, new ReadPropertyRequest(device, PropertyIdentifier.objectName, null)));
        System.out.println(send(d, new ReadPropertyRequest(created, PropertyIdentifier.presentValue, null)));
        System.out.println(send(d, new ReadPropertyRequest(device, PropertyIdentifier.objectList,
                new UnsignedInteger(0))));
        System.out.println(send(d, new ReadPropertyRequest(device, PropertyIdentifier.objectList,
                new UnsignedInteger(1))));
        System.out.println(send(d, new ReadPropertyRequest(device, PropertyIdentifier.objectList,
                new UnsignedInteger(2))));
        System.out.println(send(d, new ReadPropertyRequest(created, PropertyIdentifier.description, null)));

        // Write a value
        System.out.println(send(d, new WritePropertyRequest(created, PropertyIdentifier.presentValue, null,
                new UnsignedInteger(5), null)));
        System.out.println(send(d, new WritePropertyRequest(created, PropertyIdentifier.presentValue, null, new Real(
                5.5f), new UnsignedInteger(5))));
        System.out.println(send(d, new WritePropertyRequest(created, PropertyIdentifier.presentValue,
                new UnsignedInteger(10), new Real(5.5f), null)));
        System.out.println(send(d, new WritePropertyRequest(created, PropertyIdentifier.presentValue, null, new Real(
                5.5f), null)));

        List<WriteAccessSpecification> writeSpecs = new ArrayList<WriteAccessSpecification>();
        List<PropertyValue> pvs = new ArrayList<PropertyValue>();
        pvs.add(new PropertyValue(PropertyIdentifier.presentValue, new Real(6.7f)));
        pvs.add(new PropertyValue(PropertyIdentifier.highLimit, new Real(10f)));
        pvs.add(new PropertyValue(PropertyIdentifier.lowLimit, new Real(0f)));
        writeSpecs.add(new WriteAccessSpecification(created, new SequenceOf<PropertyValue>(pvs)));
        System.out.println(send(d, new WritePropertyMultipleRequest(
                new SequenceOf<WriteAccessSpecification>(writeSpecs))));

        // Read a value
        System.out.println(send(d, new ReadPropertyRequest(created, PropertyIdentifier.presentValue, null)));
        // ObjectIdentifier oi = new ObjectIdentifier(ObjectType.analogInput, 0);
        // BACnetObject obj = new BACnetObject(oi);
        // obj.setProperty(new PropertyValue(PropertyIdentifier.objectName, new
        // CharacterString("A cool analog input")));
        // obj.setProperty(new PropertyValue(PropertyIdentifier.description, new CharacterString(description)));
        // obj.setProperty(new PropertyValue(PropertyIdentifier.presentValue, new Real(155.2f)));
        // obj.setProperty(new PropertyValue(PropertyIdentifier.units, EngineeringUnits.centimetersOfMercury));
        // d.addObject(obj);
        // System.out.println(send(d, new ReadPropertyRequest(created, PropertyIdentifier.description, null)));
        System.out.println(send(d, new ReadPropertyRequest(device, PropertyIdentifier.objectName, null)));
        System.out.println(send(d, new ReadPropertyRequest(deviceIndirect, PropertyIdentifier.objectName, null)));

        // Read multiple
        List<ReadAccessSpecification> specs = new ArrayList<ReadAccessSpecification>();

        List<PropertyReference> refs = new ArrayList<PropertyReference>();
        // refs.add(new PropertyReference(PropertyIdentifier.objectName, null));
        // refs.add(new PropertyReference(PropertyIdentifier.units, null));
        // refs.add(new PropertyReference(PropertyIdentifier.presentValue, null));
        // refs.add(new PropertyReference(PropertyIdentifier.objectName, new UnsignedInteger(0)));
        // refs.add(new PropertyReference(PropertyIdentifier.units, new UnsignedInteger(1)));
        // refs.add(new PropertyReference(PropertyIdentifier.presentValue, new UnsignedInteger(2)));
        // specs.add(new ReadAccessSpecification(created, new SequenceOf<PropertyReference>(refs)));

        refs = new ArrayList<PropertyReference>();
        refs.add(new PropertyReference(PropertyIdentifier.all, null));
        specs.add(new ReadAccessSpecification(created, new SequenceOf<PropertyReference>(refs)));

        refs = new ArrayList<PropertyReference>();
        refs.add(new PropertyReference(PropertyIdentifier.all, null));
        specs.add(new ReadAccessSpecification(device, new SequenceOf<PropertyReference>(refs)));
        specs.add(new ReadAccessSpecification(deviceIndirect, new SequenceOf<PropertyReference>(refs)));

        // refs = new ArrayList<PropertyReference>();
        // refs.add(new PropertyReference(PropertyIdentifier.required, null));
        // specs.add(new ReadAccessSpecification(created, new SequenceOf<PropertyReference>(refs)));

        System.out.println(send(d, new ReadPropertyMultipleRequest(new SequenceOf<ReadAccessSpecification>(specs))));

        // Read conditional
        List<SelectionCriteria> criteria = new ArrayList<SelectionCriteria>();
        criteria.add(new SelectionCriteria(PropertyIdentifier.presentValue, null, RelationSpecifier.equal, new Real(0)));
        criteria.add(new SelectionCriteria(PropertyIdentifier.presentValue, null, RelationSpecifier.notEqual, new Real(
                0)));
        ObjectSelectionCriteria osc = new ObjectSelectionCriteria(SelectionLogic.or, new SequenceOf<SelectionCriteria>(
                criteria));
        new ReadPropertyConditionalRequest(osc, null);

        // Delete object
        System.out.println(send(d, new DeleteObjectRequest(created)));
        System.out.println(send(d, new DeleteObjectRequest(created)));
    }

    public static AcknowledgementService send(LocalDevice d, ConfirmedRequestService s) throws Exception {
        Address a = new Address(InetAddrCache.get("localhost", 0xbac1));
        return d.send(a, null, MaxApduLength.UP_TO_50, Segmentation.segmentedBoth, s);
    }

    public static void test2(LocalDevice d) throws Exception {
        d.sendGlobalBroadcast(new WhoIsRequest(null, null));
        Thread.sleep(500);

        RemoteDevice rd = d.getRemoteDevices().get(0);

        ObjectIdentifier nc0 = new ObjectIdentifier(ObjectType.notificationClass, 4194000);
        ObjectIdentifier nc1 = new ObjectIdentifier(ObjectType.notificationClass, 4194001);
        ObjectIdentifier nc2 = new ObjectIdentifier(ObjectType.notificationClass, 4194002);

        Recipient recipient = new Recipient(d.getConfiguration().getId());
        Destination dest = new Destination(recipient, new UnsignedInteger(0), new Boolean(false),
                new EventTransitionBits(true, true, true));
        // System.out.println(send(d, rd, new AddListElementRequest(nc0, PropertyIdentifier.recipientList, new
        // UnsignedInteger(1), dest)));
        System.out.println(send(d, rd, new WritePropertyRequest(nc0, PropertyIdentifier.recipientList,
                new UnsignedInteger(1), dest, null)));

        List<ReadAccessSpecification> specs = new ArrayList<ReadAccessSpecification>();
        specs.add(new ReadAccessSpecification(nc0, PropertyIdentifier.all));
        specs.add(new ReadAccessSpecification(nc1, PropertyIdentifier.all));
        specs.add(new ReadAccessSpecification(nc2, PropertyIdentifier.all));
        System.out
                .println(send(d, rd, new ReadPropertyMultipleRequest(new SequenceOf<ReadAccessSpecification>(specs))));

    }

    public static AcknowledgementService send(LocalDevice d, RemoteDevice rd, ConfirmedRequestService s)
            throws Exception {
        return d.send(rd, s);
    }

    // public static void main(String[] args) throws Exception {
    // final MessageControl mc1 = new MessageControl(10001);
    // mc1.init();
    // final MessageControl mc2 = new MessageControl(10002);
    // mc2.init();
    // final MessageControl mc3 = new MessageControl(10003);
    // mc3.init();
    // final MessageControl mc4 = new MessageControl(10004);
    // mc4.init();
    // final MessageControl mc5 = new MessageControl(10005);
    // mc5.init();
    //        
    // Thread t1 = new Thread() {
    // public void run() {
    // try {
    // System.out.println("Response for 1: "+ mc1.sendConfirmed(10002, (byte)1));
    // System.out.println("Response for 1: "+ mc1.sendConfirmed(10003, (byte)11));
    // System.out.println("Response for 1: "+ mc1.sendConfirmed(10004, (byte)21));
    // System.out.println("Response for 1: "+ mc1.sendConfirmed(10005, (byte)31));
    // mc1.sendUnconfirmed(10002, (byte)101);
    // mc1.sendUnconfirmed(10003, (byte)111);
    // mc1.sendUnconfirmed(10004, (byte)121);
    // mc1.sendUnconfirmed(10005, (byte)131);
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // };
    // Thread t2 = new Thread() {
    // public void run() {
    // try {
    // System.out.println("Response for 2: "+ mc2.sendConfirmed(10001, (byte)2));
    // System.out.println("Response for 2: "+ mc2.sendConfirmed(10003, (byte)12));
    // System.out.println("Response for 2: "+ mc2.sendConfirmed(10004, (byte)22));
    // System.out.println("Response for 2: "+ mc2.sendConfirmed(10005, (byte)32));
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // };
    // Thread t3 = new Thread() {
    // public void run() {
    // try {
    // System.out.println("Response for 3: "+ mc3.sendConfirmed(10001, (byte)3));
    // System.out.println("Response for 3: "+ mc3.sendConfirmed(10002, (byte)13));
    // System.out.println("Response for 3: "+ mc3.sendConfirmed(10004, (byte)23));
    // System.out.println("Response for 3: "+ mc3.sendConfirmed(10005, (byte)33));
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // };
    // Thread t4 = new Thread() {
    // public void run() {
    // try {
    // System.out.println("Response for 4: "+ mc4.sendConfirmed(10001, (byte)4));
    // System.out.println("Response for 4: "+ mc4.sendConfirmed(10002, (byte)14));
    // System.out.println("Response for 4: "+ mc4.sendConfirmed(10003, (byte)24));
    // System.out.println("Response for 4: "+ mc4.sendConfirmed(10005, (byte)34));
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // };
    // Thread t5 = new Thread() {
    // public void run() {
    // try {
    // System.out.println("Response for 5: "+ mc5.sendConfirmed(10001, (byte)5));
    // System.out.println("Response for 5: "+ mc5.sendConfirmed(10002, (byte)15));
    // System.out.println("Response for 5: "+ mc5.sendConfirmed(10003, (byte)25));
    // System.out.println("Response for 5: "+ mc5.sendConfirmed(10005, (byte)35));
    // }
    // catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // };
    //        
    // t1.start();
    // t2.start();
    // t3.start();
    // t4.start();
    // t5.start();
    //        
    // t1.join();
    // t2.join();
    // t3.join();
    // t4.join();
    // t5.join();
    //        
    // mc1.destroy();
    // mc2.destroy();
    // mc3.destroy();
    // mc4.destroy();
    // mc5.destroy();
    // }

    // public static void main(String[] args) throws Exception {
    // Service s = new ReadPropertyRequest(new ObjectIdentifier(AnalogInput.TYPE_ID, 0),
    // PropertyIdentifier.presentValue, null);
    // APDU apdu = new ConfirmedRequest(false, false, false, 0, MaxApduLength.UP_TO_50, 21, 0, 0, s);
    // IpNetwork network = new IpNetwork(0xbac1);
    // network.init();
    // network.send(apdu, "localhost");
    // network.destroy();
    // }

    // public static void main(String[] args) throws Exception {
    // int port = 0xBAC1;
    // DatagramSocket socket = new DatagramSocket(port);
    // DatagramPacket packet;
    //        
    // // Listen
    // while (true) {
    // packet = new DatagramPacket(new byte[1497], 1497);
    // socket.receive(packet);
    //            
    // System.out.println(packet.getAddress() +":"+ packet.getPort());
    // System.out.println(ArrayUtils.toHexString(packet.getData(), packet.getOffset(), packet.getLength()));
    // // /127.0.0.1:47808
    // // [1,4,0,0,13,c,c,0,0,0,0,19,55]
    // }
    //        
    // // Send
    // byte[] data = {
    // // (byte)0xc0, (byte)0xa8, (byte)0x64, (byte)0xff, (byte)0xba, (byte)0xc0, (byte)0x81, (byte)0x0b,
    // // (byte)0x00, (byte)0x18, (byte)0x01, (byte)0x20, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0xff,
    // // (byte)0x10, (byte)0x00, (byte)0xc4, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x22,
    // // (byte)0x04, (byte)0x00, (byte)0x91, (byte)0x03, (byte)0x21, (byte)0x0f
    //                
    // // (byte)0x1, (byte)0x20, (byte)0xff, (byte)0xff, (byte)0x0, (byte)0xff, (byte)0x10, (byte)0x0,
    // // (byte)0xc4, (byte)0x2, (byte)0x0, (byte)0x0, (byte)0x3, (byte)0x22, (byte)0x4, (byte)0x0,
    // // (byte)0x91, (byte)0x3, (byte)0x21, (byte)0x63
    //                
    // // (byte)0x7f, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0xba, (byte)0xc0,
    // // (byte)0x81, (byte)0x0a, (byte)0x00, (byte)0x11, (byte)0x01, (byte)0x04, (byte)0x00, (byte)0x00,
    // // (byte)0x12, (byte)0x0c, (byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x19,
    // // (byte)0x55
    //        
    // // (byte)0x7f, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0xba, (byte)0xc0,
    // // (byte)0x81, (byte)0x0a, (byte)0x00, (byte)0x11, (byte)0x01, (byte)0x04, (byte)0x00, (byte)0x00,
    // // (byte)0x12, (byte)0x0c, (byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x19,
    // // (byte)0x55
    //        
    // // (byte)0x01, (byte)0x04, (byte)0x30, (byte)0x37, (byte)0x0c, (byte)0x0c, (byte)0x00, (byte)0x00,
    // // (byte)0x00, (byte)0x00, (byte)0x19, (byte)0x55, (byte)0x3e, (byte)0x44, (byte)0x44, (byte)0x9a,
    // // (byte)0x52, (byte)0x2b, (byte)0x3f
    //                
    // (byte)0x01, (byte)0x04,
    // (byte)0x00, (byte)0x00, (byte)0x15, (byte)0x0c, (byte)0x0c, (byte)0x00, (byte)0x00, (byte)0x00,
    // (byte)0x00, (byte)0x19, (byte)0x55
    // };
    // packet = new DatagramPacket(data, data.length, InetAddress.getByAddress(
    // new byte[] {(byte)192, (byte)168, 100, 100}), 0xBAC0);
    // socket.send(packet);
    // }

    static String description = "A cool analog input indeed. So much so in fact that it qualifies as suitable for a very very very "
            + "long description in order to provoke the segmenting of messages that ask for said description. No "
            + "one actually need ask for this very very very long description of course, but just in case they "
            + "do, well, there is a very very very long description waiting on the other end for just such an "
            + "occasion. It would be rather a pity if no message did actually ask for this very very very long"
            + "description, because that would be a rather pleasant and suitable test for whether segmentation in "
            + "fact works or not, and that would be good to know.\n\n"
            + "Copy 2\n\n"
            + "A cool analog input indeed. So much so in fact that it qualifies as suitable for a very very very "
            + "long description in order to provoke the segmenting of messages that ask for said description. No "
            + "one actually need ask for this very very very long description of course, but just in case they "
            + "do, well, there is a very very very long description waiting on the other end for just such an "
            + "occasion. It would be rather a pity if no message did actually ask for this very very very long"
            + "description, because that would be a rather pleasant and suitable test for whether segmentation in "
            + "fact works or not, and that would be good to know.\n\n"
            + "Copy 3\n\n"
            + "A cool analog input indeed. So much so in fact that it qualifies as suitable for a very very very "
            + "long description in order to provoke the segmenting of messages that ask for said description. No "
            + "one actually need ask for this very very very long description of course, but just in case they "
            + "do, well, there is a very very very long description waiting on the other end for just such an "
            + "occasion. It would be rather a pity if no message did actually ask for this very very very long"
            + "description, because that would be a rather pleasant and suitable test for whether segmentation in "
            + "fact works or not, and that would be good to know.\n\n"
            + "Copy 4\n\n"
            + "A cool analog input indeed. So much so in fact that it qualifies as suitable for a very very very "
            + "long description in order to provoke the segmenting of messages that ask for said description. No "
            + "one actually need ask for this very very very long description of course, but just in case they "
            + "do, well, there is a very very very long description waiting on the other end for just such an "
            + "occasion. It would be rather a pity if no message did actually ask for this very very very long"
            + "description, because that would be a rather pleasant and suitable test for whether segmentation in "
            + "fact works or not, and that would be good to know.\n\n"
            + "Copy 5\n\n"
            + "A cool analog input indeed. So much so in fact that it qualifies as suitable for a very very very "
            + "long description in order to provoke the segmenting of messages that ask for said description. No "
            + "one actually need ask for this very very very long description of course, but just in case they "
            + "do, well, there is a very very very long description waiting on the other end for just such an "
            + "occasion. It would be rather a pity if no message did actually ask for this very very very long"
            + "description, because that would be a rather pleasant and suitable test for whether segmentation in "
            + "fact works or not, and that would be good to know.";
}
