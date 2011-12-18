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

<html>
  <head>
    <title>Error</title>
    <link rel="stylesheet" type="text/css"
          href="<%= request.getContextPath() %>/stylesheet.css"/>
  </head>

<body class="page-background">


<span class="error-heading">
</span>
<table width="100%" height="100%" border="0">
  <tr>
    <th scope="col">&nbsp;</th>
    <th width="100%" height="100%" scope="col"><img src="<%= request.getContextPath()%>/images/singularity.png" width="240" height="155"></th>
    <th scope="col">&nbsp;</th>
  </tr>
  <tr>
    <th scope="row">&nbsp;</th>
    <td width="100%" height="100%"><div align="center"><span class="error-heading">Sorry, a fatal error has occurred. The error has been logged.</span></div></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <th scope="row">&nbsp;</th>
    <td width="100%" height="100%"><div align="center"><a href="<%= request.getContextPath()%>">Please login again.</a> </div></td>
    <td>&nbsp;</td>
  </tr>
</table>

<p>&nbsp;
</p>

</body>
</html>
