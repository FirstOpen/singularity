<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ page session="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles"%>
<%@ taglib prefix="tab" uri="http://ditchnet.org/jsp-tabs-taglib"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="x" uri="http://myfaces.apache.org/extensions"%>
<%@ taglib prefix="pragmatic" uri="/WEB-INF/tld/pragmatic-controls.tld"%>
<f:view>

    <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
        <head>
            <tiles:importAttribute />
            <title>
                <tiles:getAsString name="title" />
            </title>
            <f:verbatim>
                <script language="JavaScript" src="<%= request.getContextPath() %>/lib/admin.js"></script>
                <script language="JavaScript" src="<%= request.getContextPath() %>/lib/ajax.js"></script>
                 <c:if test="${!empty googleMaps}">
                                <script src="http://maps.google.com/maps?file=api&v=1&key=ABQIAAAANXloCvRuyM2tTpU80v83_hQI_Zhbhad_12uCVyAYjh94mpXKsxTYBZ2fIGZHYYxqJkLcz8UUMYfPIQ" type="text/javascript"></script>
                                <script language="JavaScript" src="<%= request.getContextPath() %>/lib/map.js"></script>
                 </c:if>                
           </f:verbatim>

            <x:stylesheet path="/css/singularity.css" />
       
        </head>
        <body onLoad="singularityInit();">



            <tiles:insert attribute="menu" flush="false" />

            <div id="header-bar">
                <tiles:insert attribute="header" flush="false" />
            </div>

            <div id="content-panel">
              <div id="info-panel">
                 <c:choose>
                  <c:when test="${!empty infoPane}">
                    <tiles:insert attribute="infoPane" flush="false" />
                  </c:when>
                  <c:otherwise>
                    <tab:tabConfig />
                    <tab:tabContainer id="tab-bar-container" jsTabListener="myTabListener">
                        <c:if test="${!empty tabOneTitle}">
                            <tab:tabPane id="tabOne" tabTitle='${tabOneTitle}'>
                                <tiles:insert attribute="tabOne" flush="false" />
                            </tab:tabPane>
                        </c:if>
                        <c:if test="${!empty tabTwoTitle}">
                            <tab:tabPane id="tabTwo" tabTitle="${tabTwoTitle}">
                                <tiles:insert attribute="tabTwo" flush="false" />
                            </tab:tabPane>
                        </c:if>
                        <c:if test="${!empty tabThreeTitle}">
                            <tab:tabPane id="tabThree" tabTitle="${tabThreeTitle}">
                                <tiles:insert attribute="tabThree" flush="false" />
                            </tab:tabPane>
                        </c:if>
                        <c:if test="${!empty tabFourTitle}">
                            <tab:tabPane id="tabFour" tabTitle="${tabFourTitle}">
                                <tiles:insert attribute="tabFour" flush="false" />
                            </tab:tabPane>
                        </c:if>
                     
                        <tab:tabPane id="errors" tabTitle="Errors">
                               <h:messages />
                        </tab:tabPane>
                       
                    </tab:tabContainer>
                
               </c:otherwise>
               </c:choose>
               </div>
               <tiles:insert attribute="body" flush="false" />
            </div>
            <f:verbatim>
                <div id="scripts-panel">
                    <tiles:insert attribute="script" flush="false" />
                </div>
            </f:verbatim>
        </body>
        
    </html>
   </f:view>
