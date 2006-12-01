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

import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.util.InfrastructureException;



public interface ReaderDAO {
    

	/**
	 * 
	 * @param id
	 * @return
	 * @throws InfrastructureException 
	 */
	public Reader get(String id) throws InfrastructureException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws InfrastructureException 
	 */
	public Reader get(Reader reader) throws InfrastructureException;
	
	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 */
	public List findByAttrName(Map<String, String> attributeValueMap);
	
	/**
	 * 
	 * @return
	 * @throws InfrastructureException 
	 */
	public List<Reader> getAll() throws InfrastructureException;
  
	
	/**
	 * 
	 * @param reader
	 * @throws InfrastructureException 
	 */
	public void update(Reader reader) throws InfrastructureException;
	
	/**
	 * 
	 * @param reader
	 * @throws InfrastructureException 
	 */
	public void create(Reader reader) throws InfrastructureException;

	
    /**
     * @param hibernateUtil The hibernateUtil to set.
     */
    public void delete(Reader reader) throws InfrastructureException;
}
