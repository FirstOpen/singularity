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
package org.firstopen.custom.event.agent;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.custom.view.EventMonitorBean;
import org.firstopen.singularity.admin.Constants;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * 
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: Initializer.java 1243 2006-01-14 03:33:37Z TomRose $
 * 
 */
public class Initializer implements ServletContextListener {

    Log log = LogFactory.getLog(this.getClass());

    public Initializer() {
    }

    public void contextInitialized(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        log.debug("Initializing Singularity...");

        try {
            servletContext.setAttribute(Constants.EVENT_MONITOR_KEY,
                    new EventMonitorBean());

            log.debug("Initialization complete...");

        } catch (InfrastructureException e) {

            log.error("unable to initialize context", e);
        }

    }

    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();
        try {

            EventMonitorBean eventMonitorBean = (EventMonitorBean) servletContext
                    .getAttribute(Constants.EVENT_MONITOR_KEY);
            if (eventMonitorBean != null) eventMonitorBean.shutdown();
            event.getServletContext().log(
                    "Context Destroyed, eventMonitorBean shutdown complete.");
        } catch (InfrastructureException e) {

            log.error("unable to shutdown EventMonitorBean", e);
        }
    }

}
