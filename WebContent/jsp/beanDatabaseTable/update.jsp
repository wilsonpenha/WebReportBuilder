<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanDatabaseTable/script.js"></script>

<% 
	reports.bean.BeanDatabaseTable beanPage = (reports.bean.BeanDatabaseTable)request.getAttribute("beanDBTables");
%>

<%beanPage.selectObject();%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanDBTables.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="objIds">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Database Tables - Edit</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
	     <jsp:include flush="true" page="../tabSheets1.jsp?tabSheetOn=DB Tables"/>
	  </div>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>              <td width="32%" class="dataLabel">*Table Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="tableId" value="<%=beanPage.getTableId()%>">                <%
                  Vector objsTableInput = beanPage.getTables();
                  String[] idsTable = {"tableName"};
                  String[] objValuesTable = {"tableName"};
                %>
                <hwork:ComboList name="tableName" 
                  ids="<%=idsTable%>" 
                  objValues="<%=objValuesTable%>" 
                  onEvent="tabIndex=\"2\""
                  objs="<%=objsTableInput%>"
                  selected="<%=beanPage.getTableName()%>">
                </hwork:ComboList>
              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">*Schema Name : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" readOnly style="{text-transform: uppercase;}" name="schemaName" tabIndex="3" maxlength="50" size="50" value="<%=beanPage.getSchemaName()%>">
              </td>
            </tr>
          </table>
      </td>
	</tr>
    <script>seta_foco_no_primeiro()</script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" Save "  style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_UPDATE%>');" onMouseOver="javascript:mensagem('Click here to save the record.');"></td>
			<td><input type="submit" value=" Cancel "  style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('Click here to back to list.');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
