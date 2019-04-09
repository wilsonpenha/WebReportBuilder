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

import reports.Groups;
import reports.Report;

public class BeanGroups extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String groupsId = "";
  private java.lang.String bgColor = "";
  private java.lang.String fgColor = "";


  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getOrder() {return order;}
  public void setOrder(java.lang.String string) {order = string;}
  public java.lang.String getGroupsId(){return this.groupsId;}
  public void setBgColor(java.lang.String string) {bgColor = string;}
  public java.lang.String getFgColor() {return fgColor;}
  public void setFgColor(java.lang.String string) {fgColor = string;}



 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanGroups(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.Groups();}
  protected void selectList() throws Exception {
	Report report = new Report();
	report.setReportId(new BigDecimal(this.getReportId()));
  	this.list = reports.Groups.findByReportOrderByOrder(report);
  }
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.Groups.findByID(new java.math.BigDecimal(idValues[0]));}

}





/************************************************************/
// Hwork, 2005
/************************************************************/