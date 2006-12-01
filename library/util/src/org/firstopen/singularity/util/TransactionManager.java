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

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class TransactionManager {

    private Log log = LogFactory.getLog(getClass());

    /**
     * 
     */
    public TransactionManager() {
        super();

    }

    public int getStatus() {

        try {
            UserTransaction userTransaction = TransactionUtil
                    .getUserTransaction();
            return userTransaction.getStatus();
        } catch (Exception e) {
            throw new InfrastructureException(e);
        }
    }

    /**
     * 
     * 
     */
    public void begin() {
        log.debug("begin");
        try {
            UserTransaction userTransaction = TransactionUtil
                    .getUserTransaction();
            if (userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION) {
                userTransaction.begin();
                log.debug("JTA transaction started");
            }

        } catch (Exception e) {
            log.error("cannot begin transaction");

            throw new InfrastructureException(e);
        }

    }

    public void commit() {

        try {
            /*
             * close any DAO Sessions related to this 
             * transaction.
             */
            DAOUtilFactory.close();
            
            UserTransaction userTransaction = TransactionUtil
                    .getUserTransaction();
            if (userTransaction.getStatus() == Status.STATUS_ACTIVE) {
                userTransaction.commit();
                log.debug("transaction commit completed!");
            }
        } catch (Exception e) {
            log.error("commit failed!");
            // rollback();
            throw new InfrastructureException(e);
        }

    }

    public void rollback() {
        log.debug("rollback()");
        try {
            UserTransaction userTransaction = TransactionUtil
                    .getUserTransaction();
            int status = userTransaction.getStatus();
            if (status != Status.STATUS_COMMITTED
                    && status != Status.STATUS_ROLLEDBACK) {
                userTransaction.rollback();
            }
        } catch (Exception e) {

            log.error("rollback failed!");
            throw new InfrastructureException(e);
        }
    }

}
