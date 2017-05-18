<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<jsp:useBean id="beanPage" scope="request" class="reports.bean.BeanSeriesColumns"/>
<jsp:setProperty name="beanPage" property="*"/>

<%beanPage.selectObject();%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter"><%=beanPage.getPObject().getClass().getName().substring(4)%> - Edit</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>              <td width="32%" class="dataLabel">Order : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="seriesColumnsId" value="<%=beanPage.getSeriesColumnsId()%>">                <input type="hidden" name="chartGraphicId" value="<%=beanPage.getChartGraphicId()%>">                <input type="hidden" name="columnType" value="<%=beanPage.getColumnType()%>">                <input type="text" class="dataInput" name="order" tabIndex="4" maxlength="3" size="3" value="<%=beanPage.getOrder()%>">              </td>            </tr>          </table>
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
