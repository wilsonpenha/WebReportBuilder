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
  private java.lang.String groupsId = "";  private java.lang.String reportId = "";  private java.lang.String groupName = "";  private java.lang.String isReprintHeader = "";  private java.lang.String groupExpression = "";  private java.lang.String order = "";
  private java.lang.String bgColor = "";
  private java.lang.String fgColor = "";


  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getOrder() {return order;}
  public void setOrder(java.lang.String string) {order = string;}
  public java.lang.String getGroupsId(){return this.groupsId;}  public void setGroupsId(java.lang.String groupsId){this.groupsId = groupsId;}  public java.lang.String getReportId(){return this.reportId;}  public void setReportId(java.lang.String reportId){this.reportId = reportId;}  public java.lang.String getGroupName(){return this.groupName;}  public void setGroupName(java.lang.String groupName){this.groupName = groupName;}  public java.lang.String getIsReprintHeader(){return this.isReprintHeader;}  public void setIsReprintHeader(java.lang.String isReprintHeader){this.isReprintHeader = isReprintHeader;}  public java.lang.String getGroupExpression(){return this.groupExpression;}  public void setGroupExpression(java.lang.String groupExpression){this.groupExpression = groupExpression;}  public java.lang.String getBgColor() {return bgColor;}
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
