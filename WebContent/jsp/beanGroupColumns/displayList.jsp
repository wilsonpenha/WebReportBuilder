<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanGroupColumns/script.js"></script>

<% 
	reports.bean.BeanGroupColumns beanPage = (reports.bean.BeanGroupColumns)request.getAttribute("beanGroupColumns");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanGroupColumns.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="groupsId" value="<%=beanPage.getGroupsId()%>">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Group Columns - List</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.groupsCols",request); %>
	     <jsp:include flush="true" page='<%="../tabSheets2.jsp?tabSheetOn=" + tabName%>'/>
	  </div>
    </td>
  </tr>
        <tr>
          <td>
            <table width="100%" border="0" cellPadding="0" cellSpacing="0">
              <input type="hidden" name="objIds">
              <tr>
                <td class="CabecalhoCampo">
                <%
                  String[] ids = {"groupColumnsId"};
                  String[] objValues = {"description", "width", "alignment", "classType", "order", "isPrintHeader"};
                  String[] titles = {"", "Column Name", "Width", "Alignment", "Class Type", "Order", "Print Header"};
                  String[] colsWidth = {"1%", "16%", "16%", "16%", "16%", "16%", "16%"};
                %>
                <hwork:TableCheckHRef name="objIds" ids="<%=ids%>"
             	objs="<%=beanPage.getList()%>"
                  width="100%" colsWidth="<%=colsWidth%>" colors="#D3DCE7,#F7F4F2" border="0"
                  objValues="<%=objValues%>" titles="<%=titles%>"
                  blockSize="<%=Integer.parseInt(beanPage.getBlockSize())%>"
                  blockNumber="<%=Integer.parseInt(beanPage.getBlockNumber())%>"
                  onEvent="javascript:setAction(document.forms[0],ACTION_FORM_UPDATE);var obj = new Object();obj.value='#OBJIDS';if (elementos_do_formulario1('objIds','0') == 1){document.forms[0].objIds.value = obj.value;} else {document.forms[0].objIds[0].value = obj.value;}window.document.forms[0].submit();">
                </hwork:TableCheckHRef>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
	    <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tituloPrinCenter">
            <tr>
              <td height="16" width="50%" <% if (!beanPage.isFirstBlock()) {%> class="tdPrevious"><input type="button" value="<bean:message key="app.button.previous"/>" name="btnAnterior" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_PRIOR%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
              <td width="50%" <% if (!beanPage.isLastBlock()) {%> class="tdNext"><input type="button" value="<bean:message key="app.button.next"/>" name="btnProximo" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_NEXT%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
            </tr>
          </table>
          </td>
	  </tr>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" <bean:message key="app.button.add"/> "      style="width: 38px;" name="btnNew" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_FORM_INSERT%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.add.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.delete"/> "   style="width: 51px;" name="btnDelete"     <% if (beanPage.isEmpty()) {%> disabled class="stdButtonDisabled"  <%} else { %> class="stdButton" <%}%> onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_DELETE%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.delete.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
