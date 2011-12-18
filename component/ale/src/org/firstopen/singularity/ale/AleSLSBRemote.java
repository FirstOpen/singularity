/*
 * Copyright 2005 Jeff Bride
 * 
 * Licensed under the Apache License, Version 2.0 (the "License") throws RemoteException; you may not
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

package org.firstopen.singularity.ale;


import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import org.firstopen.singularity.ale.exception.ImplementationException;
import org.firstopen.singularity.ale.exception.NoSuchSubscriberException;
import org.w3c.dom.Document;

/*
 * AleSLSBRemote Purpose of this SLSB is to remotely expose ALE methods to
 * client processes
 */
public interface AleSLSBRemote extends javax.ejb.EJBObject {

    public  String define(Document doc) throws RemoteException;

    public  void define(String specName, ECSpec spec) throws RemoteException;

    @SuppressWarnings("unchecked")
    public  void unDefine(String specName)
            throws RemoteException, NoSuchSubscriberException, ImplementationException;

    public  List getECSpecNames() throws RemoteException;

    public  void subscribe(String specName, String notificationURI) throws RemoteException;

    public  void unSubscribe(String specName, String notificationURI) throws RemoteException;

    /*
     * Caller required to persists modifed ECSpec
     */
    public  void unSubscribe(ECSpec spec, String notificationURI)
            throws RemoteException, NoSuchSubscriberException, ImplementationException;

    /*
     * -- ECSpec has already been defined -- method blocks until ECSpec thread
     * returns ECReports
     */
    public  ECReports poll(String specName) throws RemoteException;

    /* ECSpec has not been previously defined */
    public  ECReports immediate(Document doc)
            throws RemoteException, ImplementationException;

    public  Set getSubscribers(String specName) throws RemoteException;

    public  String getStandardVersion() throws RemoteException;

    public  String getVendorVersion() throws RemoteException;

    public  void initializeECSpecTimer(long duration,
            long repeatPeriod, String timerName) throws RemoteException;

    public  void initialize() throws RemoteException;

}
