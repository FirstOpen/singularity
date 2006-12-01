/*
 * Copyright 2005 i-Konect LLC
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
package org.firstopen.singularity.admin.view;

import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.apache.xerces.parsers.DOMParser;
import org.firstopen.custom.view.EventMonitorBean;
import org.firstopen.epc.ale.ECBoundarySpec;
import org.firstopen.epc.ale.ECFilterSpec;
import org.firstopen.epc.ale.ECIncludePatterns;
import org.firstopen.epc.ale.ECLogicalReaders;
import org.firstopen.epc.ale.ECReportOutputSpec;
import org.firstopen.epc.ale.ECReportSetEnum;
import org.firstopen.epc.ale.ECReportSetSpec;
import org.firstopen.epc.ale.ECReportSpec;
import org.firstopen.epc.ale.ECReportSpecs;
import org.firstopen.epc.ale.ECSpec;
import org.firstopen.epc.ale.ECSpecDocument;
import org.firstopen.epc.ale.ECTime;
import org.firstopen.epc.ale.ECTimeUnit;
import org.firstopen.singularity.ale.AleSLSBHome;
import org.firstopen.singularity.ale.AleSLSBRemote;
import org.firstopen.singularity.ale.dao.ECSpecDAO;
import org.firstopen.singularity.ale.dao.ECSpecDAOFactory;
import org.firstopen.singularity.config.LogicalDevice;
import org.firstopen.singularity.config.dao.LogicalDeviceDAO;
import org.firstopen.singularity.config.dao.LogicalDeviceDAOFactory;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JNDIUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: ECSpecBean.java 925 2005-11-03 05:23:05Z TomRose $
 */
