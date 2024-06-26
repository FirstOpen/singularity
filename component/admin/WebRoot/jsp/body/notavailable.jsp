<%--

  Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 
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

<%--
          This screen will be displayed into Two Sortable tables dynamic data list. One from
          packagemove, then other from tags(that they own)
          The package move one will be simular to package move, but will have less fields and show last read date time and if tampered. clicking the more button on a record will open the alldetails page for that particular package
          �
          The tags list will be a sortable table with dynamic datalist. click on the more button will open the tagDetail tile and the tagTypeDetailTile in the tag.jsp
  --%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>


	<f:subview id="notavailable">
		
		 <h:outputText value="Feature available in future release."/>
	</f:subview>
