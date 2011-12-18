/* 
 * Copyright 2005 i-Konect LLC
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

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Sensor;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class SensorBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = -6873234217488367502L;

    private List<SelectItem> list = new ArrayList<SelectItem>();
    
    private List<String> selected = null;

    private Log log = LogFactory.getLog(getClass());
    
    private DeviceProfileBean currentDeviceProfileBean = null; 
    
    private Sensor currentSensor = new Sensor();
    
   

    /**
     * 
     */
    public SensorBean(DeviceProfileBean deviceProfileBean) {
        super();
        this.currentDeviceProfileBean = deviceProfileBean;
    }

   
    
  
    public void add(SelectItem selectItem) {
        list.add(selectItem);
    }

    /**
     * @return Returns the list.
     */
    public List<SelectItem> getList() {
        return list;
    }

    /**
     * @param list The list to set.
     */
    public void setList(List<SelectItem> list) {
        this.list = list;
    }

    /**
     * @return Returns the selected.
     */
    public List<String> getSelected() {
        return selected;
    }

    /**
     * @param selected The selected to set.
     */
    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    private void createSensorSelectList() {
      
        list.clear();
        for ( Sensor sensor : currentDeviceProfileBean.getCurrentDeviceProfile().getSensorSet()) {
           list.add(new SelectItem(sensor.getName()));
        }
        
    }
    
    public void save() {
        log.info("save");
    }
    
    public void add() {
        log.info("add");
        currentDeviceProfileBean.getCurrentDeviceProfile().addSensor(currentSensor);
        refresh();
    }
    
    public void remove() {
        log.info("remove");
        for (String sensorName : selected) {
            currentDeviceProfileBean.getCurrentDeviceProfile().removeSensor(sensorName);    
        }
        
    }
    
    public void clear() {
        log.info("clear");
    }
    
    public void search() {
        log.info("search");
    }

    /**
     * @return Returns the currentDeviceProfileBean.
     */
    public DeviceProfileBean getCurrentDeviceProfileBean() {
        return currentDeviceProfileBean;
    }

    /**
     * @param currentDeviceProfileBean The currentDeviceProfileBean to set.
     */
    public void setCurrentDeviceProfileBean(DeviceProfileBean currentDeviceProfileBean) {
        this.currentDeviceProfileBean = currentDeviceProfileBean;
        this.createSensorSelectList();
    }

    /**
     * @return Returns the currentSensor.
     */
    public Sensor getCurrentSensor() {
        return currentSensor;
    }

    /**
     * @param currentSensor The currentSensor to set.
     */
    public void setCurrentSensor(Sensor currentSensor) {
        this.currentSensor = currentSensor;
    }
    
    public void refresh() {
        createSensorSelectList();
        setCurrentSensor(new Sensor());
    }
}
