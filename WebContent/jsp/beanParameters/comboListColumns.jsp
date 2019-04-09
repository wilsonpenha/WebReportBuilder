<%@ page import = "java.util.Vector"%>
<%@ page import = "reports.*"%>
<%@ page errorPage="../common/errorpge.jsp" %>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*"/>
<jsp:useBean id="beanPage" scope="request" class="reports.bean.BeanParameters"/>
<jsp:setProperty name="beanPage" property="*"/>

<%
/**
 * JSP para montagem do data island xml do combo list dinamico,
 * este codigo monta os padrôes IE e Mozilla
 *
 * criado por Wilson em 30/01/2004
 **/ 
%>

	<?xml version='1.0'  encoding='ISO-8859-1' ?>
	<comboxml>
	<%
	Vector objsColumns = beanPage.getColumns(beanPage.getTableInput()); 
	for (int i=0; i<objsColumns.size(); i++) {
		TableColumn tableColumn = (TableColumn) objsColumns.elementAt(i);
	%>
		<o>
			<v><%=tableColumn.getColumnName()%></v>
			<t><%=tableColumn.getColumnName()%></t>
		</o>
	<% } %>
	</comboxml>
