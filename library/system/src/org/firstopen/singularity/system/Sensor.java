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

import org.firstopen.singularity.util.Named;


/**
 * A Sensor is a phyiscal sensor and may only have one Reader associated with
 * it. Each Sensor must have a unique id to allow the Device Manager to
 * guarentee the many-to-one relationship between the sensor and reader(many
 * different sensors to one reader)
 *  
 * @author Tom Rose
 * 
 * @hibernate.class 
 *   table="Sensor"
 *    lazy="false"
 * 
 */
public class Sensor implements Named, Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 8491427063126072110L;

    private String id = null;

	private String name = null;
	
    private String deviceManagerId = null;
    
    
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
    /**
	 * 
	 */
	public Sensor() {
		super();
		
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
	 * Must assign a id at creation time
	 */
	public Sensor(String name){
	    this.name = name;
	}
	
	/**
	 * @hibernate.id
	 * generator-class="uuid.hex" length="128"
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}



	
}
