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

package org.firstopen.singularity.system.dao;

import org.firstopen.singularity.util.InfrastructureException;

public class ReaderDAOFactory {

    private static final ThreadLocal<ReaderDAO> threadDAO = new ThreadLocal<ReaderDAO>();

    public ReaderDAOFactory() {
        super();

    }

    /**
     * if and Instance of ReaderDAO has been created
     * for this thread, just get the copy. Otherwise create a new
     * instance, and store it for this thread.
     */
    public static ReaderDAO create() throws InfrastructureException {
        ReaderDAO deviceProfileDAO = threadDAO.get();
        /*
         * ensure the appropriate instance of ReaderDAOImpl
         * is placed in this threads local copy.
         */
        synchronized (threadDAO) {
            if (deviceProfileDAO == null) {
                deviceProfileDAO = new ReaderDAOImpl();
                threadDAO.set(deviceProfileDAO);
            }
        }
        return deviceProfileDAO;
    }

    /*
     * cleans up used DAO, called by ReaderDAOImpl
     * on close sessiojn.
     */
    public static void destory() {
        threadDAO.set(null);
    }

}
