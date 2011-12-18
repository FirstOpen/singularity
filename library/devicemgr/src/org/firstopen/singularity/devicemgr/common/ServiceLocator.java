/*

 * Copyright 2005 i-Konect LLC

 *

 * Licensed under the Apache License, Version 2.0 (the "License");

 * you may not use this file except in compliance with the License.

 * You may obtain a copy of the License at

 *

 *      http://www.apache.org/licenses/LICENSE-2.0

 *

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS,

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

 * See the License for the specific language governing permissions and

 * limitations under the License.

 *

 */

package org.firstopen.singularity.devicemgr.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscovery;
import net.jini.lookup.entry.Name;

import org.apache.log4j.Logger;

/**
 * 
 * ServiceLocator is a simple wrapper class over Jini's LookupDiscovery
 * 
 * which returns the first matching instance of a service either via
 * 
 * unicast or multicast discovery
 * 
 */

public class ServiceLocator {

	private Object _proxy;

	private Object _lock = new Object();

	private ServiceTemplate _template;

	private static Logger log = Logger.getLogger(ServiceLocator.class);

	/**
	 * 
	 * Locates a service via Unicast discovery
	 * 
	 * @param lusHost
	 *            The id of the host where a Jini lookup service is running
	 * 
	 * @param serviceClass
	 *            The class object representing the interface of the service
	 * 
	 * @throws MalformedURLException
	 * 
	 * @throws IOException
	 * 
	 * @throws ClassNotFoundException
	 * 
	 * @return The proxy to the discovered service
	 * 
	 */

	public static Object getService(String lusHost, Class serviceClass)

	throws java.net.MalformedURLException, java.io.IOException,
			ClassNotFoundException {

		if (System.getSecurityManager() == null) {

			System.setSecurityManager(new RMISecurityManager());

		}

		LookupLocator loc = new LookupLocator("jini://" + lusHost);

		ServiceRegistrar reggie = loc.getRegistrar();

		ServiceTemplate tmpl = new ServiceTemplate(null,
				new Class[] { serviceClass }, null);

		return reggie.lookup(tmpl);

	}

	/**
	 * 
	 * Locates the first matching service via multicast discovery
	 * 
	 * @param serviceClass
	 *            The class object representing the interface of the service
	 * 
	 * @throws IOException
	 * 
	 * @throws InterruptedException
	 * 
	 * @return
	 */

	public static Object getService(Class serviceClass)

	throws java.io.IOException, InterruptedException {

		return getService(serviceClass, null, Long.MAX_VALUE);

	}

	/**
	 * 
	 * Locates the first matching service via multicast discovery
	 * 
	 * @param serviceClass
	 *            The class object representing the interface of the service
	 * 
	 * @param waitTime
	 *            How to wait for the service to be discovered
	 * 
	 * @throws IOException
	 * 
	 * @throws InterruptedException
	 * 
	 * @return
	 */

	public static Object getService(Class serviceClass, long waitTime)

	throws java.io.IOException, InterruptedException {

		return getService(serviceClass, null, waitTime);

	}

	/**
	 * 
	 * Locates the first matching service via multicast discovery
	 * 
	 * @param serviceClass
	 *            The class object representing the interface of the service
	 * 
	 * @param serviceName
	 *            The Name attribute of the service
	 * 
	 * @throws IOException
	 * 
	 * @throws InterruptedException
	 * 
	 * @return
	 */

	public static Object getService(Class serviceClass, String serviceName,
			long waitTime)

	throws java.io.IOException, InterruptedException {

		ServiceLocator sl = new ServiceLocator();

		return sl.getServiceImpl(serviceClass, serviceName, waitTime);

	}

	private Object getServiceImpl(Class serviceClass, String serviceName,
			long waitTime)

	throws java.io.IOException, InterruptedException {

		log.debug("getServiceImpl() class = " + serviceClass + " serviceName = "
				+ serviceName);

		Class[] types = new Class[] { serviceClass };

		Entry[] entry = null;

		if (serviceName != null) {

			entry = new Entry[] { new Name(serviceName) };

		}

		_template = new ServiceTemplate(null, types, entry);

		/*
		 * 
		 * if (System.getSecurityManager() == null) {
		 * 
		 * System.setSecurityManager(new RMISecurityManager());
		 *  }
		 * 
		 */

		LookupDiscovery disco =

		new LookupDiscovery(LookupDiscovery.ALL_GROUPS);

		disco.addDiscoveryListener(new Listener());

		synchronized (_lock) {

			_lock.wait(waitTime);

		}

		disco.terminate();

		if (_proxy == null) {

			throw new InterruptedException("Service not found within wait time");

		}

		return _proxy;

	}

	class Listener implements DiscoveryListener

	{

		// invoked when a LUS is discovered

		public void discovered(DiscoveryEvent ev)

		{

			ServiceRegistrar[] reg = ev.getRegistrars();

			for (int i = 0; i < reg.length && _proxy == null; i++)

			{

				findService(reg[i]);

			}

		}

		public void discarded(DiscoveryEvent ev)

		{

		}

	}

	private void findService(ServiceRegistrar lus) {

		try {

			synchronized (_lock) {

				_proxy = lus.lookup(_template);

				if (_proxy != null) {

					_lock.notifyAll();

				}

			}

		} catch (RemoteException ex) {

			ex.printStackTrace(System.err);

		}

	}

}
