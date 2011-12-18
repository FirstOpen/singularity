/*
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.devicemgr;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.entry.Entry;
import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.export.Exporter;
import net.jini.export.ProxyAccessor;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.InvocationLayerFactory;
import net.jini.jeri.ServerEndpoint;
import net.jini.jeri.tcp.TcpServerEndpoint;
import net.jini.lookup.JoinManager;
import net.jini.lookup.entry.Name;
import net.jini.lookup.entry.ServiceInfo;

import org.apache.log4j.Logger;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.filter.AssuredDeliveryDelegate;
import org.firstopen.singularity.devicemgr.filter.Event;
import org.firstopen.singularity.devicemgr.filter.TagCountFilter;
import org.firstopen.singularity.devicemgr.interrogator.Interrogator;

import com.sun.jini.config.Config;
import com.sun.jini.start.LifeCycle;

/**
 * Jini Specific Device Manager Specifics, although the DeviceManagerRemote
 * interfaces allows multiple impelmentation just islolate the Jini Specific
 * housekeeping. This will allow sub-classses to focus on the functional
 * behavior, and not have to be concerned with the distributed technology being
 * used in the implementation.
 * 
 * @author Tom Rose
 */
public class DeviceManagerJiniImpl extends DeviceManagerAbstractImpl implements
		ProxyAccessor, DeviceManagerJini, Remote {
	Logger log = null;

	RemoteEventListener remoteEventDelegate = null;

	/*
	 * Service Methods
	 */

	public void ping() throws RemoteException {
	}

	// generated service start up code
	private Object proxy;

	private Object adminProxy;

	private Exporter exporter;

	private JoinManager joinManager;

	// key into config file
	private String PACKAGE_NAME = "org.firstopen.singularity.devicemgr";

	/*
	 * information is default, should be intialized from 
	 * configuration information obtained by parent class. 
	 */
	private static String[] GROUPS = LookupDiscovery.ALL_GROUPS;

	private static String manufacturer = "i-Konect LLC";

	private static String vendor = "www.i-konect.com";

	private static String version = "1.0";

	private static String model = "A001";

	private static String serialNumber = "123456789";

	private static String name = "i_konect_Device_Mrg";

	private ServiceInfo serviceInfo = new ServiceInfo(name, manufacturer,
			vendor, version, model, serialNumber);

	// require ctor for Jini 2 NonActivatableServiceDescriptor
	public DeviceManagerJiniImpl(String[] args, LifeCycle lifeCycle)
			throws RemoteException {
		super();
		try {

			Logger log = Logger.getLogger(this.getClass());
			init();
			initialize(args);
		} catch (Exception e) {
			throw new RemoteException("unable to create Jini Device Manager", e);
		}
	}

	// Proxy accessor
	public Object getProxy() {
		return proxy;
	}

	private void initialize(String[] args) throws RemoteException {
		try {

			/*
			 * For each reader get the id to expose for this service
			 */
			Iterator<Interrogator> iterator = getInterrogators().iterator();
			ArrayList<Entry> entries = new ArrayList<Entry>();
			entries.add(serviceInfo);
            entries.add(new Name(deviceManagerID));
			while (iterator.hasNext()) {
				entries.add(new Name(iterator.next().getDeviceProfile().getDeviceProfileID()));
			}

			Configuration config = ConfigurationProvider.getInstance(args);
			String[] lookupGroups = (String[]) config.getEntry(PACKAGE_NAME,
					"initialLookupGroups", String[].class, GROUPS);
			LookupLocator[] lookupLocators = (LookupLocator[]) Config
					.getNonNullEntry(config, PACKAGE_NAME,
							"initialLookupLocators", LookupLocator[].class,
							new LookupLocator[0]);
			Entry[] atts = (Entry[]) Config.getNonNullEntry(config,
					PACKAGE_NAME, "initialLookupAttributes", Entry[].class,
					entries.toArray(new Entry[entries.size()]));
	
            // this service ID should be saved and loaded from a file
			// also all the JoinAdmin updates should be persisted
			Uuid serviceID = UuidFactory.generate();
			ServiceID lookupID = new ServiceID(serviceID
					.getMostSignificantBits(), serviceID
					.getLeastSignificantBits());
			
            /* Create default exporter for the service */
			ServerEndpoint endpoint = TcpServerEndpoint.getInstance(0);
			InvocationLayerFactory ilFactory = new BasicILFactory();
			Exporter defaultExporter = new BasicJeriExporter(endpoint,
					ilFactory, false, true);
			
            // reset exporter if specified in the configFile
			exporter = (Exporter) Config.getNonNullEntry(config, PACKAGE_NAME,
					"serverExporter", Exporter.class, defaultExporter, null);
			// export the object
			proxy = exporter.export(this);
			// here both proxy and adminProxy are the same object
			adminProxy = proxy;
			// next startup with JoinManager
			LookupDiscoveryManager luDiscoMgr = new LookupDiscoveryManager(
					lookupGroups, lookupLocators, null);
			joinManager = new JoinManager(proxy, atts, lookupID, luDiscoMgr,
					null);

			registerRemoteEventDeligates();

		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new RemoteException("Failed to init service", ex);
		}
	}

	/**
	 * create an event pipeline.
	 */
	private void registerRemoteEventDeligates() {

	
		/**
		 * TODO: probably should be part of ConfigManagerAbstractImpl instead of the
		 * distributed computing component. Also, must create a dyamic registration so the 
		 * configuration of filters can be pushed down to device managers and dynamically loaded.
		 * more thought on coupling to Jini impelentation. Perhaps is will be wise to keep it coupled
		 * and leverage Jini, since anything can be a Jini Service even OSGi bridge can be created.
		 */
		remoteEventDelegate = new TagCountFilter(new AssuredDeliveryDelegate());

	}

	// DestroyAdmin
	public void destroy() {
		// add any resource clenup code here
		joinManager.terminate();
		exporter.unexport(true);
	}

	// JoinAdmin
	/**
	 * Get the current attribute sets for the service.
	 * 
	 * @return the current attribute sets for the service
	 * @throws java.rmi.RemoteException
	 */
	public Entry[] getLookupAttributes() throws RemoteException {
		return joinManager.getAttributes();
	}

	/**
	 * Add attribute sets for the service. The resulting set will be used for
	 * all future joins. The attribute sets are also added to all
	 * currently-joined lookup services.
	 * 
	 * @param attrSets
	 *            the attribute sets to add
	 * @throws java.rmi.RemoteException
	 */
	public void addLookupAttributes(Entry[] attrSets) throws RemoteException {
		joinManager.addAttributes(attrSets, true);
	}

	/**
	 * Modify the current attribute sets, using the same semantics as
	 * ServiceRegistration.modifyAttributes. The resulting set will be used for
	 * all future joins. The same modifications are also made to all
	 * currently-joined lookup services.
	 * 
	 * @param attrSetTemplates
	 *            the templates for matching attribute sets
	 * @param attrSets
	 *            the modifications to make to matching sets
	 * @throws java.rmi.RemoteException
	 * @see net.jini.core.lookup.ServiceRegistration#modifyAttributes
	 */
	public void modifyLookupAttributes(Entry[] attrSetTemplates,
			Entry[] attrSets) throws RemoteException {
		joinManager.modifyAttributes(attrSetTemplates, attrSets, true);
	}

	/**
	 * Get the list of groups to join. An empty array means the service joins no
	 * groups (as opposed to "all" groups).
	 * 
	 * @return an array of groups to join. An empty array means the service
	 *         joins no groups (as opposed to "all" groups).
	 * @throws java.rmi.RemoteException
	 * @see #setLookupGroups
	 */
	public String[] getLookupGroups() throws RemoteException {
		return getDiscoveryManager().getGroups();
	}

	/**
	 * Add new groups to the set to join. Lookup services in the new groups will
	 * be discovered and joined.
	 * 
	 * @param groups
	 *            groups to join
	 * @throws java.rmi.RemoteException
	 * @see #removeLookupGroups
	 */
	public void addLookupGroups(String[] groups) throws RemoteException {
		try {
			getDiscoveryManager().addGroups(groups);
		} catch (java.io.IOException ex) {
			throw new RemoteException("addLookupGroups() encountered :", ex);
		}
	}

	/**
	 * Remove groups from the set to join. Leases are cancelled at lookup
	 * services that are not members of any of the remaining groups.
	 * 
	 * @param groups
	 *            groups to leave
	 * @throws java.rmi.RemoteException
	 * @see #addLookupGroups
	 */
	public void removeLookupGroups(String[] groups) throws RemoteException {
		getDiscoveryManager().removeGroups(groups);
	}

	/**
	 * Replace the list of groups to join with a new list. Leases are cancelled
	 * at lookup services that are not members of any of the new groups. Lookup
	 * services in the new groups will be discovered and joined.
	 * 
	 * @param groups
	 *            groups to join
	 * @throws java.rmi.RemoteException
	 * @see #getLookupGroups
	 */
	public void setLookupGroups(String[] groups) throws RemoteException {
		try {
			getDiscoveryManager().setGroups(groups);
			// //_store.save(_joinManager);
		} catch (java.io.IOException ex) {
			throw new RemoteException("setLookupGroups() encountered :", ex);
		}
	}

	/**
	 * Get the list of locators of specific lookup services to join.
	 * 
	 * @return the list of locators of specific lookup services to join
	 * @throws java.rmi.RemoteException
	 * @see #setLookupLocators
	 */
	public LookupLocator[] getLookupLocators() throws RemoteException {
		return getDiscoveryManager().getLocators();
	}

	/**
	 * Add locators for specific new lookup services to join. The new lookup
	 * services will be discovered and joined.
	 * 
	 * @param locators
	 *            locators of specific lookup services to join
	 * @throws java.rmi.RemoteException
	 * @see #removeLookupLocators
	 */
	public void addLookupLocators(LookupLocator[] locators)
			throws RemoteException {
		getDiscoveryManager().addLocators(locators);
	}

	/**
	 * Remove locators for specific lookup services from the set to join. Any
	 * leases held at the lookup services are cancelled.
	 * 
	 * @param locators
	 *            locators of specific lookup services to leave
	 * @throws java.rmi.RemoteException
	 * @see #addLookupLocators
	 */
	public void removeLookupLocators(LookupLocator[] locators)
			throws RemoteException {
		getDiscoveryManager().removeLocators(locators);
	}

	/**
	 * Replace the list of locators of specific lookup services to join with a
	 * new list. Leases are cancelled at lookup services that were in the old
	 * list but are not in the new list. Any new lookup services will be
	 * discovered and joined.
	 * 
	 * @param locators
	 *            locators of specific lookup services to join
	 * @throws java.rmi.RemoteException
	 * @see #getLookupLocators
	 */
	public void setLookupLocators(LookupLocator[] locators)
			throws RemoteException {
		getDiscoveryManager().setLocators(locators);
	}

	// helpers
	private LookupDiscoveryManager getDiscoveryManager() {
		return (LookupDiscoveryManager) joinManager.getDiscoveryManager();
	}

	public void registerDeviceECSpec(DeviceECSpec deviceECSpec)
			throws RemoteException {
		try {
			super.registerDeviceECSpec(deviceECSpec);

		} catch (Exception e) {
			throw new RemoteException("unable to registerECSpec", e);
		}

	}

	public Object getAdmin() throws RemoteException {
		// Administrable public Object getAdmin() {
		return adminProxy;
	}

	@Override
	public void sendRemoteEvent(DeviceECSpec ecSpecProfile) {
      
        RemoteEvent remoteEvent = new RemoteEvent(ecSpecProfile, 0, 0, null);
		
		/*
		 * if unable to deliver remote event just log it, and keep a metric
		 * for JMX to expose for health monitoring.
		 */
		try {
			remoteEventDelegate.notify(remoteEvent);
		} catch (RemoteException e) {
			log.error("unable to deliver remote event", e);
		} catch (UnknownEventException e) {
			log.error("unable to deliver remote event", e);
		}
	}

    /* (non-Javadoc)
     * @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
     */
    public void notify(RemoteEvent remoteEvent) throws UnknownEventException, RemoteException {
       
        /*
         * introspect remoteEvent for type of event to process.
         */
        
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.DeviceManager#notify(org.firstopen.singularity.devicemgr.Event)
     */
    public void notify(Event serviceEvent) throws RemoteException, Exception {
         RemoteEvent remoteEvent = new RemoteEvent(serviceEvent, 0, 0, null);
         notify(remoteEvent);
    }

}
