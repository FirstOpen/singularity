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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.jini.core.event.RemoteEvent;
import net.jini.core.event.RemoteEventListener;
import net.jini.core.event.UnknownEventException;

import org.apache.log4j.Logger;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Tag;

/**
 * @author TomRose
 * 
 */
public class TagCountFilter extends EventDelegate {

	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 * @see net.jini.core.event.RemoteEventListener#notify(net.jini.core.event.RemoteEvent)
	 * 
	 * Filters duplicate tags then any listeners further down the pipeline.
	 */
	public void notify(RemoteEvent theEvent) throws UnknownEventException,
			RemoteException {

		if (theEvent.getSource().getClass() == DeviceECSpec.class) {
			DeviceECSpec deviceECSpec = (DeviceECSpec) theEvent.getSource();
			/*
			 * look for duplicate reader events
			 */
			Collection<ReaderEvent> readerEvents = deviceECSpec.getEvents();
			
			
			HashMap<String,ReaderEvent> readerEventFilterMap = new HashMap<String,ReaderEvent>();
			

			/*
			 * for all reader events
			 */
		   
			for (Iterator<ReaderEvent> iter = readerEvents.iterator(); iter.hasNext();) {
				ReaderEvent element = iter.next();
				String rekey = element.getSensor().getName().trim() + element.getReaderName().trim();
				ReaderEvent readerEvent = readerEventFilterMap.get(rekey);
				if (readerEvent == null){
					readerEvent = new ReaderEvent(element.getSensor());
					readerEvent.setReaderName(element.getReaderName());
					readerEventFilterMap.put(rekey, readerEvent);
				}
				HashMap<String,Tag> tagFilterMap = readerEvent.getTagMap();
				Collection<Tag> tagList = element.getTagIds();
				
				for (Iterator<Tag> iterator = tagList.iterator(); iterator.hasNext();) {
					Tag tagElement = iterator.next();
										
					String key = tagElement.getValue().trim();
					
					Tag tag = tagFilterMap.get(key);
					
					if (tag == null){
						tagFilterMap.put(key,tagElement);
						tag = tagElement;
					}
				
					tag.increment(tagElement.getCount());
					
				}//end for all tags
	
				//readerEvent.addTags(tagFilterMap.values());
				
			}//end for all reader events
			
		
			deviceECSpec.setEvents(readerEventFilterMap.values());

			if (eventListener != null) eventListener.notify(theEvent);

		}

	}

	/**
	 * 
	 */
	public TagCountFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param eventListener
	 */
	public TagCountFilter(RemoteEventListener eventListener) {
		super(eventListener);
		// TODO Auto-generated constructor stub
	}

}
