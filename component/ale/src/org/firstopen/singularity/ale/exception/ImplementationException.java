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
 * @version $Id: ImplementationException.java 1243 2006-01-14 03:33:37Z TomRose $
 *
 */
public class ImplementationException extends ALEException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2027979739477661967L;

    public enum Severity { ERROR, SEVERE };
     
    private Severity severity = null;
    
	/**
	 * 
	 */
	public ImplementationException() {
		super("The Singularity ALE implementation is throwing an exception due to an unspecified problem");
		severity = Severity.ERROR;
	}

	public ImplementationException(String message) {
		super(message);
        severity = Severity.ERROR;
	}

    public ImplementationException(String message, Severity severity) {
        super(message);
        this.severity = severity;
    }

	/**
	 * @param message
	 * @param cause
	 */
	public ImplementationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ImplementationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

    /**
     * @return Returns the severity.
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * @param severity The severity to set.
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }


}
