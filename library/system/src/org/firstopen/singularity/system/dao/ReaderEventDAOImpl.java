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
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.hibernate.Session;

public class ReaderEventDAOImpl implements ReaderEventDAO {

	Log log = LogFactory.getLog(this.getClass());

	DAOUtil hibernateUtil = null;

	public ReaderEventDAOImpl() {
		super();
		hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);
	}

	public ReaderEvent get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List findByAttrName(Map attributeValueMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getAll() throws InfrastructureException {
		List readerEvents = null;
		Session session = null;

		session = hibernateUtil.getSession();
		log.debug("start query");
		readerEvents = session.createQuery("from ECSpec").list();
		log.debug("end querey");
	
		
		return readerEvents;
	}

	public void persist(ReaderEvent readerEvent) throws InfrastructureException {

		Session session = null;
		hibernateUtil.beginTransaction();
		session = hibernateUtil.getSession();
		session.persist(readerEvent);
			
	}

	public void update(ReaderEvent readerEvent)
			throws InfrastructureException {
	
		Session session = null;
		hibernateUtil.beginTransaction();
		session = hibernateUtil.getSession();
		session.saveOrUpdate(readerEvent);
		
	}

	public void create(ReaderEvent readerEvent) throws InfrastructureException {
		update(readerEvent);

	}

}
