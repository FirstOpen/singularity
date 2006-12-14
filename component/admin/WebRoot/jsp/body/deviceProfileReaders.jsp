<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="device-profile-reader-view">

    <h:form>
        <h:panelGrid columns="6" border="0" cellpadding="5" cellspacing="3" headerClass="carter-note-page" id="device-profile-gridpanel">

            <h:outputText value="Readers" />
            <h:selectManyListbox id="_selectReaders" value="#{deviceProfileBean.readerBean.selected}">
                <f:selectItems value="#{deviceProfileBean.readerBean.list}" />
            </h:selectManyListbox>
            <h:message for="_selectReaders" styleClass="errors" />

            <h:outputText value="Sensors" />
            <h:selectManyListbox id="_selectSensors" value="#{deviceProfileBean.sensorBean.selected}">
                <f:selectItems value="#{deviceProfileBean.sensorBean.list}" />
            </h:selectManyListbox>
            <h:message for="_selectSensors" styleClass="errors" />

            <h:outputText value="Reader Name" />
            <h:inputText id="_readerName" size="30" required="false" value="#{deviceProfileBean.readerBean.currentReader.name}" />
            <h:message for="_readerName" styleClass="errors" />

            <h:panelGroup />
            <h:panelGroup />
            <h:panelGroup />

            <h:commandButton value="Clear" action="#{deviceProfileBean.readerBean.clear}" />
            <h:panelGroup />

            <h:commandButton value="Add" action="#{deviceProfileBean.readerBean.create}" />
            <h:panelGroup />

            <h:commandButton value="Delete" action="#{deviceProfileBean.readerBean.delete}" />
            <h:panelGroup />


        </h:panelGrid>
    </h:form>
</f:subview>
