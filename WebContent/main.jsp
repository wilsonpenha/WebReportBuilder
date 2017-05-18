<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*" />

<%beanMain.setRootDIR(request);%>
<%beanMain.setPageId(request.getParameter("pageId"));%>

<% beanMain.execute();%>
<%=beanMain.getForm()%>
