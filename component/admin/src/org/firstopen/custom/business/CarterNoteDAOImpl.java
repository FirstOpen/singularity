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
package org.firstopen.custom.business;

import java.util.List;
import java.util.Map;

import javax.transaction.Transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class CarterNoteDAOImpl implements CarterNoteDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil hibernateUtil = null;
    
    Transaction tx = null;

    /**
     * @throws InfrastructureException 
     * @throws InfrastructureException 
     * 
     */
    public CarterNoteDAOImpl() throws InfrastructureException  {
        super();
        hibernateUtil = DAOUtilFactory
                .create(DAOUtil.hibernateAdminJNDIName);

       hibernateUtil.beginTransaction();
    }

    public CarterNote getCartersNote(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    public List findCartersNoteByAttrName(Map attributeValueMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<CarterNote> getAllCartersNotes() throws InfrastructureException {
        
        List<CarterNote> carterNotes = null;
        Session session = null;
        try {
            session = hibernateUtil.getSession();
           
            carterNotes = session.createQuery("from CarterNote").list();
        } catch (HibernateException e) {
            log.error("unable to getAll CarterNotes");
            throw new InfrastructureException(e);
        }
        return carterNotes;
    }

    public void updateCartersNote(CarterNote cartersNote)
            throws InfrastructureException {

        Session session = null;

        try {
            session = hibernateUtil.getSession();
           
            session.saveOrUpdate(cartersNote);
        } catch (HibernateException e) {
            log.error("unable to update or save Carternote");
            throw new InfrastructureException(e);
        }

    }

    public void createCartersNote(CarterNote cartersNote)
            throws InfrastructureException {
        Session session = null;

        if (cartersNote.getId() == null)
            cartersNote.setId(hibernateUtil.generateUUID());

        try {
            session = hibernateUtil.getSession();
           
            session.save(cartersNote);

        } catch (HibernateException e) {
            log.error("unable to creaete Carternote", e);
            throw new InfrastructureException(e);
        }

    }

    public boolean exists(CarterNote carterNote) throws InfrastructureException {

        boolean found = false;

        if (getCartersNote(carterNote) != null) {
            found = true;
        }

        return found;
    }

    public CarterNote getCartersNote(CarterNote carterNote)
            throws InfrastructureException {
        Session session = null;
        List result = null;
        CarterNote resultNote = null;

        try {
            session = hibernateUtil.getSession();
           
            Criteria criteria = session.createCriteria(CarterNote.class);
            result = criteria.add(
                    Example.create(carterNote).enableLike().excludeZeroes())
                    .list();

            if (result != null) {
                if (result.size() > 0) {
                    resultNote = (CarterNote) result.get(0);
                }
            }
        } catch (HibernateException e) {
            log.error("unable to get Carternote");
            throw new InfrastructureException(e);
        }
        return resultNote;

    }

    /**
     * @return Returns the hibernateUtil.
     */
    public DAOUtil getHibernateUtil() {
        return hibernateUtil;
    }

    /**
     * @param hibernateUtil
     *            The hibernateUtil to set.
     */
    public void setHibernateUtil(DAOUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.custom.business.CarterNoteDAO#delete(org.firstopen.custom.business.CarterNote)
     */
    public void delete(CarterNote carterNote) throws InfrastructureException {

        Session session = null;
        try {
            session = hibernateUtil.getSession();
           
            session.delete(carterNote);
        } catch (HibernateException e) {
            log.error("unable to delete Carternote");
            throw new InfrastructureException(e);
        }

    }

    
}
