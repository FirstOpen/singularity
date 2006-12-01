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
package org.firstopen.singularity.devicemgr.config;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.log4j.Logger;
import org.firstopen.singularity.config.ConfigServiceHome;
import org.firstopen.singularity.config.ConfigServiceRemote;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.devicemgr.interrogator.Interrogator;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.util.JNDIUtil;

/**
 * 
 * ConfigManagerAbstractImpl is an implentation of ConfigManager interface,
 * however, is abstract to seperate ConfigManager functionality from distributed
 * service infrastructure. The distributed service implementation could then be
 * Jini, OSGi, custom, etc. without rewriting the ConfigManager.
 * 
 * @author TomRose
 * 
 */
public abstract class ConfigManagerAbstractImpl implements ConfigManager {
    Logger log = Logger.getLogger(this.getClass());

    private List<Interrogator> interrogators = Collections
            .synchronizedList(new ArrayList<Interrogator>());

    private List<Reader> readers = Collections
            .synchronizedList(new ArrayList<Reader>());

    private Hashtable ecSpecHash = new Hashtable();

    // example method declared in interface class
    public void ping() throws RemoteException {

    }

    protected ConfigManagerAbstractImpl() {
        /*
         * get the configuration for this DeviceManagerRemote
         */

    }

    /**
     * Get the configuration from the configuration service. DeviceManagerRemote
     * instance must know what DeviceProfile(s) it will be servicing and
     * lookup the required information. DeviceProfiles will be identified by
     * EPC (SGTIN). Note: currently just in a properties file, will be moved to
     * configuration service or just a DB lookup.
     * 
     * 
     * <li> Create a list DeviceProfiles serviced by this device manager
     * Create a list of Readers defined for all the sensors defined for the
     * Reader Components.
     * 
     * </li>
     * 
     * Note: A Reader has DeviceProfiles, each or
     * 
     * @return List<DeviceProfile>
     * @throws Exception
     */
    public List<DeviceProfile> getConfig() throws Exception {
        /*
         * get the configuration from cache and if not get from remote. TODO:
         * implement cache
         */
        return getConfigRemote();
    }// end getConfiguration

    protected List<DeviceProfile> getConfigRemote() throws Exception {

        // getResource at the momment hard-wire for now.
        InputStream is = ClassLoader.getSystemResourceAsStream("id.properties");
        Properties properties = new Properties();
        List<DeviceProfile> physicalReaderList = null;
        try {
            properties.load(is);

            InitialContext jndiContext = JNDIUtil.getInitialContext(
                    properties.getProperty("id.epm.hostname"), "admin", "admin");
            Object objref = jndiContext.lookup("ejb/config/ConfigService");
            log.info("retrieving home interface...");
            ConfigServiceHome configServiceHome = (ConfigServiceHome) PortableRemoteObject
                    .narrow(objref, ConfigServiceHome.class);
            log.info("retrieving remote interface...");
            ConfigServiceRemote configServiceRemote = configServiceHome
                    .create();

            log.info("Config Manager initializing...");
            log.info("Retrieving DeviceProfiles Config");
            physicalReaderList = configServiceRemote.getConfig(properties
                    .getProperty("id.devicemgr.name"));
            log.info("DeviceProfiles Config successfully retrieved");

        } catch (IOException e) {
            log.error("unable to read properties stream", e);
            throw new Exception("unable to read properties stream");
        }// end while

        return physicalReaderList;
    }// end getConfiguration

    /**
     * Initialize the ConfigManager
     * 
     * <li> 1. Create DeviceProfiles, and Assign Readers 2. Create
     * Interrogator, and assign a ReaderComponet 3. DeviceManagerRemote will get
     * SensorEvents </li>
     * 
     * 
     * @throws Exception
     * 
     */
    void init() throws Exception {
        /*
         * Create ShutdownManager is called when the JVM terminates from a
         * signal
         */

        log.info("Shutdown manager registered...");
        ShutdownManager sdm = new ShutdownManager();
        Runtime.getRuntime().addShutdownHook(sdm);

        /*
         * setup the cache
         */
        log.info("Config Manager Starting...");
        getConfig();
        log.info("Config Manager Started...");
    }

}
