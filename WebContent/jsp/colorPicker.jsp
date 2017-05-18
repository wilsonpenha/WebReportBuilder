<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Color Picker</title>

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

<link href="<%=request.getContextPath()%>/jsp/common/rptStyle.css" rel="stylesheet" type="text/css">
<body>
<form name="recherche" method="post" action="yourpage.html">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr><td align="center">
<input type=hidden name="rgb" value="123">
<table style="background-color:#f6f6f6;border:none;margin:0px;" cellpadding="0" cellspacing="0" width="100%">
<tr>
<td style="border:1px outset #CCF;background-color:#ffe;" width="100%" colspan="2">
<div id=temoin style='float:right;width:40px;height:128px;'> </div>

<script language="Javascript" type="text/javascript">
<!--
var total=1657;var X=Y=j=RG=B=0;
var aR=new Array(total);var aG=new Array(total);var aB=new Array(total);
for (var i=0;i<256;i++){
aR[i+510]=aR[i+765]=aG[i+1020]=aG[i+5*255]=aB[i]=aB[i+255]=0;
aR[510-i]=aR[i+1020]=aG[i]=aG[1020-i]=aB[i+510]=aB[1530-i]=i;
aR[i]=aR[1530-i]=aG[i+255]=aG[i+510]=aB[i+765]=aB[i+1020]=255;
if(i<255){aR[i/2+1530]=127;aG[i/2+1530]=127;aB[i/2+1530]=127;}
}
function p(){
	var jla=document.getElementById('choix');
	jla.innerHTML=artabus.toUpperCase();
	jla.style.backgroundColor=artabus.toUpperCase();
	document.forms['recherche'].rgb.value=artabus.toUpperCase();
}
var hexbase=new Array("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
var i=0;var jl=new Array();

for(x=0;x<16;x++)for(y=0;y<16;y++)jl[i++]=hexbase[x]+hexbase[y];

document.write('<'+'table border="0" cellspacing="0" cellpadding="0" onMouseover="t(event)" onClick="p()">');

var H=W=63;

for (Y=0;Y<=H;Y++){
	s='<'+'tr height=2>';j=Math.round(Y*(510/(H+1))-255)
	for (X=0;X<=W;X++){
		i=Math.round(X*(total/W))
		R=aR[i]-j;if(R<0)R=0;if(R>255||isNaN(R))R=255
		G=aG[i]-j;if(G<0)G=0;if(G>255||isNaN(G))G=255
		B=aB[i]-j;if(B<0)B=0;if(B>255||isNaN(B))B=255
		s=s+'<'+'td width=2 bgcolor=#'+jl[R]+jl[G]+jl[B]+'><'+'/td>'
	}
	document.write(s+'<'+'/tr>')
}

document.write('<'+'/table>');
var ns6=document.getElementById&&!document.all
var ie=document.all
var artabus=''

function t(e){ 
	source=ie?event.srcElement:e.target
	if(source.tagName=="TABLE")return
	while(source.tagName!="TD" && source.tagName!="HTML")source=ns6?source.parentNode:source.parentElement
	document.getElementById('temoin').style.backgroundColor=artabus=source.bgColor
}
// -->
</script>
<div id=choix style='position:relative;top:2px;height:24px;width:176'  onClick="document.forms['recherche'].rgb.value='';this.style.backgroundColor=''"> </div><td></tr>
<tr><td colspan=2 align=center style="padding-top:4px"><input type="button" class="stdButton" name="btnOK" value=" OK " onClick="setColorBack();">&nbsp;<input type="button"  class="stdButton" name="btnCancel" value="Cancel" onClick="window.close();"></td>
</tr>
</table>
</td>
</tr>
</table>
</form>
<script>
function setColorBack(){
	var parentField = eval('opener.document.getElementById("<%=request.getParameter("fieldName")%>")');
	parentField.value=document.forms['recherche'].rgb.value;
	parentField.style.backgroundColor=document.forms['recherche'].rgb.value;
	window.close();
}
</script>
</body>
</html>