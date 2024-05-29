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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.application.Application;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.firstopen.custom.business.CustomApp;
import org.firstopen.custom.business.EventRecord;
import org.firstopen.epc.ale.ECReportsDocument;
import org.firstopen.singularity.admin.view.BaseBean;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.system.dao.TagDAO;
import org.firstopen.singularity.system.dao.TagDAOFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JMSUtil;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: EventMonitorBean.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public class EventMonitorBean extends BaseBean implements MessageListener {

    /**
     * 
     */
    private static final long serialVersionUID = -7365005480144560775L;

    private UIData activeSealTable;

    private UIData archivedSealTable;

    private CustomApp customApp = new CustomApp();

    private String currentTag;

    private String xml = "<markers>"
        + "<marker lat=\"37.441\" lng=\"-122.141\"/>"
        + "<marker lat=\"37.442\" lng=\"-121.142\"/>"
        + "</markers>"; 
    
    public final static String MONITOR_NAME = "CustomsTrial";

    static Log log = LogFactory.getLog(EventMonitorBean.class);

    Connection connection = null;

    Session queueSession = null;

    
    public EventMonitorBean() throws InfrastructureException {
        this(MONITOR_NAME);
    }

    public EventMonitorBean(String name) throws InfrastructureException {
        if (System.getSecurityManager() == null) {

            System.setSecurityManager(new SecurityManager());
        }

        JMSUtil.createQueue(MONITOR_NAME);
        connection = JMSUtil.getQueueConnection();
        try {
            queueSession = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            MessageConsumer receiver = queueSession.createConsumer(JMSUtil
                    .getQueue(MONITOR_NAME));

            receiver.setMessageListener(this);

            connection.start();
        } catch (JMSException e) {

            log.error("unable to start listener on Queue");
            throw new InfrastructureException(e);
        }
    }

    protected void setUp() throws Exception {
    }

    public void stopMessage(ActionEvent e) {

    }

    public void sendMessage(ActionEvent e) {

    }

    public void sendEventMessage(ActionEvent e) {

    }

    public void onMessage(Message message) {

        try {
            String textMessage = ((TextMessage) message).getText();

            log.debug("message recieved " + textMessage);

            XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setSaveAggressiveNamespaces();
            xmlOptions.setValidateOnSet();
            ECReportsDocument reports = ECReportsDocument.Factory.parse(
                    textMessage, xmlOptions);

            log.debug("parsed document" + reports.xmlText());

            validate(reports);

            customApp.addReport(reports.getECReports());

        } catch (XmlException e) {
            log.error(e);
        } catch (JMSException e) {
            log.error(e);
        }

    }

    public void shutdown() throws InfrastructureException {

        try {
            connection.stop();

            connection.close();

        } catch (JMSException e) {
            log.error("unable to stop queue listener");
            throw new InfrastructureException(e);
        }
    }

    /**
     * 
     * @param reports
     * @return
     */
    public boolean validate(ECReportsDocument reports) {
        // Set up the validation error listener.
        ArrayList validationErrors = new ArrayList();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);

        // During validation, errors are added to the ArrayList for
        // retrieval and printing by the printErrors method.

        boolean isValid = reports.validate(validationOptions);

        // Print the errors if the XML is invalid.
        if (!isValid) {
            Iterator iter = validationErrors.iterator();
            while (iter.hasNext()) {
                log.debug(">> " + iter.next() + "\n");
            }
        }
        return isValid;
    }

    /**
     * @return Returns the customApp.
     */
    public CustomApp getCustomApp() {
        return customApp;
    }

    /**
     * @param customApp
     *            The customApp to set.
     */
    public void setCustomApp(CustomApp customApp) {
        this.customApp = customApp;
    }

    /**
     * @return Returns the activeSealTable.
     */
    public UIData getActiveSealTable() {
        return activeSealTable;
    }

    /**
     * @param activeSealTable
     *            The activeSealTable to set.
     */
    public void setActiveSealTable(UIData sealTable) {
        this.activeSealTable = sealTable;
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List getActiveEvents() {
        HashMap events = customApp.getActiveEvents();
        return new ArrayList(events.entrySet());

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
    public void setArchivedSealTable(UIData sealTable2) {
        this.archivedSealTable = sealTable2;
    }

    /**
     * @return Returns the currentTag.
     */
    public String getCurrentTag() {
        return currentTag;
    }

    /**
     * @param currentTag
     *            The currentTag to set.
     */
    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }

    public void getActiveDocuments(ActionEvent e) {

        Entry entry = (Entry) activeSealTable.getRowData();
        getDocuments((EventRecord) entry.getValue());
    }

    public void getArchivedDocuments(ActionEvent e) {

        // getDocuments((EventRecord) archivedSealTable.getRowData());
        getDocument((EventRecord) archivedSealTable.getRowData());
    }

    private void getDocument(EventRecord eventRecord) {
        FacesContext context = getFacesContext();

        Application application = context.getApplication();

        CarterNoteBean carterNoteBean = (CarterNoteBean) application
                .createValueBinding("#{carterNoteBean}").getValue(context);
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
                    .createValueBinding("#{carterNote}").getValue(context);

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
     * @return Returns the xml.
     */
    public String getXml() {
       
        return xml;
    }

    /**
     * @param xml The xml to set.
     */
    public void setXml(String xml) {
        this.xml = xml;
    }
    
    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: " + scrollerEvent.getScrollerfacet()
                        + ", pageindex: " + scrollerEvent.getPageIndex());
    }
}
