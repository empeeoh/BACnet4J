package org.free.bacnet4j.util;

import gnu.io.RXTXPort;

public class RXTXHack{

	private RXTXHack(){}

	public static void closeRxtxPort(RXTXPort port){
		try{
			//port.IOLocked = 0;
		}
		catch(IllegalAccessError e){
			System.out.println(e.getMessage());
		}
		port.close();
	}
}
