/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package org.firstopen.custom.business;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.epc.ale.ECReport;
import org.firstopen.epc.ale.ECReportGroup;
import org.firstopen.epc.ale.ECReportGroupList;
import org.firstopen.epc.ale.ECReportGroupListMember;
import org.firstopen.epc.ale.ECReportList;
import org.firstopen.epc.ale.ECReports;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.system.dao.TagDAO;
import org.firstopen.singularity.system.dao.TagDAOFactory;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: CustomApp.java 1243 2006-01-14 03:33:37Z TomRose $
 * 
 */
public class CustomApp {

    Log log = LogFactory.getLog(this.getClass());

    String lockObject = new String();

    /**
     * Active EventRecords
     */
    HashMap<String, EventRecord> activeEvents = new HashMap<String, EventRecord>();

    /**
     * Archived EventRecords (EventRecord has Expired)
     */
    ArrayList<EventRecord> archivedEvents = new ArrayList<EventRecord>();

    Timer timer = new Timer();

    TimerTask timerTask = new TimeOut();

    /**
     * 
     */
    public CustomApp() {
        super();
        timer.scheduleAtFixedRate(timerTask, 30000, 30000);
    }

    public void addReport(ECReports reports) {

        Calendar date = reports.getDate();

        ECReportList reportList = reports.getReports();

        List<ECReport> ecReportList = reportList.getReportList();

        for (ECReport ecReport : ecReportList) {

            String reportName = ecReport.getReportName();

            List<ECReportGroup> groups = ecReport.getGroupList();

            for (ECReportGroup ecReportGroup : groups) {

                ECReportGroupList groupList = ecReportGroup.getGroupList();

                List<ECReportGroupListMember> members = groupList
                        .getMemberList();

                long timestamp = date.getTimeInMillis();

                for (ECReportGroupListMember member : members) {
                    String tagValue = member.getTag().getText();

                    /*
                     * Singularit ALE Extension for Tag count and ReaderID for
                     * this Event Cycle
                     */

                    NodeList nodelist = member.getExtension().getDomNode()
                            .getChildNodes();

                    Node countNode = nodelist.item(0);

                    Node readerIDelement = nodelist.item(1);

                    Node readerIDNode = readerIDelement.getFirstChild();

                    boolean tampered = isTampered(member.getTag().getBinary());

                    try {
                        updateEventRecord(tagValue, timestamp, readerIDNode
                                .getNodeValue(), Integer.parseInt(countNode
                                .getNodeValue()), tampered);
                    } finally {
                        DAOUtilFactory.close();
                    }
                }// end for all memebers
            }// ECReportGroup
        }// all ecReports

    }

    private boolean isTampered(byte[] binary) {
        boolean isTampered = false;

        String tamper = new String(binary);
        log.debug("tamper = " + tamper);

        if (tamper.length() > 0) {
            int iTamper = new BigInteger(tamper, 16).intValue();

            if (iTamper == 255) {
                isTampered = true;
            }
        }

        return isTampered;
    }

    /**
     * updates event record, and determines if events should be archived or kept
     * active.
     * 
     * @param tagValue
     * @param timestamp
     * @param readerId
     * @param i
     * @return
     */
    private synchronized void updateEventRecord(String tagValue,
            long timestamp, String readerId, int count, boolean tampered) {

        EventRecord eventRecord = findEventRecord(tagValue, timestamp,
                readerId, tampered);
        eventRecord.addEvent(timestamp, count);

        if (eventRecord.isExpired(timestamp)) {
            archiveEventRecord(eventRecord);
        }

    }

    /**
     * Moves an event record from the active event list to the archived event
     * list. Move is a REMOVE from active ADD to archive.
     * 
     * @param eventRecord
     */
    private void archiveEventRecord(EventRecord eventRecord) {
        EventRecordDAO eventRecordDAO = EventRecordDAOFactory.create();
        try {
            eventRecordDAO.update(eventRecord);

            synchronized (lockObject) {
                archivedEvents.add(0, eventRecord);
                String key = eventRecord.getTagId() + eventRecord.getReaderid();
                activeEvents.remove(key);
            }
        } catch (InfrastructureException e) {
            log.error("unable to archive EventRecord", e);
        } finally {
            DAOUtilFactory.close();
        }
    }

    /**
     * Finds the event record based on tagid in the active event list, or
     * creates a new event record and places it in the active event list.
     * 
     * @param tagValue
     * @param timestamp
     * @param readerId
     * @return
     */
    private EventRecord findEventRecord(String tagValue, long timestamp,
            String readerId, boolean tampered) {

        String key = tagValue + readerId;
        EventRecord eventRecord = null;

        synchronized (lockObject) {
            eventRecord = activeEvents.get(key);
        }
        if (eventRecord == null) {

            Tag searchTag = new Tag();
            searchTag.clear();
            searchTag.setValue(tagValue);
            searchTag = updateTag(searchTag, tampered);

            eventRecord = new EventRecord(tagValue, searchTag.getPrintedId(),
                    timestamp, readerId);
            eventRecord.addEvent(timestamp);
            eventRecord.setTampered(searchTag.isTampered());
            synchronized (lockObject) {
                activeEvents.put(key, eventRecord);
            }
        }

        return eventRecord;

    }

    private Tag updateTag(Tag searchTag, boolean tampered) {
        TagDAO tagDAO = TagDAOFactory.create();
        Tag returnTag = null;
        try {
            returnTag = tagDAO.findBy(searchTag);
            if (returnTag != null) {
                returnTag.setTampered(tampered);
                tagDAO.update(returnTag);
            } else {
                returnTag = searchTag;
                returnTag.setTampered(tampered);
            }

        } catch (InfrastructureException e) {
            log.error("unable to determine CarterNote or Tag existance");
        }

        return returnTag;
    }

    /**
     * @return Returns the activeEvents.
     */
    public HashMap<String, EventRecord> getActiveEvents() {
        return activeEvents;
    }

    /**
     * @param activeEvents
     *            The activeEvents to set.
     */
    public void setActiveEvents(HashMap<String, EventRecord> activeEvents) {
        this.activeEvents = activeEvents;
    }

    /**
     * @param archivedEvents
     *            The archivedEvents to set.
     */
    public void setArchivedEvents(ArrayList<EventRecord> archivedEvents) {
        this.archivedEvents = archivedEvents;
    }

    public void checkTimeout() {
        long now = System.currentTimeMillis();
        List<EventRecord> archiveList = new ArrayList<EventRecord>();

        synchronized (lockObject) {
            Collection<EventRecord> collection = activeEvents.values();
            for (Iterator<EventRecord> iter = collection.iterator(); iter
                    .hasNext();) {
                EventRecord eventRecord = iter.next();
                if (eventRecord.isExpired(now)) {
                    archiveList.add(eventRecord);
                }
            }// end for

            /*
             * second iterator avoids exception since archive the eventrecord
             * will change the activelist while interator is being used. Causing
             * a ConcurrentModificationException.
             */
            for (Iterator<EventRecord> iter = archiveList.iterator(); iter
                    .hasNext();) {
                EventRecord element = iter.next();
                archiveEventRecord(element);
            }

        }// end synchronized
    }// end checkTimeout

    public class TimeOut extends TimerTask {

        /**
         * @param application
         */
        @Override
        public void run() {
            checkTimeout();
        }
    }
}// end CustomApp
