package com.serotonin.bacnet4j.npdu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncomingRequestProcessor {
	private static IncomingRequestProcessor ref = null;
	private final ExecutorService es;
	private IncomingRequestProcessor(){
		es = Executors.newCachedThreadPool();
	}
	public static IncomingRequestProcessor getIncomingRequestProcessor(){
		if(ref == null){
			ref = new IncomingRequestProcessor();
		}
		return ref;
	}
	
	public void submit(final IncomingRequestParser irp){
		es.submit(new Runnable(){
			@Override
			public void run() {
				irp.run();
			}});
	}
}
