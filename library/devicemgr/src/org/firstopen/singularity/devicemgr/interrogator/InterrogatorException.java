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
package org.firstopen.singularity.devicemgr.interrogator;

/**
 * 
 * @author Tom Rose
 * @version $Id: InterrogatorException.java 1242 2006-01-14 03:34:08Z TomRose $
 * @since 1.0
 *
 */
public class InterrogatorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3158602774638263518L;

	public InterrogatorException() {
		super();
		
	}

	public InterrogatorException(String message) {
		super(message);
		
	}

	public InterrogatorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InterrogatorException(Throwable cause) {
		super(cause);
		
	}

	
}
