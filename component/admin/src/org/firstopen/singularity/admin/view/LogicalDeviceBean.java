/*
 * Copyright 2005 i-Konect LLC
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
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.firstopen.singularity.config.LogicalDevice;
import org.firstopen.singularity.config.dao.LogicalDeviceDAO;
import org.firstopen.singularity.config.dao.LogicalDeviceDAOFactory;
import org.firstopen.singularity.system.Reader;
import org.firstopen.singularity.system.dao.ReaderDAO;
import org.firstopen.singularity.system.dao.ReaderDAOFactory;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;
import org.firstopen.singularity.util.MapSet;

// Referenced classes of package org.firstopen.singularity.admin.view:
// BaseBean

public class LogicalDeviceBean extends BaseBean {

    private static final long serialVersionUID = 0x2f85ff95371b384L;

    private Log log = LogFactory.getLog(getClass());

    private HtmlForm form = null;

    private UIData uiTable;

    private LogicalDevice current = null;

    private List<LogicalDevice> list = null;

    private MapSet<String, Reader> readers = new MapSet<String,Reader>();

    private ArrayList<SelectItem> readerNames = new ArrayList<SelectItem>();

    private List<String> selected = new ArrayList<String>();

    public LogicalDeviceBean() {

        /**
         * just need a handle the session
         */
        try {
            createLogicalDeviceList();
        
        } catch (InfrastructureException e) {

            log.error("cannot initialize LogicalDeviceBean", e);
        } 

    }

    /**
     * 
     */
    private void createReaderList() {

         
         ReaderDAO readerDAO = ReaderDAOFactory.create();
         List<Reader> readerList = readerDAO.getAll();
         readers.clear();
         readerNames.clear();
         for (Reader reader : readerList) {
             readers.add(reader);
             readerNames.add(new SelectItem(reader.getName()));
         }
        
    }

    private void createLogicalDeviceList() {
        LogicalDeviceDAO logicalDeviceDAO = null;
        try {
            logicalDeviceDAO = LogicalDeviceDAOFactory.create();

            list = logicalDeviceDAO.getAll();

            if (current == null && list != null) {
                if (list.size() > 0) {
                    setCurrentLogicalDevice(list.get(0));
                }
            }

            if (current == null) {
                setCurrentLogicalDevice(new LogicalDevice());
            }
        } catch (InfrastructureException e) {
            log.error("unable to get LogicalDevice List", e);
            /*
             * do not call close session, only participates in a session
             * 
             */
        }

    }

    public void save() {
        /*
         * add the selected readers to the current logical reader.
         */
        for (String readerName : selected) {
            Reader reader = readers.get(readerName);
            current.addReader(reader);
        }
        LogicalDeviceDAO logicalDeviceDAO = null;

        try {
            logicalDeviceDAO = LogicalDeviceDAOFactory.create();

            logicalDeviceDAO.update(current);
            /*
             * refresh the list
             */
            createLogicalDeviceList();

        } catch (InfrastructureException e) {
            log.error("unable to save LogicalDevice", e);

        } 
        }

    public void getDetail(ActionEvent e) {

        setCurrentLogicalDevice((LogicalDevice) uiTable.getRowData());
        
   

    }

    public String search() {
        String result = "failed";
        try {
            refresh(current);
            result = "success";
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            log.error(e);
        }

        return result;
    }

    public void delete() {
        LogicalDeviceDAO logicalDeviceDAO = null;
        try {
            logicalDeviceDAO = LogicalDeviceDAOFactory.create();

            logicalDeviceDAO.delete(current);
            current = null;
            createLogicalDeviceList();
        } catch (InfrastructureException e1) {
            log.error("unable retrieve related deviceProfile", e1);
        } 

    }

    private void refresh(LogicalDevice searchRC) throws InfrastructureException {
        LogicalDeviceDAO logicalDeviceDAO = null;
        try {
            logicalDeviceDAO = LogicalDeviceDAOFactory.create();

            searchRC = logicalDeviceDAO.get(searchRC);

            if (searchRC != null) setCurrentLogicalDevice(searchRC);
        } catch (InfrastructureException e) {
            // TODO Auto-generated catch block
            log.error(e);
            throw e;
        } 
    }

    public String create() {
        try {
        setCurrentLogicalDevice(new LogicalDevice());
        createReaderList();
        }finally {
            DAOUtilFactory.close();
        }
        return "success";
    }

    public void refresh(String objId) {
        try {
            if (objId != null) {
                LogicalDevice serachObj = new LogicalDevice();
                serachObj.clear();
                refresh(serachObj);
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

    public LogicalDevice getCurrentLogicalDevice() {
        return current;
    }

    public void setCurrentLogicalDevice(LogicalDevice rc) {
        current = rc;
        readerNames.clear();
        for (Reader reader : current.getReaderSet()) {
            readerNames.add(new SelectItem(reader.getName()));
        }

    }

    public String next() {
        log.info("action() = next");
        return "success";
    }

    public void clear() {
        LogicalDevice logicalDevice = new LogicalDevice();
        logicalDevice.clear();
        setCurrentLogicalDevice(logicalDevice);

    }

    /**
     * @return Returns the list.
     */
    public List<LogicalDevice> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(List<LogicalDevice> list) {
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
     * @return Returns the current.
     */
    public LogicalDevice getCurrent() {
        return current;
    }

    /**
     * @param current
     *            The current to set.
     */
    public void setCurrent(LogicalDevice current) {
        this.current = current;
    }

    /**
     * @return Returns the readerNames.
     */
    public ArrayList<SelectItem> getReaderNames() {
        return readerNames;
    }

    /**
     * @param readerNames
     *            The readerNames to set.
     */
    public void setReaderNames(ArrayList<SelectItem> readerNames) {
        this.readerNames = readerNames;
    }

    /**
     * @return Returns the readers.
     */
    public MapSet<String, Reader> getReaders() {
        return readers;
    }

    /**
     * @param readers
     *            The readers to set.
     */
    public void setReaders(MapSet<String, Reader> readers) {
        this.readers = readers;
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

 
}