package org.free.bacnet4j.util;

import gnu.io.*;

public class SerialUtils{

	public SerialUtils(){}

	public static SerialPort openSerialPort(SerialParameters serialParameters)
			throws SerialPortException{
		SerialPort serialPort = null;
		try{
			CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(serialParameters.getCommPortId());
			if(cpi.getPortType() != 1)
				throw new SerialPortException((new StringBuilder()).append("Port with id ").append(serialParameters.getCommPortId()).append(" is not a serial port").toString());
			serialPort = (SerialPort)cpi.open(serialParameters.getPortOwnerName(), 1000);
			serialPort.setSerialPortParams(serialParameters.getBaudRate(), serialParameters.getDataBits(), serialParameters.getStopBits(), serialParameters.getParity());
			serialPort.setFlowControlMode(serialParameters.getFlowControlIn() | serialParameters.getFlowControlOut());
		}
		catch(SerialPortException e){
			close(serialPort);
			throw e;
		}
		catch(Exception e)
		{
			close(serialPort);
			throw new SerialPortException(e);
		}
		return serialPort;
			}

	public static void close(SerialPort serialPort){
		if(serialPort != null)
			if(serialPort instanceof RXTXPort)			
				RXTXHack.closeRxtxPort((RXTXPort)serialPort);
			else
				serialPort.close();
	}
}