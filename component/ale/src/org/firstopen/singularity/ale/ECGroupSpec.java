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

import java.util.List;

/*
 * ECGroupSpec: specifies how the filtered EPCs are grouped together for
 * reporting.
 */
public class ECGroupSpec {
	private List patternList; // pattern URIs

	public ECGroupSpec(List patternList) {
		this.patternList = patternList;
	}

	public List getPatternList() {
		return patternList;
	}

	public void setPatternList(List x) {
		patternList = x;
	}

	public void addPattern(String x) {
		patternList.add(x);
	}

	public void deletePattern(String x) {
		patternList.remove(x);
	}
}
