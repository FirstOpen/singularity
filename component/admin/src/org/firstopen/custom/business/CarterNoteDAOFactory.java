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

package org.firstopen.custom.business;

import org.firstopen.singularity.util.InfrastructureException;

/**
 * 
 * @author Tom Rose (tom.rose@i-konect.com) 
 * @version $Id: CarterNoteDAOFactory.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public class CarterNoteDAOFactory {

	public CarterNoteDAOFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static CarterNoteDAO create() throws InfrastructureException{
		return new CarterNoteDAOImpl();
	}
}
