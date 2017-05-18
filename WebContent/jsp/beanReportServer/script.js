/************************************************************/
//Description: Project pge
//Company....: Bright-Ideas
//Version....: 1.0
//Last change: 3/18/2005
/************************************************************/
ArrNomeCampo     = [];
ArrLabelCampo    = [];
ArrTipoCampo     = [];
ArrOperacaoCampo = [];

function confirmReport(form){
	for (var i=0; i < form.paramValue.length; i++) {
		if (form.paramValue[i].value==""){
			alert("The all parameters, must to be provide!");
			form.paramValue[i].focus();
			return false;
		}
	}
	return true;
}

function checkReportParams(form){
  count = 0
  for (var i=0; i < form.paramName.length; i++) {
    pos = form.validation[i].value.lastIndexOf('|');
    count = count + parseInt(form.validation[i].value.substring(pos+1));
    if (form.validation[i].value.indexOf('Yes')>=0 && form.validation[i].value.indexOf('text')>=0){
    	alert('The parameter name <'+form.paramLabel[i].value+'>, must to be provided!');
    	form.paramValue[count-1].focus();
    	return false;
    }
  }
  return true;
/*    if (campo.type == "select-one" || campo.type == "select-multiple" || campo.type == "button")
      campo.disabled = false;
    else if (campo.type == "radio") {
      var pos = this.ArrCampoReadOnly[i].indexOf("[");
      campoRadio = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos));
      for (var k=0; k < campoRadio.length; k++) {
        campo = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos)+"["+k+"]");
        campo.disabled = false;
      }
    } else if (campo.type == "checkbox") {
      var pos = this.ArrCampoReadOnly[i].indexOf("[");
      campoRadio = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos));
      for (var k=0; k < campoRadio.length; k++) {
        campo = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos)+"["+k+"]");
        campo.disabled = false;
      }
    } else {
      var pos = this.ArrCampoReadOnly[i].indexOf("[");
      if (pos > 0){
        campoRadio = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos));
        for (var k=0; k < campoRadio.length; k++) {
          campo = eval(obj+"."+this.ArrCampoReadOnly[i].substr(0,pos)+"["+k+"]");
          campo.readOnly = false;
        }
      } else
        campo.readOnly = false;
    }
  }
  document.forms[0].formAction.value = ACTION_FORM_UPDATE;*/
}

function buildReport(url){
	frmLOV = window.open(url ,
	      "winReport", "scrollbars=yes,resizable=yes,width=800,height=600");
	frmLOV.focus();
	if (frmLOV.opener == null) {
	   frmLOV.opener = self;
	}
}

function runReport(form){
	var winObj = window.open("" , "winReport", "scrollbars=yes,resizable=yes,toolsbar=yes,width=800,height=600");
	document.reportForm.target=winObj;
	winObj.focus();
}


/************************************************************/
// Bright-Ideas, 2005
/************************************************************/
