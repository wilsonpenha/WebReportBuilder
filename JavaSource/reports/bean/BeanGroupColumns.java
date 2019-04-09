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
  private java.lang.String groupColumnsId = "";  private java.lang.String description = "";  private java.lang.String width = "";  private java.lang.String alignment = "";  private java.lang.String classType = "";  private java.lang.String fieldExpression = "";  private java.lang.String order = "";   private java.lang.String bandType = "";  private java.lang.String groupsId = "";
  private java.lang.String isPrintHeader = "";
  
  private java.lang.String reportId = "";
  public java.lang.String getReportId(){return this.reportId;}
  public void setReportId(java.lang.String reportId){this.reportId = reportId;}

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getIsPrintHeader() {return isPrintHeader;}
  public void setIsPrintHeader(java.lang.String string) {isPrintHeader = string;}
  public java.lang.String getGroupColumnsId(){return this.groupColumnsId;}  public void setGroupColumnsId(java.lang.String groupColumnsId){this.groupColumnsId = groupColumnsId;}  public java.lang.String getDescription(){return this.description;}  public void setDescription(java.lang.String description){this.description = description;}  public java.lang.String getWidth(){return this.width;}  public void setWidth(java.lang.String width){this.width = width;}  public java.lang.String getAlignment(){return this.alignment;}  public void setAlignment(java.lang.String alignment){this.alignment = alignment;}  public java.lang.String getClassType(){return this.classType;}  public void setClassType(java.lang.String classType){this.classType = classType;}  public java.lang.String getFieldExpression(){return this.fieldExpression;}  public void setFieldExpression(java.lang.String fieldExpression){this.fieldExpression = fieldExpression;}  public java.lang.String getOrder(){return this.order;}  public void setOrder(java.lang.String order){this.order = order;}  public java.lang.String getBandType(){return this.bandType;}  public void setBandType(java.lang.String bandType){this.bandType = bandType;}  public java.lang.String getGroupsId(){return this.groupsId;}  public void setGroupsId(java.lang.String groupsId){this.groupsId = groupsId;}

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
