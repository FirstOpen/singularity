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

public class ECReportOutputSpec {
	public static final String LIST = "includeList";

	public static final String COUNT = "includeCount";

	private boolean includeList = false;

	private boolean includeCount = false;

	public boolean getIncludeList() {
		return includeList;
	}

	public void setIncludeList(boolean x) {
		includeList = x;
	}

	public boolean getIncludeCount() {
		return includeCount;
	}

	public void setIncludeCount(boolean x) {
		includeCount = x;
	}
}
