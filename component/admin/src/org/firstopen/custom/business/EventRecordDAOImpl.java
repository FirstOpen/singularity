/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * 
 */
public class EventRecordDAOImpl implements EventRecordDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil hibernateUtil = null;

    /**
     * @throws InfrastructureException 
     * 
     */
    public EventRecordDAOImpl() throws InfrastructureException {
        super();
        hibernateUtil = DAOUtilFactory
                .create(DAOUtil.hibernateAdminJNDIName);
        hibernateUtil.beginTransaction();

    }

    public EventRecord get(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    public List findByAttrName(Map attributeValueMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<EventRecord> getAll() throws InfrastructureException {
        List<EventRecord> eventRecords = null;
        Session session = null;
        try {
           
            session = hibernateUtil.getSession();
            eventRecords = session.createQuery("from EventRecord").list();

        } catch (Exception e) {

            log.error("query failed", e);
        }
        return eventRecords;
    }

    public void update(EventRecord eventRecord) throws InfrastructureException {

        Session session = null;

        try {
           
            session = hibernateUtil.getSession();
            session.saveOrUpdate(eventRecord);

        } catch (Exception e) {
            log.error("unable to update or save EventRecord", e);
            
        }

    }

    public void create(EventRecord eventRecord) throws InfrastructureException {
        Session session = null;

        if (eventRecord.getId() == null)
            eventRecord.setId(hibernateUtil.generateUUID());

        try {
           
            session = hibernateUtil.getSession();
            session.save(eventRecord);

        } catch (Exception e) {
            log.error("unable to creaete EventRecord", e);
            
        }

    }

    public boolean exists(EventRecord EventRecord)
            throws InfrastructureException {

        boolean found = false;

        if (get(EventRecord) != null) {
            found = true;
        }

        return found;
    }

    public EventRecord get(EventRecord eventRecord)
            throws InfrastructureException {
        EventRecord resultEvent = null;

        List result = getAll(eventRecord);
        if (result != null) {
            if (result.size() > 0) {
                resultEvent = (EventRecord) result.get(0);
            }
        }
        return resultEvent;
    }

    public List<EventRecord> getAll(EventRecord eventRecord)
            throws InfrastructureException {
        Session session = null;
        List<EventRecord> result = null;

        session = hibernateUtil.getSession();

        Criteria criteria = session.createCriteria(EventRecord.class);
        result = criteria.add(
                Example.create(eventRecord).enableLike().excludeZeroes()
                        .excludeProperty("isTampered")).list();

        return result;

    }

    @SuppressWarnings("unchecked")
    public List<EventRecord> getByTime(long starttime, long endtime)
            throws InfrastructureException {
        List<EventRecord> eventRecords = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
            Query q = session
                    .createQuery("from EventRecord e where e.lastreadtime > :starttime and e.lastreadtime < :endtime");
            q.setLong("starttime", starttime);
            q.setLong("endtime", endtime);
            eventRecords = q.list();
        } catch (Exception e) {

            log.error("query faild", e);

        }
        return eventRecords;
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

    
}
