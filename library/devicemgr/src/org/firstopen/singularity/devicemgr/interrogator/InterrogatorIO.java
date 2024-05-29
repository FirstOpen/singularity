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
package org.firstopen.singularity.devicemgr.interrogator;

import org.firstopen.singularity.system.ReaderEvent;

/**
 * 
 * Implemented by any object that wants to recieve events from an 
 * Interrogator.
 * 
 * @author TomRose
 * @version $Id: InterrogatorIO.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public interface InterrogatorIO {

	public void sendEvent(ReaderEvent event) throws Exception;
}
