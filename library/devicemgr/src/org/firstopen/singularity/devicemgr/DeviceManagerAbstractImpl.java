/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.common.ServiceLocator;
import org.firstopen.singularity.devicemgr.config.ConfigManager;
import org.firstopen.singularity.devicemgr.interrogator.Interrogator;
import org.firstopen.singularity.devicemgr.interrogator.InterrogatorFactory;
import org.firstopen.singularity.devicemgr.interrogator.InterrogatorIO;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.ShutdownManager;

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
public abstract class DeviceManagerAbstractImpl implements DeviceManager,
        InterrogatorIO {
    Logger log = Logger.getLogger(this.getClass());

    private enum rcAttributes {
        deviceProfileName, interrogatorIPAddress, interrogatorPort, linespeed, interrogatorDriver, Reader, Sensor
    };

    private List<Interrogator> interrogators = Collections
            .synchronizedList(new ArrayList<Interrogator>());

    private List<Reader> readers = Collections
            .synchronizedList(new ArrayList<Reader>());

    private Hashtable ecSpecHash = new Hashtable();

    protected String deviceManagerID = null;
    
    protected String geocoord = null;
    
    /**
     * @return Returns the geocoord.
     */
    public String getGeocoord() {
        return geocoord;
    }

    /**
     * @param geocoord The geocoord to set.
     */
    public void setGeocoord(String geocoord) {
        this.geocoord = geocoord;
    }

    // example method declared in interface class
    public void ping() throws RemoteException {

    }

    protected DeviceManagerAbstractImpl() {
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
     * @return
     * @throws Exception
     */
    
    protected void getConfiguration() throws Exception {
        // getResource at the momment hard-wire for now.
        URL url = ClassLoader.getSystemResource("physical_readers.properties");

        URLConnection site = url.openConnection();
        InputStream is = site.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader bufferReader;
        bufferReader = new BufferedReader(isr);
        HashMap<String, String[]> physicalReaderHash = new HashMap<String, String[]>();

        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                if (line.startsWith("#"))
                    continue;

                log.debug("ALE physicalReader attributes = " + line);
                StringTokenizer sTokenizer = new StringTokenizer(line);

                int i = 0;
                String deviceProfileId = "";
                String[] pReaderAttributes = new String[rcAttributes.values().length];

                while (sTokenizer.hasMoreTokens()) {
                    String token = sTokenizer.nextToken();
                    if (i == 0)
                        deviceProfileId = token;
                    else
                        pReaderAttributes[i - 1] = token;
                    i++;
                }// end while

                physicalReaderHash.put(deviceProfileId, pReaderAttributes);

                DeviceProfile deviceProfile = new DeviceProfile(
                        deviceProfileId);

                deviceProfile
                        .setName(pReaderAttributes[rcAttributes.deviceProfileName
                                .ordinal()]);
                deviceProfile
                        .setIpAddress(pReaderAttributes[rcAttributes.interrogatorIPAddress
                                .ordinal()]);
                deviceProfile
                        .setPort(pReaderAttributes[rcAttributes.interrogatorPort
                                .ordinal()]);

                deviceProfile
                        .setBaud(pReaderAttributes[rcAttributes.linespeed
                                .ordinal()]);

                deviceProfile
                        .setInterrogatorClassName(pReaderAttributes[rcAttributes.interrogatorDriver
                                        .ordinal()]);

                Set<Sensor> sensors = new HashSet<Sensor>();

                sensors.add(new Sensor(pReaderAttributes[rcAttributes.Sensor
                        .ordinal()]));

                deviceProfile.setSensorSet(sensors);

                Interrogator interrogator = InterrogatorFactory
                        .create(deviceProfile);

                // find all readers for these sensors, for now only one
                Reader reader = new Reader(deviceProfileId,
                        pReaderAttributes[rcAttributes.Reader.ordinal()]);

                // reader will have a set of sensors, just use all sensors for
                // reader component for now
                reader.setSensors(sensors);

                // add reader to the list of readers this ConfigManager is
                // servicing
                readers.add(reader);

                // add interrogator to the list of interrogators this Device
                // Manager is servicing
                interrogators.add(interrogator);

                interrogator.setInterrogatorIO(this);
            }
        } catch (IOException e) {
            log.error("unable to read properties stream", e);
            throw new Exception("unable to read properties stream");
        }// end while
    }// end initProperties

    protected void getConfigurationRemote() throws Exception {

        // getResource at the momment hard-wire for now.
        InputStream is = ClassLoader.getSystemResourceAsStream("id.properties");
        Properties properties = new Properties();

        long timeout = 15000;
        
        try {
            properties.load(is);
            deviceManagerID = properties.getProperty("id.devicemgr.name");
            geocoord = properties.getProperty("id.devicemgr.geocoord");
            log.info("Device Manager requesting ConfigManager..");
            ConfigManager configManager = (ConfigManager) ServiceLocator.getService(
                    ConfigManager.class, "ConfigManager", timeout);
            log.info("Retrieving DeviceProfiles Config");
            List<DeviceProfile> physicalReaderList = configManager.getConfig(deviceManagerID);
            log.info("DeviceProfiles Config successfully retrieved");
            for (DeviceProfile deviceProfile : physicalReaderList) {

                Interrogator interrogator = InterrogatorFactory
                        .create(deviceProfile);

                // add interrogator to the list of interrogators this Device
                // Manager is servicing
                interrogators.add(interrogator);

                interrogator.setInterrogatorIO(this);
            }
        } catch (IOException e) {
            log.error("unable to read properties stream", e);
            throw new Exception("unable to read properties stream");
        }// end while
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

        log.info("Device Manager initializing...");
        getConfigurationRemote();

      
        log.info("Device Manager Started...");
    }

    public void sendEvent(ReaderEvent event) throws Exception {

        Enumeration ecSpecEnum = ecSpecHash.keys();
        while (ecSpecEnum.hasMoreElements()) {

            String ecSpecName = (String) ecSpecEnum.nextElement();

            DeviceECSpec deviceECSpec = (DeviceECSpec) ecSpecHash
                    .get(ecSpecName);

            log.debug(System.currentTimeMillis() + ":"
                    + deviceECSpec.getEndTime());
            /*
             * if event cycle is not complete just store the event
             */
            if (deviceECSpec.getReaders().contains(event.getReaderName())){
                deviceECSpec.addEvent(event);    
            }
            

            if (System.currentTimeMillis() > deviceECSpec.getEndTime()) {
                deviceECSpec.CompleteEventCycle(this);

            }
        }

    }

    public void registerDeviceECSpec(DeviceECSpec deviceECSpec)
            throws RemoteException, Exception {

        /*
         * Calc the end time now, since the DM just received it.
         * 
         */
        deviceECSpec.calcEndTime();

        ecSpecHash.put(deviceECSpec.getSpecName(), deviceECSpec);
        /*
         * spin up the interrogators
         */
        try {

            Iterator<Interrogator> iterator = interrogators.iterator();
            while (iterator.hasNext()) {
                iterator.next().on();
            }
        } catch (Exception e) {
            String message = "unable to start interrogators";
            log.error(message, e);
            throw new Exception(message, e);
        }

    }

    public void unRegisterDeviceECSpec(String deviceECSpecName)
            throws RemoteException, Exception {
        ecSpecHash.remove(deviceECSpecName);
        System.gc();

        log.debug("deviceECSpec: " + deviceECSpecName + " removed");

        // stop interrogators
        if (ecSpecHash.size() <= 0) {
            try {
                log.debug("no more ecspecs, stop the interrogators");

                Iterator<Interrogator> iterator = interrogators.iterator();
                while (iterator.hasNext()) {
                    iterator.next().off();
                }
            } catch (Exception e) {
                String message = "unable to stop interrogators";
                log.error(message, e);
                throw new Exception(message, e);
            }
        }

    }

    /**
     * @return Returns the readers.
     */
    public List<Reader> getReaders() {
        return readers;
    }

    /**
     * @param readers
     *            The readers to set.
     */
    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

    /**
     * @return Returns the interrogators.
     */
    public List<Interrogator> getInterrogators() {
        return interrogators;
    }

    /**
     * @param interrogators
     *            The interrogators to set.
     */
    public void setInterrogators(List<Interrogator> interrogators) {
        this.interrogators = interrogators;
    }

    /**
     * 
     * Implements the physical transport of the information based on chosen
     * distributed component selection.
     * 
     */
    public abstract void sendRemoteEvent(DeviceECSpec ecSpecProfile);

}
