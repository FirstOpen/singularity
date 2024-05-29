<%--

  Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
 
       http://www.apache.org/licenses/LICENSE-2.0
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="logica-device-list-view">

    <h:panelGrid columns="1" styleClass="sm-table-title">
        <f:facet name="header">
            <h:outputText value="Logical Device List" styleClass="sm-table-title" />
        </f:facet>
        <h:panelGroup>
            <h:dataTable id="logical-device-list-datatable" rows="50" styleClass="sm-table-background" rowClasses="sm-table-odd-row,sm-table-even-row" cellpadding="3" value="#{logicalDeviceBean.list}" var="cRow" binding="#{logicalDeviceBean.uiTable}">
                <h:column>
                    <f:facet name="header">
                        <h:outputText value="Name" styleClass="sm-table-header" />
                    </f:facet>
                    <h:commandLink styleClass="sm-table-value" actionListener="#{logicalDeviceBean.getDetail}">
                        <h:graphicImage url="/images/create.gif" alt="ECSpec Name" styleClass="sm-table-toolbar-icon" />
                        <h:outputText value="#{cRow.name}" styleClass="sm-table-value" />
                    </h:commandLink>
                </h:column>
                
            </h:dataTable>
              <h:panelGrid columns="1" styleClass="scrollerTable2" columnClasses="standardTable_ColumnCentered" >
            <x:dataScroller id="scroll_1"
                    for="logical-device-list-datatable"
                    fastStep="1"
                    pageCountVar="pageCount"
                    pageIndexVar="pageIndex"
                    styleClass="scroller"
                    paginator="true"
                    paginatorMaxPages="0"
                    paginatorTableClass="paginator"
                    paginatorActiveColumnStyle="font-weight:bold;"
                    actionListener="#{logicalDeviceBean.scrollerAction}"
                    >
                <f:facet name="first" >
                    <x:graphicImage url="/images/arrow-first.gif" border="1" />
                </f:facet>
                <f:facet name="last">
                    <x:graphicImage url="/images/arrow-last.gif" border="1" />
                </f:facet>
                <f:facet name="previous">
                    <x:graphicImage url="/images/arrow-previous.gif" border="1" />
                </f:facet>
                <f:facet name="next">
                    <x:graphicImage url="/images/arrow-next.gif" border="1" />
                </f:facet>
                <f:facet name="fastforward">
                    <x:graphicImage url="/images/arrow-ff.gif" border="1" />
                </f:facet>
                <f:facet name="fastrewind">
                    <x:graphicImage url="/images/arrow-fr.gif" border="1" />
                </f:facet>
            </x:dataScroller>
            <x:dataScroller id="scroll_2"
                    for="logical-device-list-datatable"
                    rowsCountVar="rowsCount"
                    displayedRowsCountVar="displayedRowsCountVar"
                    firstRowIndexVar="firstRowIndex"
                    lastRowIndexVar="lastRowIndex"
                    pageCountVar="pageCount"
                    pageIndexVar="pageIndex"
                    >
                <h:outputFormat value="" styleClass="standard" >
                    <f:param value="#{rowsCount}" />
                    <f:param value="#{displayedRowsCountVar}" />
                    <f:param value="#{firstRowIndex}" />
                    <f:param value="#{lastRowIndex}" />
                    <f:param value="#{pageIndex}" />
                    <f:param value="#{pageCount}" />
                </h:outputFormat>
            </x:dataScroller>
          </h:panelGrid>
        </h:panelGroup>
    </h:panelGrid>

</f:subview>


