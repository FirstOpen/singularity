/*
 * Copyright 2005 Jeff Bride
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

package org.firstopen.singularity.ale.exception;

/**
 * 
 * @author Jeff Bride
 * 
 * @version $Id: ALEException.java 789 2005-10-19 18:41:24Z TomRose $
 *
 */
public abstract class ALEException extends java.lang.Exception {
		
	/**
	 * @param message
	 * @param cause
	 */
	public ALEException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param cause
	 */
	public ALEException(Throwable cause) {
		super(cause);
	
	}

	/**
	 * @param message
	 */
	public ALEException(String message) {
		super(message);
	}

	public ALEException() {
		super("LANSA ALE base exception");
	}

	
}
