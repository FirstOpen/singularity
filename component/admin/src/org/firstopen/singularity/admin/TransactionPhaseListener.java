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
package org.firstopen.singularity.admin;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.TransactionManager;

/**
 * 
 * PhaseListener that provides transaction support for all BackingBeans.
 * 
 * @author TomRose
 * @version $Id$
 * 
 */
public class TransactionPhaseListener implements PhaseListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2432126487454834605L;

    private Log log = LogFactory.getLog(getClass());

    private TransactionManager tx = new TransactionManager();

    /**
     * @throws NamingException
     * 
     */
    public TransactionPhaseListener() {
        super();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {

        if (event.getPhaseId() == PhaseId.RESTORE_VIEW)
            log.debug("Processing new  Request!");

        log.debug("before - " + event.getPhaseId().toString());

        if (event.getPhaseId() == PhaseId.RESTORE_VIEW ||
            event.getPhaseId()==PhaseId.RENDER_RESPONSE) {
            tx.begin();
        }
    }

    /**
     * 
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {

        log.debug("after -  " + event.getPhaseId().toString());

        if (event.getPhaseId() == PhaseId.RENDER_RESPONSE ||
            event.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
            log.debug("Done with Request, commit transaction...");
      
            try {
               
                tx.commit();
            } catch (InfrastructureException e) {
                log.error("commit failed, rollback transaction commencing");
                tx.rollback();
                throw e;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {

        return PhaseId.ANY_PHASE;
    }

}
