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
import java.util.Date;
import java.util.List;

public class ECReports implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 52136121353270691L;

	private String specName;

	private Date date;

	private String ALEID;

	private long totalMilliseconds = 0L;

	private ECTerminationCondition terminationCondition;

	private ECSpec spec;

	private List reports;

	public ECReports(String specName) {
		this.specName = specName;
		reports = new ArrayList();
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String x) {
		specName = x;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date x) {
		date = x;
	}

	public String getALEID() {
		return ALEID;
	}

	public void setALEID(String x) {
		ALEID = x;
	}

	public long getTotalMilliseconds() {
		return totalMilliseconds;
	}

	public void setTotalMilliseconds(long x) {
		totalMilliseconds = x;
	}

	public ECTerminationCondition getTerminationCondition() {
		return terminationCondition;
	}

	public void setTerminationCondition(ECTerminationCondition x) {
		terminationCondition = x;
	}

	public List getReports() {
		return reports;
	}

	public void setReports(List x) {
		reports = x;
	}

	public void addReport(ECReport x) {
		reports.add(x);
	}

	public ECSpec getSpec() {
		return spec;
	}

	public void setSpec(ECSpec x) {
		spec = x;
	}
}