public class ECSpecBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1248646483293185658L;

    private long duration = 2000;

    private String notificationURI = "jms://localhost/"
            + EventMonitorBean.MONITOR_NAME;

    private AleSLSBRemote aSLSB = null;

    private String ecSpecGenName = null;

    private List<String> subscriberNames = null;

    private List<SelectItem> subscribers = new ArrayList<SelectItem>();

    private List<String> selected = new ArrayList<String>();

    private List<org.firstopen.singularity.ale.ECSpec> list = null;

    private List<SelectItem> selectItemList = new ArrayList<SelectItem>();

    private List<SelectItem> logicalECSpecList = new ArrayList<SelectItem>();

    List<LogicalDevice> logicalDeviceList = null;

    private Log log = LogFactory.getLog(ECSpecBean.class);

    private UIData uiTable;

    private org.firstopen.singularity.ale.ECSpec currentRow = null;

    /* hack just for a moment while painting the screens */

    private String currentECSpecName = null;

  
    public ECSpecBean() throws Exception {
        this("ECSpecBean");

    }

    public ECSpecBean(String name) {

        try {
            if (System.getSecurityManager() == null) {

                System.setSecurityManager(new SecurityManager());
            }

            InitialContext jndiContext = JNDIUtil.getInitialContext();
            Object objref = jndiContext
                    .lookup("jnp://localhost:1099/ejb/ale/AleSLSB");
            AleSLSBHome aleSLSBHome = (AleSLSBHome) PortableRemoteObject
                    .narrow(objref, AleSLSBHome.class);

            aSLSB = aleSLSBHome.create();

            createLogicalDeviceSelectList();

            createECSpecSelectList();

        } catch (Exception e) {
            log.error("can't create ECSpecBean");
            /*
             * can't recover wrap in RuntimeException
             */
            throw new InfrastructureException(e);
        } 
    }

    private void createLogicalDeviceSelectList() {

        try {
            LogicalDeviceDAO logicalDeviceDAO = LogicalDeviceDAOFactory
                    .create();
            logicalDeviceList = logicalDeviceDAO.getAll();

            createLogicalDeviceSelectList(logicalDeviceList);
        } catch (InfrastructureException e) {
            log.error("unable to retreive physical Readers", e);
        }

    }

    private void createLogicalDeviceSelectList(Set<String> logicalDeviceNames) {
        selectItemList.clear();
        for (String logicalDeviceName : logicalDeviceNames) {
            selectItemList.add(new SelectItem(logicalDeviceName));
        }
    }

    private void createLogicalDeviceSelectList(
            List<LogicalDevice> logicalDeviceList) {

        selectItemList.clear();

        for (LogicalDevice logicalDevice : logicalDeviceList) {

            selectItemList.add(new SelectItem(logicalDevice.getName()));

        }

    }

    private void createECSpecSelectList() {
        ECSpecDAO ecSpecDAO = ECSpecDAOFactory.create();
        try {
            list = ecSpecDAO.getAll();
            if (list != null) {
                if (list.size() > 0) {
                    this.currentRow = list.get(0);
                }
            }

            if (currentRow == null) {
                currentRow = new org.firstopen.singularity.ale.ECSpec();
            }

            logicalECSpecList.clear();

            for (org.firstopen.singularity.ale.ECSpec ecSpec : list) {
                logicalECSpecList.add(new SelectItem(ecSpec.getSpecName()));
            }
        } catch (Exception e) {
            log.error("unable to retreive ECSpec  list", e);
        }
    }

    public void testMPR2010_AWID_define() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        long longVal = random.nextLong();
        String ecSpecGenName = new String("ecSpec" + longVal);

        aSLSB.define(generateECSpecDocument(ecSpecGenName));

        aSLSB.subscribe(ecSpecGenName, notificationURI);
    }

    private Document generateECSpecDocument(String specName) {

        ECSpecDocument specDoc = ECSpecDocument.Factory.newInstance();

        ECSpec ecSpecDoc = specDoc.addNewECSpec();
        ecSpecDoc.setSpecName(specName);
        ECLogicalReaders logicalReaders = ecSpecDoc.addNewLogicalReaders();
        for (String logicalReaderName : selected) {
            logicalReaders.addLogicalReader(logicalReaderName);
            log.debug("logicalReaderName is: " + logicalReaderName);
        }
        ECBoundarySpec boundarySpec = ecSpecDoc.addNewBoundarySpec();
        ECTime repeatPeriod = boundarySpec.addNewRepeatPeriod();
        repeatPeriod.setLongValue(duration);
        repeatPeriod.setUnit(ECTimeUnit.MS);
        ECTime ecTime = boundarySpec.addNewDuration();
        ecTime.setLongValue(duration);
        ecTime.setUnit(ECTimeUnit.MS);
        ECReportSpecs reportSpecs = ecSpecDoc.addNewReportSpecs();
        ECReportSpec reportSpec = reportSpecs.addNewReportSpec();
        reportSpec.setReportName("ReportName");
        ECReportSetSpec reportSetSpec = reportSpec.addNewReportSet();
        reportSetSpec.setSet(ECReportSetEnum.CURRENT);
        ECReportOutputSpec outputSpec = reportSpec.addNewOutput();
        outputSpec.setIncludeTag(true);
        outputSpec.setIncludeList(true);
        ECFilterSpec filterSpec = reportSpec.addNewFilterSpec();
        ECIncludePatterns includePatterns = filterSpec.addNewIncludePatterns();
        includePatterns.addIncludePattern("*");

        log.debug(specDoc.xmlText());

        return generateDocFromXML(specDoc.xmlText());
    }

    public void delete(ActionEvent e) {

        try {
            aSLSB.unDefine(ecSpecGenName);
            createECSpecSelectList();
        } catch (Exception ex) {
            log.error("cannot delete ECSpec", ex);
        } finally {
            DAOUtilFactory.close();
        }

    }

    public void unsubscribe(ActionEvent e) {

        try {
            aSLSB.unSubscribe(ecSpecGenName, notificationURI);

        } catch (RemoteException e1) {
            log.error(e1);
        } catch (Exception e1) {
            log.error(e1);
        }

    }

    public void define(ActionEvent e) {

        /*
         * Random random = new Random(System.currentTimeMillis()); long longVal =
         * random.nextLong(); ecSpecGenName = new String("ecSpec" + longVal);
         */

        try {
            aSLSB.define(generateECSpecDocument(ecSpecGenName));

            createECSpecSelectList();
        } catch (RemoteException e1) {
            log.error(e1);
        } finally {

            DAOUtilFactory.close();
        }

    }

    public void subscribe(ActionEvent e) {

        try {
            aSLSB.subscribe(ecSpecGenName, notificationURI);

        } catch (RemoteException e1) {
            log.error(e1);
        } catch (Exception e1) {
            log.error(e1);
        }

    }

   

    /**
     * 
     * @hibernate.property
     * @return Returns the duration.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @param duration
     *            The duration to set.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * 
     * @hibernate.property
     * @return Returns the notificationURI.
     */
    public String getNotificationURI() {
        return notificationURI;
    }

    /**
     * @param notificationURI
     *            The notificationURI to set.
     */
    public void setNotificationURI(String notificationURI) {
        this.notificationURI = notificationURI;
    }

    public Document generateDocFromXML(String xml)

    {

        InputSource inputSource = new InputSource(new StringReader(xml));
        DOMParser parser = new DOMParser();
        try {
            parser.parse(inputSource);
        } catch (SAXException e) {
            log.error("exception to do: ", e);
        } catch (IOException e) {
            log.error("exception to do: ", e);
        }

        return parser.getDocument();
    }

    public void getDetail(ActionEvent e) {

        currentRow = (org.firstopen.singularity.ale.ECSpec) uiTable
                .getRowData();
        ECSpecDAO ecSpecDAO = ECSpecDAOFactory.create();
        try {
            currentRow = ecSpecDAO.get(currentRow);
            currentRow.getXml();

            createLogicalDeviceSelectList(currentRow.getLogicalReaders());
            setEcSpecGenName(currentRow.getSpecName());
            org.firstopen.singularity.ale.ECBoundarySpec boundarySpec = currentRow
                    .getBoundaries();
            setDuration(boundarySpec.getDuration().getDuration());

            createSubscriberSelectList();
        } catch (InfrastructureException e1) {
            log.error("unable to reteive most recent version of ECSpec");
        } finally {
            DAOUtilFactory.close();
        }

    }

    /**
     * 
     */
   
    private void createSubscriberSelectList() {

        if (subscribers != null) {
            subscribers.clear();
        }
        if (subscriberNames != null) {
            subscriberNames.clear();
        }

        Set<String> subscriberSet = currentRow.getSubscribers();
        for (String subscriber : subscriberSet) {
            subscribers.add(new SelectItem(subscriber));
        }

    }

    public void create(ActionEvent e) {
        createLogicalDeviceSelectList();
        createSubscriberSelectList();
        ecSpecGenName = null;
        duration = 0;
    }

    public void update(ActionEvent e) {
        ECSpecDAO ecSpecDAO = ECSpecDAOFactory.create();
        try {
            ecSpecDAO.update(getCurrentRow());

        } catch (InfrastructureException e1) {
            log.error("unable to update ECSpec", e1);
        } finally {
            DAOUtilFactory.close();
        }

        // ecSpecDAO.update(this.getCurrentECSpec());
    }

    /*
     * 
     * public String next() {
     * 
     * ECSpecDocument specDoc = ECSpecDocument.Factory.newInstance();
     * 
     * ECSpec ecSpecDoc = specDoc.addNewECSpec();
     * ecSpecDoc.setSpecName(currentECSpecName); ECLogicalReaders logicalReader =
     * ecSpecDoc.addNewLogicalReaders();
     * logicalReader.addLogicalReader("MPR2010_AWID_01"); ECBoundarySpec
     * boundarySpec = ecSpecDoc.addNewBoundarySpec(); ECTime ecTime =
     * boundarySpec.addNewDuration(); ecTime.setLongValue(duration);
     * ecTime.setUnit(ECTimeUnit.MS); ECReportSpecs reportSpecs =
     * ecSpecDoc.addNewReportSpecs(); ECReportSpec reportSpec =
     * reportSpecs.addNewReportSpec(); reportSpec.setReportName("Report1");
     * ECReportSetSpec reportSetSpec = reportSpec.addNewReportSet();
     * reportSetSpec.setSet(ECReportSetEnum.CURRENT); ECReportOutputSpec
     * outputSpec = reportSpec.addNewOutput(); outputSpec.setIncludeTag(true);
     * outputSpec.setIncludeList(true); ECFilterSpec filterSpec =
     * reportSpec.addNewFilterSpec(); ECIncludePatterns includePatterns =
     * filterSpec.addNewIncludePatterns();
     * includePatterns.addIncludePattern("*");
     * 
     * log.debug(specDoc.xmlText());
     * 
     * return "success";
     *  }
     */
    /**
     * @return Returns the selectItemList.
     */
    public List<SelectItem> getSelectItemList() {
        return selectItemList;
    }

    /**
     * @param selectItemList
     *            The selectItemList to set.
     */
    public void setSelectItemList(List<SelectItem> logicalReaderList) {
        this.selectItemList = logicalReaderList;
    }

    /**
     * @return Returns the logicalECSpecList.
     */
    public List<SelectItem> getLogicalECSpecList() {
        return logicalECSpecList;
    }

    /**
     * @param logicalECSpecList
     *            The logicalECSpecList to set.
     */
    public void setLogicalECSpecList(List<SelectItem> logicalECSpecList) {
        this.logicalECSpecList = logicalECSpecList;
    }

    /**
     * @return Returns the ecSpecGenName.
     */
    public String getEcSpecGenName() {
        return ecSpecGenName;
    }

    /**
     * @param ecSpecGenName
     *            The ecSpecGenName to set.
     */
    public void setEcSpecGenName(String ecSpecGenName) {
        this.ecSpecGenName = ecSpecGenName;
    }

    /**
     * @return Returns the selected.
     */
    public List<String> getSelected() {
        return selected;
    }

    /**
     * @param selected
     *            The selected to set.
     */
    public void setSelected(List<String> logicalReaderNames) {
        this.selected = logicalReaderNames;
    }

    /**
     * @return Returns the uiTable.
     */
    public UIData getUiTable() {
        return uiTable;
    }

    /**
     * @param uiTable
     *            The uiTable to set.
     */
    public void setUiTable(UIData uiTable) {
        this.uiTable = uiTable;
    }

    public org.firstopen.singularity.ale.ECSpec getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(org.firstopen.singularity.ale.ECSpec currentECSpec) {
        this.currentRow = currentECSpec;
    }

    public String getCurrentECSpecName() {
        return currentECSpecName;
    }

    public void setCurrentECSpecName(String currentECSpecName) {
        this.currentECSpecName = currentECSpecName;
    }

    /**
     * @return Returns the list.
     */
    public List<org.firstopen.singularity.ale.ECSpec> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(List<org.firstopen.singularity.ale.ECSpec> list) {
        this.list = list;
    }

    /**
     * @return Returns the subscriberNames.
     */
    public List<String> getSubscriberNames() {
        return subscriberNames;
    }

    /**
     * @param subscriberNames
     *            The subscriberNames to set.
     */
    public void setSubscriberNames(List<String> subscribers) {
        this.subscriberNames = subscribers;
    }

    /**
     * @return Returns the subscribers.
     */
    public List<SelectItem> getSubscribers() {
        return subscribers;
    }

    /**
     * @param subscribers
     *            The subscribers to set.
     */
    public void setSubscribers(List<SelectItem> subscribers) {
        this.subscribers = subscribers;
    }

    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: " + scrollerEvent.getScrollerfacet()
                        + ", pageindex: " + scrollerEvent.getPageIndex());
    }
}
