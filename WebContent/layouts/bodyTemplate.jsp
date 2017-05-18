<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head> 
<html:base/> 
<title><tiles:getAsString name="title"/></title> 

<meta name="GENERATOR" content="JXEd 1.0" />
<meta name="ProgId" content="JXEd.Editor.Document" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />

<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/jquery-pack.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/jquery-form.js"></script>
<script language="JavaScript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/confirm.js"></script>
<script language="JavaScript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/common.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/TableSort.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/xbLibrary.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/xbDebug.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/xbDOM.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/xbStyle.js"></script>
<script language="javascript" charset="ISO-8859-1" src="<%=request.getContextPath()%>/jsp/common/xbStyle-css.js"></script>

<link href="<%=request.getContextPath()%>/jsp/common/rptStyle.css" rel="stylesheet" type="text/css">

</head>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*" />

<%beanMain.setRootDIR(request);%>

<% beanMain.execute();%>

<tiles:insert attribute="content-main" />

<script>
function getFormat(form){
   for (i=0;i<form.format.length;i++){
       if (form.format[i].checked)
           return form.format[i].value;
   }
}
</script>
</html>