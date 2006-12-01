/*
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

package org.firstopen.singularity.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.util.MapSet;
import org.firstopen.singularity.util.Named;

/**
 * 
 * Physical Reader Component, a reader is a device that reads some form of
 * sensor (Barcode, RFID, One-Wire, etc.). Each Physical Reader Component may
 * have multiple readerSet or streams of sensor events (such as multiple Antenna
 * for an RFID Reader).
 * 
 * A Reader is an acutal event stream source for sensor events, and it may be
 * comprised of one or more Sensors.
 * 
 * 
 * 
 * @author Tom Rose
 * 
 * @hibernate.class table="DeviceProfile" lazy="false"
 */
public class DeviceProfile implements Named, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1719934567854267686L;

    private Set<Sensor> sensorSet = new HashSet<Sensor>();

    private Set<Reader> readerSet = new HashSet<Reader>();

    private MapSet<String, Sensor> sensorMapSet = new MapSet<String, Sensor>();

    private MapSet<String, Reader> readerMapSet = new MapSet<String, Reader>();

    private String id = null;

    private String ipAddress = "127.0.0.1";

    private String port = "4000";

    private String baud = null;

    private String manufacturer = "AWID";

    private String vendor = "www.awid.com";

    private String version = "1.0";

    private String model = "MPR2010";

    private String serialNumber = "123456789";

    private String name = "AWID_DOCKDOOR_ONE";

    private String deviceProfileID = "MPR2010_AWID_02";

    private String deviceManagerID = "DeviceManager_01";

    public enum RCTYPE {
        RC_CLASS, RC_OBJECT
    };

    private RCTYPE type = RCTYPE.RC_OBJECT;

    /**
     * Intterogator Class to instantiate
     */
    private String interrogatorClassName = "org.firstopen.singularity.devicemgr.interrogator.MPR2010_AWID_IO";

    /**
     * 
     */
    public DeviceProfile() {
        super();
    }

    public DeviceProfile(String name) {
        this();
        this.deviceProfileID = name;
    }

    /**
     * @hibernate.property
     * @return Returns the manufacturer.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer
     *            The manufacturer to set.
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the model.
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model
     *            The model to set.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the id.
     */
    public String getName() {
        return name;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the serialNumber.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber
     *            The serialNumber to set.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the vendor.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * 
     * @param vendor
     *            The vendor to set.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public void setSensorSet(Set<Sensor> sensors) {
        sensorMapSet.clear();
        for (Sensor sensor : sensors) {
            addSensor(sensor);
        }
    }

    /**
     * @hibernate.id generator-class="uuid.hex" length="128"
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @hibernate.property
     * @return
     */
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @hibernate.property
     * 
     * @return
     */
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the interrogatorClass.
     */
    public String getInterrogatorClassName() {
        return interrogatorClassName;
    }

    /**
     * @param interrogatorClass
     *            The interrogatorClass to set.
     * 
     * @throws ClassNotFoundException
     */
    public void setInterrogatorClassName(String classname) {
        this.interrogatorClassName = classname;

    }

    /**
     * @hibernate.property
     * 
     * @return Returns the deviceProfileID.
     */
    public String getDeviceProfileID() {
        return deviceProfileID;
    }

    /**
     * @param deviceProfileID
     *            The deviceProfileID to set.
     */
    public void setDeviceProfileID(String deviceProfileID) {
        this.deviceProfileID = deviceProfileID;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the baud.
     */
    public String getBaud() {
        return baud;
    }

    /**
     * @param baud
     *            The baud to set.
     */
    public void setBaud(String baud) {
        this.baud = baud;
    }

    /**
     * The type defines if this is an Object or Class template the class
     * template allows a specific class of DeviceProfile to be defined and
     * applied to a specific instance of a RC of that Class.
     * 
     * @return Returns the type.
     */
    public RCTYPE getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(RCTYPE type) {
        this.type = type;
    }

    /**
     * Clear the contents.
     * 
     */
    public void clear() {

    }

    /**
     * @hibernate.property
     * @return Returns the deviceManagerID.
     */
    public String getDeviceManagerID() {
        return deviceManagerID;
    }

    /**
     * @param deviceManagerID
     *            The deviceManagerID to set.
     */
    public void setDeviceManagerID(String deviceManagerID) {
        this.deviceManagerID = deviceManagerID;
    }

    /**
     * 
     * @hibernate.set inverse="false" lazy="false" cascade="all"
     * @hibernate.collection-key column="device_profile_id"
     * @hibernate.collection-one-to-many class="org.firstopen.singularity.system.Sensor"
     * 
     * @return Returns the sensorSet.
     */
    public Set<Sensor> getSensorSet() {
        sensorSet.clear();
        sensorSet.addAll(sensorMapSet.values());
        return sensorSet;
    }

    /**
     * 
     * @param sensor
     */
    public void addSensor(Sensor sensor) {
        sensorMapSet.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        removeSensor(sensor.getName());
    }

    public void removeSensor(String sensorName) {
        sensorMapSet.delete(sensorName);
    }

    public Sensor getSensor(String sensorName) {
        return sensorMapSet.get(sensorName);
    }

    /**
     * @hibernate.set inverse="false" lazy="false" cascade="all"
     * @hibernate.collection-key column="device_profile_id"
     * @hibernate.collection-one-to-many class="org.firstopen.singularity.system.Reader"
     */
    public Set<Reader> getReaderSet() {
        readerSet.clear();
        readerSet.addAll(readerMapSet.values());
        return readerSet;
    }

    public void setReaderSet(Set<Reader> readers) {
        readerMapSet.clear();
        for (Reader reader : readers) {
            addReader(reader);
        }
    }

    /**
     * 
     * @param sensor
     */
    public void addReader(Reader reader) {
        readerMapSet.put(reader.getName(), reader);
    }

    public void removeReader(Reader reader) {
        removeReader(reader.getName());
    }

    public void removeReader(String readerName) {
        readerMapSet.delete(readerName);
    }

    public Reader getReader(String readerName) {
        return readerMapSet.get(readerName);
    }



}
