<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri='/WEB-INF/tld/hwork.tld' prefix="hwork" %>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanColumns/script.js"></script>

<% 
	reports.bean.BeanColumns beanPage = (reports.bean.BeanColumns)request.getAttribute("beanColumns");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>



<%beanPage.selectObject();%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanColumns.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<div class="divPop" id="divFields" scrolling="no">
	<iframe id="loadFrame"  width="100%" height="210" scrolling="no"></iframe>
</div>
<input type="hidden" name="dbAction">
<input type="hidden" name="objIds">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Columns - Edit</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.detailCols",request); %>
	     <jsp:include flush="true" page='<%="../tabSheets1.jsp?tabSheetOn=" + tabName%>'/>
	  </div>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>              <td width="32%" class="dataLabel">*Column Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">                <input type="hidden" name="columnsId" value="<%=beanPage.getColumnsId()%>">                <input type="text" class="dataInput" name="columnName" tabIndex="3" maxlength="50" size="50" value="<%=beanPage.getColumnName()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Width : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="width" tabIndex="4" maxlength="5" size="5" value="<%=beanPage.getWidth()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Alignment : </td>              <td width="66%" class="inputLocation">                <Select name="alignment" tabIndex="5">                   <option value="Left" <%if (beanPage.getAlignment().equals("Left")){out.print("selected");}%>>Left</option>                   <option value="Center" <%if (beanPage.getAlignment().equals("Center")){out.print("selected");}%>>Center</option>                   <option value="Right" <%if (beanPage.getAlignment().equals("Right")){out.print("selected");}%>>Right</option>                </Select>              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Class Type : </td>              <td width="66%" class="inputLocation">                <%
                  Vector objsJavaClassId = reports.JavaClass.findAll();
                  String[] idsJavaClassId = {"classType"};
                  String[] objValuesJavaClassId = {"classType"};
                %>
                <hwork:ComboList name="classType" 
                  ids="<%=idsJavaClassId%>" 
                  objValues="<%=objValuesJavaClassId%>" 
                  onEvent="tabIndex=\"8\"" 
                  objs="<%=objsJavaClassId%>"
                  selected="<%=beanPage.getClassType()%>">
                </hwork:ComboList>
              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Order : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="order" tabIndex="7" maxlength="5" size="5" value="<%=beanPage.getOrder()%>">              </td>            </tr>			<input type="hidden" name="evaluationTime" value="Now">
            <tr>              <td width="32%" class="dataLabel">*Column Expression : </td>              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="columnExpression" tabIndex="9" rows="4" cols="60" ><%=beanPage.getColumnExpression()%></textarea>
                <input type="button" name="Submit" style="{height:20px; width: 49px;}" class="stdButton" id="btnFields" value="Fields" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/fieldList.jsp?reportId=<%=beanPage.getReportId()%>&field=columnExpression';">
                <input type="button" name="Submit" style="{height:20px; width: 65px;}" class="stdButton" id="btnVariables" value="Variables" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/variablesList.jsp?reportId=<%=beanPage.getReportId()%>&field=columnExpression';">
              </td>            </tr>          </table>
      </td>
	</tr>
    <script>seta_foco_no_primeiro()</script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" <bean:message key="app.button.save"/> "  style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_UPDATE%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> "  style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
