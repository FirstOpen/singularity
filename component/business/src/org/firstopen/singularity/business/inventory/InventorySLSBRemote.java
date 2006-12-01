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

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import org.firstopen.singularity.business.TagIdMap;

public interface InventorySLSBRemote extends javax.ejb.EJBObject {
	public void updateInventory(String w, String x, Double y, Integer z)
			throws Exception, RemoteException;

	public LocStock getLocStock(String w, String x) throws Exception,
			RemoteException;

	public boolean createOrUpdateTagIdMap(HashMap newHash) throws Exception,
			RemoteException;

	public TagIdMap getTagIdMap(String tagId) throws Exception, RemoteException;

	public boolean deleteTagIdMap(String tagId) throws Exception,
			RemoteException;

	public List queryItems() throws Exception, RemoteException;

	
}
