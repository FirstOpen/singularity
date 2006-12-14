/*
 * Copyright 2005 Wavechain Consulting LLC
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

package org.firstopen.singularity.business.inventory;

import java.io.StringReader;

import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class InventorySynchBean implements
		javax.ejb.MessageDrivenBean, javax.jms.MessageListener,
		org.xml.sax.ContentHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3340823785078394144L;

	MessageDrivenContext ejbContext = null;

	InitialContext jndiContext = null;

	StringBuffer charArray = new StringBuffer();

	int length = 0;

	Logger log = null;

	public static final String COUNT_REPORT = "countReport";

	public static final String COUNT = "count";

	public static final String STOCK_ID = "epc";

	public static final String DELIMITER = "urn:epc:tag:gid-64-i:";

	public static final String DELETION = "DELETION";

	InventorySLSBRemote iSLSB = null;

	String locCode = null;

	String reportName = null;

	Double additionDouble = new Double(1.0);

	Double deletionDouble = new Double(-1.0);

	public void ejbCreate() {
	}

	public void setMessageDrivenContext(MessageDrivenContext ctx) {
		try {
			log = Logger.getLogger(getClass());
			log.debug("setMessageDrivenContext()");
			jndiContext = new InitialContext();

			InventorySLSBHome iSLSBHome = (InventorySLSBHome) jndiContext
					.lookup("ejb/inventory/InventorySLSB");
			iSLSB = iSLSBHome.create();
		} catch (Exception x) {
			log.debug("setMessageDrivenContext() Exception = " + x);
		}
		ejbContext = ctx;
	}

	public void onMessage(Message message) {
		log.debug("onMessage()");
		InputSource inSource = null;
		StringReader stringReader = null;
		try {
			/*
			 * if(System.getSecurityManager() == null) { log.debug("onMessage()
			 * Instantiating new RMISecurityManager");
			 * System.setSecurityManager(new RMISecurityManager()); }
			 */

			if (message instanceof TextMessage) {
				stringReader = new StringReader(((TextMessage) message)
						.getText());
				inSource = new InputSource(stringReader);
				XMLReader xmlReaderObj = XMLReaderFactory
						.createXMLReader("org.apache.xerces.parsers.SAXParser");
				xmlReaderObj.setContentHandler(this);
				xmlReaderObj.setFeature(
						"http://xml.org/sax/features/namespaces", true);
				xmlReaderObj
						.setFeature(
								"http://xml.org/sax/features/namespace-prefixes",
								false);
				xmlReaderObj.setFeature(
						"http://apache.org/xml/features/validation/schema",
						false);
				log.debug("onMessage()  About to parse");
				xmlReaderObj.parse(inSource);
			} else {
				message.clearBody();
			}
		} catch (Exception x) {
			x.printStackTrace();
			log.debug("onMessage():  Exception creating thread! Exception = "
					+ x);
		} finally {
			try {
				if (stringReader != null)
					stringReader.close();
			} catch (Exception x) {
				log.debug("could not close a stream");
			}
		}
	}

	public void ejbRemove() {
		log.debug("ejbRemove()");
		try {
			jndiContext.close();
			ejbContext = null;
		} catch (NamingException x) {
			log.debug("ejbRemove()  NamingException = " + x);
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
		// log.debug("startElement: localName = " +localName);
		if (localName.equals("listReport")) {
			// log.debug("startElement: attributes = "+attrs.getLength());
			log.debug("startElement: attribute value = " + attrs.getValue(0));
			if (attrs.getValue(0) != null)
				reportName = attrs.getValue(0);
		}
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
			// log.debug("endElement: localName = "+localName+" value =
			// "+charArray.toString());

			if (localName.equals(COUNT))
				log.debug("Count = " + charArray.toString());
			else if (localName.equals(STOCK_ID)) {
				String stockId = charArray.toString();
				log.debug("stockId  = " + stockId);
				locCode = "AN1";

				int start = stockId.indexOf(DELIMITER) + 21;
				if (reportName.equals(DELETION))
					iSLSB.updateInventory(locCode, stockId.substring(start),
							deletionDouble, new Integer(2));
				else
					iSLSB.updateInventory(locCode, stockId.substring(start),
							additionDouble, new Integer(2));
			}

		} catch (Exception x) {
			log.debug("endElement()  Exception = " + x);
		} finally {
			int l = charArray.length();
			charArray.delete(0, l);
		}
	}

	public void endDocument() {
		log.debug("endDocument()");
	}
}
