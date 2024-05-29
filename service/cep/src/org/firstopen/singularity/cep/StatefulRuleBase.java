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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.reteoo.FactHandleFactory;
import org.drools.spi.ConflictResolver;
import org.drools.spi.RuleBaseContext;

/**
 * @hibernate.class table="RuleBase"
 * @version $Id: StatefulRuleBase.java 1259 2006-01-18 14:18:14Z TomRose $
 */
public class StatefulRuleBase implements RuleBase {

   
    private static final long serialVersionUID = 4674147207813676268L;

    private String id = null;
    
    private RuleBase ruleBase = null;
    
    private String ruleBaseURI = null;

    private WorkingMemory workingMemory = null;
    
   
    private Set<String> ecspecs = new HashSet<String>();
    
   

    /**
     * 
     *
     */
    public StatefulRuleBase(){
        
    }
    /**
     * 
     */
    public StatefulRuleBase(RuleBase rulebase) {

        this.ruleBase = rulebase;
    }
    
  
    public WorkingMemory newWorkingMemory() {

        workingMemory = ruleBase.newWorkingMemory();
        return workingMemory;
    }

    public ConflictResolver getConflictResolver() {

        return this.ruleBase.getConflictResolver();
    }

    public FactHandleFactory getFactHandleFactory() {

        return this.getFactHandleFactory();
    }

    public List getRuleSets() {

        return this.ruleBase.getRuleSets();
    }

    public RuleBaseContext getRuleBaseContext() {
        // 
        return this.ruleBase.getRuleBaseContext();
    }

    public WorkingMemory getWorkingMemory() {
        return workingMemory;
    }

    public void addECSpec(String ecspec){
        ecspecs.add(ecspec);
    }
    
    public boolean hasECSpec(String ecspec){
        return ecspecs.contains(ecspec);
    }
    
    public void removeECSpec(String ecspec){
        ecspecs.remove(ecspec);
    }


    /** 
    *
    *The getter method for this ruleBase ecSpecs.
    *
    * 
    * 
    * 
    * 
    * 
    * 
    *  @hibernate.set
     *      inverse="false"
     *      lazy="false"
     *      cascade="all"
     * @hibernate.collection-key
     *      column="ecspecid"     
     * @hibernate.collection-element 
     *      column="ecspecname" 
     *      type="string"
    */
    public synchronized Set<String> getEcspecs() {

        return ecspecs;
    }

    public synchronized void setEcspecs(Set<String> ecspecs) {
        this.ecspecs = ecspecs;
    }
    
    /**
     * @hibernate.property 
     * @return
     */
    public synchronized String getRuleBaseURI() {
        return ruleBaseURI;
    }
    public synchronized void setRuleBaseURI(String ruleBaseURI) {
        this.ruleBaseURI = ruleBaseURI;
    }
    public synchronized RuleBase getRuleBase() {
        return ruleBase;
    }
    public synchronized void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }
    
    /**
     * @hibernate.id generator-class="uuid.hex" length="128"
     */
    public synchronized String getId() {
        return id;
    }
    public synchronized void setId(String id) {
        this.id = id;
    }
    /* (non-Javadoc)
     * @see org.drools.RuleBase#getCurrentThreadWorkingMemory()
     */
    public WorkingMemory getCurrentThreadWorkingMemory() {
        // TODO Auto-generated method stub
        return null;
    }
  

    
   /*
    * 
    * public synchronized HashSet<ECSpec> getECSpec() {
 
        HashSet<ECSpec> set = new HashSet<ECSpec>();
        
        for (Iterator iter = ecspecs.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            set.add(new ECSpec(element, null));
         }
        return set;
    }

    public synchronized void setECSpec(HashSet<ECSpec> ecspecs) {
        HashSet<String> set = new HashSet<String>();
         for (Iterator iter = ecspecs.iterator(); iter.hasNext();) {
            ECSpec element = (ECSpec) iter.next();
            set.add(element.getSpecName());
        }
        this.ecspecs = set;
    }
    */
}
