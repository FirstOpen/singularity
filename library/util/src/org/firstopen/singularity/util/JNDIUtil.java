/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIUtil {
    public static InitialContext getInitialContext() throws NamingException {
        return getInitialContext("localhost");
    }

    public static InitialContext getInitialContext(String jndiServerAddress)
            throws NamingException {
        InitialContext jndiContext = null;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://" + jndiServerAddress + ":1099");
        env
                .put(Context.URL_PKG_PREFIXES,
                        "org.jboss.naming:org.jnp.interfaces");
        jndiContext = new InitialContext(env);

        return jndiContext;
    }

    public static InitialContext getInitialContext(String jndiServerAddress,
            String user, String password) throws NamingException {
        InitialContext jndiContext = null;

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "jnp://" + jndiServerAddress + ":1099");
        env
                .put(Context.URL_PKG_PREFIXES,
                        "org.jboss.naming:org.jnp.interfaces");
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);
        jndiContext = new InitialContext(env);

        return jndiContext;
    }

}
