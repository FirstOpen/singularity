/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Basic Hibernate helper class, handles SessionFactory, Session and
 * Transaction.
 * <p>
 * Uses a initializer for the initial SessionFactory creation and holds Session
 * and Transactions in thread local variables. All exceptions are wrapped in an
 * unchecked InfrastructureException.
 * 
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 *
 * 
 * 
 */
public interface DAOUtil {

	public final String hibernateALEJNDIName = "java:/hibernate/ale/SessionFactory";

	public final String hibernateAdminJNDIName = "java:/hibernate/SessionFactory";

	public SessionFactoryManager getSessionFactoryManager();

	
	/**
	 * Returns the SessionFactory used for this class.
	 * 
	 * @return SessionFactory
	 * @
	 */
	public SessionFactory getSessionFactory()
			;

	/**
	 * Retrieves the current Session local to the thread. <p/> If no Session is
	 * open, opens a new Session for the running thread.
	 * ***Will not bind to JTA Transaction.
	 * 
	 * @return Session
	 */
	public Session getSession();

	
	/**
	 * Closes the Session local to the thread.
	 */
	public void closeSession();

	/**
	 * Start a new database transaction.
	 */
	public void beginTransaction();

	/**
	 * Commit the database transaction.
	 */
	public void commitTransaction();

	/**
	 * Rollback the database transaction.
	 */
	public void rollbackTransaction();

	/**
	 * Reconnects a Hibernate Session to the current Thread.
	 * 
	 * @param session
	 *            The Hibernate Session to be reconnected.
	 */
	public void reconnect(Session session);

	/**
	 * Disconnect and return Session from current Thread.
	 * 
	 * @return Session the disconnected Session
	 */
	public Session disconnectSession();

	/**
	 * 
	 * Geneates UUID for peristance identity
	 * @return
	 * 
	 */
	public String generateUUID();
    
    /**
     * 
     * @return String (JNDI Name used for SessionFactory)
     */
    public String getJndiName();
}