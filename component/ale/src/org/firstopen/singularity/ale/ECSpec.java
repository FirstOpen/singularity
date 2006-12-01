/*
 * Copyright 2005 Jeff Bride
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

package org.firstopen.singularity.ale;

import java.io.StringReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.firstopen.epc.ale.ECReport;
import org.firstopen.epc.ale.ECReportGroup;
import org.firstopen.epc.ale.ECReportGroupList;
import org.firstopen.epc.ale.ECReportGroupListMember;
import org.firstopen.epc.ale.ECReportGroupListMemberExtension;
import org.firstopen.epc.ale.ECReportList;
import org.firstopen.epc.ale.ECReports;
import org.firstopen.epc.ale.ECReportsDocument;
import org.firstopen.epc.ale.ECSpecDocument;
import org.firstopen.epc.ale.ECTerminationCondition;
import org.firstopen.epc.pmlcore.DataType;
import org.firstopen.singularity.ale.exception.ECSpecValidationException;
import org.firstopen.singularity.ale.exception.ImplementationException;
import org.firstopen.singularity.ale.exception.InvalidURIException;
import org.firstopen.singularity.ale.exception.NoSuchSubscriberException;
import org.firstopen.singularity.config.LogicalDevice;
import org.firstopen.singularity.config.dao.LogicalDeviceDAO;
import org.firstopen.singularity.config.dao.LogicalDeviceDAOFactory;
import org.firstopen.singularity.devicemgr.DeviceManager;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.common.DeviceProtocol;
import org.firstopen.singularity.devicemgr.common.ServiceLocator;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.system.Sensor;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JMSUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * 
 * @author Jeff Bride
 * @author Tom Rose
 * 
 * Modified from orginal.
 * 
 * @hibernate.class table="ECSpec" lazy="false"
 */
public class ECSpec implements ContentHandler, MessageListener {


    /**
     * 
     */
    private static final long serialVersionUID = 1043637307376972024L;

    /**
     * public static finals
     */

    public final static String SPEC_NAME = "specName";

    public final static String READERS = "logicalReaders";

    public final static String READER = "logicalReader";

    public final static String BOUNDARIES = "boundarySpec";

    public final static String REPORT_SPECS = "reportSpecs";

    public final static String REPORT_SPEC = "reportSpec";

    public final static int ACTIVE_STATE = 1;

    public final static int INACTIVE_STATE = 0;

    /**
     * private
     */

    private ECSpecDocument ecSpecDocument = null;

    private int currentState = INACTIVE_STATE;

    private String specName;

    private Set<String> logicalReaders; // list of interrogator names

    private ECBoundarySpec boundaries;

    private HashMap<String, ECReportSpec> reportSpecs;

    private boolean includeSpecInReports = false;

    private Set<String> subscribers;

    private List<Tag> tagList;

    private Logger log = Logger.getLogger(this.getClass());

    private StringBuffer charArray = new StringBuffer();

    private String xml = null;

    private ReaderEvent readerEvent;

    private List<ReaderEvent> events = new ArrayList<ReaderEvent>();

    private String hostname = "localhost";

    private Tag currentTag;

    /**
     * 
     * 
     */
    public ECSpec() {
        SecurityManager sm = System.getSecurityManager();
        if (sm == null) sm = new SecurityManager();

        log.debug("ECSpec.constructor()");
        InetAddress inetAddr = null;

        try {
            inetAddr = InetAddress.getLocalHost();
            hostname = inetAddr.getHostName();
        } catch (UnknownHostException e) {

            log.error("unable to obtain host IP, hostname =" + hostname, e);
        }

        try {

            logicalReaders = new HashSet<String>();
            reportSpecs = new HashMap<String, ECReportSpec>();
            subscribers = new HashSet<String>();

        } catch (Exception x) {
            log.error(x);
        } finally {
            try {
            } catch (Exception x) {
                log.error("unable to create ECSpec", x);
            }
        }
    }

    /*
     * Constructor Instantiates ECSpec. Instantiating this class fulfills the
     * "defining" action as per the ALE spec. Afterwards, clients can continue
     * to "subscribe" to this ECSpec.
     */
    public ECSpec(String specName, ECBoundarySpec boundaries) {
        this();
        log.debug("ECSpec.constructor()");
        this.specName = specName;
        this.boundaries = boundaries;
    }

    /*
     * poll() As per the ALE spec, when an ECSpec is polled, one event cycle is
     * executed
     */
    public ECReportsDocument poll() throws ImplementationException {
        log.debug("poll()    " + getSpecName());
        ECReportsDocument reports = null;
        try {

            reports = generateReports();

        } catch (XmlException e) {
            log.error(e);
            throw new ImplementationException("Unable to poll ", e);
        }
        return reports;
    }

