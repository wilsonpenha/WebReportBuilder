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
  private java.lang.String variablesId = "";

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getVariablesId(){return this.variablesId;}

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