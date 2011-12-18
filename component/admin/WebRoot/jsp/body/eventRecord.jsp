<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
    prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="active-event-record">   
<h:form>
    <h:panelGrid columns="3" border="0" cellpadding="5" cellspacing="3"
        headerClass="carter-note-page">

        <h:outputText value="Tag" />
        <h:inputText id="_xtag" size="10" required="false"
            value="#{eventArchiveBean.currentEvent.tagId}" />
        <h:message for="_xtag" styleClass="errors" />


         <h:outputText value="Printed TagID" />
        <h:inputText id="_xprintedTagId" size="10" required="false"
            value="#{eventArchiveBean.currentEvent.printedId}" />
        <h:message for="_xprintedTagId" styleClass="errors" />

        <h:outputText value="Count" />
        <h:inputText id="_xcount" size="10" required="false"
            converter="javax.faces.Integer"
            value="#{eventArchiveBean.currentEvent.count}" />
        <h:message for="_xcount" styleClass="errors" />

     
        <h:outputText value="First Read Time" />
        <h:inputText id="_xfirstreadtime" size="10" required="false"
            value="#{eventArchiveBean.currentEvent.firstreaddate}" >
            <f:convertDateTime  type="both"/>
        </h:inputText>
        <h:message for="_xfirstreadtime" styleClass="errors" />

        <h:outputText value="Last Read Time" />
        <h:inputText id="_xlastreadtime" size="10" required="false" 
            value="#{eventArchiveBean.currentEvent.lastreaddate}" >
           <f:convertDateTime type="both"/>
        </h:inputText>
        <h:message for="_xlastreadtime" styleClass="errors" />

        <h:outputText value="Tampered" />
        <h:inputText converter="javax.faces.Boolean"
            value="false"
            required="false" id="_xtampered"
            value="#{eventArchiveBean.currentEvent.tampered}" />
        <h:message for="_xtampered" styleClass="errors" />


        <h:outputText value="Reader ID" />
        <h:inputText id="_xreaderid" size="10" required="false"
            value="#{eventArchiveBean.currentEvent.readerid}" />
        <h:message for="_xreaderid" styleClass="errors" />
   </h:panelGrid>

    <h:panelGrid columns="2" border="0" cellpadding="5" cellspacing="3"
        width="100%" headerClass="carter-note-page">
        <h:commandButton id="_xclear" value="Clear"
            action="#{eventArchiveBean.clear}" />
        <h:commandButton id="_xrefresh" value="Search"
            action="#{eventArchiveBean.search}" />
      
    </h:panelGrid>
</h:form>
</f:subview>