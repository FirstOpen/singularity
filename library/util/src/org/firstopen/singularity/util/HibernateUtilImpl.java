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
package org.firstopen.singularity.util;

import java.rmi.dgc.VMID;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.SecondLevelCacheStatistics;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and
 * Transaction.
 * <p>
 * Manages SessionFactory in either JNDI or plain Java Application.
 * SessionFactory creation and holds Session and Transactions in thread local
 * variables. All exceptions are wrapped in an unchecked
 * InfrastructureException. Supports unmanged tranascion enviroment, as well as
 * managed CMT transaction enviroment. BMT is not supported by default, however,
 * it can be set.
 * 
 * 
 * @author Tom Rose (tom.rose@i-konect.com) $Id: HibernateUtilImpl.java 1242 2006-01-14 03:34:08Z TomRose $
 */

public class HibernateUtilImpl implements DAOUtil {

    private Log log = LogFactory.getLog(DAOUtil.class);

    private String jndiName = null;

    private final SessionFactoryManager sessionFactoryManager = new SessionFactoryManager();

   
    /**
     * @return Returns the supportBMT.
     */
    public boolean isSupportBMT() {
        SessionFactoryManager sfm = getSessionFactoryManager();
        Boolean bmt = sfm.threadSupportBMT.get();
        return bmt != null ? bmt.booleanValue() : false;
    }

    /**
     * @param supportBMT
     *            The supportBMT to set.
     */
    public void setSupportBMT(boolean allowBMT) {
        SessionFactoryManager sfm = getSessionFactoryManager();
        sfm.threadSupportBMT.set(new Boolean(allowBMT));
    }

    public SessionFactoryManager getSessionFactoryManager() {
        return sessionFactoryManager;
    }

    /**
     * 
     */
    public HibernateUtilImpl(String jndiName) {
        super();
        this.jndiName = jndiName;

    }

    /**
     * 
     */
    public HibernateUtilImpl() {
        super();
        this.jndiName = hibernateALEJNDIName;
    }

    /**
     * Returns the SessionFactory used for this class.
     * 
     * @return SessionFactory
     * @
     */
    public SessionFactory getSessionFactory() {

        SessionFactory sessionFactory = getSessionFactoryManager().sessionFactory;

        if (sessionFactory == null) {
            try {
                Context ctx = JNDIUtil.getInitialContext();
                sessionFactory = (SessionFactory) ctx.lookup(jndiName);
                getSessionFactoryManager().sessionFactory = sessionFactory;
            } catch (NamingException ex) {
                sessionFactory = getStandAloneSessionFactory();
                if (sessionFactory == null) {
                    throw new InfrastructureException(ex);
                }
            }
        }

        return sessionFactory;
    }

    public SessionFactory getStandAloneSessionFactory()
             {
        return new Configuration().configure().buildSessionFactory();
    }

    /**
     * Tries getCurrentSession for a managed environment, if that fails then
     * tries openSession. ThreadLocal Session is not set if JTA managed.
     */
    public Session getSession()  {

        SessionFactoryManager sfm = getSessionFactoryManager();

        Session s = sfm.threadSession.get();
        if (s == null) {
            log.debug("Opening new Session for this thread.");
            try {
                s = getSessionFactory().getCurrentSession();
                setJta(true);
                sfm.threadSession.set(s);  
            } catch (HibernateException e) {
                log.info("not JTA managed...trying openSession");
                try {
                    if (getInterceptor() != null) {
                        log.debug("Using interceptor: "
                                + getInterceptor().getClass());
                        s = getSessionFactory().openSession(getInterceptor());
                    } else {
                        s = getSessionFactory().openSession();
                    }
                    sfm.threadSession.set(s);  
                } catch (HibernateException ex) {
                    log.error("cannot getCurrentSession(), or openSession()");
                    Exception jtaException = new Exception(e);
                    Exception nonJTAException = new Exception(jtaException);
                    throw new InfrastructureException(nonJTAException);

                }
                
            }// end catch
            
        }// end if

        return s;
    }

    /**
     * Closes the Session local to the thread.
     */
    public void closeSession()  {
        SessionFactoryManager sfm = getSessionFactoryManager();

        Session s = sfm.threadSession.get();
        if (s != null) {
            try {
                sfm.threadSession.set(null);
                if (s.isOpen()) {
                    log.debug("Closing Session of this thread.");
                    if (isJta()) s.flush();
                    s.close();
                    
                    /*
                     * reset JTA indicator as session is closed
                     */
                    setJta(false);
                }
            } catch (HibernateException e) {
                log.error("unable to close session");
                throw new InfrastructureException(e);
            }
        }
    }

