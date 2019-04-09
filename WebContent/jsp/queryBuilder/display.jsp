<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<% 
	reports.bean.BeanReport beanPage = (reports.bean.BeanReport)request.getAttribute("beanReport");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>
<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form>
<input type="hidden" name="dbAction">
<input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Database Tables - List</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
	     <jsp:include flush="true" page="../tabSheets1.jsp?tabSheetOn=Query Builder"/>
	  </div>
    </td>
  </tr>
        <tr>
          <td>
            <table width="100%" border="0" cellPadding="0" cellSpacing="0">
              <input type="hidden" name="objIds">
              <tr>
                <td class="CabecalhoCampo">
					<applet codebase="http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/applets/"
							code="nickyb.sqleonardo.environment.Application.class"
							archive="sqleonardo.jar, hwork.jar, reports.jar" 
							width="1024" 
							height="768">
						<param name="reportId" value="<%=beanPage.getReportId()%>">
					</applet>
                </td>
              </tr>
            </table>
          </td>
        </tr>
</table>
</form>
</body>
