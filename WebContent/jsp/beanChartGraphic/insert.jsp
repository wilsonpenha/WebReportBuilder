<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanChartGraphic/script.js"></script>

<% 
	reports.bean.BeanChartGraphic beanPage = (reports.bean.BeanChartGraphic)request.getAttribute("beanChartGraphic");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanChartGraphic.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">ChartGraphic - Add</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.chartGraphic",request); %>
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
            <tr>              <td width="32%" class="dataLabel">*Chart Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
                <input type="hidden" name="chartGraphicId" value="<%=beanPage.getChartGraphicId()%>">                <input type="text" class="dataInput" name="chartName" tabIndex="2" maxlength="60" size="60" value="<%=beanPage.getChartName()%>">              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">*JRMX SubDataSet code : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="subDataSet" tabIndex="8" cols="65" rows="10"><%=beanPage.getSubDataSet()%></textarea>
              </td>
            </tr>
            <tr>              <td width="32%" class="dataLabel">*Chart Height : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="chartHeight" tabIndex="7" maxlength="4" size="4" value="<%=beanPage.getChartHeight()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Chart Width : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="chartWidth" tabIndex="8" maxlength="4" size="4" value="<%=beanPage.getChartWidth()%>">              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">*JRMX Chart band code : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="chartBandCode" tabIndex="8" cols="65" rows="10"><%=beanPage.getChartBandCode()%></textarea>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">*Chart Band Location : </td>
              <td width="66%" class="inputLocation">
                <Select name="bandLocation" tabIndex="9">
                   <option value="titleHeader">Title header</option>
                   <option value="titleFooter">Title footer</option>
                   <option value="pageHeader">Page Header</option>
                   <option value="pageFooter">Page Footer</option>
                   <option value="groupHeader">Group Header</option>
                   <option value="groupFooter">Group Footer</option>
                   <option value="summary">Summary</option>
                   <option value="detail">Detail</option>
                </Select>
              </td>
            </tr>
            <tr>              <td width="32%" class="dataLabel">*Chart Type : </td>              <td width="66%" class="inputLocation">                <Select name="chartType" tabIndex="9">                   <option value="Bar Chart">barChart</option>                   <option value="Bar 3D Chart">bar3DChart</option>
                   <option value="Time Series Chart">timeSeriesChart</option>                   <option value="Pie Chart">pieChart</option>
                   <option value="Pie 3D Chart">pie3DChart</option>
                </Select>              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Chart SubType : </td>              <td width="66%" class="inputLocation">                <Select name="chartSubtype" tabIndex="10">                   <option value="Type 1">Type 1</option>                   <option value="Type 2">Type 2</option>                </Select>              </td>            </tr>          </table>
      </td>
	</tr>
    <script>seta_foco_no_primeiro()</script>
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

