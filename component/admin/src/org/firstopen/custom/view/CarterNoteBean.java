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
package org.firstopen.custom.view;

import java.util.List;

import javax.faces.component.UIData;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.firstopen.custom.business.CarterNote;
import org.firstopen.custom.business.CarterNoteDAO;
import org.firstopen.custom.business.CarterNoteDAOFactory;
import org.firstopen.singularity.admin.view.BaseBean;
import org.firstopen.singularity.util.DAOUtilFactory;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: CarterNoteBean.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public class CarterNoteBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3551912771150112156L;

    private HtmlForm form;

    private CarterNoteDAO carterNoteDAO = CarterNoteDAOFactory.create();

    private CarterNote currentNote = new CarterNote();

    private Log log = LogFactory.getLog(this.getClass());

    private UIData uiTable = null;

    private List<CarterNote> list = null;

    /**
     * 
     */
    public CarterNoteBean() {
        createCarterNoteList();
      
    }

    public void getDetail(ActionEvent e) {

        currentNote = (CarterNote) uiTable.getRowData();
    }

    public void delete() {
        try {
            carterNoteDAO.delete(currentNote);
            currentNote = null;
            createCarterNoteList();

        } catch (InfrastructureException e1) {
            log.error("unable retrieve related carter note", e1);
        } 

    }

    private void createCarterNoteList() {

        try {
            list = carterNoteDAO.getAllCartersNotes();

            if (currentNote == null && list != null) {
                if (list.size() > 0) {
                    this.currentNote = list.get(0);
                }
            }

            if (currentNote == null) {
                currentNote = new CarterNote();
            }
        } catch (InfrastructureException e) {
            log.error("unable to get DeviceProfile List", e);
        } finally {
       
                DAOUtilFactory.close();
       
        }

    }

    public void save(ActionEvent e) {

        try {

            carterNoteDAO.updateCartersNote(this.getCurrentNote());
            createCarterNoteList();
        } catch (InfrastructureException ex) {
            log.error("unable to update Carter Note", ex);
        } finally {
            DAOUtilFactory.close();
        }

    }

    public String search() {
        String result = "failed";
        try {
            refresh(currentNote);
            result = "success";
        } catch (InfrastructureException e) {
            log.error("unable retrieve related carterNote", e);
        }

        return result;
    }

    private void refresh(CarterNote searchNote) throws InfrastructureException {
        try {
            searchNote = carterNoteDAO.getCartersNote(searchNote);
            if (searchNote != null) {
                currentNote = searchNote;
            }
        } catch (InfrastructureException e) {
            log.error("unable retrieve related carterNote");
            throw e;
        } finally {
            DAOUtilFactory.close();
        }
    }

    public String create() {
        currentNote = new CarterNote();
        return "success";
    }

    public void refresh(String printedId) {
        try {

            if (printedId != null) {
                CarterNote searchNote = new CarterNote();
                searchNote.clear();
                searchNote.setPrintedTagId(printedId);
                refresh(searchNote);

            }

        } catch (InfrastructureException e) {
            log.error("unable retrieve related carterNote", e);
        }
    }

    /**
     * 
     * @hibernate.property
     * @return Returns the form.
     */
    public HtmlForm getForm() {
        return form;
    }

    /**
     * @param form
     *            The form to set.
     */
    public void setForm(HtmlForm form) {
        this.form = form;
    }

    /**
     * 
     * @hibernate.property
     * @return Returns the currentNote.
     */
    public CarterNote getCurrentNote() {
        return currentNote;
    }

    /**
     * @param currentNote
     *            The currentNote to set.
     */
    public void setCurrentNote(CarterNote note) {
        this.currentNote = note;
    }

    public String next() {
        log.info("action() = next");
        return "success";
    }

    public void clear() {
        currentNote = new CarterNote();
        currentNote.clear();
    }

    /**
     * @return Returns the list.
     */
    public List<CarterNote> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(List<CarterNote> list) {
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
}
