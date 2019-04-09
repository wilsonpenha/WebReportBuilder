/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005
/************************************************************/
ArrNomeCampo     = ["sql", "orientation", "jndiName"];
ArrLabelCampo    = ["Sql", "Orientation", "Jndi Name"];
ArrTipoCampo     = [TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT];
ArrOperacaoCampo = [OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE];


/************************************************************/
// Hwork, 2005
/************************************************************/

function buildReport(url){
	frmLOV = window.open(url ,
	      "winReport", "left=0,top=0,scrollbars=yes,resizable=yes,width=800,height=600");
	frmLOV.focus();
	if (frmLOV.opener == null) {
	   frmLOV.opener = self;
	}
}