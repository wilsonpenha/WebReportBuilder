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

import reports.GroupColumns;
import reports.Groups;

public class BeanGroupColumns extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String groupColumnsId = "";
  private java.lang.String isPrintHeader = "";
  
  private java.lang.String reportId = "";
  public java.lang.String getReportId(){return this.reportId;}
  public void setReportId(java.lang.String reportId){this.reportId = reportId;}

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getIsPrintHeader() {return isPrintHeader;}
  public void setIsPrintHeader(java.lang.String string) {isPrintHeader = string;}
  public java.lang.String getGroupColumnsId(){return this.groupColumnsId;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanGroupColumns(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.GroupColumns();}
  protected void selectList() throws Exception {
	Groups groups = new Groups();
	groups.setGroupsId(new BigDecimal(this.getGroupsId()));
  	this.list = reports.GroupColumns.findByGroups(groups);
  }
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.GroupColumns.findByID(new java.math.BigDecimal(idValues[0]));}
}





/************************************************************/
// Hwork, 2005
/************************************************************/