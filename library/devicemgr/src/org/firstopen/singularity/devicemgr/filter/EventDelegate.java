package org.firstopen.singularity.devicemgr.filter;

import java.rmi.RemoteException;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import org.apache.log4j.Logger;

public abstract class EventDelegate implements RemoteEventListener {

     RemoteEventListener eventListener = null;
     
     Logger log = Logger.getLogger(this.getClass());
     
	public EventDelegate(RemoteEventListener eventListener) {
		this.eventListener = eventListener;
	}

	/**
	 * 
	 */
	public EventDelegate() {
		super();
		
	}


	public abstract void notify(RemoteEvent theEvent) throws UnknownEventException,
			RemoteException;
		

}
