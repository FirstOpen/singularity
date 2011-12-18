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
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Tag;

public class ECReportSpec {
	public final static String REPORT_NAME = "reportName";

	public final static String REPORT_SET = "reportSet";

	public final static String OUTPUT = "output";

	public final static String PATTERN = "pattern";

	public final static String TFORMAT_GID_96 = "gid-96";

	private Logger log = null;

	private String reportName;

	private String reportSet = ECReportSetSpec.CURRENT; // one of the constants

	// in ECReportSetSpec

	private ECFilterSpec filter;

	private ECGroupSpec group;

	private ECReportOutputSpec output = null;

	private boolean reportIfEmpty = false;

	private boolean reportOnlyOnChange = true;

	private Collection<Tag> currentFilteredEPCs;

	private Collection<Tag> previousFilteredEPCs;

	public ECReportSpec(String reportName, ECFilterSpec filter,
			ECGroupSpec group) {
		this.reportName = reportName;
		this.filter = filter;
		this.group = group;
		log = Logger.getLogger(this.getClass());
		group.getPatternList().add("");
	}

	public ECReport generateReport(List<ReaderEvent> events) throws Exception {
		log.debug("generateReport reportName = " + getReportName());
		ECReport report = new ECReport();
		report.setReportName(getReportName());
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			ReaderEvent event = (ReaderEvent) iter.next();
			Collection<Tag> filteredEPCs = filter(event.getTagIds());
			setCurrentFilteredEPCs(filteredEPCs);
			List patternList = group.getPatternList();
			Iterator patternListIterator = patternList.iterator();
			log.debug("generateReport() " + getReportName()
					+ " has the following # of patterns : "
					+ patternList.size());
			while (patternListIterator.hasNext()) {
				String epcPattern = (String) patternListIterator.next();
				ECReportGroup reportGroup = new ECReportGroup();
				reportGroup.setGroupName(epcPattern);
				if (output.getIncludeList())
					reportGroup
							.setGroupList(new ECReportGroupList(filteredEPCs));
				else if (output.getIncludeCount())
					reportGroup.setGroupCount(new ECReportGroupCount(
							filteredEPCs));

				report.addGroup(reportGroup);
			}
		}
		return report;
	}

	public Collection<Tag> filter(Collection<Tag> rawEPCData) throws Exception {
		log.debug("filter reportName = " + getReportName()
				+ " # of rawEPCTags = " + rawEPCData.size());
		Collection<Tag> filteredEPCs = new ArrayList<Tag>();
		List includePatterns = this.filter.getIncludePatterns();
		List excludePatterns = this.filter.getExcludePatterns();

		Iterator rawEPCDataIterator = rawEPCData.iterator();
		while (rawEPCDataIterator.hasNext()) {
			Tag tag = (Tag) rawEPCDataIterator.next();
			boolean excludeMatch = false;
			Iterator excludePatternsIterator = excludePatterns.iterator();
			while (excludePatternsIterator.hasNext()) {
				/*
				 * int[] excludePattern = (int[])excludePatternsIterator.next();
				 * excludeMatch = testForMatch(rawEpc , excludePattern);
				 * if(excludeMatch) rawEPCData.remove(rawEpc);
				 */
			}
			if (!excludeMatch && includePatterns.size() > 0) {
				/*
				 * Iterator includePatternsIterator =
				 * includePatterns.iterator();
				 * while(includePatternsIterator.hasNext()) { int[]
				 * includePattern = (int[])includePatternsIterator.next();
				 * boolean includeMatch = testForMatch(rawEpc , includePattern);
				 * filteredEPCs.add(rawEpc); }
				 */
			}
			filteredEPCs.add(tag);
		}

		log.debug("filter reportName = " + getReportName()
				+ " # of filteredEPCTags = " + filteredEPCs.size());
		return filteredEPCs;
	}

	private boolean testForMatch(String rawEpc, String pattern) {
		// log.debug("testForMatch() rawEpc = "+rawEpc+" pattern = "+pattern);
		boolean match = false;
		StringTokenizer tokenizer = new StringTokenizer(pattern, ":.");
		String tagFormat = (String) tokenizer.nextToken();
		String domainManager = (String) tokenizer.nextToken();
		String objectClass = (String) tokenizer.nextToken();
		String serialNumber = (String) tokenizer.nextToken();
		if (tagFormat.equals(TFORMAT_GID_96))
			match = testForGID_96Match(domainManager, objectClass,
					serialNumber, rawEpc);

		return match;
	}

	/*
	 * epc_type => GID-96 epc_manager => 7777777 epc_class => 666666 epc_serial =>
	 * 999999999
	 */
	private boolean testForGID_96Match(String domainManager,
			String objectClass, String serialNumber, String rawEPC) {
		log.debug("testForGID_96Match() domainManager = " + domainManager
				+ "  objectClass = " + objectClass + " serialNumber = "
				+ serialNumber + " rawEPC = " + rawEPC);
		int rawManager = Integer.parseInt(rawEPC.substring(0, 6));
		int rawClass = Integer.parseInt(rawEPC.substring(7, 12));
		int rawSerial = Integer.parseInt(rawEPC.substring(13, 22));

		/* test match for domain manager */
		if (domainManager.startsWith("*")) {
			// No match of domainManager required
		} else if (domainManager.startsWith("[")) {
			StringTokenizer tokenizer = new StringTokenizer(domainManager,
					"[]-");
			int low = Integer.parseInt(tokenizer.nextToken());
			int high = Integer.parseInt(tokenizer.nextToken());
			if (low > rawManager || rawManager > high)
				return false;
		} else {
			/* pattern must be an interger */
			if (rawManager != Integer.parseInt(domainManager))
				return false;
		}

		/* test match for object class */
		if (objectClass.startsWith("*")) {
			// No match of domainManager required
		} else if (objectClass.startsWith("[")) {
			StringTokenizer tokenizer = new StringTokenizer(objectClass, "[]-");
			int low = Integer.parseInt(tokenizer.nextToken());
			int high = Integer.parseInt(tokenizer.nextToken());
			if (low > rawClass || rawClass > high)
				return false;
		} else {
			/* pattern must be an interger */
			if (rawClass != Integer.parseInt(objectClass))
				return false;
		}

		/* test match for serial number */
		if (serialNumber.startsWith("*")) {
			// No match of domainManager required
		} else if (serialNumber.startsWith("[")) {
			StringTokenizer tokenizer = new StringTokenizer(serialNumber, "[]-");
			int low = Integer.parseInt(tokenizer.nextToken());
			int high = Integer.parseInt(tokenizer.nextToken());
			if (low > rawSerial || rawSerial > high)
				return false;
		} else {
			/* pattern must be an interger */
			if (rawSerial != Integer.parseInt(serialNumber))
				return false;
		}
		return true;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String x) {
		reportName = x;
	}

	public String getReportSet() {
		return reportSet;
	}

	public void setReportSet(String x) {
		reportSet = x;
	}

	public ECFilterSpec getFilter() {
		return filter;
	}

	public void setFilter(ECFilterSpec x) {
		filter = x;
	}

	public ECGroupSpec getGroup() {
		return group;
	}

	public void setGroup(ECGroupSpec x) {
		group = x;
	}

	public ECReportOutputSpec getOutput() {
		return output;
	}

	public void setOutput(ECReportOutputSpec x) {
		output = x;
	}

	public boolean getReportIfEmpty() {
		return reportIfEmpty;
	}

	public void setReportIfEmpty(boolean x) {
		reportIfEmpty = x;
	}

	public boolean getReportOnlyOnChange() {
		return reportOnlyOnChange;
	}

	public void setReportOnlyOnChange(boolean x) {
		reportOnlyOnChange = x;
	}

	public Collection getCurrentFilteredEPCs() {
		return currentFilteredEPCs;
	}

	public void setCurrentFilteredEPCs(Collection<Tag> x) {
		if (!getReportSet().equals(ECReportSetSpec.CURRENT))
			setPreviousFilteredEPCs(currentFilteredEPCs);
		currentFilteredEPCs = x;
	}

	public Collection<Tag> getPreviousFilteredEPCs() {
		return previousFilteredEPCs;
	}

	public void setPreviousFilteredEPCs(Collection<Tag> x) {
		previousFilteredEPCs = x;
	}
}
