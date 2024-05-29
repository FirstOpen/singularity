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

package org.firstopen.singularity.admin.view;

import java.io.Serializable;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import org.firstopen.singularity.admin.TransactionPhaseListener;

//	 Referenced classes of package org.jia.ptrack.web:
//	            Visit, AuthenticationBean

public class BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5997831905489568447L;

	public BaseBean() {
        /*
         * transction support for all actions.
         * TODO: possibly move to SpringFramework enabled transaction support.
         */
         //addPhaseListeners();
     	}

    private void addPhaseListeners() {
        LifecycleFactory lifecycelFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = lifecycelFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.addPhaseListener(new TransactionPhaseListener());

    }
	public FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public Application getApplication() {
		return getFacesContext().getApplication();
	}

	

}
