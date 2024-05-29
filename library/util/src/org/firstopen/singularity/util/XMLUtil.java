/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

package org.firstopen.singularity.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * Convert ot using JAXB where approrpriate
 * 
 * @deprecated replace with XMLBeans
 * 
 */
public class XMLUtil {
	public static final SimpleDateFormat sdfObj = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	static Class foDocClass = null;

	static Class docClass = null;

	static Log log = LogFactory.getLog(XMLUtil.class);

	static {
		try {
			foDocClass = Class.forName("org.apache.xerces.dom.DocumentImpl");
			docClass = Class.forName("org.apache.xerces.dom.DocumentImpl");
			
		} catch (java.lang.ClassNotFoundException x) {
			log.error(x);
		}
	}

	public static InputSource createInputSourceFromDocument(Document doc)
			throws Exception {
		StringWriter sWriter = null;
		try {
			OutputFormat format = new OutputFormat(doc, "UTF-8", true);
			sWriter = new StringWriter();
			XMLSerializer xmlSerializer = new XMLSerializer(sWriter, format);
			xmlSerializer.setNamespaces(true);
			xmlSerializer.serialize(doc);

			InputSource iSource = new InputSource(new StringReader(sWriter
					.toString()));
			return iSource;
		} finally {
			if (sWriter != null)
				sWriter.close();
		}
	}

	public static void createElementAndAppend(String name, int value,
			Document doc, Element appendeeElement, String attributeName,
			String attributeValue) {
		Element newElement = doc.createElementNS("urn:epcglobal:ale:xsd:1",name);
		Text text = doc.createTextNode(String.valueOf(value));
		newElement.appendChild(text);
		if (attributeName != null && !attributeName.equals("")) {
			newElement.setAttribute(attributeName, attributeValue);
		}
		appendeeElement.appendChild(newElement);
	}

	public static void createElementAndAppend(String name, double value,
			Document doc, Element appendeeElement, String attributeName,
			String attributeValue) {
		Element newElement = doc.createElementNS("urn:epcglobal:ale:xsd:1",name);
		Text text = doc.createTextNode(String.valueOf(value));
		newElement.appendChild(text);
		if (attributeName != null && !attributeName.equals("")) {
			newElement.setAttribute(attributeName, attributeValue);
		}
		appendeeElement.appendChild(newElement);
	}

	public static void createElementAndAppend(String name, Date value,
			Document doc, Element appendeeElement, String attributeName,
			String attributeValue) {
		Element newElement = null;
		if (value == null) {

			log.debug("XMLUtil.createElementAndAppend()  value == null for id = "
							+ name);
			newElement = doc.createElementNS("urn:epcglobal:ale:xsd:1",name);
			Text text = doc.createTextNode("");
			newElement.appendChild(text);
		} else {
			newElement = doc.createElementNS("urn:epcglobal:ale:xsd:1",name);
			Text text = doc.createTextNode(sdfObj.format(value));
			newElement.appendChild(text);
		}
		if (attributeName != null && !attributeName.equals("")) {
			newElement.setAttribute(attributeName, attributeValue);
		}
		appendeeElement.appendChild(newElement);
	}

	public static Element createElementAndAppend(String name, String value,
			Document doc, Element appendeeElement, String attributeName,
			String attributeValue) {
		if (value == null || value.equals("")) {
			value = "";
		}
		Element newElement = doc.createElementNS("urn:epcglobal:ale:xsd:1",name);
		Text text = doc.createTextNode(value);
		newElement.appendChild(text);
		if (attributeName != null && !attributeName.equals("")) {
			newElement.setAttribute(attributeName, attributeValue);
		}

		appendeeElement.appendChild(newElement);
		
		return newElement;
	}

	/**
	 * createXMLDoc(String) Given a id for the root element will create and
	 * return a org.w3c.dom.Document with the root node specified. The Document
	 * will be based on the Document implementation as specified in the system
	 * properties file XML_DOCUMENT_CLASS
	 * 
	 * @param String
	 *            rootName
	 * @return Document
	 * @author Peter Manta (after blatant copying of Jeff)
	 */
	public static org.w3c.dom.Document createXMLDoc(String rootName) {
		log.debug("XMLUtil.createXMLDoc starting");
		String docClassName = null;
		Class docClass = null;
		Document doc = null;
		Element root = null;
		try {
			docClass = Class.forName(docClassName);
			doc = (Document) docClass.newInstance();
			root = doc.createElementNS("urn:epcglobal:ale:xsd:1",rootName);
			doc.appendChild(root);
		} catch (Exception e) {
			log.error(e);
		} finally {
		}
		return doc;
	}// createXMLDoc

	public static void printDocumentToFile(Document doc, String fileName) {
		StringWriter xmlWriter = new StringWriter();
		try {
			OutputFormat format = new OutputFormat(doc, "UTF-8", true);
			

			XMLSerializer xmlSerializer = new XMLSerializer(xmlWriter, format);
			xmlSerializer.setNamespaces(true);
			xmlSerializer.serialize(doc);
			log.debug(xmlWriter.getBuffer());
		} catch (Exception x) {
			log.error("unable to log document", x);
		}
	}

	public static String generateXMLFromDoc(Document doc) {

		String XML_VERSION = "1.0";
		String XML_ENCODING = "UTF-8";
		String xml = null;
		StringWriter strWriter = null;
		XMLSerializer probeMsgSerializer = null;
		OutputFormat outFormat = null;

		try {
			probeMsgSerializer = new XMLSerializer();
			probeMsgSerializer.setNamespaces(true);
			strWriter = new StringWriter();
			outFormat = new OutputFormat();
			

			// Setup format settings
			outFormat.setEncoding(XML_ENCODING);
			outFormat.setVersion(XML_VERSION);
			outFormat.setIndenting(true);
			outFormat.setIndent(4);
			
			// Define a Writer
			probeMsgSerializer.setOutputCharStream(strWriter);

			// Apply the format settings
			probeMsgSerializer.setOutputFormat(outFormat);

			// Serialize XML Document
			probeMsgSerializer.serialize(doc);
			xml = strWriter.toString();
			strWriter.close();

		} catch (IOException ioEx) {
			log.error("Error ", ioEx);
		}

		return xml;
	}





	public static Document generateDocFromXML(String xml)

	{

		InputSource inputSource = new InputSource(new StringReader(xml));
		DOMParser parser = new DOMParser();
		try {
			parser.parse(inputSource);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			log.error("exception to do: ", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("exception to do: ", e);
		}

		return parser.getDocument();
	}

}
