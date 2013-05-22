package com.serotonin.bacnet4j.rs485;

import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.io.serial.SerialUtils;

public class PortTest {
    public static void main(String[] args) throws Exception {
        SerialParameters params = new SerialParameters();
        params.setCommPortId("COM4");
        params.setBaudRate(9600);
        params.setPortOwnerName("Testing");

        SerialPort serialPort = SerialUtils.openSerialPort(params);
        InputStream in = serialPort.getInputStream();
        OutputStream out = serialPort.getOutputStream();

        while (true) {
            System.out.print(Integer.toString(in.read(), 16));
        }
    }
}
