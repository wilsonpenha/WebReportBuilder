package reports.bean;

import java.util.*; 

public abstract class RptBeanObjectSearch extends RptBeanObject {

  /**
  * Executa a operação corrente do bean (dbAction).
  */
  public void execute() {
    try{
       this.doFormat();
	  if (this.dbAction == ACTION_NONE){this.dbAction = ACTION_SELECT_LIST;}
        switch (this.dbAction){case ACTION_NONE:{break;}
		  case ACTION_COPY:{
		  	this.copyLastPeriod();
		  	this.dbAction=ACTION_SELECT_LIST;
			this.execute();
			message = "Utilization Entries from the last period has been copied!";
			break;
		  }
          case ACTION_INSERT:{this.beforeInsert();beforePost();this.beanToObject();this.insert();afterPost();this.afterInsert();break;}
          case ACTION_UPDATE:{this.beforeUpdate();beforePost();this.beanToObject();this.update();afterPost();this.afterUpdate();break;}
		  case ACTION_DELETE:{
			for (int i=1; i<this.objIds.length;i++) {
			  this.beforeDelete();
			  this.beanToObject();
			  String[] idValues = this.getIDValues(this.objIds[i]);
			  this.selectObject(idValues);
			  this.delete();
			  this.afterDelete();
			}
			break;
		  }
          case ACTION_SELECT_OBJECT:{
              this.beforeSelectObject();
              this.selectObject(this.getIDValues(this.objIds[0]));
							if (this.pobject==null){
								this.dbAction = ACTION_SELECT_LIST;
								this.execute();
								this.message="Error: Object not Exist!";
								break;
							}
              this.objectToBean();
              this.afterSelectObject();
              break;}
          case ACTION_SELECT_LIST:{
            if (!this.errorSelectList){
              this.beforeSelectList();
              this.selectList();
              this.afterSelectList();
              break;
            }
            else{this.list = new Vector();}
          }
          case ACTION_SEARCH:{
            this.dbAction = ACTION_SELECT_LIST;
            this.execute();
            break;
          }
//          case ACTION_CANCEL:{this.beforeCancel();this.beanToObject();this.cancel();this.afterCancel();break;}
          case ACTION_NEXT:{this.beforeScroll();this.next();this.afterScroll();break;}
          case ACTION_PRIOR:{this.beforeScroll();this.prior();this.afterScroll();break;}
          case ACTION_FORM_INSERT:{
            this.setBeanForm(this.getFormInsert());
            break;
          }
          case ACTION_FORM_UPDATE:{
            this.dbAction = ACTION_SELECT_OBJECT;
            this.execute();
            break;
          }
          case ACTION_FORM_DELETE:{
            this.dbAction = ACTION_SELECT_OBJECT;
            this.execute();
            this.setBeanForm(this.getFormDelete());
            break;
          }
          case ACTION_FORM_DISPLAY_LIST:{
            this.setBeanForm(this.getFormDisplayList());
            break;
          }
          case ACTION_FORM_SEARCH:{
            this.setBeanForm(this.getFormSearch());
            break;
          }
          case ACTION_FORM_ERROR:{
            this.setBeanForm(this.getFormError());
            break;
          }
          case ACTION_ERROR:{break;}
        }
    }
    catch (Exception ex){
      System.out.println("Error: "+erro+"  -  "+ex.getMessage());
      ex.printStackTrace();
//      ex.printStackTrace(this.out);
      this.beforeError(ex);this.execError(ex);this.afterError(ex);
      if(!erro) {
        erro = true;
        switch (this.dbAction){
          case ACTION_SELECT_LIST:{this.errorSelectList = true;break;}
          case ACTION_SELECT_OBJECT:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          case ACTION_INSERT:{this.dbAction = ACTION_FORM_INSERT;this.execute();break;}
		  case ACTION_COPY:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          /*Essas linha foram comentada para solucionar o problema de NullPointer quando
          excluída fisicamente o registro do banco*/
          case ACTION_UPDATE:{this.dbAction = ACTION_FORM_UPDATE;this.execute();break;}
          //case ACTION_DELETE:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          //case ACTION_UPDATE:{this.dbAction = ACTION_FORM_SEARCH;this.execute();break;}
          case ACTION_DELETE:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          case ACTION_SEARCH:{this.dbAction = ACTION_FORM_SEARCH;this.execute();break;}
          case ACTION_NEXT:{this.dbAction = ACTION_FORM_DISPLAY_LIST;this.execute();break;}
          case ACTION_PRIOR:{this.dbAction = ACTION_FORM_DISPLAY_LIST;this.execute();break;}
        }
      }
    }
    try {
      this.doUnformat();
    } catch(Exception ex) {
      ex.printStackTrace();
//      ex.printStackTrace(this.out);
      this.beforeError(ex);this.execError(ex);this.afterError(ex);
    }
  }
  /**
  * Trata o evento depois da inserção de dados.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   message = MSG_INSERT_OK;
  */
  protected void afterInsert() throws Exception {
    this.dbAction = ACTION_FORM_SEARCH;
    this.execute();
    this.message = MSG_INSERT_OK;
  }

  /**
  * Trata o evento depois da deleção de dados.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   message = MSG_DELETE_OK;
  *  //se esta no ultimo bloco e nao possui nada a mostrar entao
  *  // retorna para bloco anterior
  *  if ((this.blockNumber * this.blockSize)>=this.list.size()){
  *    this.dbAction = ACTION_PRIOR;
  *    this.execute();
  */
//  protected void afterDelete() throws Exception {
//    this.dbAction = ACTION_FORM_SEARCH;
//    this.execute();
//    this.message = MSG_DELETE_OK;
//  }

}


























