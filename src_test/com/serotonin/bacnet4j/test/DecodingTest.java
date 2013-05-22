package com.serotonin.bacnet4j.test;

import com.serotonin.bacnet4j.apdu.APDU;
import com.serotonin.bacnet4j.apdu.ComplexACK;
import com.serotonin.bacnet4j.service.unconfirmed.UnconfirmedRequestService;
import com.serotonin.bacnet4j.type.constructed.ServicesSupported;
import com.serotonin.util.queue.ByteQueue;

public class DecodingTest {
    public static void main(String[] args) throws Exception {
        String s = "3c0700030c0c02000075194c3ec402000075c400000001c400000002c400800003c400800004c401400005c404c00006c404c00007c400800008c40000000ac40080000bc40080000cc40080000dc40080000ec40080000fc400800010c400000011c400800012c400800013c400800014c400800015c400800016c400800017c400c00018c400c00019c40140001ac40140001bc40140001cc404c0001dc404c0001ec40140001fc404c00020c404c00021c400800022c400800023c401400024c404c00025c400800026c401400027c401400028c404c00029c404c0002ac404c0002bc404c0002cc40000002dc40000002ec40000002fc400800030c400800031c400800032c400800033c400800034c400800035c400800036c400800037c400c00038c400c00039c40140003ac40140003bc40140003cc40140003dc404c0003ec404c0003fc404c00040c404c00041c400000042c400000043c400800044c400800045c400800046c400800047c400800048c400800049c40080004ac40080004bc400c0004cc400c0004dc40140004ec40140004fc401400050c401400051c404c00052c404c00053c404c00054c404c00055c400800056c401400057c400000058c404c00059c404c0005ac40080005bc40080005cc40140005dc404c0005ec400";

        //String s = "81,a,0,d,1,0,50,9d,9,91,1,91,1f";
        // String s = "[81,b,0,14,1,0,10,0,c4,2,0,8,98,22,1,e0,91,0,21,8]";
        // String input = "[81,4,0,23,c0,a8,1,5a,ba,c0,1,8,27,28,6,0,40,ae,0,73,8f,10,0,c4,2,0,9,60,22,1,e0,91,0,21,8]";
        // String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,38,1,3,10,0,c4,2,0,9,63,22,1,e0,91,0,21,8]";
        // String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,36,1,1,10,0,c4,2,0,8,99,22,1,e0,91,0,21,8]";
        // String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,38,1,4,10,0,c4,2,0,9,64,22,1,e0,91,0,21,8]";
        // String input = "[81,4,0,1e,c0,a8,1,5a,ba,c0,1,8,4e,36,1,2,10,0,c4,2,0,8,9a,22,1,e0,91,0,21,8]";
        // String input = "[81,b,0,1b,1,28,ff,ff,0,27,28,6,0,40,ae,0,73,8f,fe,10,8,a,9,62,1a,9,62]";
        // String input = "[81,b,0,1b,1,28,ff,ff,0,27,28,6,0,40,ae,0,73,8f,fe,10,8,a,9,61,1a,9,61]";
        // String input = "[81,b,0,12,1,88,27,28,6,0,40,ae,0,73,8f,0,9c,58]";
        // String input =
        // "[81,b,0,2b,1,0,10,4,9,8,19,1,2e,c,2,0,8,98,19,0,29,0,3e,c,0,80,0,2e,19,55,3e,44,42,c8,0,0,3f,5b,4,0,0,3f,2f]";
        // String input =
        // "[81,b,0,2b,1,0,10,4,9,8,19,1,2e,c,2,0,8,98,19,0,29,0,3e,c,0,80,0,2e,19,55,3e,44,42,c8,0,0,3f,5b,4,0,0,3f,2f]";

        // String input = "81|0a|00|17|01|20|ff|ff |00|ff|10|07|3d|09|00|65|69 |62|74|65|6d|70|31";
        // String input = "81 |0a|00|1d|01|00|10|01|c4|02|00|04 |40|c4|00|00|00|02|75|09|00|65|69 |62|74|65|6d|70|31";
        // String input = "81|0a|00|1d|01|20|ff|ff |00|ff|10|07|0a|04|40|1a|04|40 |3d|09|00|65|69|62|74|65|6d |70|31";

        // WhoIs sent by Delta equipment.
        // String input = "81 0b 00 1b 01 28 ff ff 00 27 d8 06 00 18 71 73 0d 8a fe 10 08 0a 11 5e 1a c3 4f";

        // IAm response
        // String input = "81 0b 00 1b 01 28 ff ff 00 c4 80 01 04 fc 10 00 c4 02 00 76 c4 21 80 91 03 21 08";
        // String input = "[81,b,0,18,1,20,ff,ff,0,ff,10,0,c4,2,1,2c,c8,22,5,c0,91,3,21,4d]";
        // String input = "81,a,0,17,1,4,2,75,1,e,c,2,1,2c,c8,1e,9,4d,9,62,9,8b,1f";

        // Delta private message.
        //        String s = "81 0b 00 4b 01 a8 ff ff 00 28 3c 06 00 40 ae 00 20 28 ff 81 00 08 30 75 00 00 07 00 ff ff 04 00 23 00 31 00 00 00 00 00 00 00 00 00 00 00 40 ae 00 00 02 04 00 91 01 93 01 46 00 00 00 00 00 0c 00 01 00 06 00 20 29 01 00 00 00";
        //String s = "81 0b 00 4b 01 a8 ff ff 00 28 3c 06 00 40 ae 00 20 28 ff 81 00 08 30 75 00 00 07 00 ff ff 04 00 23 00 31 00 00 00 00 00 00 00 00 00 00 00 40 ae 00 00 02 04 00 00 00 00 00 1e 00 00 00 00 00 0c 00 01 00 06 00 20 29 01 00 00 00";
        //        String s = "81 0b 00 45 01 28 ff ff 00 c4 7d 01 04 fd 10 04 09 08 19 01 2e 0c 02 00 75 98 19 00 29 00 3e 0c 00 00 00 65 19 55 3e 44 41 b5 3a 7a 3f 5b 04 00 00 0c 00 80 00 01 19 55 3e 44 41 b4 00 00 3f 5b 04 00 00 3f 2f";
        // String input = "81 0a 00 0d 01 00 50 04 09 91 02 91 2a";

        // String s = "810a045b010800140600000000000030030e"
        // + "0c020000001e294d4e7513004e6577205669727475616c204465766963654f"
        // + "291c4e71004f293a4e750a006c6f63616c686f73744f290c4e7400312e304f29"
        // + "0a4e220bb84f290b4e220bb84f29494e21014f294b4ec4020000004f294f4e9"
        // + "1084f29704e91004f29794e750d00534341444120456e67696e654f29784e2"
        // + "17b4f29464e752500534341444120456e67696e652053657276657220666f7"
        // + "22057696e646f77732056312e304f292c4e750700322e302e32324f29624e2"
        // + "1014f298b4e21044f29614e850600fffbb83fff4f29604e850501ffffffde4f294c4"
        // + "ec402000000c4024000004f293e4e2204004f29a74e21014f296b4e91004f29"
        // + "394eb40a3919604f29384ea46e0518014f29774e3201e04f29184e114f29744"
        // + "e4f291e4ec40200000021146506000000000000c4023fffff21016506a9fee42"
        // + "2bac0c4023fffff211461004f299b4e21004f299a4e4f299d4e2ea4ffffffffb4ffffff"
        // + "ff2f4f29994e213c4f29984e4f2a02004e114f2a02084e7536005363616e6e69"
        // + "6e672044657669636573203a204c6f77204c696d6974203d20302048696768"
        // + "204c696d6974203d20343139343330324f2a02014e233fffff4f29a84e750800"
        // + "3132332d4445564f2a03ed4e4f294d4e7513004e6577205669727475616c20"
        // + "4465766963654f291c4e71004f293a4e750a006c6f63616c686f73744f290c4e"
        // + "7400312e304f290a4e220bb84f290b4e220bb84f29494e21014f294b4ec4020"
        // + "000004f294f4e91084f29704e91004f29794e750d00534341444120456e6769"
        // + "6e654f29784e217b4f29464e752500534341444120456e67696e6520536572"
        // + "76657220666f722057696e646f77732056312e304f292c4e750700322e302e"
        // + "32324f29624e21014f298b4e21044f29614e850600fffbb83fff4f29604e85050"
        // + "1ffffffde4f294c4ec402000000c4024000004f293e4e2204004f29a74e21014f2"
        // + "96b4e91004f29394eb40a391a0f4f29384ea46e0518014f29774e3201e04f29"
        // + "184e114f29744e4f291e4ec40200000021146506000000000000c4023fffff21"
        // + "016506a9fee422bac0c4023fffff211461004f299b4e21004f299a4e4f299d4e2"
        // + "ea4ffffffffb4ffffffff2f4f29994e213c4f29984e4f2a02004e114f2a02084e75360"
        // + "05363616e6e696e672044657669636573203a204c6f77204c696d6974203d2"
        // + "0302048696768204c696d6974203d20343139343330324f2a02014e233fffff4"
        // + "f29a84e7508003132332d4445564f2a03ed4e4f1f0c024000001e294d4e7510"
        // + "004d79204e6577204f626a65637420304f291c4e71004f29254e91014f29484"
        // + "e91004f29534e1e090a1e1f1f4f294e4e0c00000000190029003c000000004f"
        // + "29234e8205004f29004e8205e04f29114e21004f294b4ec4024000004f294f4"
        // + "e91094f29244e91004f29824e2ea4ffffffffb4ffffffff2f2ea4ffffffffb4ffffffff2f2ea"
        // + "4ffffffffb4ffffffff2f4f29a84e7507003132332d45454f2a03e94e104f2a03ee4e" + "71004f1f";

        // String s =
        // "81 04 00 21 0a 14 01 05 ba c0 01 28 ff ff 00 00 0b 01 01 fe 10 00 c4 02 00 2a f9 21 80 91 03 21 12";
        // String s =
        // "81 04 00 21 0a 14 01 05 ba c0 01 28 ff ff 00 00 0d 01 19 fe 10 00 c4 02 00 32 e1 21 80 91 03 21 12";

        // String s = "810a0015010030080c0c004003e8195729083e003f";

        //        new IpNetwork().testDecoding(toBytes(s));

        ComplexACK ca = (ComplexACK) APDU.createAPDU(null, new ByteQueue(toBytes(s)));
        ca.parseServiceData();
    }

