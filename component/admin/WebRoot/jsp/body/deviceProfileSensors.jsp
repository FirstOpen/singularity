<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="device-profile-sensor-view">

    <h:form>
        <h:panelGrid columns="3" border="0" cellpadding="5" cellspacing="3" headerClass="carter-note-page" id="device-profile-sensor-gridpanel">

            <h:outputText value="Sensors" />
            <h:selectManyListbox id="_defineSensors" value="#{deviceProfileBean.sensorBean.selected}">
                <f:selectItems value="#{deviceProfileBean.sensorBean.list}" />
            </h:selectManyListbox>
            <h:message for="_defineSensors" styleClass="errors" />


            <h:outputText value="Name" />
            <h:inputText id="_sensorName" size="30" required="false" value="#{deviceProfileBean.sensorBean.currentSensor.name}" />
            <h:message for="_sensorName" styleClass="errors" />

            <h:panelGroup />
            <h:panelGrid columns="3" border="0" cellpadding="5" cellspacing="3" headerClass="carter-note-page" id="device-profile-sensor-p9">

                <h:commandButton value="Clear" action="#{deviceProfileBean.sensorBean.clear}" />
                <h:commandButton value="Add" action="#{deviceProfileBean.sensorBean.add}" />
                <h:commandButton value="Remove" action="#{deviceProfileBean.sensorBean.remove}" />

            </h:panelGrid>
            <h:panelGroup />


        </h:panelGrid>
    </h:form>
</f:subview>
