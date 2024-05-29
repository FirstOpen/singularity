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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIData;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.firstopen.singularity.config.DeviceProfile;
import org.firstopen.singularity.config.dao.DeviceProfileDAO;
import org.firstopen.singularity.config.dao.DeviceProfileDAOFactory;
import org.firstopen.singularity.devicemgr.DeviceManager;
import org.firstopen.singularity.devicemgr.common.ServiceLocator;
import org.firstopen.singularity.devicemgr.interrogator.Interrogator;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.ReflectionUtil;

// Referenced classes of package org.firstopen.singularity.admin.view:
// BaseBean

public class DeviceProfileBean extends BaseBean {

    private static final long serialVersionUID = 0x2f85ff95371b384L;

    private HtmlForm form = null;

    private DeviceProfile currentDeviceProfile = null;

    private Log log = LogFactory.getLog(getClass());

    private UIData uiTable;

    private List<DeviceProfile> list = null;

    private SensorBean sensorBean = null;

    private ReaderBean readerBean = null;

    public DeviceProfileBean() {
        super();
        /*
         * Initialize the Reader adn Sensor Beans before the DeviceProfileBean
         * is initalized. SensorBean and ReaderBean will be refreshed each time
         * the currrentDeviceProfile is set in this Bean.
         */
        sensorBean = new SensorBean(this);
        readerBean = new ReaderBean(this);

        try {

            createDeviceProfileList();

        } catch (InfrastructureException e) {

            log.error("cannot initialize DeviceProfileBean", e);
        }

    }

    private void createDeviceProfileList() {
        DeviceProfileDAO deviceProfileDAO = null;
        try {
            deviceProfileDAO = DeviceProfileDAOFactory.create();

            list = deviceProfileDAO.getAll();

            if (currentDeviceProfile == null && list != null) {
                if (list.size() > 0) {
                    setCurrentDeviceProfile(list.get(0));
                }
            }

            if (currentDeviceProfile == null) {
                setCurrentDeviceProfile(new DeviceProfile());
            }
        } catch (InfrastructureException e) {
            log.error("unable to get DeviceProfile List", e);

        }

    }

    public void save() {
        DeviceProfileDAO deviceProfileDAO = null;

        try {
            deviceProfileDAO = DeviceProfileDAOFactory.create();

            deviceProfileDAO.update(currentDeviceProfile);
            /*
             * refresh the list
             */
            createDeviceProfileList();

            DeviceManager dManager = (DeviceManager) ServiceLocator.getService(
                    DeviceManager.class, currentDeviceProfile
                            .getDeviceManagerID(), 250);
            log.debug("Located device manager = " + dManager
                    + " for interrogatorId = "
                    + currentDeviceProfile.getDeviceManagerID());
            dManager.ping();

        } catch (Exception ex) {
            log.error(ex);
        }
    }
    public void getDetail(ActionEvent e) {
        setCurrentDeviceProfile((DeviceProfile) uiTable.getRowData());
    }

    public String search() {
        String result = "failed";
        try {
            refresh(currentDeviceProfile);
            result = "success";
        } catch (InfrastructureException e) {
            log.error(e);
        }

        return result;
    }

    public void delete() {

        DeviceProfileDAO deviceProfileDAO = null;

        try {
            deviceProfileDAO = DeviceProfileDAOFactory.create();

            deviceProfileDAO.delete(currentDeviceProfile);
            currentDeviceProfile = null;
            createDeviceProfileList();
        } catch (InfrastructureException e1) {
            log.error("unable retrieve related deviceProfile", e1);
       }
    }
    private void refresh(DeviceProfile searchRC) throws InfrastructureException {
        DeviceProfileDAO deviceProfileDAO = null;
        try {
            deviceProfileDAO = DeviceProfileDAOFactory.create();

            searchRC = deviceProfileDAO.get(searchRC);

            if (searchRC != null) setCurrentDeviceProfile(searchRC);
        } catch (InfrastructureException e) {
            log.error("unable to refresh current device profile");
            throw e;
        }
    }

    public String create() {
        setCurrentDeviceProfile(new DeviceProfile());
        return "success";
    }

    public void refresh(String objId) {
        try {
            if (objId != null) {
                DeviceProfile searchDP = new DeviceProfile();
                searchDP.clear();
                refresh(searchDP);
            }
        } catch (InfrastructureException e) {
            log.error("unable retrieve related deviceProfile", e);
        }
    }

    public HtmlForm getForm() {
        return form;
    }

    public void setForm(HtmlForm form) {
        this.form = form;
    }

    public DeviceProfile getCurrentDeviceProfile() {
        return currentDeviceProfile;
    }

    public void setCurrentDeviceProfile(DeviceProfile rc) {
        currentDeviceProfile = rc;
        sensorBean.refresh();
        readerBean.refresh();

    }

    public String next() {
        log.info("action() = next");
        return "success";
    }

    public void clear() {
        DeviceProfile deviceProfile = new DeviceProfile();
        deviceProfile.clear();
        setCurrentDeviceProfile(deviceProfile);

    }

    /**
     * @return Returns the list.
     */
    public List<DeviceProfile> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(List<DeviceProfile> list) {
        this.list = list;
    }

    /**
     * @return Returns the uiTable.
     */
    public UIData getUiTable() {
        return uiTable;
    }

    /**
     * @param uiTable
     *            The uiTable to set.
     */
    public void setUiTable(UIData uiTable) {
        this.uiTable = uiTable;
    }

    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: " + scrollerEvent.getScrollerfacet()
                        + ", pageindex: " + scrollerEvent.getPageIndex());
    }

    /**
     * @return Returns the sensorBean.
     */
    public SensorBean getSensorBean() {
        return sensorBean;
    }

    /**
     * @param sensorBean
     *            The sensorBean to set.
     */
    public void setSensorBean(SensorBean sensorBean) {
        this.sensorBean = sensorBean;
    }

    /**
     * @return Returns the readerBean.
     */
    public ReaderBean getReaderBean() {
        return readerBean;
    }

    /**
     * @param readerBean
     *            The readerBean to set.
     */
    public void setReaderBean(ReaderBean readerBean) {
        this.readerBean = readerBean;

    }

    public Set<String> getInterrogators() {

        String packageName = "org.firstopen.singularity.devicemgr.interrogator";
        Set<String> classFileNames = ReflectionUtil
                .getClassFilenames(packageName);

        HashSet<String> classes = new HashSet<String>();
        for (String filename : classFileNames) {

            /*
             * removes the .class extension
             */
            String classname = filename.substring(0, filename.length() - 6);

            /*
             * check for instance of Interrogator
             */

            String fullname = packageName + "." + classname;

            Class c;
            try {
                c = Class.forName(fullname);

                if (!c.isInterface() && classname.endsWith("IO")) {

                    Object o = Class.forName(fullname).newInstance();

                    if (o instanceof Interrogator) {
                        System.out.println(classname);
                    }
                }// ends with
            } catch (ClassNotFoundException e) {
              
                log.error(e);
            } catch (InstantiationException e) {
              
                log.error(e);
            } catch (IllegalAccessException e) {
              
                log.error(e);
            }
        }// end for all classFileNames
        return classes;
    }

}