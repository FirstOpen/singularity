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

public class LogicalDeviceDAOFactory {

    private static final ThreadLocal<LogicalDeviceDAO> threadDAO = new ThreadLocal<LogicalDeviceDAO>();

    public LogicalDeviceDAOFactory() {
        super();

    }

    /**
     * if and Instance of LogicalDeviceDAO has been created
     * for this thread, just get the copy. Otherwise create a new
     * instance, and store it for this thread.
     */
    public static LogicalDeviceDAO create() throws InfrastructureException {
        LogicalDeviceDAO logicalDeviceDAO = threadDAO.get();
        /*
         * ensure the appropriate instance of LogicalDeviceDAOImpl
         * is placed in this threads local copy.
         */
        synchronized (threadDAO) {
            if (logicalDeviceDAO == null) {
                logicalDeviceDAO = new LogicalDeviceDAOImpl();
                threadDAO.set(logicalDeviceDAO);
            }
        }
        return logicalDeviceDAO;
    }

    /*
     * cleans up used DAO, called by LogicalDeviceDAOImpl
     * on close sessiojn.
     */
    public static void destory() {
        threadDAO.set(null);
    }

}
