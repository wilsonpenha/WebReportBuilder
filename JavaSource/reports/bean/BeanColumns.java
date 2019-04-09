/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 6/21/2005
/************************************************************/
package reports.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import br.com.hwork.persistencia.PObject;
import br.com.hwork.text.Formatter;

import reports.Columns;
import reports.Report;

public class BeanColumns extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String reportId = "";
  private java.lang.String width = "";


  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getReportId(){return this.reportId;}
  public void setLabelWidth(java.lang.String string) {labelWidth = string;}
  public java.lang.String getEvaluationTime() {return evaluationTime;}
  public void setEvaluationTime(java.lang.String string) {evaluationTime = string;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanColumns(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.Columns();}
  protected void selectList() throws Exception {
	Report report = new Report();
	report.setReportId(new BigDecimal(this.getReportId()));
  	this.list = reports.Columns.findByReportBandType(report, this.bandType);
  }
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.Columns.findByID(new java.math.BigDecimal(idValues[0]));}

}





/************************************************************/
// Hwork, 2005
/************************************************************/