<%--
   JavaServer Faces in Action example code, Copyright (C) 2004 Kito D. Mann.

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/stylesheet.css" />
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<f:view>
<html>
  <head>
    <title>
      <h:outputText value="Login"/>
    </title>

    <link rel="stylesheet" type="text/css"
          href="<%= request.getContextPath() %>/stylesheet.css"/>

    <script language="JavaScript">

       function set_image(button, img)
       {
         button.src = img;
       }

    </script>
  </head>

<body>

<f:verbatim>
<form method="POST" action="<%= request.getContextPath() %>/j_security_check"  >
</f:verbatim>
  <h:panelGrid columns="2" border="0" cellpadding="3" cellspacing="3" styleClass="login-table-background">

     <h:graphicImage url="/images/logon.jpg" alt="Welcome to Singularity"
       title="Welcome to Singularity" width="232" height="154"/>

       <h:panelGrid columns="3" border="0" cellpadding="5" cellspacing="3"
                    headerClass="login-heading">

          <f:facet name="header">
              <h:outputText value="Welcome to Singularity"/>
          </f:facet>

          <h:messages globalOnly="true" styleClass="errors"/> 
          <h:panelGroup/>
          <h:panelGroup/>

          <h:outputLabel for="j_username">
             <h:outputText value="Username"/>
          </h:outputLabel>
          <h:inputText id="j_username" size="20" maxlength="30"
                       required="true" value="#{authenticationBean.username}"> 
            <f:validateLength minimum="5" maximum="30"/>
          </h:inputText>
          <h:message for="j_username" styleClass="errors"/>

          <h:outputLabel for="j_password">
            <h:outputText value="Password"/>
          </h:outputLabel>
      
          <h:inputSecret id="j_password" size="20" maxlength="20"
                         required="true"
                         value="#{authenticationBean.password}">       
            <f:validateLength minimum="5" maximum="15"/>
          </h:inputSecret>
          <h:message for="j_password" styleClass="errors"/>

          <h:panelGroup/>   
      
          <h:commandButton action="j_security_check" title="Logon"
                           image="/images/submit.png"
                           onmouseover="set_image(this,'/admin/images/submit_over.png')"
                           onmouseout="set_image(this,'/admin/images/submit.png')"/>

          <h:panelGroup/>

       </h:panelGrid>

      

     
  </h:panelGrid>
<f:verbatim>
</form>
</f:verbatim>
</body>
</html>
</f:view>

