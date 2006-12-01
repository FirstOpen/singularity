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

package org.firstopen.singularity.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.config.DeviceProfile;
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
public class DeviceProfileDAOImpl implements DeviceProfileDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil daoUtil = null;

    /**
     * @throws InfrastructureException 
     * 
     */
    public DeviceProfileDAOImpl() throws InfrastructureException {
        super();
        daoUtil = DAOUtilFactory
                .create(DAOUtil.hibernateALEJNDIName);
        daoUtil.beginTransaction();

    }

    public DeviceProfile get(String id) throws InfrastructureException {
        Session session = null;
        DeviceProfile deviceProfile = null;

        try {
            session = daoUtil.getSession();
            
            deviceProfile = (DeviceProfile) session.load(DeviceProfile.class,
                    id);
        } catch (HibernateException e) {
            log.error("unable to update or save DeviceProfile");
            throw new InfrastructureException(e);
        }
        return deviceProfile;
    }

    public List findByAttrName(Map attributeValueMap) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<DeviceProfile> getAll() throws InfrastructureException {

        List<DeviceProfile> deviceProfiles = null;
        Session session = null;
        try {
            session = daoUtil.getSession();
            
            deviceProfiles = session.createQuery("from DeviceProfile").list();
        } catch (HibernateException e) {
            log.error("unable to getAll DeviceProfiles");

            throw new InfrastructureException(e);
        }
        return deviceProfiles;
    }

    public void update(DeviceProfile deviceProfile)
            throws InfrastructureException {

        Session session = null;
        try {

            session = daoUtil.getSession();
            
            session.saveOrUpdate(deviceProfile);

        } catch (HibernateException e) {
            log.error("unable to update or save DeviceProfile");
            throw new InfrastructureException(e);
        }

    }

    public void create(DeviceProfile deviceProfile)
            throws InfrastructureException {
        Session session = null;

        if (deviceProfile.getId() == null)
            deviceProfile.setId(daoUtil.generateUUID());

        try {
            session = daoUtil.getSession();
            
            session.save(deviceProfile);

        } catch (HibernateException e) {
            log.error("unable to create DeviceProfile");
            throw new InfrastructureException(e);
        }

    }

    public boolean exists(DeviceProfile deviceProfile)
            throws InfrastructureException {

        boolean found = false;

        if (get(deviceProfile) != null) {
            found = true;
        }

        return found;
    }

    public DeviceProfile get(DeviceProfile deviceProfile)
            throws InfrastructureException {
        DeviceProfile resultEvent = null;

        List result = getAll(deviceProfile);
        if (result != null) {
            if (result.size() > 0) {
                resultEvent = (DeviceProfile) result.get(0);
            }
        }
        return resultEvent;
    }

    public List<DeviceProfile> getAll(DeviceProfile deviceProfile)
            throws InfrastructureException {
        Session session = null;
        List<DeviceProfile> result = null;
        try {
            session = daoUtil.getSession();
            Criteria criteria = session.createCriteria(DeviceProfile.class);
            result = criteria.add(
                    Example.create(deviceProfile).enableLike().excludeZeroes())
                    .list();
        } catch (HibernateException e) {
            log.error("unable to getAll DeviceProfiles");
            throw new InfrastructureException(e);
        }
        return result;

    }

    @SuppressWarnings("unchecked")
    public List<DeviceProfile> getDeviceProfileByTime(long starttime,
            long endtime) throws InfrastructureException {
        List<DeviceProfile> deviceProfiles = null;
        Session session = null;
        try {
            session = daoUtil.getSession();
            
            Query q = session
                    .createQuery("from DeviceProfile e where e.lastreadtime > :starttime and e.lastreadtime < :endtime");
            q.setLong("starttime", starttime);
            q.setLong("endtime", endtime);
            deviceProfiles = q.list();
        } catch (HibernateException e) {
            log.error("unable to getAll DeviceProfiles");
            throw new InfrastructureException(e);
        }
        return deviceProfiles;
    }

    /**
     * @return Returns the daoUtil.
     */
    protected DAOUtil getDAOUtil() {
        return daoUtil;
    }

    /**
     * @param daoUtil
     *            The daoUtil to set.
     */
    protected void setDAOUtil(DAOUtil daoUtil) {
        this.daoUtil = daoUtil;
    }

    /**
     * 
     */
    public void delete(DeviceProfile deviceProfile)
            throws InfrastructureException {
        try {
            Session session = daoUtil.getSession();
            
            session.delete(deviceProfile);
        } catch (HibernateException e) {
            log.error("unable to delete DeviceProfile");
            throw new InfrastructureException(e);
        }
    }


}
