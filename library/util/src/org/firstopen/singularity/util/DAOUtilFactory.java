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
package org.firstopen.singularity.util;

import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates DAO Utilities for session and transaction management.
 * 
 * Specific DAOs will use a specific DAOUtil depending on its 
 * particular storage mechinism/location, and this knowledge is encapsulated
 * in the specific DAO. During the course of an application many DAOs might
 * be used, so as a convience the close method will commit and close sessions
 * accross all DAOs (in the case of Hibernate, across sessions from each SessionFactory).
 * 
 * In the case of non-JTA environment, this will provide the undesriable behavior 
 * of the commits not participating in a distributed transaction.
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: DAOUtilFactory.java 1242 2006-01-14 03:34:08Z TomRose $
 * 
 */
public class DAOUtilFactory {

    private static final HashMap<String, DAOUtil> daoUtilMap = new HashMap<String, DAOUtil>();

    private static Log log = LogFactory.getLog(DAOUtilFactory.class);

    public static synchronized DAOUtil create(String jndiName) {
        DAOUtil hibernateUtil = daoUtilMap.get(jndiName);
        if (hibernateUtil == null) {
            hibernateUtil = new HibernateUtilImpl(jndiName);
            log.debug("adding -" + jndiName + "- to DAOUtil Cache");
            daoUtilMap.put(jndiName, hibernateUtil);
        }
        return hibernateUtil;

    }

    /**
     * Commits all open transctions and closes the sesssion for all DAOs
     * created on this thread.
     */
    public static synchronized void close() {
        Collection<DAOUtil> daoUtilCollection = daoUtilMap.values();

        for (DAOUtil daoUtil : daoUtilCollection) {

            log.debug("closing session for " + daoUtil.getJndiName());
            closeSession(daoUtil);
        }
    }

    private static void closeSession(DAOUtil daoUtil) {
        try {

            daoUtil.commitTransaction();

        } catch (InfrastructureException e) {
            log.error("unable to commit, trasaction rolled back.");
            daoUtil.rollbackTransaction();
            throw e;
        } finally {
            daoUtil.closeSession();
        }
    }
}
