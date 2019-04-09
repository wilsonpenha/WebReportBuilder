<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanReport/script.js"></script>

<% 
	reports.bean.BeanReport beanPage = (reports.bean.BeanReport)request.getAttribute("beanReport");
%>

<%beanPage.selectObject();%>
<%beanPage.setRequest(request);%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanReport.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Report - Edit</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.report",request); %>
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
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.reportName"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
                <input type="text" class="dataInput" name="reportName" tabIndex="2" maxlength="50" size="50" value="<%=beanPage.getReportName()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.description"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="description" tabIndex="3" maxlength="100" size="60" value="<%=beanPage.getDescription()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.title"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="title" tabIndex="4" maxlength="100" size="60" value="<%=beanPage.getTitle()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.image"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="image" tabIndex="5" maxlength="100" size="60" value="<%=beanPage.getImage()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.sql"/> : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="sql" tabIndex="8" cols="65" rows="10"><%=beanPage.getSql()%></textarea>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.titleBgColor"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="titleBgColor" id="titleBgColor" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getTitleBgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('titleBgColor');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.titleFgColor"/> : </td> 
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="titleFgColor" id="titleFgColor"  tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getTitleFgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('titleFgColor');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.detailHeaderBgColor"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="detailHeadBgColor" id="detailHeadBgColor" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getDetailHeadBgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('detailHeadBgColor');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.detailHeaderFgColor"/> : </td> 
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="detailHeadFgColor" id="detailHeadFgColor" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getDetailHeadFgColor()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('detailHeadFgColor');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.detailColorOn"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="detailColorOn" id="detailColorOn" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getDetailColorOn()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('detailColorOn');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.detailColorOff"/> : </td> 
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="detailColorOff" id="detailColorOff" tabIndex="3" maxlength="5" size="10" value="<%=beanPage.getDetailColorOff()%>">
                <input type="button" name="colorPicker" class="btnColorPicker" onClick="javascript:getColorCode('detailColorOff');">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*<bean:message key="tabReports.label.orientation"/> : </td>
              <td width="66%" class="inputLocation">
                <Select name="orientation" tabIndex="9">
                   <option value="Portrait" <%if (beanPage.getOrientation().equals("Portrait")){out.print("selected");}%>>Portrait</option>
                   <option value="Landscape" <%if (beanPage.getOrientation().equals("Landscape")){out.print("selected");}%>>Landscape</option>
                </Select>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.pageHeader"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="pageHeader" tabIndex="10" maxlength="100" size="60" value="<%=beanPage.getPageHeader()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.pageFooter"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="pageFooter" tabIndex="11" maxlength="100" size="60" value="<%=beanPage.getPageFooter()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.summary"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="summary" tabIndex="12" maxlength="100" size="60" value="<%=beanPage.getSummary()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.jndiName"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="jndiName" tabIndex="13" maxlength="100" size="60" value="<%=beanPage.getJndiName()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.contextType"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="contextType" tabIndex="14" maxlength="100" size="60" value="<%=beanPage.getContextType()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.template"/> : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="template" tabIndex="15" maxlength="100" size="60" value="<%=beanPage.getTemplate()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel"><bean:message key="tabReports.label.attachmentFile"/> : </td>
              <td width="66%" class="inputLocation">
                <%=beanPage.getAttachmentNames()%>
              </td>
            </tr>
            <tr>
             <td width="32%" class="dataLabel"><bean:message key="tabReports.label.reportPreview"/> : </td>
              <td width="66%" colspan="3" class="inputLocation">
                <input type="radio" name="format"  value="html" tabIndex="16" checked> HTML 
                <input type="radio" name="format"  value="pdf" tabIndex="17" > PDF
                <input type="radio" name="format"  value="csv" tabIndex="18" > CSV 
                <input type="radio" name="format"  value="xls" tabIndex="19" > XLS 
                <input type="checkbox" name="isGraphic" id="isGraphic" value="Yes" tabIndex="20" > <bean:message key="tabReports.label.chartGraphic"/> 
               </td>
            </tr>
          </table>
      </td>
	</tr>
    <script>
    seta_foco_no_primeiro();
    document.getElementById('titleBgColor').style.backgroundColor=document.getElementById('titleBgColor').value;
    document.getElementById('titleFgColor').style.backgroundColor=document.getElementById('titleFgColor').value;
    document.getElementById('detailHeadBgColor').style.backgroundColor=document.getElementById('detailHeadBgColor').value;
    document.getElementById('detailHeadFgColor').style.backgroundColor=document.getElementById('detailHeadFgColor').value;
    document.getElementById('detailColorOn').style.backgroundColor=document.getElementById('detailColorOn').value;
    document.getElementById('detailColorOff').style.backgroundColor=document.getElementById('detailColorOff').value;
    </script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" <bean:message key="app.button.save"/> "  style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_UPDATE%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.copy"/> "  style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:if (confirm('Confirm this copy?')){setAction(document.forms[0],'31');return true;}else{return false;}" onMouseOver="javascript:mensagem('<bean:message key="app.button.copy.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> "  style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
			<td><input type="button" value=" <bean:message key="app.button.run"/> "     style="width: 38px;" onClick="javascript:buildReport('<%=request.getContextPath()%>/reportPreview.do?reportId=<%=beanPage.getReportId()%>&reload=true&format='+getFormat(document.forms[0])+'&graphic='+document.getElementById('isGraphic').checked);" name="btnNew" class="stdButton" onMouseOver="javascript:mensagem('<bean:message key="app.button.run.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>
