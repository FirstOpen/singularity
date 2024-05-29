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
package org.firstopen.singularity.util;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


import sun.misc.Launcher;

/**
 * @author Tom Rose
 * @version $Id$
 * 
 */
public class ReflectionUtil {

    /**
     * 
     */
    public ReflectionUtil() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Will return a set of class file names for the specified package
     * name (i.e. org.firstopen.singularity). If no classes are found
     * will return an emtpy set.
     * 
     * @param pckgname
     * @return
     */
    public static Set<String> getClassFilenames(String pckgname)  {
        HashSet<String> classes = new HashSet<String>();

        if (!pckgname.startsWith("/")) {
            pckgname = "/" + pckgname;
        }

        pckgname = pckgname.replace('.', '/');

        URL url = Launcher.class.getResource(pckgname);

        File directory = new File(url.getFile());

        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (String filename : files) {

                // we are only interested in .class files
                if (filename.endsWith(".class")) {
                   
                    classes.add(filename);
                }
            }// end for
        }
        
        return classes;
    }
}
