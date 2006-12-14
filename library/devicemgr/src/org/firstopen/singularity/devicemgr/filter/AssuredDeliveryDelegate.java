/*
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.devicemgr.filter;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import org.apache.log4j.Logger;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.common.DeviceProtocol;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.JMSUtil;
import org.firstopen.singularity.util.StringUtil;
import org.firstopen.singularity.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author TomRose
 * 
 */
public class AssuredDeliveryDelegate extends EventDelegate {

	
    Logger log = Logger.getLogger(this.getClass());
	
	/* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.filter.ReaderProtocol#notify(net.jini.core.event.RemoteEvent)
     */
	public void notify(RemoteEvent theEvent) throws UnknownEventException,
			RemoteException {

		if (theEvent.getSource().getClass() == DeviceECSpec.class) {
			DeviceECSpec deviceECSpec = (DeviceECSpec) theEvent.getSource();

		    Document doc = generateReaderEventDocument(deviceECSpec.getEvents());

					
			JMSUtil.deliverMessageToQueue(deviceECSpec.getHost(),deviceECSpec.getQueueName(), doc);
			log.debug("Message Sent on -> "
					+ deviceECSpec.getQueueName());

			if (eventListener != null) eventListener.notify(theEvent);

		}

	}

	/**
	 * 
	 */
	public AssuredDeliveryDelegate() {
		super();
		
	}

	/**
	 * @param eventListener
	 */
	public AssuredDeliveryDelegate(RemoteEventListener eventListener) {
		super(eventListener);
		
	}

	/* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.filter.ReaderProtocol#generateDocFromListOfTags(org.w3c.dom.Document, org.w3c.dom.Element, java.util.Collection)
     */
	public  Element generateDocFromListOfTags(Document doc, Element root,
			Collection<Tag> aList) {

	
		Iterator<Tag> iterator = null;
		try {
			log.debug("generateDocFromListOfTags() ");
			iterator = aList.iterator();
			while (iterator.hasNext()) {
				Tag tag =  iterator.next();
			
				Element dataElement = XMLUtil.createElementAndAppend("tag", "", doc, root, null, null);
				dataElement.setAttribute("count", new String(
						tag.getCount() + ""));
				
				
				dataElement.setAttribute("binValue", new String(
						tag.getBinvalue()) + "");
				
				dataElement.setAttribute("value", new String(
						tag.getValue()) + "");

			}
			
		} catch (Exception x) {
			x.printStackTrace();
		} 
		return root;

	}
	/* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.filter.ReaderProtocol#generateDocFromListOfIntArrays(org.w3c.dom.Document, org.w3c.dom.Element, java.util.List)
     */
	public  Element generateDocFromListOfIntArrays(Document doc, Element root,
			List aList) {


	
		Iterator iterator = null;
		try {
			log.debug("generateXMLFromListOfIntArrays() ");
			iterator = aList.iterator();
			while (iterator.hasNext()) {
				int[] iArray = (int[]) iterator.next();
				XMLUtil.createElementAndAppend("data", StringUtil
						.intArrayToString(iArray, true), doc, root, null, null);
			}
			
		} catch (Exception x) {
			x.printStackTrace();
		} 
		return root;

	}
	/* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.filter.ReaderProtocol#generateDocFromListTag(org.w3c.dom.Document, org.w3c.dom.Element, java.util.List)
     */
	public  Element generateDocFromListTag(Document doc, Element root,
			List<Tag> aList) {


	
		Iterator iterator = null;
		try {
			log.debug("generateXMLFromListOfIntArrays() ");
			iterator = aList.iterator();
			while (iterator.hasNext()) {
				Tag tag = (Tag) iterator.next();
				XMLUtil.createElementAndAppend("data", tag.getValue() , doc, root, null, null);
			}
			
		} catch (Exception x) {
			x.printStackTrace();
		} 
		return root;

	}


	/* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.filter.ReaderProtocol#generateReaderEventDocument(java.util.Collection)
     */
	public  Document generateReaderEventDocument(Collection<ReaderEvent> events) {
		Class docClass = null;
		Document doc = null;
		Element root = null;
		PrintWriter xmlWriter = null;
		Iterator iterator = null;

		try {
			log.debug("generateReaderEventDocument() ");
			docClass = Class.forName("org.apache.xerces.dom.DocumentImpl");
			doc = (Document) docClass.newInstance();
			root = doc.createElementNS(DeviceProtocol.URN_SINGULARITY_ALE_XSD_1,DeviceProtocol.EVENT_CYCLE);
			doc.appendChild(root);

			iterator = events.iterator();
			while (iterator.hasNext()) {
				ReaderEvent readerEvent = (ReaderEvent) iterator.next();

				Element readerEventElement = doc.createElementNS(DeviceProtocol.URN_SINGULARITY_ALE_XSD_1,DeviceProtocol.READER_EVENT);

				readerEventElement.setAttribute(DeviceProtocol.SENSORID, readerEvent
						.getSensor().getName());

				readerEventElement.setAttribute(DeviceProtocol.TIMESTAMP, new String(
						readerEvent.getTimestamp() + ""));

				readerEventElement.setAttribute(DeviceProtocol.READERID, new String(
						readerEvent.getReaderName() + ""));
                
                readerEventElement.setAttribute(DeviceProtocol.GEOCOORD, new String(
                        readerEvent.getGeocoord() + ""));
              
				generateDocFromListOfTags(doc, readerEventElement,
						readerEvent.getTagIds());

				root.appendChild(readerEventElement);

			}

		} catch (Exception x) {
			log.error(x);
		} finally {
			if (xmlWriter != null)
				xmlWriter.close();
		}
		return doc;
	}
}
