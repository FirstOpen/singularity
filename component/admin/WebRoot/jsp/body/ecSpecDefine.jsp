<%--

  Copyright 2005 i-Konect LLC
 
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


<f:subview id="ecspec-define-view">
    <h:form>
        <h:panelGrid columns="3" border="0" cellpadding="5"
            cellspacing="3" headerClass="carter-note-page">
    
            <h:outputText value="ECSpec Name" />
            <h:inputText id="_defineECSpecName" value="#{ecSpecBean.ecSpecGenName}" />
            <h:message for="_defineECSpecName" styleClass="errors" />

            <h:outputText value="Duration (ms)" />
            <h:inputText id="_defineECSpecDuration" value="#{ecSpecBean.duration}" >
               <f:convertNumber/>
            </h:inputText>
            <h:message for="_defineECSpecDuration" styleClass="errors" />

            <h:outputText value="Logical Reader" />
            <h:selectManyListbox id="_defineECSpecLogicalDevice"
                value="#{ecSpecBean.selected}">
                <f:selectItems value="#{ecSpecBean.selectItemList}" />
            </h:selectManyListbox>
            <h:message for="_defineECSpecLogicalDevice" styleClass="errors" />

           <h:outputText value="Subscribers" />
            <h:selectManyListbox id="_defineSubscribers"
                value="#{ecSpecBean.subscriberNames}">
                <f:selectItems value="#{ecSpecBean.subscribers}" />
            </h:selectManyListbox>
            <h:message for="_defineSubscribers" styleClass="errors" />

            <h:commandButton value="Define"
                actionListener="#{ecSpecBean.define}" />
            <h:commandButton value="Delete"
                actionListener="#{ecSpecBean.delete}" />
            <h:commandButton value="New"
                actionListener="#{ecSpecBean.create}" />
        </h:panelGrid>
    </h:form>
</f:subview>