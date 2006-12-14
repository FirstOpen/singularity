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

package org.firstopen.singularity.system;

/**
 * Object does not have an expected method or field. Business objects need to
 * have published set methods for each of its member fields and get method for
 * the key field specified.
 * 
 * @author TomJoseph
 */
public class BadObjectType extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6737579166964127942L;

	/**
	 * Construct BadObjectType exception with specified message.
	 * 
	 * @param aMsg
	 *            exception message
	 */
	public BadObjectType(String msg) {
		super(msg);
	}

	/**
	 * Construct BadObjectType exception with default message.
	 */
	public BadObjectType() {
		this("bad object type");
	}
}
