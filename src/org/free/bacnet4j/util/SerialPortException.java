package org.free.bacnet4j.util;

public class SerialPortException extends Exception{

	public SerialPortException(String message)
	{
		super(message);
	}

	public SerialPortException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -1L;
}
