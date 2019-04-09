<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<% 
	reports.bean.BeanReport beanPage = (reports.bean.BeanReport)request.getAttribute("beanReport");
%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanReportServer.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Report Server - List</td>
  </tr>
        <tr>
          <td>
            <table width="100%" border="0" cellPadding="0" cellSpacing="0">
              <input type="hidden" name="objIds">
              <tr>
                <td class="CabecalhoCampo">
                <%
                  String[] ids = {"reportId"};
                  String[] objValues = {"description", "orientation"};
                  String[] titles = {"", "Report Name", "Orientation"};
                  String[] colsWidth = {"1%", "80%", "20%"};
                %>
                <hwork:TableHRef name="objIds" ids="<%=ids%>"
             	objs="<%=beanPage.getList()%>"
                  width="100%" colsWidth="<%=colsWidth%>" colors="#D3DCE7,#F7F4F2" border="0"
                  objValues="<%=objValues%>" titles="<%=titles%>"
                  blockSize="<%=Integer.parseInt(beanPage.getBlockSize())%>"
                  blockNumber="<%=Integer.parseInt(beanPage.getBlockNumber())%>"
                  onEvent="javascript:setAction(document.forms[0],ACTION_FORM_UPDATE);var obj = new Object();obj.value='#OBJIDS';if (elementos_do_formulario1('objIds','0') == 1){document.forms[0].objIds.value = obj.value;} else {document.forms[0].objIds[0].value = obj.value;}window.document.forms[0].submit();">
                </hwork:TableHRef>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
	    <td>
          <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tituloPrinCenter">
            <tr>
              <td height="16" width="50%" <% if (!beanPage.isFirstBlock()) {%> class="tdPrevious"><input type="button" value="Previous" name="btnAnterior" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_PRIOR%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
              <td width="50%" <% if (!beanPage.isLastBlock()) {%> class="tdNext"><input type="button" value="Next" name="btnProximo" class="btnList" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_NEXT%>');document.forms[0].submit();"><%} else {%>>&nbsp;<%}%></td>
            </tr>
          </table>
          </td>
	  </tr>
</table>
</form>
</body>