    /**
     * onMessage An ECSpec object implements javax.jms.MessageListener and is
     * listening for incoming raw data files on a queue that was created when
     * this object is first instantiated. This method is called as each raw
     * datafile , from each interrogator, is sent to queue
     */
    public void onMessage(Message message) {
        InputSource inSource = null;
        StringReader stringReader = null;
        try {
            log.debug("ECSpec.onMessage()");
            if (message instanceof TextMessage) {

                String textMessage = ((TextMessage) message).getText();

                log.debug("message recieved " + textMessage);

                stringReader = new StringReader(textMessage);

                inSource = new InputSource(stringReader);

                XMLReader xmlReaderObj = XMLReaderFactory
                        .createXMLReader("org.apache.xerces.parsers.SAXParser");
                xmlReaderObj.setContentHandler(this);
                xmlReaderObj.setFeature(
                        "http://xml.org/sax/features/namespaces", false);
                xmlReaderObj
                        .setFeature(
                                "http://xml.org/sax/features/namespace-prefixes",
                                false);
                // xmlReaderObj.setFeature(
                // "http://apache.org/xml/features/validation/schema", true);
                xmlReaderObj.parse(inSource);

            } else {
                message.clearBody();
            }
        } catch (Exception x) {
            log.error(x);
        } finally {
            try {
                if (stringReader != null) stringReader.close();
            } catch (Exception x) {
                log.error("unable to retieve events from queue", x);
            }
        }
    }

    /**
     * 
     * @return
     * @throws ImplementationException
     * @throws XmlException
     */
    public ECReportsDocument generateReports() throws ImplementationException,
            XmlException {
        ECReportsDocument reportsDocument = null;
        ECReports reports = null;
        try {
            ecSpecDocument = ECSpecDocument.Factory.newInstance();

            reportsDocument = ECReportsDocument.Factory.newInstance();

            reports = reportsDocument.addNewECReports();

            reports.setId(specName);
            reports.setALEID(specName);
            reports.setSpecName(specName);

            reports.setTerminationCondition(ECTerminationCondition.DURATION);

            if (getIncludeSpecInReports())
                reports.setECSpec(ecSpecDocument.addNewECSpec());

            if (boundaries.getDuration() != null)
                reports.setTotalMilliseconds((boundaries.getDuration())
                        .getDuration());

            ECReportList reportList = reports.addNewReports();

            Iterator rsIterator = reportSpecs.keySet().iterator();
            while (rsIterator.hasNext()) {
                String reportName = (String) rsIterator.next();
                ECReport report = reportList.addNewReport();

                report.setReportName(reportName);

                ECReportGroup reportGroup = report.addNewGroup();

                ECReportGroupList reportGroupList = reportGroup
                        .addNewGroupList();
                
                for (ReaderEvent element : events) {
                 
                    Collection<Tag> tags = element.getTagIds();
                    for (Iterator<Tag> iterator = tags.iterator(); iterator
                            .hasNext();) {

                        Tag eventTag = iterator.next();
                        ECReportGroupListMember member = reportGroupList
                                .addNewMember();

                        DataType reportTag = member.addNewTag();
                        reportTag.setText(eventTag.getValue());
                        reportTag.setBinary(eventTag.getBinvalue());

                        ECReportGroupListMemberExtension extension = member
                                .addNewExtension();

                        /*
                         * add extension Tag Count
                         */
                        Node reportTagCount = extension.getDomNode()
                                .getOwnerDocument().createTextNode(
                                        Integer.toString(eventTag.getCount()));
                        extension.getDomNode().appendChild(reportTagCount);

                        /*
                         * add extension TagReaderID
                         */
                        Node reportTagReaderIDNode = extension.getDomNode()
                                .getOwnerDocument().createTextNode(
                                        element.getReaderName());
                        Element readerIDelement = extension.getDomNode()
                                .getOwnerDocument().createElement(DeviceProtocol.READERID);
                        readerIDelement.appendChild(reportTagReaderIDNode);
                        extension.getDomNode().appendChild(readerIDelement);
                        
                        /*
                         * add extension geocoords
                         */
                        Node geoCoordNode = extension.getDomNode()
                                .getOwnerDocument().createTextNode(
                                        element.getGeocoord());
                        Element geoCoordElement = extension.getDomNode()
                                .getOwnerDocument().createElement(DeviceProtocol.GEOCOORD);
                        geoCoordElement.appendChild(geoCoordNode);
                        extension.getDomNode().appendChild(geoCoordElement);


                    }// for all tags

                }// for all ReaderEvents
            }

            GregorianCalendar calendar = new GregorianCalendar();
            if (events.size() > 0) {
                calendar.setTime(events.get(0).getDate());
            } else { // report is empty
                calendar.setTimeInMillis(System.currentTimeMillis());
            }

            reports.setDate(calendar);

        } catch (Exception x) {
            log.error("unable to create ECReports", x);
            throw new ImplementationException("Problem attempting to poll "
                    + getSpecName());
        }

        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSaveAggressiveNamespaces();
        xmlOptions.setValidateOnSet();
        log.debug("parsed document" + reports.xmlText());

        // Set up the validation error listener.
        ArrayList validationErrors = new ArrayList();
        XmlOptions validationOptions = new XmlOptions();
        validationOptions.setErrorListener(validationErrors);

        // Do some editing to myDoc.

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

        return reportsDocument;
    }

