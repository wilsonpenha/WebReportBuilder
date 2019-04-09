<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*" />

<%request.getSession().invalidate();%>

<%response.sendRedirect("../main.jsp");%>
