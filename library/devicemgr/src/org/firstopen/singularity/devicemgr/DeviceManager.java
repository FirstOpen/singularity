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
package org.firstopen.singularity.devicemgr;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.filter.Event;

public interface DeviceManager extends Remote {



	/**
	 * Returns void
	 * 
	 * @return void
	 * @throws RemoteException,
	 *             Exception if a remote communication problem occurs
	 */
	public void ping() throws RemoteException, Exception;

	/**
	 * Returns a message string.
	 * 
	 * @return message boolean success of registration
	 * @throws RemoteException,
	 *             Exception if a remote communication problem occurs
	 */
	public void registerDeviceECSpec(DeviceECSpec deviceECSpec)
			throws RemoteException, Exception;

	/**
	 * Returns a message string.
	 * 
	 * @return message boolean success of registration
	 * @throws RemoteException,
	 *             Exception if a remote communication problem occurs
	 */
	public void unRegisterDeviceECSpec(String deviceECSpecName)
			throws RemoteException, Exception;

	/**
	 * 
	 * Implements the physical transport of the information based on chosen
	 * distributed component selection.
	 * 
	 */
	public abstract void sendRemoteEvent(DeviceECSpec ecSpecProfile)
			throws RemoteException, Exception;

    /**
     * 
     * Notify service of a remote event
     */
    public abstract void notify(Event serviceEvent)
            throws RemoteException, Exception;

}
