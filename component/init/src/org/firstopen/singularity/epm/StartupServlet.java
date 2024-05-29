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
package org.firstopen.singularity.epm;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.ale.AleSLSBHome;
import org.firstopen.singularity.ale.AleSLSBRemote;
import org.firstopen.singularity.util.JNDIUtil;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class StartupServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 3479514272358203398L;


    private Log log = LogFactory.getLog(this.getClass());
    
    /**
     * Constructor of the object.
     */
    public StartupServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * Returns information about the servlet, such as author, version, and
     * copyright.
     * 
     * @return String information about this servlet
     */
    public String getServletInfo() {
        return "Singularity EPM Cluster Initializer";
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occure
     */
    public void init() throws ServletException {
      
           

            InitialContext jndiContext;
            try {
                jndiContext = JNDIUtil.getInitialContext();
           
            Object objref = jndiContext.lookup("ejb/ale/AleSLSB");
            AleSLSBHome aleSLSBHome = (AleSLSBHome) PortableRemoteObject.narrow(
                    objref, AleSLSBHome.class);

            AleSLSBRemote aleSLSB  = aleSLSBHome.create();
            aleSLSB.initialize();
            
            
            } catch (NamingException e) {
                // TODO Auto-generated catch block
                log.error(e);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                log.error(e);
            } catch (CreateException e) {
                // TODO Auto-generated catch block
                log.error(e);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error(e);
            }
    }

}
