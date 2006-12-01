package com.refactored_networks.epc.epcnetwork;
/**
 * Copyright 2005 Refactored Networks, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
public class NoSuchIndexException extends java.lang.Exception {

/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257562897721996601L;

public
NoSuchIndexException() {
        super();
}

public
NoSuchIndexException(String s) {
        super(s);
}

public
NoSuchIndexException(Exception e) {
        super(e.getMessage());
}

}

	