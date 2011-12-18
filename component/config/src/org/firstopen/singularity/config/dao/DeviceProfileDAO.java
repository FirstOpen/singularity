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

package org.firstopen.singularity.config.dao;

import java.util.List;
import java.util.Map;

import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.util.InfrastructureException;



public interface DeviceProfileDAO {
    

	/**
	 * 
	 * @param id
	 * @return
	 * @throws InfrastructureException 
	 */
	public DeviceProfile get(String id) throws InfrastructureException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws InfrastructureException 
	 */
	public DeviceProfile get(DeviceProfile deviceProfile) throws InfrastructureException;
	
	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 */
	public List findByAttrName(Map attributeValueMap);
	
	/**
	 * 
	 * @return
	 * @throws InfrastructureException 
	 */
	public List<DeviceProfile> getAll() throws InfrastructureException;
  
	
	/**
	 * 
	 * @param reader
	 * @throws InfrastructureException 
	 */
	public void update(DeviceProfile reader) throws InfrastructureException;
	
	/**
	 * 
	 * @param reader
	 * @throws InfrastructureException 
	 */
	public void create(DeviceProfile reader) throws InfrastructureException;

	
    /**
     * @param hibernateUtil The hibernateUtil to set.
     */
    public void delete(DeviceProfile deviceProfile) throws InfrastructureException;
}
