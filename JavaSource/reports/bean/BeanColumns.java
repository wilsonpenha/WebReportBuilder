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
  private java.lang.String reportId = "";  private java.lang.String columnsId = "";  private java.lang.String columnName = "";  private java.lang.String labelWidth = "";
  private java.lang.String width = "";  private java.lang.String alignment = "";  private java.lang.String classType = "";  private java.lang.String order = "";  private java.lang.String bandType = "";  private java.lang.String columnExpression = "";   private java.lang.String evaluationTime = "";


  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getReportId(){return this.reportId;}  public void setReportId(java.lang.String reportId){this.reportId = reportId;}  public java.lang.String getColumnsId(){return this.columnsId;}  public void setColumnsId(java.lang.String columnsId){this.columnsId = columnsId;}  public java.lang.String getColumnName(){return this.columnName;}  public void setColumnName(java.lang.String columnName){this.columnName = columnName;}  public java.lang.String getWidth(){return this.width;}  public void setWidth(java.lang.String width){this.width = width;}  public java.lang.String getAlignment(){return this.alignment;}  public void setAlignment(java.lang.String alignment){this.alignment = alignment;}  public java.lang.String getClassType(){return this.classType;}  public void setClassType(java.lang.String classType){this.classType = classType;}  public java.lang.String getOrder(){return this.order;}  public void setOrder(java.lang.String order){this.order = order;}  public java.lang.String getBandType(){return this.bandType;}  public void setBandType(java.lang.String bandType){this.bandType = bandType;}  public java.lang.String getColumnExpression(){return this.columnExpression;}  public void setColumnExpression(java.lang.String columnExpression){this.columnExpression = columnExpression;}  public java.lang.String getLabelWidth() {return labelWidth;}
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
