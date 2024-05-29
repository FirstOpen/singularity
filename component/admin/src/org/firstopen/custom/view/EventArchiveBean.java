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
package org.firstopen.custom.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.firstopen.custom.business.EventRecord;
import org.firstopen.custom.business.EventRecordDAO;
import org.firstopen.custom.business.EventRecordDAOFactory;
import org.firstopen.singularity.admin.view.BaseBean;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.system.dao.TagDAO;
import org.firstopen.singularity.system.dao.TagDAOFactory;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: EventArchiveBean.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public class EventArchiveBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3551912771150112156L;

    private HtmlForm form;

    private UIData archivedSealTable;

    private EventRecord currentEvent = new EventRecord();

    private List<EventRecord> events = new ArrayList<EventRecord>();

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 
     */
    public EventArchiveBean() {
        super();

    }

    /**
     * TODO: what is this?
     */
    public String search() {
        String result = "failed";
        try {

            refresh(currentEvent);
            result = "success";
        } catch (InfrastructureException e) {
            log.equals("search failed");
        }

        return result;
    }

    private void refresh(EventRecord searchEventRecord)
            throws InfrastructureException {
        EventRecordDAO eventRecordDAO = null;
        try {
            eventRecordDAO = EventRecordDAOFactory.create();

            List<EventRecord> resultList = eventRecordDAO
                    .getAll(searchEventRecord);
            if (resultList != null) {
                if (resultList.size() > 0) {
                    events = resultList;
                }
            }
        } catch (InfrastructureException e) {
            log.error("cannot refresh");
            throw e;
        } 

    }

    /**
     * 
     * @hibernate.property
     * @return Returns the form.
     */
    public HtmlForm getForm() {
        return form;
    }

    /**
     * @param form
     *            The form to set.
     */
    public void setForm(HtmlForm form) {
        this.form = form;
    }

    public String next() {
        log.info("action() = next");
        return "success";
    }

    /**
     * 
     * 
     */
    public void clear() {
        currentEvent = new EventRecord();
        currentEvent.clear();
    }

    public void getArchivedDocuments(ActionEvent e) {

        // getDocuments((EventRecord) archivedSealTable.getRowData());
        getDocument((EventRecord) archivedSealTable.getRowData());
    }

    private void getDocument(EventRecord eventRecord) {
        FacesContext context = getFacesContext();

        Application application = context.getApplication();

        CarterNoteBean carterNoteBean = (CarterNoteBean) application
                .createValueBinding("#{carterNote}").getValue(context);
        carterNoteBean.setCurrentNote(eventRecord.getCarterNote());
    }

    private void getDocuments(EventRecord eventRecord) {

        String tagValue = eventRecord.getTagId();
        log.info("tagvlaue is " + tagValue);
        TagDAO tagDAO = TagDAOFactory.create();

        try {
            Tag searchTag = new Tag();
            searchTag.setId(null);
            searchTag.setValue(tagValue);
            Tag tag = tagDAO.findBy(searchTag);

            /*
             * TODO: just for clarity, can remove local variables later.
             */

            FacesContext context = getFacesContext();

            Application application = context.getApplication();

            CarterNoteBean carterNoteBean = (CarterNoteBean) application
                    .createValueBinding("#{carterNoteBean}").getValue(context);

            if (tag != null) {
                log.debug("Persistant tag.value " + tag.getValue());
                carterNoteBean.refresh(tag.getPrintedId());
            } else {
                carterNoteBean.clear();

                log.info("no attached documents for printedID: "
                        + searchTag.getPrintedId());
            }

        } catch (InfrastructureException x) {
            log.error("unable to retreive Tag", x);
        }

    }

    /**
     * @return Returns the archivedSealTable.
     */
    public UIData getArchivedSealTable() {
        return archivedSealTable;
    }

    /**
     * @param archivedSealTable
     *            The archivedSealTable to set.
     */
    public void setArchivedSealTable(UIData archivedSealTable) {
        this.archivedSealTable = archivedSealTable;
    }

    /**
     * @return Returns the currentEvent.
     */
    public EventRecord getCurrentEvent() {
        return currentEvent;
    }

    /**
     * @param currentEvent
     *            The currentEvent to set.
     */
    public void setCurrentEvent(EventRecord currentEvent) {
        this.currentEvent = currentEvent;
    }

    /**
     * @return Returns the events.
     */
    public List<EventRecord> getEvents() {
        return events;
    }

    /**
     * @param events
     *            The events to set.
     */
    public void setEvents(List<EventRecord> events) {
        this.events = events;
    }
    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: " + scrollerEvent.getScrollerfacet()
                        + ", pageindex: " + scrollerEvent.getPageIndex());
    }
}
