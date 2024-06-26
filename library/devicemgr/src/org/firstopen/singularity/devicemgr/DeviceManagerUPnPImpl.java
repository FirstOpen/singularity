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

import java.rmi.RemoteException;

import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.firstopen.singularity.devicemgr.common.DeviceECSpec;
import org.firstopen.singularity.devicemgr.filter.Event;

/**
 * @author TomRose
 * @version $Id$
 * TODO: AWT Components need removed
 */
public class DeviceManagerUPnPImpl extends DeviceManagerAbstractImpl implements DeviceManagerUPnP {

    /**
     * 
     */
    public DeviceManagerUPnPImpl() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.DeviceManagerAbstractImpl#sendRemoteEvent(org.firstopen.singularity.devicemgr.common.DeviceECSpec)
     */
    @Override
    public void sendRemoteEvent(DeviceECSpec ecSpecProfile) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.firstopen.singularity.devicemgr.DeviceManager#notify(org.firstopen.singularity.devicemgr.filter.Event)
     */
    public void notify(Event serviceEvent) throws RemoteException, Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    private final static String DESCRIPTION_FILE_NAME = "description/description.xml";
    
    private final static String CLOCK_DEVICE_TYPE = "urn:schemas-upnp-org:device:clock:1";
    private final static String CLOCK_SERVICE_TYPE = "urn:schemas-upnp-org:service:timer:1";
    
    private final static String LIGHT_DEVICE_TYPE = "urn:schemas-upnp-org:device:light:1";
    private final static String LIGHT_SERVICE_TYPE = "urn:schemas-upnp-org:service:power:1";

    private final static String AIRCON_DEVICE_TYPE = "urn:schemas-upnp-org:device:aircon:1";
    private final static String AIRCON_SERVICE_TYPE = "urn:schemas-upnp-org:service:temp:1";
    
    private final static String WASHER_DEVICE_TYPE = "urn:schemas-upnp-org:device:washer:1";
    private final static String WASHER_SERVICE_TYPE = "urn:schemas-upnp-org:service:state:1";

    private ControlPoint ctrlPoint;
    private Device tvDev;
    
    public void init()
    {
        //////////////////////////////////////
        // Control Ponit
        //////////////////////////////////////
        
        ctrlPoint = new ControlPoint();
        
        ctrlPoint.addNotifyListener(this);
        ctrlPoint.addSearchResponseListener(this);
        ctrlPoint.addEventListener(this);
        
        //////////////////////////////////////
        // Device
        //////////////////////////////////////

        try {
            tvDev = new Device(DESCRIPTION_FILE_NAME);
        }
        catch (InvalidDescriptionException e) {}
                
        Action getPowerAction = tvDev.getAction("GetPower");
        getPowerAction.setActionListener(this);
        
        Action setPowerAction = tvDev.getAction("SetPower");
        setPowerAction.setActionListener(this);
        
        ServiceList serviceList = tvDev.getServiceList();
        Service service = serviceList.getService(0);
        service.setQueryListener(this);

        on();
    }
    
    public void finalize()
    {
        off();
    }

    ////////////////////////////////////////////////
    //  Component
    ////////////////////////////////////////////////
/*
    private Component comp;
    
    public void setComponent(Component comp)
    {
        this.comp = comp;   
    }
    
    public Component getComponent()
    {
        return comp;
    }
  */  
    ////////////////////////////////////////////////
    //  on/off
    ////////////////////////////////////////////////

    private boolean onFlag = false;
    
    public void on()
    {
        ctrlPoint.search();
        onFlag = true;
    }

    public void off()
    {
        ctrlPoint.unsubscribe();    
        onFlag = false;
    }

    public boolean isOn()
    {
        return onFlag;
    }
    
    public void setPowerState(String state)
    {
        if (state == null) {
            off();
            return;
        }
        if (state.compareTo("1") == 0) {
            on();
            return;
        }
        if (state.compareTo("0") == 0) {
            off();
            return;
        }
    }
    
    public String getPowerState()
    {
        if (onFlag == true)
            return "1";
        return "0";
    }

    ////////////////////////////////////////////////
    //  Clock
    ////////////////////////////////////////////////

    private String clockTime = ""; 
    
    public String getClockTime()
    {
        return clockTime;   
    }
    
    ////////////////////////////////////////////////
    //  Aircon
    ////////////////////////////////////////////////

