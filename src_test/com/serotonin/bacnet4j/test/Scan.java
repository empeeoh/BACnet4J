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

import java.io.IOException;

import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.event.DefaultDeviceEventListener;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.service.unconfirmed.WhoIsRequest;
import com.serotonin.util.IpAddressUtils;

/**
 * @author Matthew Lohbihler
 */
public class Scan {
    public static void main(String[] args) throws Exception {
        run();

        Thread.sleep(30000);
    }

    private static void run() {
        LocalDevice d = new LocalDevice(1234, "0.0.0.0");
        d.getEventHandler().addListener(new Handler());
        try {
            d.initialize();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        byte[] ip = new byte[4];
        ip[0] = (byte) 192; // A
        ip[1] = (byte) 168; // B
        ip[2] = (byte) 0; // C
        ip[3] = (byte) 0; // D

        WhoIsRequest whois = new WhoIsRequest();
        while (true) {
            String ipstr = IpAddressUtils.toIpString(ip);
            // System.out.println(ipstr);
            d.setBroadcastAddress(ipstr);
            try {
                d.sendBroadcast(whois);
            }
            catch (BACnetException e) {
                System.out.println("Exception at " + ipstr);
                e.printStackTrace();
            }

            try {
                increment(ip);
            }
            catch (Exception e) {
                break;
            }
        }
    }

    private static void increment(byte[] ip) throws Exception {
        int value = (ip[3] & 0xff) + 1;
        if (value < 256)
            ip[3] = (byte) value;
        else {
            ip[3] = 0;
            value = (ip[2] & 0xff) + 1;
            if (value < 256) {
                ip[2] = (byte) value;
                // System.out.println("C="+ value);
            }
            else {
                ip[2] = 0;
                value = (ip[1] & 0xff) + 1;
                if (value < 256) {
                    ip[1] = (byte) value;
                    System.out.println("B=" + value);
                }
                else {
                    ip[1] = 0;
                    value = (ip[0] & 0xff) + 1;
                    if (value < 256) {
                        ip[0] = (byte) value;
                        System.out.println("A=" + value);
                    }
                    else
                        throw new Exception("done");
                }
            }
        }
    }

    static class Handler extends DefaultDeviceEventListener {
        @Override
        public void iAmReceived(RemoteDevice d) {
            System.out.println(d);
        }
    }
}
