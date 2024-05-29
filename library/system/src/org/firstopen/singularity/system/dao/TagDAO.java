/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.util.InfrastructureException;


public interface TagDAO {
  

    /**
     * 
     * @param tag
     * @return
     * @throws InfrastructureException
     */
    public  List<Tag> getList(Tag tag) throws InfrastructureException;

    /**
     * 
     * @param searchTag
     * @return
     * @throws InfrastructureException
     */
    public  Tag get(Tag searchTag) throws InfrastructureException;

    /**
     * 
     * @param id
     * @return
     * @throws InfrastructureException
     */
    public  Tag get(String id) throws InfrastructureException;

    /**
     * 
     * @param attributeValueMap
     * @return
     */
    public  List<Tag> findByAttrName(Map attributeValueMap);

    /**
     * 
     * @return
     */
    public  List<Tag> getAll();

    /**
     * 
     * @param tag
     * @throws InfrastructureException
     */
    public  void update(Tag tag) throws InfrastructureException;

    /**
     * 
     * @param tag
     * @throws InfrastructureException
     */
    public  void create(Tag tag) throws InfrastructureException;

    /**
     * get the first Tag that matches the example Tag
     */
    public  Tag findBy(Tag tag) throws InfrastructureException;

   
}
