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

public class InvalidURIException extends ALEException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3271021848083730722L;

	/**
	 * 
	 */
	public InvalidURIException() {
		super(
				"The URI specified for a subscriber cannot be parsed, does not id a scheme recognized by this implementation or violates rules imposed by a particular scheme");

	}

}
