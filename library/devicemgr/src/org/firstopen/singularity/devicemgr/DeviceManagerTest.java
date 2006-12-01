package org.firstopen.singularity.devicemgr;



import java.rmi.RemoteException;

import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.filter.Event;


public class DeviceManagerTest extends DeviceManagerAbstractImpl {

	@Override
	public void sendRemoteEvent(DeviceECSpec ecSpecProfile) {
     
		log.info(ecSpecProfile.getSpecName());

	}

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.DeviceManager#notify(org.firstopen.singularity.devicemgr.Event)
     */
    public void notify(Event serviceEvent) throws RemoteException, Exception {
        // TODO Auto-generated method stub
        
    }

}
