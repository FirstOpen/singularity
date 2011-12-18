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

/**
 * @hibernate.class table="ECTime"
 */
public class ECTime {
	private Integer timeId;

	private long duration = 0L;

	private String unit = ECTimeUnit.MS;

	// No-Arg constructor required by Hibernate
	public ECTime() {
	}

	/**
	 * @hibernate.id generator-class="native"
	 */
	public Integer getTimeId() {
		return timeId;
	}

	public void setTimeId(Integer x) {
		timeId = x;
	}

	/**
	 * @hibernate.property
	 */
	public long getDuration() {
		return duration;
	}

	public void setDuration(long x) {
		duration = x;
	}

	public void setDuration(String x) throws NumberFormatException {
		duration = Long.parseLong(x);
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String x) {
		unit = x;
	}
}
