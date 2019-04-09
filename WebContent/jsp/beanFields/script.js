/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/21/2005
/************************************************************/
ArrNomeCampo     = ["fieldName", "classType"];
ArrLabelCampo    = ["Field Name", "Class Type"];
ArrTipoCampo     = [TIPOFORM_TEXT, TIPOFORM_TEXT];
ArrOperacaoCampo = [OP_UPDATE,OP_UPDATE];

function selectAll(){
   if (document.forms[0].objIds.length)
	   for (i=0;i<document.forms[0].objIds.length;i++){
	   		document.forms[0].objIds[i].checked=true;
	   }
}
function deselectAll(){
   if (document.forms[0].objIds.length)
	   for (i=0;i<document.forms[0].objIds.length;i++){
	   		document.forms[0].objIds[i].checked=false;
	   }
}

/************************************************************/
// Hwork, 2005
/************************************************************/
