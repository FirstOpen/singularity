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
package org.firstopen.singularity.devicemgr.emulators;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Shutdown;
import org.firstopen.singularity.system.ShutdownManager;
import org.firstopen.singularity.util.InfrastructureException;

import com.sun.jini.config.Config;

/**
 * @author Tom Rose
 * @version $Id$
 * 
 */
public class EmulatorManager {

    private static final Log log = LogFactory.getLog(EmulatorManager.class);

    /**
     * @param args
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

  
        XMLConfiguration config;
        try {
            URL url = ClassLoader.getSystemResource("emulation.xml");

            
            config = new XMLConfiguration(url);
            
            Object obj = config.getProperty("emulatons.emulator.class");
            Collection<String> classes = null;
            if (obj instanceof Collection) {
                classes = (Collection<String>) obj;
            }else
                throw new ClassNotFoundException("property is not of type Collection");
            
            Thread[] threads = new Thread[50];
            int i = 0;
            for (String className : classes) {

                List<String> portList = (List<String>) config
                        .getList("emulations.emulator(" + i + ").ports");
                Class c = Class.forName(className);

                log.info("Shutdown manager registered...");
                ShutdownManager sdm = new ShutdownManager();
                Runtime.getRuntime().addShutdownHook(sdm);

                Class partypes[] = new Class[1];
                partypes[0] = String.class;
                Constructor ct = c.getConstructor(partypes);
                for (String port : portList) {
                    Object arglist[] = new Object[1];
                    arglist[0] = new Integer(37);
                    arglist[1] = new Integer(47);
                    Object emulator = ct.newInstance(arglist);

                    ShutdownManager.addManagedObject((Shutdown) emulator);
                    threads[i] = new Thread((Runnable) emulator);
                    threads[i].start();

                }// end for all ports

                i++;
            }// end for all emulators
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {

            throw e;
        }
    }// end main

}
