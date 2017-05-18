<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector, reports.Report, java.math.BigDecimal"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanGroupColumns/script.js"></script>

<% 
	reports.bean.BeanGroupColumns beanPage = (reports.bean.BeanGroupColumns)request.getAttribute("beanGroupColumns");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<%beanPage.selectObject();%>


<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanGroupColumns.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<div class="divPop" id="divFields" scrolling="no">
	<iframe id="loadFrame"  width="100%" height="210" scrolling="no"></iframe>
</div>
<input type="hidden" name="dbAction">
<input type="hidden" name="objIds">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Group Columns - Edit</td>
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
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>              <td width="32%" class="dataLabel">*Column Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="groupColumnsId" value="<%=beanPage.getGroupColumnsId()%>">                <input type="text" class="dataInput" name="description" tabIndex="2" maxlength="30" size="30" value="<%=beanPage.getDescription()%>">              </td>             </tr>            <tr>              <td width="32%" class="dataLabel">*Width : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="width" tabIndex="3" maxlength="5" size="5" value="<%=beanPage.getWidth()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Alignment : </td>              <td width="66%" class="inputLocation">                <Select name="alignment" tabIndex="4">                   <option value="Left" <%if (beanPage.getAlignment().equals("Left")){out.print("selected");}%>>Left</option>                   <option value="Center" <%if (beanPage.getAlignment().equals("Center")){out.print("selected");}%>>Center</option>                   <option value="Right" <%if (beanPage.getAlignment().equals("Right")){out.print("selected");}%>>Right</option>                </Select>              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Class Type : </td>              <td width="66%" class="inputLocation">                <%
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
              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">Field Expression : </td>              <td width="66%" class="inputLocation">                <textarea class="dataInput" name="fieldExpression" tabIndex="6" rows="4" cols="60" ><%=beanPage.getFieldExpression()%></textarea>                <input type="button" name="Submit" style="{height:20px; width: 49px;}" class="stdButton" id="btnFields" value="Fields" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/fieldList.jsp?reportId=<%=beanPage.getReportId()%>&field=fieldExpression';">
                <input type="button" name="Submit" style="{height:20px; width: 65px;}" class="stdButton" id="btnVariables" value="Variables" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/variablesList.jsp?reportId=<%=beanPage.getReportId()%>&field=fieldExpression';">
              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">Column Position : </td>
              <td width="66%" class="inputLocation">                <%
                  Report report = new Report();
                  report.setReportId(new BigDecimal(beanPage.getReportId()));
                  Vector objsColumns = reports.Columns.findByReportBandType(report, "Detail");
                  String[] idsColumns = {"order"};
                  String[] objValuesColumns = {"order","columnName"};
                %>
                <hwork:ComboList name="order" 
                  ids="<%=idsColumns%>" 
                  objValues="<%=objValuesColumns%>" 
                  onEvent="tabIndex=\"7\"" 
                  objs="<%=objsColumns%>"
                  selected="<%=beanPage.getOrder()%>">
                </hwork:ComboList>
              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">Print Header : </td>
              <td width="66%" class="inputLocation">
                <Select name="isPrintHeader" tabIndex="8">
                   <option value="Yes" <%if (beanPage.getIsPrintHeader().equals("Yes")){out.print("selected");}%>>Yes</option>
                   <option value="No" <%if (beanPage.getIsPrintHeader().equals("No")){out.print("selected");}%>>No</option>
                </Select>
              </td>
            </tr>
            <tr>              <td width="32%" class="dataLabel">Band Type : </td>              <td width="66%" class="inputLocation">                <Select name="bandType" tabIndex="8">                   <option value="Header" <%if (beanPage.getBandType().equals("Header")){out.print("selected");}%>>Header</option>                   <option value="Footer" <%if (beanPage.getBandType().equals("Footer")){out.print("selected");}%>>Footer</option>                </Select>              </td>            </tr>                <input type="hidden" name="groupsId" value="<%=beanPage.getGroupsId()%>">          </table>
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
