/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

package org.firstopen.singularity.system;

import java.io.Serializable;
import java.util.Set;

import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.Named;

/**
 * A Reader is an acutal event stream source for sensor events, and it may be
 * comprised of one or more Sensors. However, each sensor will have one and only
 * one DeviceProfile. This will faciliate multiple sensors being combined to create 
 * a physical event stream. This is a bit different that logical readers, as thesse 
 * devices are combind at a much lower level (DeviceManager) as opposed to ALE Serivice.
 * This is a specific requirement for the ALE Specification.
 * 
 * @author TomRose
 * 
 * @hibernate.class 
 *   table="Reader"
 *   lazy="false"
 */
public class Reader implements Named, Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 4714619899284029107L;

    private Set<Sensor> sensors = null;

    private String id = null;
    
    private String name = null;

    private String deviceManagerId = null;
    

	public  Reader() {
		super();
	}
    /**
     * 
     * @param deviceManagerId
     * @param name
     * @throws Exception
     */
	public Reader(String deviceManagerId, String name) throws Exception {
		if (name == null || deviceManagerId == null )
			throw new InfrastructureException("Reader must have a name and device profile");
		this.name = name;
        this.deviceManagerId = deviceManagerId;
	}
	/**
	 * @hibernate.id 
	 * generator-class="uuid.hex" length="128"
	 * 
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
     * @hibernate.set
     *      inverse="false"
     *      lazy="false"
     *      cascade="all"
     * @hibernate.collection-key
     *      column="reader_id"     
	 * @hibernate.collection-one-to-many
	 *      class="org.firstopen.singularity.system.Sensor"
	 */
	public Set<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(Set<Sensor> sensors) {
		this.sensors = sensors;
	}
	/**
	 * @hibernate.property
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
    /**
     * @hibernate.property
     * 
     * @return Returns the deviceManagerId.
     */
    public String getDeviceManagerId() {
        return deviceManagerId;
    }
    /**
     * @param deviceManagerId The deviceManagerId to set.
     */
    public void setDeviceManagerId(String deviceManagerId) {
        this.deviceManagerId = deviceManagerId;
    }

	
}

