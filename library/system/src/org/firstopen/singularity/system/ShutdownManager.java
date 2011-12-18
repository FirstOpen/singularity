package org.firstopen.singularity.system;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShutdownManager extends Thread {

	static private HashSet<Shutdown> managedObjectList = new HashSet<Shutdown>();

	static private Log log = LogFactory.getLog(ShutdownManager.class);

	public static synchronized void addManagedObject(Shutdown managedObject) {
		managedObjectList.add(managedObject);
	}

	public static synchronized void removeManagedObject(Shutdown managedObject) {
		managedObjectList.remove(managedObject);
	}

	public void run() {
		log.info("Shutting down managed resources");
		for (Iterator<Shutdown> iter = managedObjectList.iterator(); iter
				.hasNext();) {
			Shutdown element = iter.next();
			if (element.shutdown() == false) {
				log.error("unable to shutdown managed object: "
						+ element.getClass().getName());
			}
		}
	}

} // end ShutdownManager
