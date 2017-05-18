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

import reports.Report;
import reports.Variables;

public class BeanVariables extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String variablesId = "";  private java.lang.String reportId = "";  private java.lang.String variableName = "";  private java.lang.String variableDescription = "";  private java.lang.String classType = "";  private java.lang.String resetType = "";  private java.lang.String incrementType = "";  private java.lang.String resetGroupId = "";  private java.lang.String incrementGroupId = "";  private java.lang.String calculation = "";  private java.lang.String variableExpression = "";

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getVariablesId(){return this.variablesId;}  public void setVariablesId(java.lang.String variablesId){this.variablesId = variablesId;}  public java.lang.String getReportId(){return this.reportId;}  public void setReportId(java.lang.String reportId){this.reportId = reportId;}  public java.lang.String getVariableName(){return this.variableName;}  public void setVariableName(java.lang.String variableName){this.variableName = variableName;}  public java.lang.String getVariableDescription(){return this.variableDescription;}  public void setVariableDescription(java.lang.String variableDescription){this.variableDescription = variableDescription;}  public java.lang.String getClassType(){return this.classType;}  public void setClassType(java.lang.String classType){this.classType = classType;}  public java.lang.String getResetType(){return this.resetType;}  public void setResetType(java.lang.String resetType){this.resetType = resetType;}  public java.lang.String getIncrementType(){return this.incrementType;}  public void setIncrementType(java.lang.String incrementType){this.incrementType = incrementType;}  public java.lang.String getResetGroupId(){return this.resetGroupId;}  public void setResetGroupId(java.lang.String resetGroupId){this.resetGroupId = resetGroupId;}  public java.lang.String getIncrementGroupId(){return this.incrementGroupId;}  public void setIncrementGroupId(java.lang.String incrementGroupId){this.incrementGroupId = incrementGroupId;}  public java.lang.String getCalculation(){return this.calculation;}  public void setCalculation(java.lang.String calculation){this.calculation = calculation;}  public java.lang.String getVariableExpression(){return this.variableExpression;}  public void setVariableExpression(java.lang.String variableExpression){this.variableExpression = variableExpression;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanVariables(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.Variables();}
  protected void selectList() throws Exception {
  	Report report = new Report();
  	report.setReportId(new BigDecimal(this.getReportId()));
  	this.list = reports.Variables.findByReport(report);
  }
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.Variables.findByID(new java.math.BigDecimal(idValues[0]));}
}





/************************************************************/
// Hwork, 2005
/************************************************************/
