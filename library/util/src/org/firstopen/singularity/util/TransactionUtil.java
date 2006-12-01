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
package org.firstopen.singularity.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class TransactionUtil {

    /**
     * 
     */
    public TransactionUtil() {
        super();
        // TODO Auto-generated constructor stub
    }

  

    /**
     * 
     * @return TransactionManager 
     * @throws NamingException
     */
    public static TransactionManager getTransactionManager() throws NamingException {
        // String jndiName = "javax.transaction.TransactionManager";

        String jndiName = "java:/TransactionManager";

        InitialContext ic = JNDIUtil.getInitialContext();
       return (TransactionManager) ic.lookup(jndiName);
    }
    
    /**
     * 
     * @return TransactionManager 
     * @throws NamingException
     */
    public static UserTransaction getUserTransaction() throws NamingException {
        // String jndiName = "javax.transaction.TransactionManager";

        String jndiName = "java:comp/UserTransaction";

        InitialContext ic = JNDIUtil.getInitialContext();
       return (UserTransaction) ic.lookup(jndiName);
    }
}
