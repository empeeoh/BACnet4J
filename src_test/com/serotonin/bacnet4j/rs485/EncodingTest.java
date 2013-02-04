package com.serotonin.bacnet4j.rs485;

import com.serotonin.bacnet4j.npdu.NPCI;
import com.serotonin.util.queue.ByteQueue;

public class EncodingTest {
    //    public static void main(String[] args) {
    //        Frame frame = new Frame(FrameType.bacnetDataNotExpectingReply, (byte) 0xff, (byte) 8, new byte[] { 0x1, 0x20,
    //                (byte) 0xff, (byte) 0xff, 0x0, (byte) 0xff, 0x10, 0x0, (byte) 0xc4, 0x2, 0x4, 0x3a, 0x10, 0x22, 0x1,
    //                (byte) 0xe0, (byte) 0x91, 0x3, 0x22, 0x1, 0x15 });
    //
    //        ByteArrayOutputStream out = new ByteArrayOutputStream();
    //        MasterNode master = new MasterNode(null, out, (byte) 8, 1);
    //        master.testSendFrame(frame);
    //
    //        System.out.println(StreamUtils.dumpArrayHex(out.toByteArray()));
    //    }

    //    public static void main(String[] args) throws Exception {
    //        //        byte[] b = new byte[] { 0x1, 0x20, (byte) 0xff, (byte) 0xff, 0x0, (byte) 0xff, 0x10, 0x0, (byte) 0xc4, 0x2,
    //        //                0x4, 0x3a, 0x10, 0x22, 0x1, (byte) 0xe0, (byte) 0x91, 0x3, 0x22, 0x1, 0x15 };
    //        //        byte[] b = new byte[] { 0x1, 0x20, (byte) 0xff, (byte) 0xff, 0x0, (byte) 0xff  };
    //        byte[] b = new byte[] { 0x10, 0x0, (byte) 0xc4, 0x2, 0x4, 0x3a, 0x10, 0x22, 0x1, (byte) 0xe0, (byte) 0x91, 0x3,
    //                0x22, 0x1, 0x15 };
    //
    //        ByteQueue queue = new ByteQueue(b);
    //        ServicesSupported ss = new ServicesSupported();
    //        ss.setAll(true);
    //        System.out.println(APDU.createAPDU(ss, queue));
    //    }

    public static void main(String[] args) throws Exception {
        byte[] b = new byte[] { 0x1, 0x20, (byte) 0xff, (byte) 0xff, 0x0, (byte) 0xff };
        ByteQueue queue = new ByteQueue(b);
        new NPCI(queue);
    }

    //    public static void main(String[] args) {
    //        ByteQueue queue = new ByteQueue();
    //
    //        IAmRequest iam = new IAmRequest(new ObjectIdentifier(ObjectType.device, 277008), new UnsignedInteger(
    //                MaxApduLength.UP_TO_50.getId()), Segmentation.segmentedBoth, new UnsignedInteger(277));
    //        UnconfirmedRequest req = new UnconfirmedRequest(iam);
    //        req.write(queue);
    //        System.out.println(queue);
    //    }
}
