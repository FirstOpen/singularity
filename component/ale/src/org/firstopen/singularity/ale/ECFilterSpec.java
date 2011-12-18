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

import org.apache.log4j.Logger;

public class ECFilterSpec {
	public static final String INCLUDE_PATTERNS = "includePatterns";

	public static final String EXCLUDE_PATTERNS = "excludePatterns";

	private List includePatterns; // List of EPC patterns

	private List excludePatterns; // List of EPC patterns

	Logger log = null;

	public ECFilterSpec(List includePatterns, List excludePatterns) {
		this.includePatterns = includePatterns;
		this.excludePatterns = excludePatterns;
		log = Logger.getLogger(this.getClass());
	}

	public List getIncludePatterns() {
		return includePatterns;
	}

	public void setIncludePatterns(List x) {
		includePatterns = x;
	}

	public void addIncludePattern(String x) {
		includePatterns.add(x);
		log.debug("added includePattern = " + x);
	}

	public void deleteIncludePattern(String x) {
		includePatterns.remove(x);
	}

	public List getExcludePatterns() {
		return excludePatterns;
	}

	public void setExcludePatterns(List x) {
		excludePatterns = x;
	}

	public void addExcludePattern(String x) {
		log.debug("added excludePattern = " + x);
		excludePatterns.add(x);
	}

	public void deleteExcludePattern(String x) {
		excludePatterns.remove(x);
	}
}
