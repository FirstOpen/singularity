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

package org.firstopen.singularity.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Reader;
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
public class ReaderDAOImpl implements ReaderDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil hibernateUtil = null;

    /**
     * @throws InfrastructureException 
     * 
     */
    public ReaderDAOImpl() throws InfrastructureException {
        super();
        hibernateUtil = DAOUtilFactory
                .create(DAOUtil.hibernateALEJNDIName);
        hibernateUtil.beginTransaction();

    }

    public Reader get(String id) throws InfrastructureException {
        Session session = null;
        Reader reader = null;

        try {
            session = hibernateUtil.getSession();
            
            reader = (Reader) session.load(Reader.class,
                    id);
        } catch (HibernateException e) {
            log.error("unable to update or save Reader");
            throw new InfrastructureException(e);
        }
        return reader;
    }

    public List findByAttrName(Map<String, String> attributeValueMap) {
     

    return null;
    }

 
    @SuppressWarnings("unchecked")
    public List<Reader> getAll() throws InfrastructureException {

        List<Reader> readers = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
            
            readers = session.createQuery("from Reader").list();
        } catch (HibernateException e) {
            log.error("unable to getAll Readers");

            throw new InfrastructureException(e);
        }
        return readers;
    }

    public void update(Reader reader)
            throws InfrastructureException {

        Session session = null;
        try {

            session = hibernateUtil.getSession();
            
            session.saveOrUpdate(reader);

        } catch (HibernateException e) {
            log.error("unable to update or save Reader");
            throw new InfrastructureException(e);
        }

    }

    public void create(Reader reader)
            throws InfrastructureException {
        Session session = null;

        if (reader.getId() == null)
            reader.setId(hibernateUtil.generateUUID());

        try {
            session = hibernateUtil.getSession();
            
            session.save(reader);

        } catch (HibernateException e) {
            log.error("unable to create Reader");
            throw new InfrastructureException(e);
        }

    }

    public boolean exists(Reader reader)
            throws InfrastructureException {

        boolean found = false;

        if (get(reader) != null) {
            found = true;
        }

        return found;
    }

    public Reader get(Reader reader)
            throws InfrastructureException {
        Reader resultEvent = null;

        List result = getAll(reader);
        if (result != null) {
            if (result.size() > 0) {
                resultEvent = (Reader) result.get(0);
            }
        }
        return resultEvent;
    }

    public List<Reader> getAll(Reader reader)
            throws InfrastructureException {
        Session session = null;
        List<Reader> result = null;
        try {
            session = hibernateUtil.getSession();
            Criteria criteria = session.createCriteria(Reader.class);
            result = criteria.add(
                    Example.create(reader).enableLike().excludeZeroes())
                    .list();
        } catch (HibernateException e) {
            log.error("unable to getAll Readers");
            throw new InfrastructureException(e);
        }
        return result;

    }

    @SuppressWarnings("unchecked")
    public List<Reader> getReaderByTime(long starttime,
            long endtime) throws InfrastructureException {
        List<Reader> readers = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
            
            Query q = session
                    .createQuery("from Reader e where e.lastreadtime > :starttime and e.lastreadtime < :endtime");
            q.setLong("starttime", starttime);
            q.setLong("endtime", endtime);
            readers = q.list();
        } catch (HibernateException e) {
            log.error("unable to getAll Readers");
            throw new InfrastructureException(e);
        }
        return readers;
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
    public void delete(Reader reader)
            throws InfrastructureException {
        try {
            Session session = hibernateUtil.getSession();
            
            session.delete(reader);
        } catch (HibernateException e) {
            log.error("unable to delete Reader");
            throw new InfrastructureException(e);
        }
    }

}
