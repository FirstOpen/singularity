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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.firstopen.singularity.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ECSpecBuilder implements org.xml.sax.ContentHandler {
	private Logger log = null;

	private Class docClass = null;

	ECSpec spec = null;

	Document specDoc = null;

	StringBuffer charArray = new StringBuffer();

	int length = 0;

	String specName = null;

	Set readers = new HashSet();

	ECBoundarySpec boundaries = new ECBoundarySpec();

	List ecReports = new ArrayList();

	/* variables needed for ECReportSpec */
	String reportName = null;

	String reportSet = null;

	boolean includeList = false;

	boolean includeCount = false;

	ECFilterSpec filter = new ECFilterSpec(new ArrayList(), new ArrayList());

	boolean parsingIncludePatterns = true;

	ECGroupSpec group = new ECGroupSpec(new ArrayList());

	String pattern = null;

	Attributes attributes = null;

	private String xml = null;

	public ECSpecBuilder(Document specDoc) {
		log = Logger.getLogger(getClass());
		this.specDoc = specDoc;
		this.xml = XMLUtil.generateXMLFromDoc(specDoc);
		XMLUtil.printDocumentToFile(specDoc, "ECSpec.xml");
	}

	public ECSpec buildECSpec() {

		try {
			log.debug("buildECSpec()  About to parse");
			XMLReader xmlReaderObj = XMLReaderFactory
					.createXMLReader("org.apache.xerces.parsers.SAXParser");
			xmlReaderObj.setContentHandler(this);
			xmlReaderObj.setFeature("http://xml.org/sax/features/namespaces",
					true);
			xmlReaderObj.setFeature(
					"http://xml.org/sax/features/namespace-prefixes", false);
			xmlReaderObj.setFeature(
					"http://apache.org/xml/features/validation/schema", false);

			InputSource iSource = XMLUtil
					.createInputSourceFromDocument(specDoc);
			xmlReaderObj.parse(iSource);
			return spec;
		} catch (Exception x) {
			x.printStackTrace();
			return null;
		}
	}

	public void startDocument() {
		log.debug("startDocument");
	}

	public void startPrefixMapping(java.lang.String prefix, String uri) {
		log.debug("startPrefixMapping()  ");
	}

	public void skippedEntity(java.lang.String name) {
		log.debug("skippedEntity()  id = " + name);
	}

	public void setDocumentLocator(org.xml.sax.Locator locator) {
		log.debug("setDocumentLocator()  locator = " + locator);
	}

	public void endPrefixMapping(java.lang.String prefix) {
		log.debug("endPrefixMapping()  prefix = " + prefix);
	}

	public void processingInstruction(java.lang.String target,
			java.lang.String data) {
		log.debug("processingInstruction() ");
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attrs) {
		// log.debug("startElement: localName = "+localName);
		if (localName.equals(ECFilterSpec.INCLUDE_PATTERNS))
			parsingIncludePatterns = true;
		if (localName.equals(ECFilterSpec.EXCLUDE_PATTERNS))
			parsingIncludePatterns = false;
		if (attrs.getLength() > 0) {
			for (int t = 0; t < attrs.getLength(); t++) {
				log.debug("attrs key = " + attrs.getLocalName(t) + " value = "
						+ attrs.getValue(t));
				if (attrs.getLocalName(t).equals(ECReportSpec.REPORT_NAME))
					reportName = attrs.getValue(t);
				else if (attrs.getLocalName(t).equals(ECReportOutputSpec.LIST)) {
					Boolean bool = new Boolean(attrs.getValue(t));
					includeList = bool.booleanValue();
				} else if (attrs.getLocalName(t).equals(
						ECReportOutputSpec.COUNT)) {
					Boolean bool = new Boolean(attrs.getValue(t));
					includeCount = bool.booleanValue();
				}
			}
		}

		attributes = attrs;
	}

	public void ignorableWhitespace(char buf[], int offset, int len) {
		log.debug("ignorableWhitespace: offset = " + offset + "; length = "
				+ len);
	}

	public void characters(char buf[], int offset, int len) {
		/*
		 * While parsing a java.io.Reader (as opposed to a java.io.InputStream),
		 * SAX parser calls this method when it encounters the '\n' character
		 * ... no good for me 'cause in doing so, my charArray StringBuffer will
		 * be over-written and populated with a '\n'.
		 */
		if (buf[offset] == '\n')
			return;

		charArray.append(buf, offset, len);
		length = len;
	}

	public void endElement(String nameSpaceURI, String localName, String qName) {
		try {
			log.debug("endElement: localName = " + localName + " value = "
					+ charArray.toString());
			if (localName.equals(ECSpec.SPEC_NAME))
				specName = (charArray.toString());
			else if (localName.equals(ECBoundarySpec.DURATION)) {
				ECTime time = new ECTime();
				time.setDuration(charArray.toString());
				boundaries.setDuration(time);
			}
			else if (localName.equals(ECBoundarySpec.REPEAT_PERIOD)) {
				ECTime time = new ECTime();
				time.setDuration(charArray.toString());
				boundaries.setRepeatPeriod(time);
			}
			else if (localName.equals(ECSpec.READER))
				readers.add(charArray.toString());
			else if (localName.equals(ECReportSpec.REPORT_SET))
				reportSet = attributes.getValue(0);
			else if (localName.equals(ECReportSpec.PATTERN)) {
				pattern = charArray.toString();
				if (parsingIncludePatterns)
					filter.addIncludePattern(pattern);
				else
					filter.addExcludePattern(pattern);
			} else if (localName.equals(ECSpec.REPORT_SPEC)) {
				if (reportName == null)
					log.error("ECSpecBuilder ..... No reportName specified!!");
				ECReportSpec rSpec = new ECReportSpec(reportName, filter, group);
				rSpec.setReportSet(reportSet);

				ECReportOutputSpec output = new ECReportOutputSpec();
				log.debug("output includeCount = " + includeCount
						+ " and includeList = " + includeList);
				output.setIncludeCount(includeCount);
				output.setIncludeList(includeList);
				rSpec.setOutput(output);

				ecReports.add(rSpec);

				filter = new ECFilterSpec(new ArrayList(), new ArrayList());
			}

			charArray.delete(0, charArray.length());
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public void endDocument() {
		log.debug("endDocument()  Will append root Elements to Document objects");

		if (specName == null)
			log.error("Please specify the ECSpec Name in the XML Document");
		else if (boundaries == null)
			log.error("Please specify the boundaries for the ECSpec Document");
		else {
			spec = new ECSpec(specName, boundaries);
			spec.setXml(this.xml);
			try {
				Iterator iterator = readers.iterator();
				while (iterator.hasNext()) {
					spec.addInterrogator((String) iterator.next());
				}

				iterator = ecReports.iterator();
				while (iterator.hasNext()) {
					spec.addReportSpec((ECReportSpec) iterator.next());
				}
			} catch (Exception x) {
				x.printStackTrace();
			}
		}
	}
}
