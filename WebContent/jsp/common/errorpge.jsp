<html>
<body>
<%@ page language="java" %>
<%@ page isErrorPage="true" %>
<%@ page import="java.io.*" %>
<% if (exception!=null) {%>
The follow error has throwed : <%=exception.getMessage()%>
<%} %>
</body>
</html>

