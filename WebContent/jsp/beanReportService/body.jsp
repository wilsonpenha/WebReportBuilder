<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>JasperReports WebReportBuilder by HWORK Services...</title>

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
<script language="JavaScript" src="beanReport/script.js"></script>

<link href="<%=request.getContextPath()%>/jsp/common/rptStyle.css" rel="stylesheet" type="text/css">
</head>

<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<% 
	reports.bean.BeanReport beanPage = (reports.bean.BeanReport)request.getAttribute("beanReport");
%>
<body>

</body>
</html>

