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

package org.firstopen.singularity.ale.dao;

import java.util.List;
import java.util.Map;

import org.firstopen.singularity.ale.ECSpec;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * @author Tom Rose (tom.rose@i-konect.com) 
 * @version $Id: ECSpecDAO.java 1243 2006-01-14 03:33:37Z TomRose $
 * 
 */

public interface ECSpecDAO {

   


    /**
     * 
     * @param id
     * @return
     * @throws InfrastructureException 
     */
    public ECSpec get(String id) throws InfrastructureException;
    
    /**
     * 
     * @param id
     * @return
     * @throws InfrastructureException 
     */
    public ECSpec get(ECSpec ecSpec) throws InfrastructureException;
    
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
    public List<ECSpec> getAll() throws InfrastructureException;
  
    
    /**
     * 
     * @param ecSpec
     * @throws InfrastructureException 
     */
    public void update(ECSpec ecSpec) throws InfrastructureException;
    
    /**
     * 
     * @param ecSpec
     * @throws InfrastructureException 
     */
    public void create(ECSpec ecSpec) throws InfrastructureException;

    /**
     * @return Returns the DAOUtil.
     */
    public DAOUtil getDAOUtil();

    /**
     * @param DAOUtil The DAOUtil to set.
     */
    public void setDAOUtil(DAOUtil DAOUtil);
	
  
    /**
     * 
     * @param ecSpec
     * @throws InfrastructureException
     */
    public void delete(ECSpec ecSpec) throws InfrastructureException;
}
