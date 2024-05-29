/*
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
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
package org.firstopen.singularity.admin.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.Sensor;

/**
 * @author TomRose
 * @version $Id$
 * 
 */
public class ReaderBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = 6015026263660668923L;

    private List<SelectItem> list = new ArrayList<SelectItem>();

    private List<String> selected = null;

    private Log log = LogFactory.getLog(getClass());

    private Reader currentReader = new Reader();

    private DeviceProfileBean currentDeviceProfileBean = null;

    /**
     * 
     */
    public ReaderBean(DeviceProfileBean deviceProfileBean) {
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
     * @param list
     *            The list to set.
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
     * @param selected
     *            The selected to set.
     */
    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    private void createReaderSelectList() {

        list.clear();
        for (Reader reader : currentDeviceProfileBean.getCurrentDeviceProfile()
                .getReaderSet()) {
            list.add(new SelectItem(reader.getName()));
        }

    }

    public void create() {
        log.info("create");

        HashSet<Sensor> sensors = new HashSet<Sensor>();

        /*
         * get the selected sensors for this reader, populate a set of sensors
         * for the reader.
         */
        for (String sensorName : currentDeviceProfileBean.getSensorBean()
                .getSelected()) {
            sensors.add(currentDeviceProfileBean.getCurrentDeviceProfile()
                    .getSensor(sensorName));

        }
        /*
         * place the sensor list on the readers, and add the phyiscal reader to
         * the current Device Profile.
         */
        currentReader.setSensors(sensors);
        currentDeviceProfileBean.getCurrentDeviceProfile().addReader(
                currentReader);
        
        String deviceManagerId = currentDeviceProfileBean
                                  .getCurrentDeviceProfile()
                                  .getDeviceManagerID();
        currentReader.setDeviceManagerId(deviceManagerId);
        refresh();
    }

    public void delete() {
        log.info("delete");
        for (String readerName : selected) {
            currentDeviceProfileBean.getCurrentDeviceProfile().removeReader(
                    readerName);
        }
        refresh();
    }

    public void clear() {
        log.info("clear");

    }

    public void search() {
        log.info("search");
    }

    public void save() {
        log.info("save");
    }

    /**
     * @return Returns the currentDeviceProfileBean.
     */
    public DeviceProfileBean getCurrentDeviceProfileBean() {
        return currentDeviceProfileBean;
    }

    /**
     * @param currentDeviceProfileBean
     *            The currentDeviceProfileBean to set.
     */
    public void setCurrentDeviceProfileBean(
            DeviceProfileBean currentDeviceProfileBean) {
        this.currentDeviceProfileBean = currentDeviceProfileBean;
    }

    /**
     * @return Returns the currentReader.
     */
    public Reader getCurrentReader() {
        return currentReader;
    }

    /**
     * @param currentReader
     *            The currentReader to set.
     */
    public void setCurrentReader(Reader currentReader) {
        this.currentReader = currentReader;
    }

    public void refresh() {

        createReaderSelectList();
        setCurrentReader(new Reader());
    }
}
