<script language="JavaScript" src="<%=request.getContextPath()%>/common/confirm.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/common/common.js"></script>
<%@ page language="java" %>
<%@ page errorPage="common/errorpge.jsp" %>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*"/> 

<%if (beanMain.getPageId().equals(beanMain.LOGIN_PAGE)) {%>
  <jsp:include flush="true" page="login.jsp"/>
<%}else{%>
  <jsp:include flush="true" page="mainPages/mainPage.jsp"/>
<%}%>



