/* 
 * Copyright 2005 i-Konect LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.util;

import java.io.IOException;
import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.management.ObjectName;
import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;

/**
 * 
 * @author i-Konect LLC, Tom Rose
 * 
 * Old Style Queue/Topic utilites. Migrated implementation to J2EE 1.4 Specification 
 * Using the unified messaging API.
 * 
 */
public class JMSUtil {
    private static Log log = LogFactory.getLog(JMSUtil.class);

    public static Queue createTopic(String topicName)
            throws InfrastructureException {

        Queue queue = null;
        InitialContext jndiContext = null;
        try {
            queue = (Queue) jndiContext.lookup("topic/" + topicName);
        } catch (Exception e) {
            /*
             * try to create the queue
             */
            try {
                jndiContext = JNDIUtil.getInitialContext();

                ManagementHome mejbHome = (ManagementHome) jndiContext
                        .lookup("ejb/mgmt/MEJB");
                Management mejb = mejbHome.create();
                ObjectName objectName = new ObjectName(
                        "jboss.mq:service=DestinationManager");
                mejb.invoke(objectName, "createTopic", new Object[] {
                        topicName, "topic/" + topicName }, new String[] {
                        String.class.getName(), String.class.getName() });

                queue = queueExists(topicName);
            } catch (Exception e1) {
                log.error("unable to create queue/" + topicName, e1);
                throw new InfrastructureException();
            }
        }
        return queue;
    }

    public static Queue createQueue(String queueName)
            throws InfrastructureException {

        Queue queue = null;
        InitialContext jndiContext = null;
        try {
            queue = queueExists(queueName);
        } catch (Exception e) {
            /*
             * try to create the queue
             */
            try {
                jndiContext = JNDIUtil.getInitialContext();

                ManagementHome mejbHome = (ManagementHome) jndiContext
                        .lookup("ejb/mgmt/MEJB");
                Management mejb = mejbHome.create();
                ObjectName objectName = new ObjectName(
                        "jboss.mq:service=DestinationManager");
                mejb.invoke(objectName, "createQueue", new Object[] {
                        queueName, "queue/" + queueName }, new String[] {
                        String.class.getName(), String.class.getName() });

                queue = queueExists(queueName);
            } catch (Exception e1) {
                log.error("unable to create queue/" + queueName, e1);
                throw new InfrastructureException();
            }
        }
        return queue;
    }

    public static Queue queueExists(String queueName)
            throws InfrastructureException {
        Queue queue = null;
        try {
            InitialContext jndiContext = JNDIUtil.getInitialContext();
            queue = (Queue) jndiContext.lookup("queue/" + queueName);
        } catch (Exception x) {
            throw new InfrastructureException(x);
        }
        return queue;
    }

    public static Queue topicExists(String topicName)
            throws InfrastructureException {
        Queue queue = null;
        try {
            InitialContext jndiContext = JNDIUtil.getInitialContext();
            queue = (Queue) jndiContext.lookup("queue/" + topicName);
        } catch (Exception x) {
            throw new InfrastructureException(x);
        }
        return queue;
    }

    public static String listMessageCounter(String queueName) throws Exception {
        InitialContext jndiContext = JNDIUtil.getInitialContext();
        ManagementHome mejbHome = (ManagementHome) jndiContext
                .lookup("ejb/mgmt/MEJB");
        Management mejb = mejbHome.create();
        ObjectName objectName = new ObjectName(
                "jboss.mq.destination:service=Queue,id=" + queueName);
        return (String) mejb.invoke(objectName, "listMessageCounter",
                new Object[] {}, new String[] {});
    }

    public static Connection registerListenerOnQueue(
            MessageListener listener, String queueName) throws Exception {
        InitialContext jndiContext = JNDIUtil.getInitialContext();
        Queue queue = (Queue) jndiContext.lookup("queue/" + queueName);
        ConnectionFactory qcf = (ConnectionFactory) jndiContext
                .lookup("ConnectionFactory");
        Connection connection = qcf.createConnection();
        Session m_session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        MessageConsumer m_receiver = m_session.createConsumer(queue);
        m_receiver.setMessageListener(listener);
        return connection;
    }

    public static Connection getQueueConnection()
            throws InfrastructureException {
        InitialContext jndiContext;
        Connection connection;
        try {
            jndiContext = JNDIUtil.getInitialContext();
            ConnectionFactory qcf = (ConnectionFactory) jndiContext
                    .lookup("ConnectionFactory");
            connection = qcf.createConnection();
        } catch (Exception e) {

            log.error("unsable to get queue connnetion", e);
            throw new InfrastructureException("unsable to get queue connnetion");
        }

        return connection;
    }

    public static Queue getQueue(String queueName)
            throws InfrastructureException {
        InitialContext jndiContext;
        Queue queue;
        try {
            jndiContext = JNDIUtil.getInitialContext();

            queue = (Queue) jndiContext.lookup("queue/" + queueName);
        } catch (Exception e) {

            log.error("unsable to get queue connnetion" + queueName, e);
            throw new InfrastructureException("unsable to get queue connnetion"
                    + queueName);
        }
        return queue;
    }

