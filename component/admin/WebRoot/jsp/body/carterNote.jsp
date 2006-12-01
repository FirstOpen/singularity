<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles"
    prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<h:form>


    <h:panelGrid columns="9" border="0" cellpadding="5" cellspacing="3"
        headerClass="carter-note-page">

        <h:outputText value="Shipper" />
        <h:inputText id="_shipper" size="10" required="false"
            value="#{carterNoteBean.currentNote.shipper}" />
        <h:message for="_shipper" styleClass="errors" />


        <h:outputText value="Package ID" />
        <h:inputText id="_packageId" size="10" required="false"
            value="#{carterNoteBean.currentNote.packageId}" />
        <h:message for="_packageId" styleClass="errors" />

        <h:outputText value="Seal Number" />
        <h:inputText id="_sealNumber" size="10" required="false"
            value="#{carterNoteBean.currentNote.sealNumber}" />
        <h:message for="_sealNumber" styleClass="errors" />

        <h:outputText value="Printed TagID" />
        <h:inputText id="_printedTagId" size="10" required="false"
            value="#{carterNoteBean.currentNote.printedTagId}" />
        <h:message for="_printedTagId" styleClass="errors" />

        <h:outputText value="Booking Reference" />
        <h:inputText id="_bookingRef" size="10" required="false"
            value="#{carterNoteBean.currentNote.bookingReference}" />
        <h:message for="_bookingRef" styleClass="errors" />

        <h:outputText value="Exporter Reference" />
        <h:inputText id="_exporterRef" size="10" required="false"
            value="#{carterNoteBean.currentNote.exporter}" />
        <h:message for="_exporterRef" styleClass="errors" />

        <h:outputText value="Entry Number" />
        <h:inputText id="_entryNumber" size="10" required="false"
            value="#{carterNoteBean.currentNote.entryNumber}" />
        <h:message for="_entryNumber" styleClass="errors" />


        <h:outputText value="Carrier" />
        <h:inputText id="_carrier" size="10" required="false"
            value="#{carterNoteBean.currentNote.carrier}" />
        <h:message for="_carrier" styleClass="errors" />


        <h:outputText value="Port of Loading" />
        <h:inputText id="_portOfLoading" size="10" required="false"
            value="#{carterNoteBean.currentNote.portOfLoading}" />
        <h:message for="_portOfLoading" styleClass="errors" />

        <h:outputText value="Port of Discharge" />
        <h:inputText id="_portOfDischarge" size="10" required="false"
            value="#{carterNoteBean.currentNote.portOfDischarge}" />
        <h:message for="_portOfDischarge" styleClass="errors" />

        <h:outputText value="Vessel" />
        <h:inputText id="_vessel" size="10" required="false"
            value="#{carterNoteBean.currentNote.vessel}" />
        <h:message for="_vessel" styleClass="errors" />
        
        <h:outputText value="Country Code" />
        <h:inputText id="_countryCode" size="10" required="false"
            value="#{carterNoteBean.currentNote.countryCode}" />
        <h:message for="_countryCode" styleClass="errors" />

        <h:outputText value="Dangerous Goods" />
        <h:inputText id="_dangerousGoods" size="10" required="false"
            value="#{carterNoteBean.currentNote.dangerousGoods}" />
        <h:message for="_dangerousGoods" styleClass="errors" />

        <h:outputText value="Gross Weight" />
        <h:inputText id="_grossweight" size="10" required="false"
            value="#{carterNoteBean.currentNote.grossWeight}" />
        <h:message for="_grossweight" styleClass="errors" />


        <h:outputText value="Humidity Control" />
        <h:inputText id="_humidityControl" size="10" required="false"
            value="#{carterNoteBean.currentNote.humidityControl}" />
        <h:message for="_humidityControl" styleClass="errors" />

        <h:outputText value="Internal PKG Desc" />
        <h:inputText id="_internalPackageDesc" size="10"
            required="false"
            value="#{carterNoteBean.currentNote.internalPackageDesc}" />
        <h:message for="_internalPackageDesc" styleClass="errors" />

        <h:outputText value="Number Internal PKG " />
        <h:inputText id="_numInternalPackageDesc" size="10"
            required="false"
            value="#{carterNoteBean.currentNote.numInternalPackages}" />
        <h:message for="_numInternalPackageDesc" styleClass="errors" />

        <h:outputText value="Over Deminsion Detail" />
        <h:inputText id="_overDeminsionDetail" size="10"
            required="false"
            value="#{carterNoteBean.currentNote.overDeminsionDetail}" />
        <h:message for="_overDeminsionDetail" styleClass="errors" />

        <h:commandButton id="_clear" value="Clear"
            action="#{carterNoteBean.clear}" />
        <h:commandButton id="_refresh" value="Search"
            action="#{carterNoteBean.search}" />
        <h:panelGroup/>        
       
        <h:commandButton id="_submit" value="Save"
            actionListener="#{carterNoteBean.save}" />
        <h:commandButton id="_search" value="New"
            action="#{carterNoteBean.create}" />
         <h:commandButton id="_remove" value="Delete"
            action="#{carterNoteBean.delete}" />      
        <h:panelGroup/>        
        <h:panelGroup/>  
        <h:panelGroup/>     
    </h:panelGrid>
</h:form>
