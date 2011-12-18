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

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.firstopen.singularity.ale.dao.ECSpecDAO;
import org.firstopen.singularity.ale.dao.ECSpecDAOFactory;
import org.firstopen.singularity.ale.exception.ImplementationException;
import org.firstopen.singularity.ale.exception.NoSuchSubscriberException;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JMSUtil;
import org.firstopen.singularity.util.JNDIUtil;
import org.w3c.dom.Document;

/**
 * 
 * @author TomRose
 * @version $Id: AleSLSB.java 1262 2006-02-17 03:36:20Z TomRose $
 * 
 * TODO: push ECSpec behaviors out of AleSLB. Too much ECSpec behavior must be
 * defined outside the object.
 */
public class AleSLSB implements SessionBean {
    /**
     * 
     */
    private static final long serialVersionUID = 4187500022358481093L;

    private SessionContext sessionContext = null;

    private Logger log = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#ejbActivate()
     */
    public void ejbActivate() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#ejbPassivate()
     */
    public void ejbPassivate() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#ejbRemove()
     */
    public void ejbRemove() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#ejbCreate()
     */
    public void ejbCreate() {
        log = Logger.getLogger(getClass());
        if (System.getSecurityManager() == null) {
            log.debug("onMessage() Instantiating new RMISecurityManager");
            System.setSecurityManager(new SecurityManager());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#setSessionContext(javax.ejb.SessionContext)
     */
    public void setSessionContext(SessionContext sessionContext)
            throws EJBException, RemoteException {
        this.sessionContext = sessionContext;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#define(org.w3c.dom.Document)
     */
    public String define(Document doc) {
        ECSpecBuilder specBuilder = new ECSpecBuilder(doc);
        ECSpec spec = specBuilder.buildECSpec();
        define(spec.getSpecName(), spec);

        return spec.getSpecName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#define(java.lang.String,
     *      org.firstopen.singularity.ale.ECSpec)
     */
    public void define(String specName, ECSpec spec) {
        ECSpecDAO ecSpecDAO = null;
        try {
            ecSpecDAO = ECSpecDAOFactory.create();

            ecSpecDAO.update(spec);

            log.debug("ECSpec Saved");

            JMSUtil.createQueue(specName);

            /*
             * TODO: perhaps change name to something else, or remove,
             * as this may fail in some JMS implemenations, even if 
             * JNDI name is different.
             */
            
            JMSUtil.createTopic(specName);

        } catch (InfrastructureException e) {
            log.error("unable to store ECSpec " + specName);
            throw new EJBException(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#unDefine(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public void unDefine(String specName) throws NoSuchSubscriberException,
            ImplementationException {

        ECSpecDAO ecSpecDAO = null;

        try {
            ecSpecDAO = ECSpecDAOFactory.create();

            ECSpec spec = ecSpecDAO.get(specName);

            /*
             * allocate list to a new array so the conncurrent modification
             * exception will not be thrown on unsubscribe as the sbuscriber
             * list changes.
             */
            String[] subscribers = (String[]) spec.getSubscribers().toArray(
                    new String[1]);

            for (String subscriber : subscribers) {
                unSubscribe(spec, subscriber);
            }

            ecSpecDAO.delete(spec);

            try {
                JMSUtil.queueExists(specName);
                JMSUtil.terminateQueue(specName);
            } catch (InfrastructureException e) {
                log.warn("ecSpec " + specName
                        + " deleted, queue does not exists.");
            }
        } catch (InfrastructureException e) {
            log.error(e);
            throw new ImplementationException(e);

        } 
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#getECSpecNames()
     */
    public List getECSpecNames() {
        List specNameList = null;
        // RETURN A LIST OF ALL ECSPECS NAMES IN DATABASE
        return specNameList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#subscribe(java.lang.String,
     *      java.lang.String)
     */
    public void subscribe(String specName, String notificationURI) {

        ECSpec spec = null;
        ECTime repeatPeriod = null;

        /*
         * TODO: need to move state managment to ECSpec should not be here.
         * 
         * register notificationURI with ECSpec
         */

        ECSpecDAO ecSpecDAO = null;
        try {
            ecSpecDAO = ECSpecDAOFactory.create();

            log.debug("ECSpec " + specName);

            spec = ecSpecDAO.get(specName);

            /*
             * create message queue for device mangers to send events for this
             * ecspec. will exist for the life of the ECSpec, if for some reason
             * its destroyed, re-initalize
             */
            try {
                JMSUtil.queueExists(specName);
            } catch (InfrastructureException e) {
                /*
                 * Recover and reset state of ECSpec to reinialize
                 */
                JMSUtil.createQueue(specName);
                spec.setCurrentState(ECSpec.INACTIVE_STATE);
            }

            spec.registerNotificationURI(notificationURI);

            repeatPeriod = spec.getBoundaries().getRepeatPeriod();

            if (spec.getCurrentState() == ECSpec.INACTIVE_STATE) {

                initializeECSpecTimer(repeatPeriod.getDuration(), repeatPeriod
                        .getDuration(), specName);

                spec.setCurrentState(ECSpec.ACTIVE_STATE);

                ecSpecDAO.update(spec);

            }

            log.debug("ECSpec updated");

        } catch (Exception e) {
            log.error("unable to subscribe to " + specName);
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#unSubscribe(java.lang.String,
     *      java.lang.String)
     */
    public void unSubscribe(String specName, String notificationURI) {

        ECSpecDAO ecSpecDAO = null;
        try {
            ecSpecDAO = ECSpecDAOFactory.create();

            ECSpec spec = (ECSpec) ecSpecDAO.get(specName);

            unSubscribe(spec, notificationURI);

            ecSpecDAO.update(spec);

        } catch (Exception e) {
            throw new EJBException(e);

        } 
    }

    /*
     * Caller required to persists modifed ECSpec
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#unSubscribe(org.firstopen.singularity.ale.ECSpec,
     *      java.lang.String)
     */
    public void unSubscribe(ECSpec spec, String notificationURI)
            throws NoSuchSubscriberException, ImplementationException {

        spec.unRegisterNotificationURI(notificationURI);

        if (spec.getSubscribers().isEmpty()) {
            spec.setCurrentState(ECSpec.INACTIVE_STATE);

            try {
                spec.unRegisterSpecWithDeviceManagers();
            } catch (InfrastructureException e) {
                log.error("cannot unregister Device Managers");
                throw new ImplementationException();
            }

        }

    }

    /*
     * -- ECSpec has already been defined -- method blocks until ECSpec thread
     * returns ECReports
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#poll(java.lang.String)
     */
    public ECReports poll(String specName) {
        log.debug("poll()  specName = " + specName);
        ECReports ecReports = null;
        ECSpec spec;
        // spec = CALL_HIBERNATE(specName);

        /*
         * 26 Oct 04 -- JA Bride As of now, I won't let an ECSpec that currently
         * has subscribers and is active to be polled ... this decision may
         * change based on user feedback and further development of the ALE spec
         * if(!spec.isAlive()) { ecReports = spec.poll(); //NOW IN ACTIVE
         * STATE(line #617) } else throw new ImplementationException(specName +"
         * has already been subscribed to and is currently active. Please
         * re-attempty to poll after all current subscribers have
         * un-subscribed.");
         */

        return ecReports;
    }

    /* ECSpec has not been previously defined */
    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#immediate(org.w3c.dom.Document)
     */
    public ECReports immediate(Document doc) throws ImplementationException {
        String specName = define(doc);
        ECReports reports = poll(specName);
        try {
            unDefine(specName);
        } catch (NoSuchSubscriberException e) {
            log.error("no such subscriber");
            throw new ImplementationException(e);
        }
        return reports;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#getSubscribers(java.lang.String)
     */
    public Set getSubscribers(String specName) {
        ECSpec spec = null;
        /*
         * try { // spec = CALL_HIBERNATE; }
         * catch(org.firstopen.singularity.ale.exception.SecurityException x) {
         * x.printStackTrace(); throw new ImplementationException("A
         * SecurityException was thrown when attempting to get "+specName); }
         */
        return spec.getSubscribers();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#getStandardVersion()
     */
    public String getStandardVersion() {
        return "1.0";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#getVendorVersion()
     */
    public String getVendorVersion() {
        return "1.0";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#initializeECSpecTimer(long, long,
     *      java.lang.String)
     */
    public void initializeECSpecTimer(long duration, long repeatPeriod,
            String timerName) {
        ECSpecTimerSLSBLocal tSLSB = null;
        InitialContext jndiContext;
        try {
            jndiContext = JNDIUtil.getInitialContext();

            ECSpecTimerSLSBHome tHome = (ECSpecTimerSLSBHome) jndiContext
                    .lookup("ejb/ale/ECSpecTimerSLSB");
            tSLSB = tHome.create();

            tSLSB.initializeTimer(duration, repeatPeriod, timerName);

        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.test#initialize()
     */
    public void initialize() {

    }

}
