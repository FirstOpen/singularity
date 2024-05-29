/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

package org.firstopen.singularity.config;

import java.util.List;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;
import org.firstopen.singularity.config.dao.DeviceProfileDAO;
import org.firstopen.singularity.config.dao.DeviceProfileDAOFactory;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;

public class ConfigServiceBean implements SessionBean {
    /**
     * 
     */
    private static final long serialVersionUID = 9176225388863130838L;

    private SessionContext sessionContext;

    private Logger log = null;

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(javax.ejb.SessionContext sContext) {
        this.sessionContext = sContext;
    }

    public void ejbCreate() {
        log = Logger.getLogger(getClass());
    }

    public List<DeviceProfile> getConfig(String deviceName) throws InfrastructureException{
        
        List<DeviceProfile> rcs = null;
        DeviceProfileDAO deviceProfileDAO = null;
     
        try {
            deviceProfileDAO = DeviceProfileDAOFactory
            .create();
            rcs = deviceProfileDAO.getAll();
        } catch (InfrastructureException e) {
            log.error("unable to retrieve DeviceProfiles");
            throw e;
            
        }finally {
            DAOUtilFactory.close();
        }
        
        return rcs;
    }

    public String getStandardVersion() {
        return "1.0";
    }

    public String getVendorVersion() {
        return "1.0";
    }

}
