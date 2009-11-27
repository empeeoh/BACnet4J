/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.service.unconfirmed.WhoHasRequest;
import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.primitive.CharacterString;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

/**
 * @author Matthew Lohbihler
 */
public class DeserializationTest {
//    public static void main(String[] args) throws Exception {
//        byte[] bytes = {0xc, 0x0, (byte)0x80, 0x0, 0x1, 0x19, 0x55, 0x3e, 0x31, 0x5, 0x3f};
//        ComplexACK ack = new ComplexACK(false, false, (byte)0, 0, 0, (byte)12, new ByteQueue(bytes));
//        ack.parseServiceData();
//    }
    
    public static void main(String[] args) throws Exception {
        test2();
        test3();
        test5();
        test6();
    }
    
    static void test2() throws Exception {
        WhoHasRequest req = new WhoHasRequest(null, new CharacterString("ELEC"));
        ByteQueue queue = new ByteQueue();
        req.write(queue);
        System.out.println(queue);
        new WhoHasRequest(queue);
    }
    
    static void test3() throws Exception {
        WhoHasRequest req = new WhoHasRequest(null, new ObjectIdentifier(ObjectType.accessDoor, 5));
        ByteQueue queue = new ByteQueue();
        req.write(queue);
        System.out.println(queue);
        new WhoHasRequest(queue);
    }
    
    static void test5() throws Exception {
        WhoHasRequest req = new WhoHasRequest(
                new WhoHasRequest.Limits(new UnsignedInteger(0), new UnsignedInteger(4194303)),
                new CharacterString("ELEC"));
        ByteQueue queue = new ByteQueue();
        req.write(queue);
        System.out.println(queue);
        new WhoHasRequest(queue);
    }
    
    static void test6() throws Exception {
        WhoHasRequest req = new WhoHasRequest(
                new WhoHasRequest.Limits(new UnsignedInteger(0), new UnsignedInteger(4194303)),
                new ObjectIdentifier(ObjectType.accessDoor, 5));
        ByteQueue queue = new ByteQueue();
        req.write(queue);
        System.out.println(queue);
        new WhoHasRequest(queue);
    }
}
