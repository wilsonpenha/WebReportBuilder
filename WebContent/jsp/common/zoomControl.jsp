<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<style>
.divZoom { font-family: verdana; font-size: 12px }
.drag {position: relative; cursor: hand}
.fl { width: 7em; float: left; padding: 2px 5px; text-align: right; }
.fr { width: 9em; float: left; padding: 2px 5px; text-align: right; }
fieldset { padding: 0px 10px 5px 5px; }
select, input, button { font: 11px Tahoma,Verdana,sans-serif; }
button { width: 70px; }
.space { padding: 5px; }
.title { background: #ddf; color: #000; font-weight: bold; font-size: 120%; padding: 3px 10px; margin-bottom: 10px;
border-bottom: 1px solid black; letter-spacing: 2px;
}
html, body {
  background: ButtonFace;
  color: ButtonText;
  font: 11px Tahoma,Verdana,sans-serif;
  margin: 0px;
  padding: 0px;
}

</style>
<script language="JavaScript" type="text/javascript">
<!-- 
var myChart = dialogArguments;
var sPosition;
var showPerc=100;
document.onmousedown=dragLayer
document.onmouseup=new Function("dragMe=false")

function init() {
perc = "100%";
document.getelementById('perc').value=perc
pos =perc.substr(0,perc.length-1) 
document.getelementById('knobImg').style.left = pos/2 +" px"
}

function chgZoom() {
  if (kObj.id =="knobImg") {
    if (Math.round(sPosition/5)==sPosition/5) {
      auxPos =Math.round(sPosition/5) *10
   oZoom = myChart.document.body
     newZoom= auxPos+'%'
     oZoom.style.zoom =newZoom;
	document.getelementById('perc').value =newZoom;
     }
  }
}


var dragMe=false, kObj, yPos,direction
function moveLayer() {
if (event.button==1 && dragMe) {
	oldX= kObj.style.pixelLeft; kObj.style.pixelLeft=temp2+event.clientX-xPos; 
	if (kObj.style.pixelLeft > oldX) direction="dn"; else direction="up";
	if (kObj.style.pixelLeft < 2 && direction=="up") {kObj.style.pixelLeft=2; direction="dn";}
	if (kObj.style.pixelLeft > 100 && direction=="dn") {kObj.style.pixelLeft=100; direction="up";}
	sPosition=kObj.style.pixelLeft; showPerc =  sPosition-2 ; 
	chgZoom(); 
	return false; }
}


function dragLayer() {
if (!document.all) return;
	if (event.srcElement.className=="drag")	
	{dragMe=true; kObj=event.srcElement; temp2=kObj.style.pixelLeft ; xPos=event.clientX; document.onmousemove=moveLayer; }
}

</script>

</head>
<body text="#000000" bgcolor="#FFFFFF" onload="init()">

<form method="post" action="" name="_zoomControl">
<body bgcolor="lightgrey" leftmargin="0" topmargin="0">
<div class="title">Report Zoom Control</div>
<fieldset style="float: left; margin-left: 5px; height:100px">
<legend>Zoom Control</legend>
<div id="zoomControl" class = "divZoom">
<br>

<table border="0" cellspacing="0" cellpadding="0">
<tr valign="top"><td width="156"><div id="outerLyr" style="position:absolute; width:120px; height:20px; z-index:1; left: 20px; top: 60px; background-color: #000000"> 
	<img id="knobImg" src="images/knob.jpg" class="drag" style="width: 20px; height: 14px; z-index:3; left: 50px; top: 2px">
  <div id="innerLyr" style="position:absolute; width:117px; height:17px; z-index:2; background-color: #777777; left: 2px; top: 2px; overflow: hidden">
  </div>
<div id="barLyr" style="position:absolute; width:110px; height: 2px ; z-index:2; background-color: #000000; left: 5px; top: 9px; overflow: hidden" ></div>
</div>
<div style="position:absolute; left: 20px; top: 75px; ">
<br>

<input name="perc" value="100%" style="width:40px;" readonly> 
<input type="button" onclick="window.close()" value="Close"></div></td></tr>
</table>
<div class="space"></div></fieldset>
</body></form>
</body>
</html>


