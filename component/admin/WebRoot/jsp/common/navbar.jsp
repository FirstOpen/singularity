<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="/WEB-INF/tld/pragmatic-controls.tld" prefix="pragmatic" %>

<f:subview id="navigation-bar" >
     <f:verbatim >
        <pragmatic:controlPanelBar-head leftPos="0" topPos="0px" cpBarWidth="180px" cpBarHeight="100%" panelWidth="160px" />
        <pragmatic:controlPanelBar-body configXmlPath="WEB-INF/controlpanel-bar.xml" sessionName="XYZ" activeItemId="home" resourceBundle="ControlPanel" language="en"/>  
    </f:verbatim>
</f:subview>
