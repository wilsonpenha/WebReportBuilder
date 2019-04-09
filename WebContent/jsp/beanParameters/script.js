/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/21/2005
/************************************************************/
ArrNomeCampo     = ["parameterName", "classType", "parameterDescription", "sql", "inputType", "tableInput", "fieldKey", "fieldDisplay","radioOptions"];
ArrLabelCampo    = ["Parameter Name", "Class Type", "Parameter Description", "SQL Statement", "Input Type", "Table Input", "Field Key", "Field Display","Radio Options"];
ArrTipoCampo     = [TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_SELECT, TIPOFORM_SELECT, TIPOFORM_SELECT, TIPOFORM_SELECT, TIPOFORM_TEXT];
ArrOperacaoCampo = [OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE, OP_INSERT_UPDATE];

function chargeComboColumns(tableInput) {
    // define o source do XML

    reportId=document.forms[0].reportId.value;
    Columns.src="beanParameters/comboListColumns.jsp?reportId="+reportId+"&tableInput="+tableInput;
}

function releaseDiv(type){
	if (type=="Text"){
		ArrNomeCampo     = ["parameterName", "classType", "parameterDescription", "sql", "inputType"];
		ArrLabelCampo    = ["Parameter Name", "Class Type", "Parameter Description", "SQL Statement", "Input Type"];
		escodeDiv('divDataCombo');
		escodeDiv('divRadio');
	}else if (type=="Radio"){
		ArrNomeCampo     = ["parameterName", "classType", "parameterDescription", "sql", "inputType", "radioOptions"];
		ArrLabelCampo    = ["Parameter Name", "Class Type", "Parameter Description", "SQL Statement", "Input Type", "Radio Options"];
		ArrTipoCampo     = [TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_SELECT, TIPOFORM_TEXT];
		escodeDiv('divDataCombo');
		mostraDiv('divRadio');
	}else if (type=="Data Combo"){
		ArrNomeCampo     = ["parameterName", "classType", "parameterDescription", "sql", "inputType", "tableInput", "fieldKey", "fieldDisplay"];
		ArrLabelCampo    = ["Parameter Name", "Class Type", "Parameter Description", "SQL Statement", "Input Type", "Table Input", "Field Key", "Field Display"];
        ArrTipoCampo     = [TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_TEXT, TIPOFORM_SELECT, TIPOFORM_TEXT, TIPOFORM_SELECT, TIPOFORM_SELECT];
		mostraDiv('divDataCombo');
		escodeDiv('divRadio');
	}
}

function carrega() {
  if (document.readyState == "complete"){
    var dtNow = new Date();
    var tableInput = document.getElementById('tableInput').value;
    var reportId=document.forms[0].reportId.value;
    Columns.src="beanParameters/comboListColumns.jsp?reportId="+reportId+"&tableInput="+tableInput;
	releaseDiv(document.forms[0].inputType.value);
  }
}

/************************************************************/
// Hwork, 2005
/************************************************************/