    /**
     * 
     * @param subscriber
     * @throws InvalidURIException
     */
    public void registerNotificationURI(String subscriber)
            throws InvalidURIException {
        subscribers.add(subscriber);
    }

    /**
     * 
     * @param subscriber
     * @throws NoSuchSubscriberException
     */
    public void unRegisterNotificationURI(String subscriber)
            throws NoSuchSubscriberException {
        subscribers.remove(subscriber);
    }

    /**
     * @hibernate.id generator-class="assigned"
     */
    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String x) {
        specName = x;
    }

    /**
     * @hibernate.set id="logicalReaders" table="ECSpec_IIds"
     * @hibernate.collection-key column="specName"
     * @hibernate.collection-element column="deviceManagerId" type="string"
     *                               lazy="false"
     */
    public Set<String> getLogicalReaders() {
        return logicalReaders;
    }

    public void setLogicalReaders(Set<String> x) {
        logicalReaders = x;
    }

    /**
     * Places string in Set of logicalReaders Calls method that instantiates IO
     * threads to reader (if hasn't already been done so by another ECSpec
     * thread
     * 
     */
    public void addInterrogator(String deviceManagerId) throws Exception {
        log.debug("addInterogator deviceManagerId = " + deviceManagerId);
        logicalReaders.add(deviceManagerId);
    }

    /**
     * 
     * @param deviceManagerId
     */
    public void removeInterrogator(String deviceManagerId) {
        getLogicalReaders().remove(deviceManagerId);
    }

    /* NOTE: XDoclet doesn't seem to have a way to indicate --> lazy = "false" */
    /**
     * @hibernate.many-to-one id="boundaries"
     *                        class="org.firstopen.singularity.ale.ECBoundarySpec"
     *                        unique="true" cascade="all" lazy="false"
     */
    public ECBoundarySpec getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(ECBoundarySpec x) {
        boundaries = x;
    }

    /**
     * @hibernate.set id="subscribers" table="ECSpec_Subscribers"
     * @hibernate.collection-key column="specName"
     * @hibernate.collection-element column="subscribers" type="string"
     *                               lazy="false"
     */
    public Set getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<String> x) {
        subscribers = x;
    }

    /**
     * @hibernate.property
     */
    public boolean getIncludeSpecInReports() {
        return includeSpecInReports;
    }

    public void setIncludeSpecInReports(boolean x) {
        this.includeSpecInReports = x;
    }

    /*
     * From 8.2 of ALE spec : If the reportSpecs parameter is .......contains
     * two ECReportSpec instances with the same reportName, then the define and
     * immediate methods SHALL raise an ECSpecValidation.
     */
    public void addReportSpec(ECReportSpec x) throws ECSpecValidationException {
        if (reportSpecs.get(x.getReportName()) != null)
            throw new ECSpecValidationException(getSpecName()
                    + "already has a reportSpec with id " + x.getReportName());
        reportSpecs.put(x.getReportName(), x);
    }

    public void deleteReportSpec(ECReportSpec x) {
        reportSpecs.remove(x.getReportName());
    }

    public void startDocument() throws SAXException {
        log.debug("Document Start");
    }

    public void startPrefixMapping(java.lang.String prefix, String uri) {
        log.debug("startPrefixMapping");
    }

    public void skippedEntity(java.lang.String name) {
        log.debug("skippedEntity");
    }

    public void setDocumentLocator(org.xml.sax.Locator locator) {
        log.debug("setDocumentLocator");
    }

    public void endPrefixMapping(java.lang.String prefix) {
        log.debug("endPrefixMapping");
    }

    public void processingInstruction(java.lang.String target,
            java.lang.String data) {
        log.debug("processingInstruction");
    }

    public void ignorableWhitespace(char buf[], int offset, int len) {
        log.debug("ignorableWhitespace");
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attrs) throws SAXException {
        log.debug("start element" + localName);
        if (qName.equals(DeviceProtocol.READER_EVENT)) {
            try {
                Sensor sensor = new Sensor(attrs.getValue(DeviceProtocol.SENSORID));
                readerEvent = new ReaderEvent(sensor);
                readerEvent.setTimestamp(Long.parseLong(attrs
                        .getValue(DeviceProtocol.TIMESTAMP)));
                readerEvent.setReaderName(attrs.getValue(DeviceProtocol.READERID));
                tagList = new ArrayList<Tag>();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("exception to do: ", e);
            }

        } else if (qName.equals(DeviceProtocol.TAG)) {
            currentTag = new Tag();
            currentTag.setCount(Integer.parseInt(attrs.getValue(DeviceProtocol.COUNT)));
            currentTag.setBinvalue(attrs.getValue(DeviceProtocol.BINVALUE).getBytes());
            currentTag.setValue(attrs.getValue(DeviceProtocol.VALUE));
        }
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        log.debug("characters");
        /*
         * While parsing a java.io.Reader (as opposed to a java.io.InputStream),
         * SAX parser calls this method when it encounters the '\n' character
         * ... no good for me 'cause in doing so, my charArray StringBuffer will
         * be over-written and populated with a '\n'.
         */
        if (buf[offset] == '\n') return;

        charArray.append(buf, offset, len);
    }

    public void endElement(String nameSpaceURI, String localName, String qName)
            throws SAXException {
        log.debug("QName=" + qName + " uri=" + nameSpaceURI);
        if (qName.equals(DeviceProtocol.TAG)) {
            tagList.add(currentTag);
        }
        charArray.delete(0, charArray.length());

        if (qName.equals(DeviceProtocol.READER_EVENT)) {
            readerEvent.setTagIds(tagList);
            events.add(readerEvent);
            tagList = null;
            readerEvent = null;
        }
    }

    public void endDocument() {
    }

    /**
     * 
     * @param dataQueue
     */
    public void registerSpecWithDeviceManagers(Queue dataQueue) {

        boolean success = true;

        DeviceECSpec deviceECSpec = getDeviceECSpec(dataQueue);

        List<LogicalDevice> logicalDevices = this.getLogicalDevices();

        Set<String> deviceManagerSet = new HashSet<String>();

        long timeout = 10000;
        for (LogicalDevice logicalDevice : logicalDevices) {

            Set<Reader> readers = logicalDevice.getReaderSet();

            for (Reader reader : readers) {

                deviceManagerSet.add(reader.getDeviceManagerId());

                deviceECSpec.addReader(reader.getName());
            }

        }

        for (String deviceManagerId : deviceManagerSet) {

            DeviceManager dManager;
            try {
                dManager = (DeviceManager) ServiceLocator.getService(
                        DeviceManager.class, deviceManagerId, timeout);

                dManager.registerDeviceECSpec(deviceECSpec);

                log.debug(getSpecName() + " is registered with DM");

            } catch (Exception e) {
                success = false;
                log.error("unable to register " + deviceManagerId, e);
            }
        }// end for all device managers

        if (!success)
            throw new InfrastructureException(
                    "not able to register with all device managers");
    }

    public boolean unRegisterSpecWithDeviceManagers()
            throws InfrastructureException {

        List<LogicalDevice> logicalDevices = this.getLogicalDevices();

        Set<String> deviceManagerSet = new HashSet<String>();

        long timeout = 10000;
        for (LogicalDevice logicalDevice : logicalDevices) {

            Set<Reader> readers = logicalDevice.getReaderSet();

            for (Reader reader : readers) {

                deviceManagerSet.add(reader.getDeviceManagerId());

            }

        }

        try {
            for (String deviceManagerId : deviceManagerSet) {

                log.debug("looking up :" + deviceManagerId);
                DeviceManager dManager;

                dManager = (DeviceManager) ServiceLocator.getService(
                        DeviceManager.class, deviceManagerId, timeout);

                log.debug("Located device manager = " + dManager
                        + " for deviceManagerId = " + deviceManagerId);
                dManager.unRegisterDeviceECSpec(getSpecName());
                log.debug(getSpecName() + " is unregistered with DM");
            }

        } catch (Exception e) {
            log.error("cannot unregister with Device Managers");
            throw new InfrastructureException(e);
        }
        return true;
    }

    public void sendReports() throws ImplementationException {
        try {

            ECReportsDocument reports = generateReports();

            for (Iterator iter = subscribers.iterator(); iter.hasNext();) {
                String subscriber = (String) iter.next();

                log.debug("subscriber=" + subscriber);

                log.debug("send reports: " + xml);

                /*
                 * Document doc = XMLUtil.generateDocFromECReports(reports);
                 * String xml = XMLUtil.generateXMLFromDoc(doc); NameValuePair
                 * nvSpecName = new NameValuePair("specName",specName);
                 * NameValuePair nvPair = new NameValuePair(specName,xml);
                 * nvpairs[0] = nvPair; nvpairs[1] = nvSpecName; HTTPConnector
                 * httpConnector = new HTTPConnector(subscriber,nvpairs);
                 * httpConnector.post();
                 */
                URI uri = new URI(subscriber);
                log.debug(uri.getHost() + ":" + uri.getPath());

                JMSUtil.deliverMessageToQueue(uri.getHost(), uri.getPath(),
                        reports.xmlText());

            }// end for
            /*
             * persists ReaderEvents
             * 
             * 
             * ReaderEventDAO readerEventDAO = ReaderEventDAOFactory.create();
             * for (Iterator iter = events.iterator(); iter.hasNext();) {
             * ReaderEvent element = (ReaderEvent) iter.next(); try {
             * readerEventDAO.updateReaderEvent(element); } catch
             * (InfrastructureException e) { // TODO Auto-generated catch block
             * e.printStackTrace(); } }// end for
             * 
             */
        } catch (URISyntaxException e) {
            log.error("sendReports failed: ", e);
            throw new ImplementationException(e);
        } catch (XmlException e) {
            log.error("sendReports failed: ", e);
            throw new ImplementationException(e);
        }

    }

    /**
     * 
     * @hibernate.property type="text" lazy="false"
     * 
     * @return Returns the xml.
     */
    public String getXml() {
        return xml;
    }

    /**
     * @param xml
     *            The xml to set.
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * 
     * 
     * @return Returns the reportSpecs.
     */
    public HashMap getReportSpecs() {
        return reportSpecs;
    }

    /**
     * @param reportSpecs
     *            The reportSpecs to set.
     */
    public void setReportSpecs(HashMap<String, ECReportSpec> reportSpecs) {
        this.reportSpecs = reportSpecs;
    }

    /**
     * @hibernate.property
     * 
     * @return Returns the currentState.
     */
    public int getCurrentState() {
        return currentState;
    }

    /**
     * @param currentState
     *            The currentState to set.
     */
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    private List<LogicalDevice> getLogicalDevices() {

        LogicalDeviceDAO logicalDeviceDAO = LogicalDeviceDAOFactory.create();

        Set<String> logicalDeviceNameList = this.getLogicalReaders();
        if (logicalDeviceNameList == null)
            throw new InfrastructureException("logicalDeviceNameList is null");

        if (logicalDeviceNameList.size() <= 0)
            throw new InfrastructureException("logicalDeviceNameList is empty");

        List<LogicalDevice> logicalDeviceList = new ArrayList<LogicalDevice>();

        LogicalDevice logicalDeviceExample = null;
        try {
            for (String logicalDeviceName : logicalDeviceNameList) {
                logicalDeviceExample = new LogicalDevice();
                logicalDeviceExample.clear();
                logicalDeviceExample.setName(logicalDeviceName);
                LogicalDevice ld = logicalDeviceDAO.get(logicalDeviceExample);
                if (ld == null)
                    throw new InfrastructureException(
                            "cannot find logical device: " + logicalDeviceName);
                logicalDeviceList.add(ld);
            }// end for
        } catch (InfrastructureException e) {
            throw e;
        }

        return logicalDeviceList;
    }

    private DeviceECSpec getDeviceECSpec(Queue dataQueue) {

        DeviceECSpec deviceECSpec = new DeviceECSpec();

        try {
            ECTime time = this.getBoundaries().getDuration();

            deviceECSpec.setSpecName(getSpecName());

            deviceECSpec.setDuration(time.getDuration());

            deviceECSpec.setQueueName(dataQueue.getQueueName());

            deviceECSpec.setHost(hostname);
            /*
             * create a new set, otherwise hibernate will have lazyloading that
             * will cause RMI martialing to fail, since ECSpec is not
             * serializable.
             */
            Set<String> logicalReaders = new HashSet<String>();
            logicalReaders.addAll(logicalReaders);
            deviceECSpec.setLogicalReaders(logicalReaders);

        } catch (JMSException e) {
            log.error("unable to get JMS Queue Name to create DeviceECSpec");
            throw new InfrastructureException(e);
        }

        return deviceECSpec;

    }
}
