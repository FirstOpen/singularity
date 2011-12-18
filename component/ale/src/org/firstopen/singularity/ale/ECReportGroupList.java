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

package org.firstopen.singularity.ale;

import java.util.ArrayList;
import java.util.Collection;

public class ECReportGroupList implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699955514357165322L;
	private Collection members;

	public ECReportGroupList(Collection filteredEPCs) {
		members = new ArrayList();
		parseFilteredEPCsIntoGroups(filteredEPCs);
	}

	public void parseFilteredEPCsIntoGroups(Collection filteredEPCs) {
		try {
			setMembers(filteredEPCs);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public Collection getMembers() {
		return members;
	}

	public void setMembers(Collection x) {
		members = x;
	}
}
