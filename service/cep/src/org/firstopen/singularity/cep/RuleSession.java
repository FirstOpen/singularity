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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.drools.rule.RuleSet;

public class RuleSession {

    Log log = LogFactory.getLog(RuleSession.class);
    
    private LocalRuleExecutionSetProvider ruleSetProvider;

    private RuleAdministrator ruleAdministrator;

    private RuleServiceProvider ruleServiceProvider;

    private RuleRuntime ruleRuntime;

    public static final String RULE_SERVICE_PROVIDER = "http://drools.org/";

    public synchronized HashMap<String, RuleBase> getAllRuleBases()
            throws RulesNotFoundException {

        HashMap<String, RuleBase> ruleBases = new HashMap<String, RuleBase>();

        /*
         * Get Rule sets from persistance storeage. DB contains the XML for the
         * rules.
         */

        String files[] = null;
        try {
            files = getRuleURLS();
        } catch (RuleFilesNotFoundException e) {
            log.error("unable to find rules", e);
            throw new RulesNotFoundException("unable to find rule files");
        }

        for (int i = 0; i < files.length; i++) {

            RuleBase ruleBase = null;
            String ruleSetName = null;
            try {
                ruleBase = RuleBaseLoader.loadFromUrl(new URL(files[i]));

                ruleBase.newWorkingMemory();

                ArrayList<String> ecspecs = new ArrayList<String>();

                ruleBase.getRuleBaseContext().put("ECSpec", ecspecs);

                RuleSet ruleset = (RuleSet) ruleBase.getRuleSets().get(0);

                ruleSetName = ruleset.getName();

                ruleBases.put(ruleSetName, ruleBase);

                createRuleEngine();

                InputStream is = new FileInputStream(files[i]);

                addRuleExecutionSet(files[i], is);

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

    public void createRuleEngine() throws Exception {
        RuleServiceProviderManager.registerRuleServiceProvider(
                RULE_SERVICE_PROVIDER, RuleServiceProviderImpl.class);

        ruleServiceProvider = RuleServiceProviderManager
                .getRuleServiceProvider(RULE_SERVICE_PROVIDER);

        ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

        ruleSetProvider = ruleAdministrator
                .getLocalRuleExecutionSetProvider(null);
    }

    public void addRuleExecutionSet(String bindUri, InputStream resourceAsStream)
            throws Exception {
        Reader ruleReader = new InputStreamReader(resourceAsStream);

        RuleExecutionSet ruleExecutionSet = ruleSetProvider
                .createRuleExecutionSet(ruleReader, null);

        ruleAdministrator.registerRuleExecutionSet(bindUri, ruleExecutionSet,
                null);
    }

    public StatefulRuleSession getStatefulRuleSession(String key)
            throws Exception {
        ruleRuntime = ruleServiceProvider.getRuleRuntime();

        return (StatefulRuleSession) ruleRuntime.createRuleSession(key, null,
                RuleRuntime.STATEFUL_SESSION_TYPE);
    }
    
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
}
