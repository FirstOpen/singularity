package org.firstopen.singularity.config;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface InterrogatorManagementSLSBHome extends javax.ejb.EJBHome {
	public InterrogatorManagementSLSBRemote create() throws RemoteException,
			CreateException;
}
