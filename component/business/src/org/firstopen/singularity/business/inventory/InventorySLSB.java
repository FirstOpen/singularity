/*
 * Copyright 2005 Wavechain Consulting LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.business.inventory;

import java.util.HashMap;
import java.util.List;

import javax.ejb.SessionBean;

import org.apache.log4j.Logger;
import org.firstopen.singularity.business.Locations;
import org.firstopen.singularity.business.TagIdMap;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.hibernate.Session;

public class InventorySLSB implements SessionBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6203972506232416154L;

	private Logger log = null;

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() {
	}

	public void setSessionContext(javax.ejb.SessionContext sContext) {
	}

	public void ejbCreate() {
		log = Logger.getLogger(getClass());
	}

	public void updateInventory(String locCode, String stockId,
			Double quantityChange, Integer reOrderLevel) throws Exception {
		Session session = null;
		LocStock locStock = null;
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);
		try {

			session = hibernateUtil.getSession();
			LocStockPK locStockPK = new LocStockPK(locCode, stockId);
			locStock = (LocStock) session.load(LocStock.class, locStockPK);
			if (quantityChange != null) {
				Double currentQuantity = locStock.getQuantity();
				if (currentQuantity != null)
					locStock.setQuantity(new Double(currentQuantity
							.doubleValue()
							+ quantityChange.doubleValue()));
				else
					locStock.setQuantity(quantityChange);
			}
			if (reOrderLevel != null)
				locStock.setReOrderLevel(reOrderLevel);

			session.save(locStock);
		} catch (org.hibernate.MappingException x) {
			log
					.error("createInventory() possible problem meeting relationship requirements of the Inventory class with another class.  Exception = "
							+ x);
			throw x;
		} catch (Exception x) {
			x.printStackTrace();
			throw x;
		} 
	}

	public LocStock getLocStock(String locCode, String stockId)
			throws Exception {
		Session session = null;
		LocStock locStock = null;
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);

		try {
			session = hibernateUtil.getSession();
			LocStockPK locStockPK = new LocStockPK(locCode, stockId);
			locStock = (LocStock) session.load(LocStock.class, locStockPK);
		} catch (Exception x) {
			x.printStackTrace();
			throw x;
		} 

		return locStock;
	}

	public boolean createOrUpdateTagIdMap(HashMap newHash) throws Exception {
		Session session = null;

		boolean successful = true;
		String tagId = (String) newHash.get("tagId");
		TagIdMap tagIdMap = null;
		String location = null;
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);

		try {
			session = hibernateUtil.getSession();
			tagIdMap = (TagIdMap) session.get(TagIdMap.class, tagId);
			if (tagIdMap == null)
				tagIdMap = new TagIdMap();

			/* Ensure that location field, if passed, is valid */
			if ((location = (String) newHash.get("location")) != null)
				session.load(Locations.class, location);
			tagIdMap.update(newHash);
			session.save(tagIdMap);
		} catch (Exception x) {
			x.printStackTrace();
			successful = false;
			throw x;
		} 

		return successful;
	}

	/*
	 * Note: session.load() is used in this method to take advantage of cache
	 * mechanism. session.get() seems to by-pass second level cache
	 */
	public TagIdMap getTagIdMap(String tagId) throws Exception {
		Session session = null;
		log.debug("getTagIdMap() tagId = " + tagId);
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);

		try {
			session = hibernateUtil.getSession();
			return (TagIdMap) session.load(TagIdMap.class, tagId);
		} catch (Exception x) {
			x.printStackTrace();
			throw x;
		} 
	}

	public boolean deleteTagIdMap(String tagId) throws Exception {
		Session session = null;

		boolean successful = true;
		TagIdMap tagIdMap = null;
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);

		try {
			session = hibernateUtil.getSession();
			tagIdMap = (TagIdMap) session.load(TagIdMap.class, tagId);
			session.delete(tagIdMap);
		} catch (Exception x) {
			successful = false;
			x.printStackTrace();
			throw x;
		} 
		return successful;
	}

	public List queryItems() throws Exception {
		Session session = null;
		List queryList = null;
		DAOUtil hibernateUtil = DAOUtilFactory
				.create(DAOUtil.hibernateALEJNDIName);

		try {
			session = hibernateUtil.getSession();

		} catch (Exception x) {
			x.printStackTrace();
			throw x;
		} 

		return queryList;
	}

}
