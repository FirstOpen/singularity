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

import javax.faces.context.FacesContext;



public class AuthenticationBean extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8929625053062199775L;
	
	private String username = null;;
	private String password = null;
	private String authUsername = username;
	
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

	public AuthenticationBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public void login(){
		
	}

	public String getAuthUsername() {
		if (this.authUsername == null)
			this.authUsername = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
		return authUsername;
	}

	public void setAuthUsername(String authUsername) {
		
		this.authUsername = authUsername;
	}

}
