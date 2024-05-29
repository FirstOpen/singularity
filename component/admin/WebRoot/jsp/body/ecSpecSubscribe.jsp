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

<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
    prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="aleecspecsubscribe">
    <h:form id="ecspecSubscribeForm">
        <h:panelGrid columns="3" border="0" cellpadding="5"
            cellspacing="3" headerClass="carter-note-page">

            <h:outputText value="Notify URI" />
            <h:inputText id="_notification"
                value="#{ecSpecBean.notificationURI}" />
            <h:message for="_notification" styleClass="errors" />

            <h:outputText value="ECSpecs" />
            <h:selectOneMenu id="_ecspecselection"
                value="#{ecSpecBean.ecSpecGenName}">
                <f:selectItems value="#{ecSpecBean.logicalECSpecList}" />
            </h:selectOneMenu>
            <h:message for="_ecspecselection" styleClass="errors" />

            <h:commandButton value="Subscribe"
                actionListener="#{ecSpecBean.subscribe}" />
            <h:commandButton value="UnSubscribe"
                actionListener="#{ecSpecBean.unsubscribe}" />
            <h:panelGroup />
        </h:panelGrid>
    </h:form>
</f:subview>


