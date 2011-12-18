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

import org.firstopen.singularity.system.Zone;



public interface ZoneDAO {



		/**
		 * 
		 * @param id
		 * @return
		 */
		public Zone getZone(String id);
		
		/**
		 * 
		 * @param attributeValueMap
		 * @return
		 */
		public List findZoneByAttrName(Map attributeValueMap);
		
		/**
		 * 
		 * @return
		 */
		public List getAllZones();
		
		/**
		 * 
		 * @param zone
		 */
		public void updateZone(Zone zone);
		
		/**
		 * 
		 * @param zone
		 */
		public void createZone(Zone zone);
		
}
