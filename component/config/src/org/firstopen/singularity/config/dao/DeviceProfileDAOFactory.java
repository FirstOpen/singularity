/*
 * Copyright 2005 i-Konect LLC
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

package org.firstopen.singularity.config.dao;

import org.firstopen.singularity.util.InfrastructureException;

public class DeviceProfileDAOFactory {

    private static final ThreadLocal<DeviceProfileDAO> threadDAO = new ThreadLocal<DeviceProfileDAO>();

    public DeviceProfileDAOFactory() {
        super();

    }

    /**
     * if and Instance of DeviceProfileDAO has been created
     * for this thread, just get the copy. Otherwise create a new
     * instance, and store it for this thread.
     */
    public static DeviceProfileDAO create() throws InfrastructureException {
        DeviceProfileDAO deviceProfileDAO = null;
        /*
         * ensure the appropriate instance of DeviceProfileDAOImpl
         * is placed in this threads local copy.
         */
        synchronized (threadDAO) {
            deviceProfileDAO = threadDAO.get();
            if (deviceProfileDAO == null) {
                deviceProfileDAO = new DeviceProfileDAOImpl();
                threadDAO.set(deviceProfileDAO);
            }
        }
        return deviceProfileDAO;
    }

    /*
     * cleans up used DAO, called by DeviceProfileDAOImpl
     * on close sessiojn.
     */
    public static void destory() {
        threadDAO.set(null);
    }

}
