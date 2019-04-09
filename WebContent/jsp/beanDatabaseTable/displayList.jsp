<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanDatabaseTable/script.js"></script>

<% 
	reports.bean.BeanDatabaseTable beanPage = (reports.bean.BeanDatabaseTable)request.getAttribute("beanDBTables");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanDBTables.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Database Tables - List</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
	     <jsp:include flush="true" page="../tabSheets1.jsp?tabSheetOn=DB Tables"/>
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
                  String[] ids = {"tableId"};
                  String[] objValues = {"tableName","schemaName"};
                  String[] titles = {"", "Table Name","Schema Name"};
                  String[] colsWidth = {"1%", "50%","50%"};
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
              <td height="16" width="50%" <% if (!beanPage.isFirstBlock()) {%> class="tdPrevious"><input type="button" value="Previous" name="btnAnterior" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_PRIOR%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
              <td width="50%" <% if (!beanPage.isLastBlock()) {%> class="tdNext"><input type="button" value="Next" name="btnProximo" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_NEXT%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
            </tr>
          </table>
          </td>
	  </tr>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" Add "      style="width: 38px;" name="btnNew" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_FORM_INSERT%>');" onMouseOver="javascript:mensagem('Click here to add new record.');"></td>
			<td><input type="submit" value=" Delete "   style="width: 51px;" name="btnDelete"     <% if (beanPage.isEmpty()) {%> disabled class="stdButtonDisabled"  <%} else { %> class="stdButton" <%}%> onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_DELETE%>');" onMouseOver="javascript:mensagem('Click here to delete the selected items.');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
