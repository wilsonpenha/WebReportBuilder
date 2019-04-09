<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector, java.sql.SQLException"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanFields/script.js"></script>

<% 
	reports.bean.BeanGroups beanPage = (reports.bean.BeanGroups)request.getAttribute("beanGroups");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>


<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanGroups.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
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
    <td height="30" class="topTitleCenter">Groups - Add</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.groups",request); %>
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
            <tr>              <td width="32%" class="dataLabel">*Group Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="groupsId" value="<%=beanPage.getGroupsId()%>">                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">                <input type="text" class="dataInput" name="groupName" tabIndex="3" maxlength="30" size="30" value="<%=beanPage.getGroupName()%>">              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">*Hierarchs : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="order" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getOrder()%>">
              </td>
            </tr> 
            <tr>
              <td width="32%" class="dataLabel">*Background Color : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" id="bgColor" name="bgColor" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getBgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('bgColor');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*Foreground Color : </td> 
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" id="fgColor" name="fgColor" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getFgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('fgColor');">
              </td>
            </tr>
            <tr>              <td width="32%" class="dataLabel">*Reprint Header On Each Page : </td>              <td width="66%" class="inputLocation">                <Select name="isReprintHeader" tabIndex="4">                   <option value="Yes">Yes</option>                   <option value="No">No</option>                </Select>              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Group Expression : </td>              <td width="66%" class="inputLocation">                <textarea class="dataInput" name="groupExpression" tabIndex="5" rows="4" cols="60" ><%=beanPage.getGroupExpression()%></textarea>                <input type="button" name="Submit" style="{height:20px; width: 49px;}" class="stdButton" id="btnFields" value="Fields" onClick="javascript:document.getElementById('loadFrame').src='<%=request.getContextPath()%>/jsp/fieldList.jsp?reportId=<%=beanPage.getReportId()%>&field=groupExpression';">
              </td>            </tr>          </table>
      </td>
	</tr>
    <script>
    seta_foco_no_primeiro();
    document.getElementById('bgColor').style.backgroundColor=document.getElementById('bgColor').value;
    document.getElementById('fgColor').style.backgroundColor=document.getElementById('fgColor').value;
    </script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" <bean:message key="app.button.save"/> " style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_INSERT%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> " style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
