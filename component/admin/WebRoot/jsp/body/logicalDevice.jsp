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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
    prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>


<f:subview id="logical-device-view">
    <h:form>
        <h:panelGrid columns="3" border="0" cellpadding="5"
            cellspacing="3" headerClass="carter-note-page">
    
            <h:outputText value="Logical Device Name" />
            <h:inputText id="_defineLogicalDeviceName" value="#{logicalDeviceBean.currentLogicalDevice.name}" />
            <h:message for="_defineLogicalDeviceName" styleClass="errors" />

            <h:outputText value="Readers" />
            <h:selectManyListbox id="_defineReaderList"
                value="#{logicalDeviceBean.selected}">
                <f:selectItems value="#{logicalDeviceBean.readerNames}" />
            </h:selectManyListbox>
            <h:message for="_defineReaderList" styleClass="errors" />

         
            <h:commandButton value="Save"
                action="#{logicalDeviceBean.save}" />
            <h:commandButton value="Delete"
                action="#{logicalDeviceBean.delete}" />
            <h:commandButton value="New"
                action="#{logicalDeviceBean.create}" />
        </h:panelGrid>
    </h:form>
</f:subview>