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

import java.util.List;

import javax.faces.component.UIData;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.datascroller.ScrollerActionEvent;
import org.firstopen.singularity.system.Tag;
import org.firstopen.singularity.system.dao.TagDAO;
import org.firstopen.singularity.system.dao.TagDAOFactory;
import org.firstopen.singularity.util.InfrastructureException;

/**
 * @author Tom Rose (tom.rose@i-konect.com)
 * @version $Id: TagBean.java 1243 2006-01-14 03:33:37Z TomRose $
 */
public class TagBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = 7072058115206872593L;

    private HtmlForm form;

    private Tag currentRow = new Tag();

    private UIData uiTable = null;

    private List<Tag> list = null;

    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 
     */
    public TagBean() {
        super();
        createTagList();

    }

    /**
     * 
     */
    private void createTagList() {
        TagDAO tagDAO = TagDAOFactory.create();
        list = tagDAO.getAll();
    }

    public void getDetail(ActionEvent e) {
        currentRow = (Tag) uiTable.getRowData();

    }

    public void update(ActionEvent e) {
        TagDAO tagDAO = TagDAOFactory.create();
        tagDAO.update(currentRow);
        createTagList();

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
     * @return Returns the currentRow.
     */
    public Tag getCurrentRow() {
        return currentRow;
    }

    /**
     * @param currentRow
     *            The currentRow to set.
     */
    public void setCurrentRow(Tag note) {
        this.currentRow = note;
    }

    public String next() {
        return "success";
    }

    public void processTag(ActionEvent e) {

        HtmlOutputText currentTag = (HtmlOutputText) e.getComponent()
                .getChildren().get(0);

        TagDAO tagDAO = TagDAOFactory.create();
        System.out.println(currentTag.getValue());
        try {
            Tag tag = new Tag();
            tag.setId(null);
            tag.setValue((String) currentTag.getValue());
            tagDAO.findBy(tag);
            log.info("tagvlaue is " + currentTag.getValue());

        } catch (InfrastructureException e1) {
            log.error("unable to retreive Tag", e1);
        }
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

    /**
     * @return Returns the list.
     */
    public List<Tag> getList() {
        return list;
    }

    /**
     * @param list
     *            The list to set.
     */
    public void setList(List<Tag> list) {
        this.list = list;
    }

    public void scrollerAction(ActionEvent event) {
        ScrollerActionEvent scrollerEvent = (ScrollerActionEvent) event;
        FacesContext.getCurrentInstance().getExternalContext().log(
                "scrollerAction: facet: " + scrollerEvent.getScrollerfacet()
                        + ", pageindex: " + scrollerEvent.getPageIndex());
    }
}
