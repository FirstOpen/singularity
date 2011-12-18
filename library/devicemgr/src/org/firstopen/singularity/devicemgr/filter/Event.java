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
package org.firstopen.singularity.devicemgr.filter;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class Event {

    Object eventObj = null;
    /**
     * @param eventObj
     */
    public Event(Object eventObj) {
        super();
      
        this.eventObj = eventObj;
    }
    /**
     * @return Returns the eventObj.
     */
    public Object getEventObj() {
        return eventObj;
    }
    /**
     * @param eventObj The eventObj to set.
     */
    public void setEventObj(Object eventObj) {
        this.eventObj = eventObj;
    }
    /**
     * 
     */
    public Event() {
        super();
      
    }

    

}
