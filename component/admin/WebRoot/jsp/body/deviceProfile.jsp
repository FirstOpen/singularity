<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
    prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="device-profile-view">
 
<h:form>
   <h:panelGrid columns="6" border="0" cellpadding="5" cellspacing="3"
        headerClass="carter-note-page" id="device-profile-gridpanel">

        <h:outputText value="IPAddress" />
        <h:inputText id="_ipaddress" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.ipAddress}" />
        <h:message for="_ipaddress" styleClass="errors" />

        <h:outputText value="Port" />
        <h:inputText id="_port" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.port}" />
        <h:message for="_port" styleClass="errors" />

        <h:outputText value="Baud Rate (If Serial)" />
        <h:inputText id="_baud" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.baud}" />
        <h:message for="_baud" styleClass="errors" />

        <h:outputText value="Manufacturer" />
        <h:inputText id="_manufaturer" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.manufacturer}" />
        <h:message for="_manufaturer" styleClass="errors" />

        <h:outputText value="Vendor" />
        <h:inputText id="_vendor" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.vendor}" />
        <h:message for="_vendor" styleClass="errors" />

        <h:outputText value="Version" />
        <h:inputText id="_version" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.version}" />
        <h:message for="_version" styleClass="errors" />

        <h:outputText value="Model" />
        <h:inputText id="_model" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.model}" />
        <h:message for="_model" styleClass="errors" />

        <h:outputText value="Serial Number" />
        <h:inputText id="_serialNumber" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.serialNumber}" />
        <h:message for="_serialNumber" styleClass="errors" />


        <h:outputText value="Name" />
        <h:inputText id="_name" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.name}" />
        <h:message for="_name" styleClass="errors" />

        <h:outputText value="Device ID" />
        <h:inputText id="_deviceProfileID" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.deviceProfileID}" />
        <h:message for="_deviceProfileID" styleClass="errors" />
        
        <h:outputText value="Interrogator Class" />
        <h:inputText id="_interrogatorClassName" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.interrogatorClassName}" />
        <h:message for="_interrogatorClassName" styleClass="errors" />
        
        <h:outputText value="DeviceMgr ID" />
        <h:inputText id="_dmgrid" size="30" required="false"
            value="#{deviceProfileBean.currentDeviceProfile.deviceManagerID}" />
        <h:message for="_dmgrid" styleClass="errors" />
      
        <h:commandButton id="_clear" value="Clear"
            action="#{deviceProfileBean.clear}" />
            
        <h:commandButton id="_refresh" value="Search"
            action="#{deviceProfileBean.search}" />
        <h:panelGroup/>        
        
        <h:commandButton id="_save" value="Save"
            action="#{deviceProfileBean.save}" />

        <h:commandButton id="_new" value="New"
            action="#{deviceProfileBean.create}" />
            
        <h:commandButton id="_delete" value="Delete"
            action="#{deviceProfileBean.delete}" />
            
    </h:panelGrid>
 </h:form>
</f:subview>