<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="map-view">
    <f:verbatim>
        <div id="map" style="width: 500px; height: 300px"></div>
        <div id="message"></div>
     
    </f:verbatim>
  
          <h:inputHidden id="coords" value="#{eventMonitorBean.xml}" />
     
</f:subview>
