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

package org.firstopen.singularity.ale;

import java.io.Serializable;

/**
 * 
 * @author Tom Rose (tom.rose@i-konect.com) 
 * @version $Id: TimerInfo.java 776 2005-10-18 17:31:07Z TomRose $
 *
 */
public class TimerInfo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7657182110770343839L;
	
	private String name = null;
	
	
	/**
	 * @param name
	 * @param duration
	 */
	public TimerInfo(String name) {
		super();
	
		this.name = name;
	}

	/**
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

	}