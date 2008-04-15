package com.serotonin.bacnet4j;

import com.serotonin.bacnet4j.npdu.ip.IpNetwork;

public class BACnetFactory {
    public static BACnetNetwork createIpNetwork() {
        return new IpNetwork();
    }
}
