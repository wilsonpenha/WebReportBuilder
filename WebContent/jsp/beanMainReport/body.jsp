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
<%@ page language="java" %>
<%@ page errorPage="../common/errorpge.jsp" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*"/>

<%request.getSession().removeAttribute("JASPER_PRINT");%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/factory.do">

<input type="hidden" name="dbAction">
<input type="hidden" name="reportId" value="<%=request.getParameter("reportId")%>">
<input type="hidden" name="reload" value="<%=request.getParameter("reload")%>">
<input type="hidden" name="format" value="<%=request.getParameter("format")%>">
<input type="hidden" name="isGraphic" value="<%=request.getParameter("isGraphic")%>">
<input type="hidden" name="designer" value="true">
<input type="hidden" name="headerOrientation" value="Vertical">
<input type="hidden" name="footerOrientation" value="Horizontal">
<input type="hidden" name="paramName">
<input type="hidden" name="paramValue">

<script>document.forms[0].submit();</script>

</form>
</body>
</html>