    /**
     * Start a new database transaction only if JTA not present.
     */
    public void beginTransaction()  {

        /*
         * if no session created already, it will create one,
         * and set transaction flags.
         */
        Session s = getSession();
        
        if (!isJta() || isSupportBMT()) {
            SessionFactoryManager sfm = getSessionFactoryManager();

            Transaction tx = (Transaction) sfm.threadTransaction.get();
            try {
                if (tx == null) {
                    tx = s.beginTransaction();
                    log
                    .debug("Starting new database transaction in this thread.");
          
                    sfm.threadTransaction.set(tx);
                }
            } catch (HibernateException ex) {
                throw new InfrastructureException(ex);
            }
        }
    }

    /**
     * Commit the database transaction.
     */
    public void commitTransaction()  {

        if (!isJta() || isSupportBMT()) {
            SessionFactoryManager sfm = getSessionFactoryManager();

            Transaction tx = (Transaction) sfm.threadTransaction.get();

            /*
             * if tx is null do nothing, else must be non-JTA
             */
            if (tx != null) {

                try {
                    if (!tx.wasCommitted() && !tx.wasRolledBack()) {
                        log
                                .debug("Committing database transaction of this thread.");
                        tx.commit();
                        sfm.threadTransaction.set(null);
                    }
                } catch (HibernateException ex) {
                    rollbackTransaction();
                    throw new InfrastructureException(ex);
                }

            }// end if
        }// if jta
    }

    /**
     * Rollback the database transaction.
     */
    public void rollbackTransaction()  {
        if (!isJta() == false || isSupportBMT() == true) {
            SessionFactoryManager sfm = getSessionFactoryManager();
            Transaction tx = (Transaction) sfm.threadTransaction.get();
            if (tx != null) {
                try {
                    sfm.threadTransaction.set(null);

                    if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
                        log
                                .debug("Tyring to rollback database transaction of this thread.");
                        tx.rollback();

                    }
                } catch (HibernateException ex) {
                    throw new InfrastructureException(ex);
                }
            }
        }// if jta
    }

    /**
     * Reconnects a Hibernate Session to the current Thread.
     * 
     * @param session
     *            The Hibernate Session to be reconnected.
     */
    public void reconnect(Session session)  {
        SessionFactoryManager sfm = getSessionFactoryManager();
        try {
            session.reconnect();
            sfm.threadSession.set(session);
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    /**
     * Disconnect and return Session from current Thread.
     * 
     * @return Session the disconnected Session
     */
    public Session disconnectSession()  {
        SessionFactoryManager sfm = getSessionFactoryManager();
        Session session = getSession();
        try {
            sfm.threadSession.set(null);
            if (session.isConnected())
                if (session.isOpen()) session.disconnect();
        } catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return session;
    }

    /**
     * Register a Hibernate interceptor with the current thread.
     * <p>
     * Every Session opened is opened with this interceptor after registration.
     * Has no effect if the current Session of the thread is already open,
     * effective on next close()/getSession().
     */
    public void registerInterceptor(Interceptor interceptor) {
        SessionFactoryManager sfm = getSessionFactoryManager();
        sfm.threadInterceptor.set(interceptor);
    }

    private Interceptor getInterceptor() {
        SessionFactoryManager sfm = getSessionFactoryManager();
        Interceptor interceptor = (Interceptor) sfm.threadInterceptor.get();
        return interceptor;
    }

    public void setStatisticsEnabled(boolean enabled)
             {
        getSessionFactory().getStatistics().setStatisticsEnabled(enabled);
    }

    public void printSecondLevelCacheStatistics(Class cachedClass)
             {

        SecondLevelCacheStatistics slcs = getSessionFactory().getStatistics()
                .getSecondLevelCacheStatistics(cachedClass.getName());
        log.debug("SecondLevelCacheStatistics = "
                + slcs.getElementCountInMemory());
    }

    /**
     * @return Returns the jndiName.
     */
    public String getJndiName() {
        return jndiName;
    }

    /**
     * @param jndiName
     *            The jndiName to set.
     */
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String generateUUID()  {
        VMID vmid = new VMID();
        return vmid.toString();
    }

    /**
     * @return Returns the jta.
     */
    public boolean isJta() {
        SessionFactoryManager sfm = getSessionFactoryManager();
        Boolean jta = sfm.threadJTA.get();
        return jta != null ? jta.booleanValue() : false;
    }

    /**
     * @param jta
     *            The jta to set.
     */
    public void setJta(boolean jta) {
        SessionFactoryManager sfm = getSessionFactoryManager();
        sfm.threadJTA.set(new Boolean(jta));
    }

}
