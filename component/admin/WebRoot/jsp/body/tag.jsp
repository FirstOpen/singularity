<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"/>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>

<f:subview id="tag-detail-view">
<h:form>
      <h:panelGrid  columns="3" border="0"
                    cellpadding="5"
                    cellspacing="3"
                    headerClass="tag-page">

             <h:outputText value="TagId"/>
             <h:inputText id="_tagId" size="10"  required="true"
                          value="#{tagBean.currentRow.value}"/> 
             <h:message for="_tagId" styleClass="errors"/>

         
             <h:outputText value="PrintedID"/>     
             <h:inputText id="_printedId" size="10"  required="true" 
                          value="#{tagBean.currentRow.printedId}"/> 
             <h:message for="_printedId" styleClass="errors"/>        

 
   </h:panelGrid>

   <h:commandButton id="_submit" value="Save"  actionListener="#{tagBean.update}" />
 </h:form>
 </f:subview>