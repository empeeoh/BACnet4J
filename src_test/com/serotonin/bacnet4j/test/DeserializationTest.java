/*
    Copyright (C) 2006-2009 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.type.primitive.Real;
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
        Real real = new Real(1.23F);
        ByteQueue queue = new ByteQueue();
        real.write(queue);
        System.out.println(queue);
    }
}
