<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">

<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/common/confirm.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/common/common.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/TableSort.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/xbLibrary.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/xbDebug.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/xbDOM.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/xbStyle.js"></script>
<script language="javascript" src="<%=request.getContextPath()%>/jsp/common/xbStyle-css.js"></script>
<script language="JavaScript" src="beanColumns/beanColumns/script.js"></script>

<link href="<%=request.getContextPath()%>/jsp/common/rptStyle.css" rel="stylesheet" type="text/css">

</head>

<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ page import = "reports.Report, reports.Fields, java.math.BigDecimal"%>
<%@ page language="java" %>
<%@ page errorPage="common/errorpge.jsp" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*"/>
<body>
<form>
<div align="center" style="width:250px; height:22px; z-index:1; overflow: hidden" >
<table border="0" cellpadding="0" cellspacing="1">
<tr><td>
<input type="button" style="{height:20px; width: 49px;}" name="btnClose" id="btnClose" class="stdButton" value="Close" onClick="javascript:parent.escodeDiv('divFields');">
<input type="button" style="{height:20px; width: 49px;}" name="btnSelect" id="btnSelect" class="stdButton" value="Select" onClick="javascript:selectFields();parent.escodeDiv('divFields');">
</td></tr>
</table>
</div>
<div style="width:100%; height:182; z-index:1; overflow: auto;" >
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr><td>
<input type="hidden" name="objIds">
<%
  Report report = new Report();
  report.setReportId(new BigDecimal(request.getParameter("reportId")));
  Vector objs = Fields.findByReport(report);
  String[] ids = {"fieldName"};
  String[] objValues = {"fieldName"};
  String[] titles = {"", "Field Name"};
  String[] colsWidth = {"1%", "99%"};
%>
<%if (objs.size()==0){%>
<script>document.getelementById('btnSelect').disabled=true;</script>
<%}%>
<hwork:TableCheck name="objIds" ids="<%=ids%>"
  objs="<%=objs%>"
  width="100%" colsWidth="<%=colsWidth%>" colors="#D3DCE7,#F7F4F2" border="0"
  objValues="<%=objValues%>" titles="<%=titles%>"
  blockSize="200"
  blockNumber="0"> 
</hwork:TableCheck> 
</td></tr>
</table>
</div>
<script>
function selectFields(){
    objFields = document.forms[0].objIds;
	if (objFields.length){
	   for (i=1;i<objFields.length;i++){
	       if (objFields[i].checked==true){
	           field = eval('parent.document.forms[0].<%=request.getParameter("field")%>');
	    	   field.value=field.value+' $F{'+objFields[i].value+'} ';
	       }
	   }
	}
}
parent.mostraDiv('divFields');

</script>
</form>
</body>
</html>

