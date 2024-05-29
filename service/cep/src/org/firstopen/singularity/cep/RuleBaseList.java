/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RuleBaseList {

    HashMap<String, StatefulRuleBase> rulebases = null;

    HashMap<String, List<StatefulRuleBase>> ecSpecList = new HashMap<String, List<StatefulRuleBase>>();

    public synchronized HashMap<String, StatefulRuleBase> getRulebases() {
        return this.rulebases;
    }

    public StatefulRuleBase getRuleBase(String rulebaseName) {
        return this.rulebases.get(rulebaseName);
    }

    public synchronized void setRulebases(
            HashMap<String, StatefulRuleBase> rulebases) {
        Collection collection = rulebases.values();
        for (Iterator iter = collection.iterator(); iter.hasNext();) {
            StatefulRuleBase statefulRuleBase = (StatefulRuleBase) iter.next();
            Set<String> ecspecs = statefulRuleBase.getEcspecs();
            for (Iterator iterator = ecspecs.iterator(); iterator.hasNext();) {
                String ecspec = (String) iterator.next();
                List<StatefulRuleBase> list = this.ecSpecList.get(ecspec);
                if (list == null) {
                    list = new ArrayList<StatefulRuleBase>();
                    this.ecSpecList.put(ecspec, list);
                }
                list.add(statefulRuleBase);
            }// end for all ecspecs on ruleBase
        }// end for all ruleBases
    }

    public List<StatefulRuleBase> getRuleBases(String ecSpec) {
        return ecSpecList.get(ecSpec);

    }

    public void addECSpec(String rulebasename, String ecspec) {

        StatefulRuleBase statefulRuleBase = getRuleBase(rulebasename);
        statefulRuleBase.addECSpec(ecspec);
        List<StatefulRuleBase> list = this.ecSpecList.get(ecspec);
        if (list == null) {
            list = new ArrayList<StatefulRuleBase>();
            this.ecSpecList.put(ecspec, list);
        }
        list.add(statefulRuleBase);

    }
    
    public Set<String> getAllECSpecs(){
        
        return ecSpecList.keySet();
    }
}// RuleBaseList
