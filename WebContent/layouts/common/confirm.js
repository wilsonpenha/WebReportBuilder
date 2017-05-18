/* *********************  Constantes ********************/

TIPOFORM_TEXT 		=1;
TIPOFORM_DATA 		=2;
TIPOFORM_CEP  		=3;
TIPOFORM_TEL  		=4;
TIPOFORM_CPF  		=5;
TIPOFORM_CGC   		=6;
TIPOFORM_HORA  		=7;
TIPOFORM_MESANO  		=8;
TIPOFORM_SELECT  		=9;
TIPOFORM_RADIO_CHECK    	=10;
TIPOFORM_NUMERICO       	=11;
TIPOFORM_INTEIRO       	=12;
TIPOFORM_DATAVALIDA		=13;
TIPOFORM_NUMERICOVALIDA       	=14;
TIPOFORM_INTEIROVALIDA       	=15;
TIPOFORM_INSCRICAO             =16;
TIPOFORM_UTILIZATION            =17;

OP_INSERT               	= 1;
OP_UPDATE               	= 2;
OP_INSERT_UPDATE        	= 1;

ArrNomeCampo  		= new Array();
ArrTipoCampo  		= new Array();
ArrLabelCampo 		= new Array();
ArrStatusCampo 	    = new Array();
ArrOperacaoCampo	= new Array();


FORM_DISPLAY_LIST = "displayList.jsp";
FORM_INSERT = "insert.jsp";
FORM_INSERT_SEM_CONFIRMA = "insertsemconfirma.jsp";
FORM_UPDATE = "update.jsp";
FORM_DELETE = "delete.jsp";

ACTION_FORM_VIEW 	      = "26";
ACTION_FORM_UPDATE 	= "21";
ACTION_FORM_INSERT 	= "20";
ACTION_FORM_SEARCH      = "25";
ACTION_SELECT_OBJECT	= "4";
ACTION_TELAANTERIOR 	= "99";
ACTION_DISPLAY_LIST     = "5"
ACTION_INSERT           = "1";
ACTION_UPDATE 	      = "2";
ACTION_DELETE 		= "3";
ACTION_RECOVER 		= "6";
ACTION_SEARCH           ="10";

function ConfirmaSubmit(str, tipoForm)
 {
	if(str.dbAction.value == ACTION_INSERT || str.dbAction.value == ACTION_UPDATE ) {
	//var iChars = "~`#%^&*()+=[]\';/{}\"<>?";
	 var iChars = "`%^&()[]\';{}\"<>";
		inputCol = document.getElementsByTagName("INPUT");
		for (z=0;z<inputCol.length;z++) {
			if (inputCol[z].type =="text") {
				var cName = inputCol[z].value;
				for (var i = 0; i < cName.length; i++) {
					if (iChars.indexOf(cName.charAt(i)) != -1) {
						alert ('Some special characters was found in the field.\nSpecial characters such as '+iChars+' are not allowed.\nPlease remove them.\nInternal field name :'+inputCol[z].name +'('+cName.charAt(i)+')');
						inputCol[z].focus();
						return false;
					}
				}
			}
		}
	}

  if(tipoForm==FORM_DISPLAY_LIST && str.dbAction.value==ACTION_DISPLAY_LIST)
  {return ConfirmaDisplayList(str);
  } else
  if(tipoForm==FORM_DISPLAY_LIST && str.dbAction.value==ACTION_FORM_UPDATE)
  {return ConfirmaDisplayList(str);
  } else
  if(tipoForm==FORM_DISPLAY_LIST && str.dbAction.value==ACTION_DELETE)
  {return ConfirmaDisplayList(str);
  } else
  if(tipoForm==FORM_DISPLAY_LIST && str.dbAction.value==ACTION_RECOVER)
  {return ConfirmaDisplayList(str);
  } else
  if(tipoForm==FORM_INSERT && str.dbAction.value==ACTION_INSERT)
   {return ConfirmaCampos(str,1);
   } else
  if(tipoForm==FORM_UPDATE && str.dbAction.value==ACTION_UPDATE)
   {return ConfirmaCampos(str,2);
   } else
  if(tipoForm==FORM_INSERT_SEM_CONFIRMA && str.dbAction.value==ACTION_INSERT)
   {str.dbAction.value = ACTION_INSERT;
    return ConfirmaCampos(str,3);
   }
   
 return true;
}