    static void test2() throws Exception {
        ServicesSupported servicesSupported = new ServicesSupported();
        servicesSupported.setAll(true);

        String s = "[c,2,0,0,7b,29,1,3d,14,3,54,68,69,73,20,69,73,20,74,68,65,20,6d,65,73,73,61,67,65]";
        byte[] b = toBytes(s);
        UnconfirmedRequestService.createUnconfirmedRequestService(servicesSupported, (byte) 5, new ByteQueue(b));
    }

    // public static void main(String[] args) throws Exception {
    // String s = "[1e,9,a,1e,1f,1f,4f,29,4e,4e,c,0,0,0,0,19,0,29,0,3c,0,0,0,0,4f,29,23,"
    // + "4e,82,5,0,4f,29,0,4e,82,5,e0,4f,29,11,4e,21,0,4f,29,4b,4e,c4,2,40,0,0,4f,2"
    // + "9,4f,4e,91,9,4f,29,24,4e,91,0,4f,29,82,4e,2e,a4,ff,ff,ff,ff,b4,ff,ff,ff,ff,2f,2e,"
    // + "a4,ff,ff,ff,ff,b4,ff,ff,ff,ff,2f,2e,a4,ff,ff,ff,ff,b4,ff,ff,ff,ff,2f,4f,29,a8,4e,75,7,0,3"
    // + "1,32,33,2d,45,45,4f,2a,3,e9,4e,10,4f,2a,3,ee,4e,71,0,4f,1f]";
    // byte[] b = toBytes(s);
    //
    // NotificationParameters.createNotificationParameters(new ByteQueue(b));
    // }

    public static byte[] toBytes(String s) {
        if (s.startsWith("["))
            s = s.substring(1);
        if (s.endsWith("]"))
            s = s.substring(0, s.length() - 1);
        String[] parts = s.split(",");
        if (parts.length == 1)
            parts = s.split("\\|");
        if (parts.length == 1)
            parts = s.split(" ");
        if (parts.length == 1) {
            parts = new String[s.length() / 2];
            for (int i = 0; i < parts.length; i++)
                parts[i] = s.substring(i * 2, i * 2 + 2);
        }

        byte[] bytes = new byte[parts.length];

        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) Integer.parseInt(parts[i].trim(), 16);

        return bytes;
    }
}
