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

package org.firstopen.singularity.devicemgr.common;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.firstopen.singularity.devicemgr.DeviceManager;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Tag;

/**
 * 
 * @author i-Konect LLC
 * @version $Id: DeviceECSpec.java 795 2005-10-20 05:52:04Z TomRose $
 * 
 */
public class DeviceECSpec implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6456601534725987114L;

    private Collection<ReaderEvent> events = new ArrayList<ReaderEvent>();

    private String specName = null;

    private String host = null;

    private String queueName = null;

    private HashMap<String, Tag> rawDataList = new HashMap<String, Tag>();

    private Set<String> readers  = new HashSet<String>();

    private Set<String> logicalReaders = null;
    
    private long duration = 0;

    private long endTime = 0;

    public DeviceECSpec() {

    }

    public void reset() {
        rawDataList = new HashMap<String, Tag>();
        events = new ArrayList<ReaderEvent>();
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String x) {
        specName = x;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String x) {
        queueName = x;
    }

    public Collection<Tag> getRawDataList() {
        return rawDataList.values();

    }

    /**
     * @param rawDataList
     *            The rawDataList to set.
     */
    public void setRawDataList(HashMap<String, Tag> rawDataList) {
        this.rawDataList = rawDataList;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long x) {
        duration = x;
    }

    public synchronized void addTagIds(List<Tag> list) {
        String key;
        /*
         * no duplicate tags, just adjust the count
         */
        for (Iterator<Tag> iter = list.iterator(); iter.hasNext();) {

            Tag element = iter.next();

            key = element.getValue();

            Tag tag = rawDataList.get(key);

            if (tag == null) {
                rawDataList.put(key, element);
            } else {
                tag.increment();
            }

        }
    }

    public DeviceECSpec(String name) {
        super();
        specName = name;
    }

    /**
     * 
     * @return Returns the endTime.
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     *            The endTime to set.
     */
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void calcEndTime() {
        endTime = System.currentTimeMillis() + duration;
    }

    /**
     * 
     * @return Returns the events.
     */
    public Collection<ReaderEvent> getEvents() {
        return events;
    }

    /**
     * @param name
     *            The events to set.
     */
    public synchronized void setEvents(Collection<ReaderEvent> events) {
        this.events = events;
    }

    public synchronized void addEvent(ReaderEvent event) {
        events.add(event);
    }

    /**
     * @return Returns the host.
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host
     *            The host to set.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 
     * Implements the physical transport of the information based on chosen
     * distributed component selection. Keeps the object locked so multiple
     * threads will not change ECSpec Profile between EventCycle transmission.
     * @throws Exception 
     * @throws RemoteException 
     * @throws Exception 
     * @throws Exception 
     * @throws RemoteException 
     * @throws  
     * 
     */
    public synchronized void CompleteEventCycle(DeviceManager deviceManager) throws RemoteException, Exception{
       
        deviceManager.sendRemoteEvent(this);
        reset();
        calcEndTime();

    }

    /**
     * @return Returns the readers.
     */
    public Set<String> getReaders() {
        return readers;
    }

    /**
     * @param readers
     *            The readers to set.
     */
    public void setReaders(Set<String> readers) {
        this.readers = readers;
    }

    public void addReaders(Set<String> readers) {
        this.readers.addAll(readers);
    }
    
    public void addReader(String reader) {
        this.readers.add(reader);
    }
    /**
     * @return Returns the logicalReaders.
     */
    public Set<String> getLogicalReaders() {
        return logicalReaders;
    }

    /**
     * @param logicalReaders The logicalReaders to set.
     */
    public void setLogicalReaders(Set<String> logicalReaderNames) {
        this.logicalReaders = logicalReaderNames;
    }

}
