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
package org.firstopen.custom.business;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * 
 * @hibernate.class table="EventRecord" lazy="false"
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: EventRecord.java 1243 2006-01-14 03:33:37Z TomRose $
 * 
 */
public class EventRecord {

    private static final long serialVersionUID = -3841467057028537632L;

    private String id = null;

    private String tagId = null;

    private int count = 0;

    private String readerid = null;

    private boolean isTampered = false;

    private boolean expired = false;

    private Date firstreaddate = null;

    private Date lastreaddate = null;

    private boolean documents = false;

    private CarterNote carterNote = null;

    private String printedId = null;

    static Log log = LogFactory.getLog(EventRecord.class);

   
    /**
     * @throws InfrastructureException 
     * 
     */
    public EventRecord() {
        this(System.currentTimeMillis());
       
    }

    /**
     * @param firstreadtime
     * @throws InfrastructureException 
     */
    public EventRecord(long firstreadtime){
        super();
        setFirstreadtime(firstreadtime);
        setLastreadtime(firstreadtime);
    }

    /**
     * @param firstreadtime
     * @throws InfrastructureException 
     */
    public EventRecord(String tagValue, String printedId, long firstreadtime,
            String readerId) {
        this(firstreadtime);
        setReaderid(readerId);
        setTagId(tagValue);
        setPrintedId(printedId);
    }

    /**
     * @hibernate.property
     * @return Returns the isTampered.
     */
    public boolean isTampered() {
        return isTampered;
    }

    /**
     * @param isTampered
     *            The isTampered to set.
     */
    public void setTampered(boolean isTampered) {
        this.isTampered = isTampered;
    }

    public void incrementCount() {
        count++;
    }

    public void incrementCount(int iCount) {
        count = count + iCount;
    }

    public void decrementCount() {
        count--;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the count.
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     *            The count to set.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * query db for count < 30 seconds from now update weather the tamper has
     * happened in the last 30 seconds of readtime.
     */
    public void update() {

    }

    /**
     * 
     * possibly cleared, therefore time offset will be set to 0;
     * 
     * @hibernate.property
     * @return Returns the firstreadtime.
     */
    public long getFirstreadtime() {
        long firstreadtime = 0;
        if (firstreaddate != null) {
            firstreadtime = firstreaddate.getTime();
        }

        return firstreadtime;
    }

    /**
     * @param firstreadtime
     *            The firstreadtime to set.
     */
    public void setFirstreadtime(long firstreadtime) {
        if (firstreaddate == null) {
            this.firstreaddate = new Date();
        }
        this.firstreaddate.setTime(firstreadtime);
        expired = false;
    }

    /**
     * 
     * @hibernate.property
     * @return Returns the lastreadtime.
     */
    public long getLastreadtime() {
        long lastreadtime = 0;
        if (lastreaddate != null) {
            lastreadtime = lastreaddate.getTime();
        }
        return lastreadtime;
    }

    /**
     * @param lastreadtime
     *            The lastreadtime to set.
     */
    public void setLastreadtime(long lastreadtime) {
        if (lastreaddate == null) {
            this.lastreaddate = new Date();
        }
        this.lastreaddate.setTime(lastreadtime);

        attachDocuments(false);
    }

    /**
     * 
     * @param currentreadtime
     * @return
     */
    public boolean isExpired(long currentreadtime) {
        if ((currentreadtime - this.getFirstreadtime()) > 30000)
            expired = true;
        return expired;
    }

    /**
     * 
     * @return Returns the expired.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * @param expired
     *            The expired to set.
     */
    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    /**
     * 
     * @return Returns the firstreaddate.
     */
    public Date getFirstreaddate() {
        return firstreaddate;
    }

    /**
     * 
     * 
     * 
     * @param firstreaddate
     * 
     * The firstreaddate to set.
     */
    public void setFirstreaddate(Date firstreaddate) {
        this.firstreaddate = firstreaddate;
        expired = false;
    }

    /**
     * 
     * 
     * 
     * @return Returns the lastreaddate.
     */
    public Date getLastreaddate() {
        return lastreaddate;
    }

    /**
     * 
     * @param lastreaddate
     * 
     * The lastreaddate to set.
     */
    public void setLastreaddate(Date lastreaddate) {
        this.lastreaddate = lastreaddate;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the readerid.
     */
    public String getReaderid() {
        return readerid;
    }

    /**
     * @param readerid
     *            The readerid to set.
     */
    public void setReaderid(String readerid) {
        if (readerid != null) {
            if (readerid.equals("")) {
                readerid = null;
            }
        }
        this.readerid = readerid;
    }

    /**
     * @return Returns the documents.
     */
    public boolean isDocuments() {
        return documents;
    }

    /**
     * @param documents
     *            The documents to set.
     */
    public void setDocuments(boolean documents) {
        this.documents = documents;
    }

    private void attachDocuments(boolean flush) {

        if (getPrintedId() != null)
            if (documents == false || (documents == true && flush == true)) {
                CarterNote currentNote = new CarterNote();
                currentNote.clear();
                currentNote.setPrintedTagId(getPrintedId());
                CarterNoteDAO carterNoteDAO = null;
                try {
                     carterNoteDAO = CarterNoteDAOFactory.create();
                    carterNote = carterNoteDAO.getCartersNote(currentNote);
                    if (carterNote == null) {
                        documents = false;
                    } else {
                        documents = true;
                    }
                } catch (InfrastructureException e) {
                    documents = false;
                    log.error("unable to attach documnets", e);
                }  finally {

                    try {
                      DAOUtilFactory.close();
                    } catch (InfrastructureException e) {
                       log.error("unlable close session are CarterNote",e);
                    }
                }

            }
    }

    /**
     * @hibernate.many-to-one class="org.firstopen.custom.business.CarterNote"
     *                        cascade="none"
     * 
     * @return Returns the carterNote.
     */
    public CarterNote getCarterNote() {
        return carterNote;
    }

    /**
     * @param carterNote
     *            The carterNote to set.
     */
    public void setCarterNote(CarterNote carterNote) {
        this.carterNote = carterNote;
    }

    /**
     * @hibernate.id generator-class="uuid.hex" length="128"
     * 
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @param timestamp
     */
    public void addEvent(long timestamp) {
        this.setLastreadtime(timestamp);
        this.incrementCount();
    }

    /**
     * @hibernate.property
     * @return Returns the tagId.
     */
    public String getTagId() {
        return tagId;
    }

    /**
     * 
     * @param tagId
     *            The tagId to set.
     */
    public void setTagId(String tagId) {
        if (tagId != null) {
            if (tagId.equals("")) {
                tagId = null;
            }
        }
        this.tagId = tagId;

    }

    /**
     * @hibernate.property
     * @return
     */
    public String getPrintedId() {
        return printedId;
    }

    public void setPrintedId(String printedId) {
        if (printedId != null) {
            if (printedId.equals("")) {
                printedId = null;
            }
        }
        this.printedId = printedId;
        attachDocuments(true);
    }

    public void addEvent(long timestamp, int count) {
        this.setLastreadtime(timestamp);
        this.incrementCount(count);

    }

    public void clear() {

        id = null;

        tagId = null;

        printedId = null;

        count = 0;

        readerid = null;

        isTampered = false;

        expired = false;

        firstreaddate = null;

        lastreaddate = null;

        documents = false;

        carterNote = null;

    }
}