    private String airconTemp = ""; 
    
    public String getAirconTempture()
    {
        return airconTemp;  
    }

    ////////////////////////////////////////////////
    //  Message
    ////////////////////////////////////////////////

    private String message = ""; 
    
    public void setMessage(String msg)
    {
        message = msg;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    ////////////////////////////////////////////////
    //  Device (Common)
    ////////////////////////////////////////////////
    
    public boolean isDevice(SSDPPacket packet, String deviceType)
    {
        String usn = packet.getUSN();
        if (usn.endsWith(deviceType))
            return true;
        return false;
    }
    
    public Service getDeviceService(String deviceType, String serviceType)
    {
        Device dev = ctrlPoint.getDevice(deviceType);
        if (dev == null)
            return null;
        Service service = dev.getService(serviceType);
        if (service == null)
            return null;
        return service;
    }

    public boolean subscribeService(SSDPPacket packet, String deviceType, String serviceType)
    {
        Service service = getDeviceService(deviceType, serviceType);
        if (service == null)
            return false;
        return ctrlPoint.subscribe(service);
    }
    
    ////////////////////////////////////////////////
    //  SSDP Listener
    ////////////////////////////////////////////////
    
    public void checkNewDevices(SSDPPacket packet)
    {
        subscribeService(packet, CLOCK_DEVICE_TYPE, CLOCK_SERVICE_TYPE);
        subscribeService(packet, AIRCON_DEVICE_TYPE, AIRCON_SERVICE_TYPE);
        subscribeService(packet, LIGHT_DEVICE_TYPE, LIGHT_SERVICE_TYPE);
        subscribeService(packet, WASHER_DEVICE_TYPE, WASHER_SERVICE_TYPE);
    }
    
    public void checkRemoveDevices(SSDPPacket packet)
    {
        if (isDevice(packet, CLOCK_DEVICE_TYPE) == true)
            clockTime = "";
        if (isDevice(packet, AIRCON_DEVICE_TYPE) == true)
            airconTemp = "";
    }
    
    ////////////////////////////////////////////////
    //  Control Point Listener
    ////////////////////////////////////////////////
    
    public void deviceNotifyReceived(SSDPPacket packet)
    {
        if (packet.isAlive() == true)
            checkNewDevices(packet);
        if (packet.isByeBye() == true)
            checkRemoveDevices(packet);
    }
    
    public void deviceSearchResponseReceived(SSDPPacket packet)
    {
        checkNewDevices(packet);
    }
    
    public void eventNotifyReceived(String uuid, long seq, String name, String value)
    {
        //System.out.println("Notify = " + uuid + ", " + seq + "," + name + "," + value);
        Service service = ctrlPoint.getSubscriberService(uuid);
        if (service == null)
            return;
        if (service.isService(CLOCK_SERVICE_TYPE) == true)
            clockTime = value;
        else if (service.isService(AIRCON_SERVICE_TYPE) == true)
            airconTemp = value;
        else {
            if (value != null && 0 < value.length()) {
                Device dev = service.getDevice();
                String fname = dev.getFriendlyName();
                message = fname + ":" + value;
            }
        }
     //   comp.repaint();
    }

    ////////////////////////////////////////////////
    // ActionListener
    ////////////////////////////////////////////////

    public boolean actionControlReceived(Action action)
    {
        String actionName = action.getName();
        if (actionName.equals("GetPower") == true) {
            String state = getPowerState();
            Argument powerArg = action.getArgument("Power");
            powerArg.setValue(state);
           // comp.repaint();
            return true;
        }
        if (actionName.equals("SetPower") == true) {
            Argument powerArg = action.getArgument("Power");
            String state = powerArg.getValue();
            setPowerState(state);
            state = getPowerState();
            Argument resultArg = action.getArgument("Result");
            resultArg.setValue(state);
         //   comp.repaint();
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////////
    // QueryListener
    ////////////////////////////////////////////////

    public boolean queryControlReceived(StateVariable stateVar)
    {
        stateVar.setValue(getPowerState());
        return true;
    }

    ////////////////////////////////////////////////
    //  start/stop
    ////////////////////////////////////////////////
    
    public void start()
    {
        ctrlPoint.start();
        tvDev.start();
    }

    public void stop()
    {
        ctrlPoint.stop();
        tvDev.stop();
    }

  }
