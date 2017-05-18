<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanVariables/script.js"></script>

<% 
	reports.bean.BeanVariables beanPage = (reports.bean.BeanVariables)request.getAttribute("beanVariables");
%>

<%beanPage.selectObject();%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>


<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<div class="divPop" id="divFields" scrolling="no">
	<iframe id="loadFrame"  width="100%" height="210" scrolling="no"></iframe>
</div>
<form method="post" action="<%=request.getContextPath()%>/beanVariables.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="objIds">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Variables - Edit</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.variables",request); %>
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
            <tr>
              <td width="32%" class="dataLabel">Variable Name : </td>
              <td width="66%" class="inputLocation">
                <input type="hidden" name="variablesId" value="<%=beanPage.getVariablesId()%>">
                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
                <input type="text" class="dataInput" name="variableName" tabIndex="3" maxlength="30" size="30" value="<%=beanPage.getVariableName()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Variable Description : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="variableDescription" tabIndex="4" maxlength="30" size="30" value="<%=beanPage.getVariableDescription()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*Class Type : </td>
              <td width="66%" class="inputLocation">
                <%
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
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Reset Type : </td>
              <td width="66%" class="inputLocation">
                <Select name="resetType">
                   <option value="None" <%if (beanPage.getResetType().equals("None")){out.print("selected");}%>>None</option>
                   <option value="Report" <%if (beanPage.getResetType().equals("Report")){out.print("selected");}%>>Report</option>
                   <option value="Page" <%if (beanPage.getResetType().equals("Page")){out.print("selected");}%>>Page</option>
                   <option value="Group" <%if (beanPage.getResetType().equals("Group")){out.print("selected");}%>>Group</option>
                   <option value="Column" <%if (beanPage.getResetType().equals("Column")){out.print("selected");}%>>Column</option>
                </Select>

              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Increment Type : </td>
              <td width="66%" class="inputLocation">
                <Select name="incrementType">
                   <option value="None" <%if (beanPage.getIncrementType().equals("None")){out.print("selected");}%>>None</option>
                   <option value="Report" <%if (beanPage.getIncrementType().equals("Report")){out.print("selected");}%>>Report</option>
                   <option value="Page" <%if (beanPage.getIncrementType().equals("Page")){out.print("selected");}%>>Page</option>
                   <option value="Group" <%if (beanPage.getIncrementType().equals("Group")){out.print("selected");}%>>Group</option>
                   <option value="Column" <%if (beanPage.getIncrementType().equals("Column")){out.print("selected");}%>>Column</option>
                </Select>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Reset Group : </td>
              <td width="66%" class="inputLocation">
                <%
                  reports.Report report = new reports.Report();
                  report.setReportId(new java.math.BigDecimal(beanPage.getReportId()));
                  Vector objsresetGroupId = reports.Groups.findByReport(report);
                  String[] idsresetGroupId = {"groupsId"};
                  String[] objValuesresetGroupId = {"groupName"};
                %>
                <hwork:ComboList name="resetGroupId" 
                  ids="<%=idsresetGroupId%>" 
                  objValues="<%=objValuesresetGroupId%>" 
                  onEvent="tabIndex=\"8\"" 
                  blank="true" 
                  objs="<%=objsresetGroupId%>"
                  selected="<%=beanPage.getResetGroupId()%>">
                </hwork:ComboList>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Increment Group Id : </td>
              <td width="66%" class="inputLocation">
                <%
                  Vector objsincrementGroupId = reports.Groups.findByReport(report);
                  String[] idsincrementGroupId = {"groupsId"};
                  String[] objValuesincrementGroupId = {"groupName"};
                %>
                <hwork:ComboList name="incrementGroupId" 
                  ids="<%=idsincrementGroupId%>" 
                  objValues="<%=objValuesincrementGroupId%>" 
                  onEvent="tabIndex=\"9\"" 
                  blank="true" 
                  objs="<%=objsincrementGroupId%>"
                  selected="<%=beanPage.getIncrementGroupId()%>">
                </hwork:ComboList>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Calculation : </td>
              <td width="66%" class="inputLocation">
                <Select name="calculation" tabIndex="10">
                   <option value="Nothing" <%if (beanPage.getCalculation().equals("Nothing")){out.print("selected");}%>>Nothing</option>
                   <option value="Sum" <%if (beanPage.getCalculation().equals("Sum")){out.print("selected");}%>>Sum</option>
                   <option value="Count" <%if (beanPage.getCalculation().equals("Count")){out.print("selected");}%>>Count</option>
                   <option value="Average" <%if (beanPage.getCalculation().equals("Average")){out.print("selected");}%>>Average</option>
                   <option value="Lowest" <%if (beanPage.getCalculation().equals("Lowest")){out.print("selected");}%>>Lowest</option>
                   <option value="Highest" <%if (beanPage.getCalculation().equals("Highest")){out.print("selected");}%>>Highest</option>
                </Select>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Variable Expression : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="variableExpression" tabIndex="11" cols="65" rows="4"><%=beanPage.getVariableExpression()%></textarea>
                <input type="button" name="Submit" style="{height:20px; width: 49px;}" class="stdButton" id="btnFields" value="Fields" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/fieldList.jsp?reportId=<%=beanPage.getReportId()%>&field=variableExpression';">
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
			<td><input type="submit" value=" <bean:message key="app.button.save"/> "  style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_UPDATE%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> "  style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
