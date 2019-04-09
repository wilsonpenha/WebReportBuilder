<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanFields/script.js"></script>

<% 
	reports.bean.BeanFields beanPage = (reports.bean.BeanFields)request.getAttribute("beanFields");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<%beanPage.selectObject();%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanFields.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="objIds">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Fields - Edit</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.fields",request); %>
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
            <tr>              <td width="32%" class="dataLabel">*Field Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="fiedlsId" value="<%=beanPage.getFiedlsId()%>">                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">                <input type="text" class="dataInput" name="fieldName" tabIndex="3" maxlength="30" size="30" value="<%=beanPage.getFieldName()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">Field Description : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="fieldDescription" tabIndex="4" maxlength="30" size="30" value="<%=beanPage.getFieldDescription()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Class Type : </td>              <td width="66%" class="inputLocation">                <%
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
              </td>            </tr>
            <tr>              <td colspan="2" width="98%" class="dataLabel">&nbsp;</td>            </tr>
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

