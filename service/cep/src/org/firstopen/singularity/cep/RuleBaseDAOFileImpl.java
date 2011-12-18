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
package org.firstopen.singularity.cep;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.drools.rule.RuleSet;
import org.firstopen.singularity.util.DAOUtil;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.hibernate.Session;

public class RuleBaseDAOFileImpl implements RuleBaseDAO {
    
    Log log = LogFactory.getLog(RuleBaseDAOFileImpl.class);

  
    public RuleBase getRuleBase(String ruleBaseName) {
        // TODO Auto-generated method stub
        return null;
    }

   
    public  HashMap<String, StatefulRuleBase> getAllRuleBases()
            throws RulesNotFoundException {

        
        HashMap<String, StatefulRuleBase> ruleBases = new HashMap<String, StatefulRuleBase>();

        /*
         * Get Rule sets from persistance storeage. DB contains the XML for the
         * rules.
         */

  
        List qruleBases = queryRuleBase();
        for (Iterator iter = qruleBases.iterator(); iter.hasNext();) {
            StatefulRuleBase statefulRuleBase = (StatefulRuleBase) iter.next();
            
            RuleBase ruleBase = null;
            String ruleSetName = null;
            try {
                ruleBase = RuleBaseLoader.loadFromUrl(new URL("file://" + statefulRuleBase.getRuleBaseURI()));

                ruleBase.newWorkingMemory();
     
                RuleSet ruleset = (RuleSet) ruleBase.getRuleSets().get(0);

                ruleSetName = ruleset.getName();
                
                statefulRuleBase.setRuleBase(ruleBase);
                
                ruleBases.put(ruleSetName, statefulRuleBase );
                
            } catch (Exception e) {
                /*
                 * Partial rule set is valid for now
                 */
                log.error("unable to load ruleset (" + ruleSetName + ")", e);
            }

        }

        if (ruleBases == null)
            throw new RulesNotFoundException("unable to find any rules");

        return ruleBases;
    }

    /**
     * Gets a list of files from the location specified by
     * "org.firstopen.singularity.ruledir" System Property.
     * @return
     * @throws RuleFilesNotFoundException
     */
    @SuppressWarnings("unused")
    private String[] getRuleURLS() throws RuleFilesNotFoundException {

        String s[] = null;
        // Create File Object
        String loc = System.getProperty("org.firstopen.singularity.ruledir");
        File dir = new File(loc);

        // List directory
        if (dir.isDirectory()) {
            s = dir.list();
            for (int i = 0; i < s.length; i++) {

                s[i] = "file://" + loc + "/" + s[i];
                log.debug("file is " + s[i]);
            }
        }
        if (s == null)
            throw new RuleFilesNotFoundException("No drl rule files found @ "
                    + dir.getPath());
        return s;
    }

   
    private List queryRuleBase() throws RulesNotFoundException{
        List ruleBases = null;
        
        Session session = null;
  
        try {
    		DAOUtil hibernateUtil = DAOUtilFactory
			.create(DAOUtil.hibernateALEJNDIName);

            session = hibernateUtil.getSession();
        } catch (Exception e) {
          log.error("unable to get hibernate session", e);
          throw new RulesNotFoundException();
        }
        ruleBases = session.createQuery("from StatefulRuleBase as q").list();
        return ruleBases;
    }
}// RuleBaseDAOFileImpl
