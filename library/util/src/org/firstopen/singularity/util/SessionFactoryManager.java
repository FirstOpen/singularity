/* 
 * Copyright 2005 i-Konect LLC
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

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SessionFactoryManager {

	public SessionFactory sessionFactory = null;

	public final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();

	public final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	public final ThreadLocal<Interceptor> threadInterceptor = new ThreadLocal<Interceptor>();

    public final ThreadLocal<Boolean> threadJTA = new ThreadLocal<Boolean>();
    
    public final ThreadLocal<Boolean> threadSupportBMT = new ThreadLocal<Boolean>();
}
