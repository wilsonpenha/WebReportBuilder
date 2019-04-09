package reports.bean;

import br.com.hwork.persistencia.*;

public abstract class RptBeanObjectDetail extends RptBeanObject {

  protected PObject objectDetail;

  protected void afterDelete() throws Exception {
    this.setBeanForm(this.getFormInsert());
    this.pobject = this.getPObjectInstance();
    this.objectToBean();
    this.dbAction = ACTION_FORM_INSERT;
    this.execute();
    this.message = MSG_DELETE_OK;
  }

  protected void afterSelectList() throws Exception {
    if (this.list.size() > 0) {
      this.dbAction = this.ACTION_FORM_UPDATE;
    }
    else {
      this.dbAction = this.ACTION_FORM_INSERT;
    }
    this.execute();
  }

}