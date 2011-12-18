/*
 * Copyright 2005 i-Konect LLC
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

package org.firstopen.singularity.devicemgr.interrogator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.firstopen.singularity.config.DeviceProfile;



/**
 * 
 * @author TomRose
 * @version $Id: InterrogatorFactory.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public class InterrogatorFactory {

	public static Interrogator create(DeviceProfile deviceProfile)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		
		Interrogator interrogator = null;

		Class iClass = Class.forName(deviceProfile.getInterrogatorClassName());
		
		Class[] paramaters = { DeviceProfile.class };

		Constructor constructor = iClass.getConstructor(paramaters);

		interrogator = (Interrogator) constructor
				.newInstance(deviceProfile);

		return interrogator;
	}

}
