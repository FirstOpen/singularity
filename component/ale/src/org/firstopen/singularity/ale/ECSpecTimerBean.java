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

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.firstopen.singularity.ale.dao.ECSpecDAO;
import org.firstopen.singularity.ale.dao.ECSpecDAOFactory;
import org.firstopen.singularity.ale.exception.ImplementationException;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JMSUtil;
import org.firstopen.singularity.util.XMLUtil;

public class ECSpecTimerBean implements SessionBean, TimedObject {
    /**
     * 
     */
    private static final long serialVersionUID = 2196640002068305676L;

    private Logger log = null;

    private SessionContext sc;

    private TimerHandle timerHandle = null;

    private DAOUtil hibernateUtil = null;

    public void ejbCreate() {
        log = Logger.getLogger(getClass());
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        sc = ctx;
    }

    public void initializeTimer(long initialPeriod, long repeatPeriod,
            String timerName) throws Exception {
        final String specName = timerName;

        log.debug("initializeTimer(" + initialPeriod + ") timerName = "
                + timerName);

        ECSpecDAO ecSpecDAO = ECSpecDAOFactory.create();

        ECSpec spec = ecSpecDAO.get(specName);

        ECSpecBuilder specBuilder = new ECSpecBuilder(XMLUtil
                .generateDocFromXML(spec.getXml()));
        /*
         * TODO: useing xml to store object, need to fix hibernate mapping
         * annotation for more complex persistance model.
         */
        ECSpec ecSpec2 = specBuilder.buildECSpec();

        spec.setReportSpecs(ecSpec2.getReportSpecs());

        /*
         * queue is already created when ECSpec is subscribed to for the first
         * time.
         */
        Queue dataQueue = JMSUtil.queueExists(specName);

        spec.registerSpecWithDeviceManagers(dataQueue);

        TimerService ts = sc.getTimerService();

        ts.createTimer(initialPeriod, repeatPeriod, specName);

    }

    public void ejbTimeout(Timer timer) {
        log.debug("ejbTimeout() timerName = " + timer.getInfo());

        String specName = (String) timer.getInfo();

        ECSpecDAO ecSpecDAO = ECSpecDAOFactory.create();
        
        Connection connection = null;
        Session queueSession = null;
        try {


            ECSpec spec =  ecSpecDAO.get(specName);
            
            ECSpecBuilder specBuilder = new ECSpecBuilder(XMLUtil
                    .generateDocFromXML(spec.getXml()));

            ECSpec ecSpec2 = specBuilder.buildECSpec();

            spec.setReportSpecs(ecSpec2.getReportSpecs());

            if (spec.getCurrentState() == ECSpec.INACTIVE_STATE) {
                timer.cancel();
                spec.unRegisterSpecWithDeviceManagers();
            }

            /*
             * fail over, when server crashes timer events will resume, if the
             * queue is not created then must recreate.
             */
            Queue dataQueue = JMSUtil.queueExists(specName);

            if (dataQueue == null) {
                dataQueue = JMSUtil.createQueue(specName);

            }
            log.debug("Registering Listener for Queue " + specName);

            connection = JMSUtil.getQueueConnection();
            queueSession = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            MessageConsumer receiver = queueSession.createConsumer(JMSUtil
                    .getQueue(specName));

            connection.start();

            /*
             * Wait for the first message, then read all the messages out of the
             * queue, then shut it down
             */
            Message m = receiver.receive();
            while (true) {
                if (m == null) break;
                if (m instanceof TextMessage) {
                    spec.onMessage(m);
                } else {
                    break;
                }
                /*
                 * keep reading until messages stop comming in Note: this may
                 * mean that some messages will be disgarded because they are
                 * outside the window need to spend more time on the "late
                 * message model" for temporal event processing.
                 */

                m = receiver.receiveNoWait();
            }

            log.debug("# of subscribers = " + spec.getSubscribers().size());

            try {
                queueSession.close();
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                log
                        .error(
                                "unable to close JMS Queue connection and session",
                                e);
            }
            spec.sendReports();

        
        } catch (InfrastructureException e) {
            log.error("event cycle is not complete, cannot fine ECSPec id=" + specName, e);
        } catch (JMSException e) {
            log.error("event cycle is not complete, unable to connect to Queue", e);
        } catch (ImplementationException e) {
            log.error("event cycle is not complete", e);
        } finally {
          DAOUtilFactory.close();
        }

    }

    public void cancelTimer(String timerName) {
        log.debug("cancelTimer() timerName = " + timerName);
        try {
            TimerService ts = sc.getTimerService();
            Collection timers = ts.getTimers();
            Iterator it = timers.iterator();
            while (it.hasNext()) {
                Timer myTimer = (Timer) it.next();
                if ((myTimer.getInfo().equals(timerName))) {
                    myTimer.cancel();
                    log.debug("Successfully Cancelled " + timerName);

                }
            }
        } catch (Exception x) {
            log.error(x);
        }
        return;
    }

    public void getTimerInfo() {
        if (timerHandle != null) {
            Timer timer = timerHandle.getTimer();
            log.debug("getTimerInfo = " + timer.getInfo());
        }
    }
}
