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
package org.firstopen.singularity.ale.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.ale.ECSpec;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * 
 */
public class ECSpecDAOImpl implements ECSpecDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil DAOUtil = null;

    /**
     * @throws InfrastructureException
     * 
     */
    public ECSpecDAOImpl() throws InfrastructureException {
        super();
        DAOUtil = DAOUtilFactory
                .create(DAOUtil.hibernateALEJNDIName);
        try {
            DAOUtil.beginTransaction();

        } catch (HibernateException e) {
            log.error("query failed");
            throw new InfrastructureException(e);
        }
    }

    public ECSpec get(String id) throws InfrastructureException {
        ECSpec ecSpec = null;
        try {

            Session session = DAOUtil.getSession();

            ecSpec = (ECSpec) session.load(ECSpec.class, id);

            if (ecSpec == null)
                throw new InfrastructureException("unable to find ecSpec");
        } catch (HibernateException e) {
            log.error("query failed");
            throw new InfrastructureException(e);
        }
        return ecSpec;
    }

    public List findECSpecByAttrName(Map attributeValueMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<ECSpec> getAll() throws InfrastructureException {
        List<ECSpec> ecSpecs = null;
        Session session = null;
        try {
            session = DAOUtil.getSession();
            ecSpecs = session.createQuery("from ECSpec").list();

        } catch (HibernateException e) {
            log.error("query failed");
            throw new InfrastructureException(e);
        }
        return ecSpecs;
    }

    public void update(ECSpec ecSpec) throws InfrastructureException {

        Session session = null;

        try {
            session = DAOUtil.getSession();
            session.saveOrUpdate(ecSpec);

        } catch (HibernateException e) {
            log.error("unable to update or save ECSpec");
            throw new InfrastructureException(e);
        }

    }

    public void create(ECSpec ecSpec) throws InfrastructureException {
        Session session = null;
        try {
            session = DAOUtil.getSession();
            session.save(ecSpec);

        } catch (HibernateException e) {
            log.error("unable to creaete ECSpec", e);
            throw new InfrastructureException(e);
        }

    }

    public boolean exists(ECSpec ECSpec) throws InfrastructureException {

        boolean found = false;

        if (get(ECSpec) != null) {
            found = true;
        }

        return found;
    }

    public ECSpec get(ECSpec ecSpec) throws InfrastructureException {
        ECSpec resultEvent = null;

        List result = getList(ecSpec);
        if (result != null) {
            if (result.size() > 0) {
                resultEvent = (ECSpec) result.get(0);
            }
        }
        return resultEvent;
    }

    @SuppressWarnings("unchecked")
    public List<ECSpec> getList(ECSpec ecSpec) throws InfrastructureException {
        Session session = null;
        List<ECSpec> result = null;

        try {
            session = DAOUtil.getSession();

            Criteria criteria = session.createCriteria(ECSpec.class);
            result = criteria.add(
                    Example.create(ecSpec).enableLike().excludeZeroes()).list();
        } catch (HibernateException e) {
            log.error("unable to update or save ECSpec");
            throw new InfrastructureException(e);

        }
        return result;

    }

    /**
     * @return Returns the DAOUtil.
     */
    public DAOUtil getDAOUtil() {
        return DAOUtil;
    }

    /**
     * @param DAOUtil
     *            The DAOUtil to set.
     */
    public void setDAOUtil(DAOUtil DAOUtil) {
        this.DAOUtil = DAOUtil;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.dao.ECSpecDAO#findByAttrName(java.util.Map)
     */
    public List findByAttrName(Map attributeValueMap) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.ale.dao.ECSpecDAO#delete(org.firstopen.singularity.ale.ECSpec)
     */
    public void delete(ECSpec ecSpec) throws InfrastructureException {
        Session session = null;
        try {
            session = DAOUtil.getSession();
            session.delete(ecSpec);

        } catch (HibernateException e) {
            log.error("unable to update or save ECSpec");
            throw new InfrastructureException(e);

        }
    } // end delete

 
}// end class ECSpecDAOImpl
