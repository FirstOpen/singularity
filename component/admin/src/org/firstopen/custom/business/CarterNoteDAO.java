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
package org.firstopen.custom.business;

import java.util.List;
import java.util.Map;

import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: CarterNoteDAO.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public interface CarterNoteDAO {

    
    
    /**
     * 
     * @param id
     * @return
     * @throws InfrastructureException
     */
    public CarterNote getCartersNote(CarterNote carterNote)
            throws InfrastructureException;

    /**
     * 
     * @param id
     * @return
     */
    public CarterNote getCartersNote(String id);

    /**
     * 
     * @param attributeValueMap
     * @return
     */
    public List findCartersNoteByAttrName(Map attributeValueMap);

    /**
     * 
     * @return
     * @throws InfrastructureException
     */
    public List<CarterNote> getAllCartersNotes() throws InfrastructureException;

    /**
     * 
     * @param cartersNote
     * @throws InfrastructureException
     */
    public void updateCartersNote(CarterNote cartersNote)
            throws InfrastructureException;

    /**
     * 
     * @param cartersNote
     * @throws InfrastructureException
     */
    public void createCartersNote(CarterNote cartersNote)
            throws InfrastructureException;

    /**
     * 
     * @param cartersNote
     * @return boolean
     * @throws InfrastructureException
     */
    public boolean exists(CarterNote cartersNote)
            throws InfrastructureException;

    /**
     * @return Returns the hibernateUtil.
     */
    public DAOUtil getHibernateUtil();

    /**
     * @param hibernateUtil
     *            The hibernateUtil to set.
     */
    public void setHibernateUtil(DAOUtil hibernateUtil);

    public void delete(CarterNote carterNote)
            throws InfrastructureException;
}