//*************  VALIDACOES DE CONSULTA ******************************

function ConfirmaDisplayList(str)
 {
  if(str.dbAction.value == ACTION_FORM_UPDATE)
   {
    if(VerificaQtosCamposChecados(str.objIds)==0)
     {alert("No items selected.");
      return false;}
    else
    if(VerificaQtosCamposChecados(str.objIds)>1)
     {alert("Only 1 item must be selected.");
      return false;}
    else if (verificaExclusao(str))
     {alert("Deleted record can't be edited.");
      return false;}
   }else
  if(str.dbAction.value == ACTION_DELETE || str.dbAction.value == ACTION_RECOVER)
   {
    if(VerificaQtosCamposChecados(str.objIds)==0)
     {alert("No items selected.");
      return false;}
     else
      {
        if(str.dbAction.value == ACTION_DELETE){
          msg="Are you sure you want to delete the selected Record(s)?";
        }else{
          msg="Are you sure you want to recover the selected Record(s)?";
        }
        if (confirm(msg))
         {return true;
         }else{return false;}
      }
   }
 return true;
 }

//*************  VALIDACOES DE INSERT ******************************
function ConfirmaCampos(str,op)
 { var msg="";
   if (op ==1){
     msg="Are you sure you want to save this record ?";}
   else if (op ==2)
    {msg="Are you sure you want to save this record ?";}
   else if (op ==3)
    { op=1;}

   var confirma = false;

   if (msg=="") {
     confirma = true;
   } else {
     confirma = confirm(msg);
   }

   if (confirma){
       var retorno = false;
       for (var i=0; i < this.ArrNomeCampo.length; i++){
         if (this.ArrTipoCampo[i] == TIPOFORM_TEXT && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno = e_Text('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_DATA && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Data('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_CEP  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Cep('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_TEL  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Data('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_CPF  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Cpf('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_INSCRICAO  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Inscricao('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i],'document.forms[0].sigla') }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_CGC  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )    { retorno  = e_Cnpj('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_HORA  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )   { retorno  = e_Data('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_MESANO  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno  = e_Data('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_SELECT  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno  = esta_Select('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_RADIO_CHECK  && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) )  { retorno  = esta_Checado('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]) }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_NUMERICO && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = e_Num('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_INTEIRO && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = e_Int('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_NUMERICOVALIDA && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = e_NumValida('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_INTEIROVALIDA && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = e_IntValida('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_UTILIZATION && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = verifyUtilization(); }
         else
         if (this.ArrTipoCampo[i] == TIPOFORM_DATAVALIDA && ((op == 1 && this.ArrOperacaoCampo[i]==1) || (op==2 && (this.ArrOperacaoCampo[i]==1 ||this.ArrOperacaoCampo[i]==2)) ) ) { retorno = e_DataValida('document.forms[0].'+ArrNomeCampo[i],ArrLabelCampo[i]); }
         else { retorno =true;}

//         alert(this.ArrNomeCampo.length);
//         alert(i+' - '+this.ArrNomeCampo[i]+' - '+this.ArrTipoCampo[i]+' - '+retorno);
         
         if (retorno == false){ 
            posicao = eval('document.forms[0].'+ArrNomeCampo[i]);
            if (posicao.type != "hidden" && this.ArrTipoCampo[i] != TIPOFORM_RADIO_CHECK) {
                posicao.focus();
            }
            return false;
         }
       }

       if (retorno ==false){  
          return false;}else{return true;
       }

     }else{
        return false;
     }
     return true;
 }

function camposLiberados(obj){
  for (var i=0; i < this.ArrCampoReadOnly.length; i++) {
    campo = eval(obj+"."+this.ArrCampoReadOnly[i]);
   // alert(this.ArrCampoReadOnly[i]);
    if (campo.type == "select-one" || campo.type == "select-multiple" || campo.type == "button")
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
  document.forms[0].formAction.value = ACTION_FORM_UPDATE;
}

function setaFocusNoPrimeiro(obj){
  for (var i=0; i < this.ArrCampoReadOnly.length; i++) {
    var pos = this.ArrCampoReadOnly[i].indexOf("[");
    if (pos<0)
      campo = eval(obj+"."+this.ArrCampoReadOnly[i]);
      if (campo.type != "hidden"){
        campo.focus();
        break;
      }
  }
}



