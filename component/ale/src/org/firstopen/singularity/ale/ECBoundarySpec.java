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

import org.firstopen.singularity.ale.exception.ECSpecValidationException;

/**
 * @hibernate.class table="ECBoundarySpec"
 */
public class ECBoundarySpec {
	public final static String DURATION = "duration";

	public final static String REPEAT_PERIOD = "repeatPeriod";
	
	private Integer boundarySpecId;

	private ECTrigger startTrigger;

	private ECTime repeatPeriod;

	private ECTrigger stopTrigger;

	private ECTime duration;

	private ECTime stableSetInterval;

	// No-arg constructor required by Hibernate
	public ECBoundarySpec() {
	}

	/**
	 * @hibernate.id generator-class="native"
	 */
	public Integer getBoundarySpecId() {
		return boundarySpecId;
	}

	public void setBoundarySpecId(Integer x) {
		boundarySpecId = x;
	}

	public ECTrigger getStartTrigger() {
		return startTrigger;
	}

	public void setStartTrigger(ECTrigger x) throws ECSpecValidationException {
		/* As per 8.2.1 */
		if (getRepeatPeriod() != null)
			throw new ECSpecValidationException(
					"RepeatPeriod has already been specified.  Set repeatPeriod = null if startTrigger is to be specified");

		startTrigger = x;
	}

	/**
	 * @hibernate.many-to-one id="repeatPeriod"
	 *                        class="org.firstopen.singularity.ale.ECTime"
	 *                        unique="true" cascade="all"
	 */
	public ECTime getRepeatPeriod() {
		return repeatPeriod;
	}

	public void setRepeatPeriod(ECTime x) throws ECSpecValidationException {
		/* As per 8.2.1 */
		if (getStartTrigger() != null)
			throw new ECSpecValidationException(
					"startTrigger has already been specified.  Set startTrigger = null if repeatPeriod is to be specified");
		/*
		 * As per 8.2.1
		 */ 
		  if(x.getDuration() < 0) throw new
		     ECSpecValidationException("RepeatPeriod must be non-negative");
		  repeatPeriod = x;
		 
	}

	public ECTrigger getStopTrigger() {
		return stopTrigger;
	}

	public void setStopTrigger(ECTrigger x) {
		stopTrigger = x;
	}

	/**
	 * @hibernate.many-to-one id="duration"
	 *                        class="org.firstopen.singularity.ale.ECTime"
	 *                        unique="true" cascade="all"
	 */
	public ECTime getDuration() {
		return duration;
	}

	public void setDuration(ECTime x) throws ECSpecValidationException {
		/* As per 8.2.1 */
		if (x.getDuration() < 0)
			throw new ECSpecValidationException("Duration must be non-negative");
		duration = x;
	}

	/**
	 * @hibernate.many-to-one id="stableSetInterval"
	 *                        class="org.firstopen.singularity.ale.ECTime"
	 *                        unique="true" cascade="all"
	 */
	public ECTime getStableSetInterval() {
		return stableSetInterval;
	}

	public void setStableSetInterval(ECTime x) throws ECSpecValidationException {
		/*
		 * As per 8.2.1 if(x.getDuration() < 0) throw new
		 * ECSpecValidationException("StableSetInterval must be non-negative");
		 */
		stableSetInterval = x;
	}
}
