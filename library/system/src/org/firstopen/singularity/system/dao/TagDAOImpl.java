/* 
 * Copyright 2005 i-Konect LLC
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

package org.firstopen.singularity.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

public class TagDAOImpl implements TagDAO {

    Log log = LogFactory.getLog(this.getClass());

    DAOUtil hibernateUtil = null;

    public TagDAOImpl() {
        super();
        hibernateUtil = DAOUtilFactory
                .create(DAOUtil.hibernateALEJNDIName);

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

   /**
    * 
    */
    @SuppressWarnings("unchecked")
    public List<Tag> getList(Tag tag) throws InfrastructureException {
        Session session = null;
        List<Tag> result = null;

        session = hibernateUtil.getSession();

        Criteria criteria = session.createCriteria(Tag.class);
        result = criteria.add(Example.create(tag).enableLike().excludeZeroes())
                .list();

        return result;

    }

  /**
   * 
   */
    public Tag get(Tag searchTag) throws InfrastructureException {
        Tag tag = null;

        List result = getList(searchTag);
        if (result != null) {
            if (result.size() > 0) {
                tag = (Tag) result.get(0);
            }
        }
        return tag;
    }

   
    /**
     * 
     */
    public Tag get(String id) throws InfrastructureException {

        Session session = hibernateUtil.getSession();

        Tag tag = (Tag) session.load(Tag.class, id);

        if (tag == null)
            throw new InfrastructureException("unable to find ecSpec");
        return tag;

    }

   
    public List<Tag> findByAttrName(Map attributeValueMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Tag> getAll() {

        List<Tag> Tags = null;
        Session session = null;
        try {
            DAOUtil hibernateUtil = DAOUtilFactory
                    .create(DAOUtil.hibernateALEJNDIName);

            session = hibernateUtil.getSession();
            Tags = session.createQuery("from Tag").list();
        } catch (Exception e) {

            log.error("query faild", e);

        }
        return Tags;
    }

    public void update(Tag tag) throws InfrastructureException {
        Session session = null;

        try {
            hibernateUtil.beginTransaction();
            session = hibernateUtil.getSession();
            session.saveOrUpdate(tag);
           
        } catch (Exception e) {
            log.error("unable to update Tag", e);
            hibernateUtil.rollbackTransaction();
            throw new InfrastructureException(e);
        }

    }

    /**
     * 
     */
    public void create(Tag tag) throws InfrastructureException {
        Session session = null;

        try {
            hibernateUtil.beginTransaction();
            session = hibernateUtil.getSession();
            session.save(tag);
          
        } catch (Exception e) {
            log.error("unable to create Tag", e);
            hibernateUtil.rollbackTransaction();
            throw new InfrastructureException(e);
        }

    }

   
   /**
    * 
    */
    public Tag findBy(Tag tag) throws InfrastructureException {
        Session session = null;

        List result = null;

        session = hibernateUtil.getSession();
        Criteria criteria = session.createCriteria(Tag.class);
        result = criteria
                .add(
                        Example.create(tag).excludeProperty("tampered")
                                .excludeZeroes()).list();

        Tag resultTag = null;
        if (result != null) {
            if (result.size() > 0) {
                resultTag = (Tag) result.get(0);
            }
        }

        return resultTag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.system.dao.TagDAOTest#getHibernateUtil()
     */
    protected DAOUtil getHibernateUtil() {
        return hibernateUtil;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.firstopen.singularity.system.dao.TagDAOTest#setHibernateUtil(org.firstopen.singularity.util.DAOUtil)
     */
    protected void setHibernateUtil(DAOUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }
   
}

