package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.type.primitive.UnsignedInteger;
import com.serotonin.util.queue.ByteQueue;

public class ServiceDataDecoding {
    public static void main(String[] args) throws Exception {
        ByteQueue queue = new ByteQueue();
        //        queue.push((byte) 30);
        //        queue.push((byte) 10);
        //        queue.push((byte) 1);
        //        queue.push((byte) 0);
        //        new UnsignedInteger(queue);

        UnsignedInteger u = new UnsignedInteger(30);
        u.write(queue);
        System.out.println();

    }

    //    public static void main(String[] args) throws Exception {
    //        String s = "[c,7,80,30,0,1e,29,4b,4e,c4,7,80,30,0,4f,29,4d,4e,75,c,0,4f,54,45,53,20,44,4f,4f,52,20,31,4f,29,4f,4e,91,1e,4f,29,55,4e,91,0,4f,29,6f,4e,82,4,0,4f,29,24,4e,91,0,4f,29,67,4e,91,0,4f,29,51,4e,10,4f,29,57,4e,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4f,29,68,4e,91,0,4f,29,e7,4e,91,0,4f,29,e9,4e,91,0,4f,29,eb,4e,91,2,4f,29,e6,4e,1e,4f,29,e3,4e,64,4f,29,e5,4e,7,8,4f,29,1c,4e,75,c,0,4f,54,45,53,20,44,4f,4f,52,20,31,4f,29,e2,4e,91,0,4f,29,e4,4e,c4,1,0,20,0,4f,29,e8,4e,0,4f,29,ea,5e,91,2,91,20,5f,1f]";
    //        ByteQueue queue = new ByteQueue(toBytes(s));
    //
    //        AcknowledgementService.createAcknowledgementService(ReadPropertyMultipleAck.TYPE_ID, queue);
    //    }
    //
    //    public static byte[] toBytes(String s) {
    //        if (s.startsWith("["))
    //            s = s.substring(1);
    //        if (s.endsWith("]"))
    //            s = s.substring(0, s.length() - 1);
    //        String[] parts = s.split(",");
    //        if (parts.length == 1)
    //            parts = s.split("\\|");
    //        if (parts.length == 1)
    //            parts = s.split(" ");
    //        if (parts.length == 1) {
    //            parts = new String[s.length() / 2];
    //            for (int i = 0; i < parts.length; i++)
    //                parts[i] = s.substring(i * 2, i * 2 + 2);
    //        }
    //
    //        byte[] bytes = new byte[parts.length];
    //
    //        for (int i = 0; i < bytes.length; i++)
    //            bytes[i] = (byte) Integer.parseInt(parts[i].trim(), 16);
    //
    //        return bytes;
    //    }
}
