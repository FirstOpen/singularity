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

package org.firstopen.singularity.admin.view;

import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionBean extends BaseBean {

	String username;
	String password;
	String zoneurl="/images/green-light.png";
	Log log = LogFactory.getLog(this.getClass());
	boolean flip = true;

	
	public void changeLight(ActionEvent e){
		if (flip){
		   setZoneurl("/images/green-dark.png");
		   flip=false;
		}
		else {
			   setZoneurl("/images/green-light.png");
			   flip=true;
		}
	    log.debug("url is: a" + getZoneurl());
	    System.out.print("tesT");
		 
	}
	public String getZoneurl() {
		return zoneurl;
	}


	public void setZoneurl(String zoneurl) {
		this.zoneurl = zoneurl;
	}

	/**
	 *
	 * 
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *
	 * 
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public SessionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
