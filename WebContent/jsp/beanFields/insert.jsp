<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector, java.sql.SQLException"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanFields/script.js"></script>

<% 
	reports.bean.BeanFields beanPage = (reports.bean.BeanFields)request.getAttribute("beanFields");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<style type="text/css">
<!--
.divQuery {
	overflow: auto;
	z-index: auto;
	height: 300;
	width: 1024;
}
-->
</style>
<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanFields.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="executeQuery">
<input type="hidden" name="jndiName" value="<%=beanPage.getJndiName()%>">
<input type="hidden" name="contextType" value="<%=beanPage.getContextType()%>">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="30" class="topTitleCenter">Fields - Add</td>
  </tr>
  <tr>
    <td><div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.fields",request); %>
	     <jsp:include flush="true" page='<%="../tabSheets1.jsp?tabSheetOn=" + tabName%>'/>
      </div></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>
    <div align="center">
    <table width="1024" border="0" cellPadding="0" cellSpacing="0">
        <tr >
          <td class="dataLabel" style="text-align:center"> Enter the SQL statement </td>
        </tr>
        <tr >
          <td valign="top"><textarea name="sql" rows="7" cols="100%" class="dataInput" style="{font-size: 20px; width: 100%;}"><%=beanPage.getSql()%></textarea>
          </td>
        </tr>
        <tr>
          <td height="10" valign="top"></td>
        </tr>
        <tr>
          <td valign="top"><table border="0" align="center" cellpadding="0" cellSpacing="4">
              <tr>
                <td width="150" style="text-align:left" class="dataLabel">Max rows :
                  <input type="text" name="maxRows" size="6" value="50"></td>
                <td class="dataLabel"><input type="radio" name="viewData" <%if (beanPage.getViewData().equals("Yes")){%>checked<%}%> value="Yes">
                  Data query</td>
                <td class="dataLabel"><input type="radio" name="viewData" <%if (beanPage.getViewData().equals("No")){%>checked<%}%> value="No">
                  Structure query</td>
                <td>&nbsp;</td>
                <td><input type="submit" name="buttonExecQuery" value="Execute" onClick="javascript:document.forms[0].executeQuery.value='Yes';setAction(document.forms[0],'<%=BeanObject.ACTION_FORM_INSERT%>');"" style="{width: 65px;}" class="stdButton" align="top"></td>
                <td><input type="button" name="buttonLimpar" value="Clear SQL" onClick="limpaQuery()" style="{width: 65px; vertical-align: text-top;}" class="stdButton" align="top"></td>
			    <%if (beanPage.getViewData().equals("No")){%>
                <td><input type="button" name="buttonLimpar" value="Select all" onClick="selectAll();" style="{width: 65px; vertical-align: text-top;}" class="stdButton" align="top"></td>
                <td><input type="button" name="buttonLimpar" value="Deselect all" onClick="deselectAll();" style="{width: 80px; vertical-align: text-top;}" class="stdButton" align="top"></td>
                <td class="dataLabel"><input type="checkbox" name="createColumns" <%if (beanPage.getCreateColumns().equals("Yes")){%>checked<%}%> value="Yes">
                  Create Columns</td>
                <%}%>
              </tr>
            </table></td>
        </tr>
      </table></div></td>
  </tr>
  <input type="hidden" name="objIds">
  <tr>
    <td><div align="center">
    <% if (beanPage.getViewData().equals("No")){%>
      <table width="1024" border="0" cellPadding="0" cellSpacing="0">
        <tr>
          <td class="CabecalhoCampo">
	      <div class="divQuery">
	      <%if (beanPage.open()){
		      String[] ids = {"fieldName", "fieldComment", "fieldType"};
		      String[] objValues = {"fieldName", "fieldComment", "fieldType"};
		      String[] titles = {"", "Field Name", "Comment", "Class Type"};
		      String[] colsWidth = {"1%", "33%", "33%", "33%"};
		    %>
            <hwork:TableCheck name="objIds" ids="<%=ids%>"
		 	objs="<%=beanPage.getQueryMetaCols()%>"
		      width="100%" colsWidth="<%=colsWidth%>" colors="#D3DCE7,#F7F4F2" border="0"
		      objValues="<%=objValues%>" titles="<%=titles%>"
		      blockSize="200"
		      blockNumber="0"> </hwork:TableCheck> 
	      <%}else{
			   if (!beanPage.getErrorMsg().trim().equals("")){%>
			      <%=beanPage.getErrorMsg()%>
	           <%}
	        }%>
	      </div>
	      </td>
        </tr>
      </table>
      <%}else {%>
	      <table width="1024" border="0" cellPadding="0" cellSpacing="0">
          <tr>
          <td class="CabecalhoCampo">
	      <div class="divQuery">
	      <%if (beanPage.open()){%>
	            <table id="DivTable" class="tableTag" width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
	              <tr id="linha-1"  bgcolor="#D3DCE7">
	                <td width="5"></td>
	                <%for (int i=1;i<=beanPage.getRetorno().getMetaData().getColumnCount();i++){%>
				        <td nowrap="1">
					      <b><%=beanPage.getRetorno().getMetaData().getColumnName(i)%></b><img src="common/images/asc.gif" onclick="TableSortWait('DivTable', '1' ,'aa','1');"><img src="common/images/desc.gif" onclick="TableSortWait('DivTable', '1' ,'ad','1');">
				        </td>
	                <%}%>
	              </tr>
	              <%while (beanPage.getRetorno().next()){%>
			         <%if (beanPage.getRetorno().getRow() % 2 == 0) {%>
				      <tr id="linha<%=beanPage.getRetorno().getRow()-1%>" bgcolor="#F7F4F2" style="{cursor:hand}" onMouseover="setColorOn('linha<%=beanPage.getRetorno().getRow()-1%>');" onMouseout="setColorOff('linha<%=beanPage.getRetorno().getRow()-1%>');">
				       <%}else{%>
				      <tr id="linha<%=beanPage.getRetorno().getRow()-1%>" bgcolor="#D3DCE7" style="{cursor:hand}" onMouseover="setColorOn('linha<%=beanPage.getRetorno().getRow()-1%>');" onMouseout="setColorOff('linha<%=beanPage.getRetorno().getRow()-1%>');">
				       <%}%>
				         <td id="<%=beanPage.getRetorno().getRow()-1%>"width="5">
					       <%=beanPage.getRetorno().getRow()%>
				         </td>
				         <%for (int i=1;i<=beanPage.getRetorno().getMetaData().getColumnCount();i++){%>
					        <td nowrap="1" id="<%=beanPage.getRetorno().getRow()-1%>">
						      <%=beanPage.getRetorno().getString(i)%>
					        </td>
				         <%}%>
				      </tr>
				      <%if (beanPage.getRetorno().getRow()>Integer.parseInt(beanPage.getMaxRows())){
						break;
					  }%>
	              <%}%>
	              <%
				try{
					if (beanPage.getOpenConn()!=null && !beanPage.getOpenConn().isClosed()){
						System.out.println("Data Connection is closed!");
						beanPage.getRetorno().close();
						beanPage.getOpenConn().close();
					}
				}catch (SQLException es){}
		        %>
	  	        </table>
	      <%}else{
			   if (!beanPage.getErrorMsg().trim().equals("")){%>
			      <%=beanPage.getErrorMsg()%>
	           <%}
	        }%>
	      </div>
	        </td>
	        </tr>
		  	  </table>
	  <%}%>
</div></td>
</tr>
<script>seta_foco_no_primeiro()</script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<%if (beanPage.getViewData().equals("No")){%>
			<td><input type="submit" value=" <bean:message key="app.button.save"/> " style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_INSERT%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<%}%>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> " style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
