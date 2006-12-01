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

/*
 * ECReportSetSpec Denotes what set of EPCs is to be considered for filtering
 * and output: 1. All EPCs read in the current event cycle 2. additions from the
 * previous event cycle 3. deletions from the previous event cycle
 */
public class ECReportSetSpec {
	public static final String CURRENT = "current";

	public static final String ADDITIONS = "additions";

	public static final String DELETIONS = "deletions";
}
