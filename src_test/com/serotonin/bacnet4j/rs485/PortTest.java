package com.serotonin.bacnet4j.rs485;

import gnu.io.SerialPort;
import java.io.InputStream;
import org.free.bacnet4j.util.SerialParameters;
import org.free.bacnet4j.util.SerialUtils;

public class PortTest {
    public static void main(String[] args) throws Exception {
        SerialParameters params = new SerialParameters();
        params.setCommPortId("COM4");
        params.setBaudRate(9600);
        params.setPortOwnerName("Testing");

        SerialPort serialPort = SerialUtils.openSerialPort(params);
        InputStream in = serialPort.getInputStream();
        //OutputStream out = serialPort.getOutputStream();

        while (true) {
            System.out.print(Integer.toString(in.read(), 16));
        }
    }
}
