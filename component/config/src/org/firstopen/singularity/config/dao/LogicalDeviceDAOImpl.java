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

package org.firstopen.singularity.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.LogicalDevice;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * 
 */
public class LogicalDeviceDAOImpl implements LogicalDeviceDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil hibernateUtil = null;

    /**
     * @throws InfrastructureException 
     * 
     */
    public LogicalDeviceDAOImpl() throws InfrastructureException {
        super();
        hibernateUtil = DAOUtilFactory
                .create(DAOUtil.hibernateALEJNDIName);
        hibernateUtil.beginTransaction();

    }

    public LogicalDevice get(String id) throws InfrastructureException {
        Session session = null;
        LogicalDevice logicalDevice = null;

        try {
            session = hibernateUtil.getSession();
            
            logicalDevice = (LogicalDevice) session.load(LogicalDevice.class,
                    id);
        } catch (HibernateException e) {
            log.error("unable to update or save LogicalDevice");
            throw new InfrastructureException(e);
        }
        return logicalDevice;
    }

    public List findByAttrName(Map attributeValueMap) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<LogicalDevice> getAll() throws InfrastructureException {

        List<LogicalDevice> logicalDevices = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
            
            logicalDevices = session.createQuery("from LogicalDevice").list();
        } catch (HibernateException e) {
            log.error("unable to getAll LogicalDevices");

            throw new InfrastructureException(e);
        }
        return logicalDevices;
    }

    public void update(LogicalDevice logicalDevice)
            throws InfrastructureException {

        Session session = null;
        try {

            session = hibernateUtil.getSession();
            
            session.saveOrUpdate(logicalDevice);

        } catch (HibernateException e) {
            log.error("unable to update or save LogicalDevice");
            throw new InfrastructureException(e);
        }

    }

    public void create(LogicalDevice logicalDevice)
            throws InfrastructureException {
        Session session = null;

        if (logicalDevice.getId() == null)
            logicalDevice.setId(hibernateUtil.generateUUID());

        try {
            session = hibernateUtil.getSession();
            
            session.save(logicalDevice);

        } catch (HibernateException e) {
            log.error("unable to create LogicalDevice");
            throw new InfrastructureException(e);
        }

    }

    public boolean exists(LogicalDevice logicalDevice)
            throws InfrastructureException {

        boolean found = false;

        if (get(logicalDevice) != null) {
            found = true;
        }

        return found;
    }

    public LogicalDevice get(LogicalDevice logicalDevice)
            throws InfrastructureException {
        LogicalDevice resultEvent = null;

        List result = getAll(logicalDevice);
        if (result != null) {
            if (result.size() > 0) {
                resultEvent = (LogicalDevice) result.get(0);
            }
        }
        return resultEvent;
    }

    public List<LogicalDevice> getAll(LogicalDevice logicalDevice)
            throws InfrastructureException {
        Session session = null;
        List<LogicalDevice> result = null;
        try {
            session = hibernateUtil.getSession();
            Criteria criteria = session.createCriteria(LogicalDevice.class);
            result = criteria.add(
                    Example.create(logicalDevice).enableLike().excludeZeroes())
                    .list();
        } catch (HibernateException e) {
            log.error("unable to getAll LogicalDevices");
            throw new InfrastructureException(e);
        }
        return result;

    }

    @SuppressWarnings("unchecked")
    public List<LogicalDevice> getLogicalDeviceByTime(long starttime,
            long endtime) throws InfrastructureException {
        List<LogicalDevice> logicalDevices = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
            
            Query q = session
                    .createQuery("from LogicalDevice e where e.lastreadtime > :starttime and e.lastreadtime < :endtime");
            q.setLong("starttime", starttime);
            q.setLong("endtime", endtime);
            logicalDevices = q.list();
        } catch (HibernateException e) {
            log.error("unable to getAll LogicalDevices");
            throw new InfrastructureException(e);
        }
        return logicalDevices;
    }

    /**
     * @return Returns the hibernateUtil.
     */
    protected DAOUtil getHibernateUtil() {
        return hibernateUtil;
    }

    /**
     * @param hibernateUtil
     *            The hibernateUtil to set.
     */
    protected void setHibernateUtil(DAOUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    /**
     * 
     */
    public void delete(LogicalDevice logicalDevice)
            throws InfrastructureException {
        try {
            Session session = hibernateUtil.getSession();
            
            session.delete(logicalDevice);
        } catch (HibernateException e) {
            log.error("unable to delete LogicalDevice");
            throw new InfrastructureException(e);
        }
    }

}