    public static void terminateQueue(String queueName)
            throws InfrastructureException {
        InitialContext jndiContext;
        try {
            jndiContext = JNDIUtil.getInitialContext();
            ManagementHome mejbHome = (ManagementHome) jndiContext
                    .lookup("ejb/mgmt/MEJB");
            Management mejb = mejbHome.create();
            ObjectName objectName = new ObjectName(
                    "jboss.mq.destination:service=Queue,id=" + queueName);

            objectName = new ObjectName("jboss.mq:service=DestinationManager");
            mejb.invoke(objectName, "destroyQueue", new Object[] { queueName },
                    new String[] { String.class.getName() });
        } catch (Exception e) {

            log.error("Unable Terminate Queue: " + queueName, e);
            throw new InfrastructureException("Unable Terminate Queue: "
                    + queueName);
        }

    }

    public static void terminateTopic(String topicName)
            throws InfrastructureException {
        InitialContext jndiContext;
        try {
            jndiContext = JNDIUtil.getInitialContext();
            ManagementHome mejbHome = (ManagementHome) jndiContext
                    .lookup("ejb/mgmt/MEJB");
            Management mejb = mejbHome.create();
            ObjectName objectName = new ObjectName(
                    "jboss.mq.destination:service=Queue,id=" + topicName);
            // mejb.invoke(objectName, "stop", new Object[]{}, new String[]{});
            objectName = new ObjectName("jboss.mq:service=DestinationManager");
            mejb.invoke(objectName, "destroyTopic", new Object[] { topicName },
                    new String[] { String.class.getName() });
        } catch (Exception e) {

            log.error("Unable Terminate Topic: " + topicName, e);
            throw new InfrastructureException("Unable Terminate Topic: "
                    + topicName);
        }

    }

    public static void deliverMessageToTopic(String host, String topicName,
            String xml) {
        log.debug("IntegrationMod.deliverMessageToQueue queueName = "
                + topicName + " and doc = " + xml);

        char test = topicName.charAt(0);

        if (test == '/')
            topicName = topicName.substring(1);

        try {

            InitialContext context = JNDIUtil.getInitialContext(host);

            Connection connection = null;
            Session session = null;
            MessageProducer publisher = null;
            ConnectionFactory tcf = (ConnectionFactory) context
                    .lookup("ConnectionFactory");

            Topic topic = (Topic) context.lookup(topicName);

            connection = tcf.createConnection();
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            publisher = session.createProducer(topic);

            TextMessage message = session.createTextMessage();

            log.debug("message value is -> " + xml);

            message.setText(xml);

            publisher.send(message);

        } catch (Exception e) {
            log.error("unable to send message on topic", e);
        } finally {

        }
    }

    public static void deliverMessageToQueue(String host, String queueName,
            String xml) {
        log.debug("IntegrationMod.deliverMessageToQueue queueName = "
                + queueName + " and doc = " + xml);
        MessageProducer m_sender = null;
        Session m_session = null;
        Connection connection = null;

        char test = queueName.charAt(0);

        if (test == '/')
            queueName = queueName.substring(1);

        try {

            InitialContext context = JNDIUtil.getInitialContext(host);

            ConnectionFactory qcf = (ConnectionFactory) context
                    .lookup("ConnectionFactory");

            Queue queue = (Queue) context.lookup("queue/" + queueName);

            connection = qcf.createConnection();

          
            m_session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            m_sender = m_session.createProducer(queue);

            TextMessage message = m_session.createTextMessage();

            log.debug("message value is -> " + xml);

            message.setText(xml);

            m_sender.send(message);

        } catch (Exception e) {
            log.error("IntegrationMod.deliverMessageToQueue() Exception = ", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    log.error("unable ot close JMS Connection", e);
                }
            }
        }
    }

    public static void deliverMessageToQueue(String host, String queueName,
            Document doc) {
        log.debug("IntegrationMod.deliverMessageToQueue queueName = "
                + queueName + " and doc = " + doc);

        OutputFormat format = new OutputFormat(doc, "UTF-8", true);

        StringWriter resultStringWriter = new StringWriter();
        XMLSerializer xmlSerializer = new XMLSerializer(resultStringWriter,
                format);
        xmlSerializer.setNamespaces(true);
        try {
            xmlSerializer.serialize(doc);

            log.debug("IntegrationMod.deliverMessageToQueue queueName = "
                    + queueName + " and doc = " + doc);
            String xml = resultStringWriter.toString();

            deliverMessageToQueue(host, queueName, xml);

        } catch (IOException e) {
            log.error(e);
        }

    }

    public static void deliverMessageToQueue(String queueName, Document doc) {
        deliverMessageToQueue("localhost", queueName, doc);

    }

    public static void deliverMessageToQueue(String queueName, String xml) {
        log.debug("IntegrationMod.deliverMessageToQueue queueName = "
                + queueName + " and xml = " + xml);
        deliverMessageToQueue("localhost", queueName, xml);
    }

}
