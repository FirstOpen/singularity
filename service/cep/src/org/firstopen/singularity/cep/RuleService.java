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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ReflectionException;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.FactException;
import org.drools.WorkingMemory;
import org.firstopen.singularity.ale.AleSLSBHome;
import org.firstopen.singularity.ale.AleSLSBRemote;
import org.firstopen.singularity.system.ReaderEvent;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.JNDIUtil;
import org.jboss.system.ServiceMBeanSupport;

/**
 * @author TomRose
 * 
 */
public class RuleService extends ServiceMBeanSupport implements
		RuleServiceMBean, NotificationListener {

	private Log log = LogFactory.getLog(this.getClass());

	private RuleBaseList ruleBaseList = new RuleBaseList();

	public void createService() throws Exception {

		addNotifyListener(this);
	}

	public void startService() throws Exception {
		loadRuleBase();
	}

	public void stopService() throws Exception {

	}

	public void destoryService() throws Exception {

	}

	/**
	 * Get Rule sets from persistance storeage. DB contains the XML for the
	 * rules.
	 * 
	 */
	public void loadRuleBase() throws RulesNotFoundException {

		RuleBaseDAO ruleBaseDAO = RuleBaseDAOFactory.create();

		ruleBaseList.setRulebases(ruleBaseDAO.getAllRuleBases());

		ruleChangeNotification("rules have changed, cached reloaded");

		DAOUtilFactory.close();
	}

	/**
	 * 
	 * @param listener
	 * @throws InstanceNotFoundException
	 */
	protected void addNotifyListener(NotificationListener listener)
			throws InstanceNotFoundException {
		getServer().addNotificationListener(getServiceName(), listener, null,
				null);
	}

	/**
	 * Called by the ALE service
	 * 
	 * @param ecspec
	 * @param events
	 * @throws FactException
	 */
	public void assertEvents(String ecspec, List<ReaderEvent> events)
			throws FactException {

		List ruleBases = ruleBaseList.getRuleBases(ecspec);
		for (Iterator iter = ruleBases.iterator(); iter.hasNext();) {
			StatefulRuleBase rulebase = (StatefulRuleBase) iter.next();
			WorkingMemory workingMemory = rulebase.getWorkingMemory();

			for (Iterator iterator = events.iterator(); iterator.hasNext();) {
				ReaderEvent event = (ReaderEvent) iterator.next();
				workingMemory.assertObject(event);

			}// end for all events
			workingMemory.fireAllRules();

		}// end for each ecspec
	}

	/**
	 * 
	 * @param rulebaseName
	 */
	public void resetSessionState(String rulebaseName) {
		ruleBaseList.getRuleBase(rulebaseName).newWorkingMemory();
	}

	/**
	 * 
	 * <p>
	 * Broadcasts a notification to the cluster partition. * * This example does
	 * not ensure that a notification sequence number * is unique throughout the
	 * partition.
	 * </p>
	 * 
	 * @param message
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 */
	public void ruleChangeNotification(String message) {

		long now = System.currentTimeMillis();

		Notification notification = new Notification(
				"org.firstopen.singularity.rulechange", super.getServiceName(),
				now, now, message);

		sendNotification(notification);
	}

	/**
	 * 
	 * <p>
	 * call back for notification listener
	 * </p>
	 * 
	 * @param notification
	 * @param handback
	 */
	public void handleNotification(Notification notification, Object handback) {
		log.debug("notification recveived");

	}

	/**
	 * 
	 * @param rulebasename
	 */
	public void printECSpec(String rulebasename) {
		StatefulRuleBase rulebase = ruleBaseList.getRuleBase(rulebasename);

		Set<String> ecspecList = rulebase.getEcspecs();
		for (String ecspec : ecspecList) {

			log.debug("element = " + ecspec);

		}
	}

	/**
	 * @param rulebasename
	 * @param ecspec
	 */
	public void addECSpec(String rulebasename, String ecspec) {

		ruleBaseList.addECSpec(rulebasename, ecspec);

	}

	/**
	 * Subscribe with a local JMX service uri
	 * 
	 */
	protected void subscribeALE() {

		InitialContext jndiContext;
		try {
			jndiContext = JNDIUtil.getInitialContext();

			AleSLSBHome aHome = (AleSLSBHome) jndiContext
					.lookup("ejb/ale/AleSLSB");
			AleSLSBRemote aSLSB = aHome.create();

			Set<String> ecSpecList = ruleBaseList.getAllECSpecs();
			for (Iterator iter = ecSpecList.iterator(); iter.hasNext();) {
				String element = (String) iter.next();
				aSLSB.subscribe(element, "jmx://localhost/" + serviceName);
			}// end for all ecSpecs
		} catch (Exception e) {
			log.error(e);
			throw new InfrastructureException(e);
		}
	}
}// end RuleService
