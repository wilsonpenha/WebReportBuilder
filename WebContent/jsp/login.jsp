<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<META HTTP-EQUIV="imagetoolbar" CONTENT="no">

<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/common/confirm.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/common/common.js"></script>
<%@ page language="java" %>
<%@ page errorPage="common/errorpge.jsp" %>

<jsp:useBean id="beanMain" scope="session" class="reports.bean.Main"/>
<jsp:setProperty name="beanMain" property="*"/>

<link href="<%=request.getContextPath()%>/jsp/common/rptStyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
<head>
<title>JasperReports WebReportBuilder by HWORK Services...</title>


<script>
function launch(){
 if (event.keyCode == 13){
 submitLoginScreen();
 }
}
function submitLoginScreen(){
  frm = document.forms[0];
  if(frm.j_username.value==''){
   alert('Please Enter User Name');
	frm.j_username.focus();	
   return;
  }
  if(frm.j_password.value==''){
   alert('Please Enter Password');
	frm.j_password.focus();
   return;
  }
  frm.submit();
}
</script>
</head>
<body bgcolor="white" topmargin="0" leftmargin="0" onkeydown="launch()" onload="document.forms[0].j_username.focus();">

<form method="post" action="j_security_check">
<DIV style="position:absolute;top:244;Left:176; z-index:123;" >
</div>


<DIV style="position:absolute;top:15;Left:610; z-index:123;" >

</div>

<DIV style="position:absolute;top:109px;Left:397px; z-index:123;" >

</div>


<DIV style="position:absolute;top:253px;Left:398px; z-index:123;" >
<P style="font-family: Verdana, Helvetica, sans-serif;font-size: 12px;font-weight:normal;color: red;text-decoration: none;text-align: left;">

	<%
	if("1".equals(request.getParameter("error")))
		out.print("Invalid Userid");
	if("2".equals(request.getParameter("error")))
		out.print("Invalid Role");
	%>
</P>
<P style="font-family: Verdana, Helvetica, sans-serif;font-size: 12px;font-weight:normal;color: steelblue;text-decoration: none;text-align: left;">
Please enter your name and password into the system. <br>
If you need an account or encounter any problems please <br>
email <A style="text-decoration: none" href="mailto:ted.turner@pfizer.com">
Ted Turner</A> at <A style="text-decoration: none" href="mailto:ted.turner@pfizer.com">ted.turner@pfizer.com</A><br>
and he will be happy to assist you.
</P>	
</div>

<div id="Layer1" style="position:absolute; width:200px; height:115px; z-index:124; left: 72px; top: 110px;">
  <table width="278" height="225" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/jsp/common/images/backLogin.gif">
    <tr>
      <td width="94">&nbsp;</td>
      <td width="184">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" class="titleCenter">User Log In</td>
    </tr>
    <tr>
      <td class="loginLabel">&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td class="loginLabel">Login name </td>
      <td><input name="j_username" type="text" class="inputLogin" size=21></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td class="loginLabel">Password</td>
      <td><input name="j_password" type="password" class="inputLogin" size=21></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input name="Submit" type="submit" class="signInBtn" value="Sign In"></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
</div>
<DIV style="position:absolute;top:517px;Left:31px; z-index:123;" >
<p style="font-family: Arial,Verdana, Helvetica, sans-serif;font-size: 11px;font-weight:normal;color: steelblue;text-decoration: none;text-align: left;">
<i>&copy; Copyright 2003-2005, HWORK.</i></p>
</div>


</form>
</body>

</html>
