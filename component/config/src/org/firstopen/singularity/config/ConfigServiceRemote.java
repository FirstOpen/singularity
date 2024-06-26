/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.firstopen.singularity.config;

import java.rmi.RemoteException;
import java.util.List;

import org.firstopen.singularity.util.InfrastructureException;



/*
 * ConfigServiceRemote Purpose of this SLSB is to remotely expose Configuration
 * for the DeviceManagers
 */
public interface ConfigServiceRemote extends javax.ejb.EJBObject {

    
    public List<DeviceProfile> getConfig(String deviceName) throws  RemoteException, InfrastructureException;
	
	public String getStandardVersion() throws RemoteException;

	public String getVendorVersion() throws  RemoteException;
    
    
	
}
