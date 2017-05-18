/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Report extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal reportId;
  private java.lang.String reportName;
  private java.lang.String description;
  private java.lang.String title;
  private java.lang.String image;
  private java.lang.String header;
  private java.lang.String footer;
  private java.lang.String sql;
  private java.lang.String orientation;
  private java.lang.String pageHeader;
  private java.lang.String pageFooter;
  private java.lang.String summary;
  private java.lang.String template;
  private String jndiName;
  private String contextType;
  private String titleBgColor;
  private String titleFgColor;
  private String detailHeadBgColor;
  private String detailHeadFgColor;
  private String detailColorOn;
  private String detailColorOff;
  private byte[] jasperJrxml;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public Report() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access reportName: Report Name
  */
  public java.lang.String getReportName(){return this.reportName;}
  /**
  * Access description: Description
  */
  public java.lang.String getDescription(){return this.description;}
  /**
  * Access title: Title
  */
  public java.lang.String getTitle(){return this.title;}
  /**
  * Access image: Image
  */
  public java.lang.String getImage(){return this.image;}
  /**
  * Access header: Header
  */
  public java.lang.String getHeader(){return this.header;}
  /**
  * Access footer: Footer
  */
  public java.lang.String getFooter(){return this.footer;}
  /**
  * Access sql: Sql
  */
  public java.lang.String getSql(){return this.sql;}
  /**
  * Access orientation: Orientation
  */
  public java.lang.String getOrientation(){return this.orientation;}
  public java.lang.String getPageHeader(){return this.pageHeader;}
  public java.lang.String getPageFooter(){return this.pageFooter;}
  public java.lang.String getSummary(){return this.summary;}
  public java.lang.String getTemplate(){return this.template;}
  public String getJndiName() {return this.jndiName;}
  public String getContextType() {return this.contextType;}

  public String getDetailColorOff() {return detailColorOff;}
  public String getDetailColorOn() {return detailColorOn;}
  public String getDetailHeadBgColor() {return detailHeadBgColor;}
  public String getDetailHeadFgColor() {return detailHeadFgColor;}
  public String getTitleBgColor() {return titleBgColor;}
  public String getTitleFgColor() {return titleFgColor;}
  public byte[] getJasperJrxml() {return jasperJrxml;}
  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change reportName: Report Name
  */
  public void setReportName(java.lang.String reportName){this.reportName = reportName;}
  /**
  * Change description: Description
  */
  public void setDescription(java.lang.String description){this.description = description;}
  /**
  * Change title: Title
  */
  public void setTitle(java.lang.String title){this.title = title;}
  /**
  * Change image: Image
  */
  public void setImage(java.lang.String image){this.image = image;}
  /**
  * Change header: Header
  */
  public void setHeader(java.lang.String header){this.header = header;}
  /**
  * Change footer: Footer
  */
  public void setFooter(java.lang.String footer){this.footer = footer;}
  /**
  * Change sql: Sql
  */
  public void setSql(java.lang.String sql){this.sql = sql;}
  /**
  * Change orientation: Orientation
  */
  public void setOrientation(java.lang.String orientation){this.orientation = orientation;}
  public void setPageHeader(java.lang.String pageHeader){this.pageHeader = pageHeader;}
  public void setPageFooter(java.lang.String pageFooter){this.pageFooter = pageFooter;}
  public void setSummary(java.lang.String summary){this.summary = summary;}
  public void setTemplate(java.lang.String template){this.template = template;}
  public void setJndiName(String jndiName) {this.jndiName = jndiName;}
  public void setContextType(String contextType) {this.contextType = contextType;}
  public void setDetailColorOff(String string) {detailColorOff = string;}
  public void setDetailColorOn(String string) {detailColorOn = string;}
  public void setDetailHeadBgColor(String string) {detailHeadBgColor = string;}
  public void setDetailHeadFgColor(String string) {detailHeadFgColor = string;}
  public void setTitleBgColor(String string) {titleBgColor = string;}
  public void setTitleFgColor(String string) {titleFgColor = string;}
  public void setJasperJrxml(byte[] jasperJrxml) {this.jasperJrxml = jasperJrxml;}


  /*-------------------------------------------------------------------------
                      Navigation Methods
  -------------------------------------------------------------------------*/


  /*-------------------------------------------------------------------------
                      Events Methods
  -------------------------------------------------------------------------*/
  protected void beforePost(String transID)
  throws Exception{
  }

  /*-------------------------------------------------------------------------
                      System Business Methods
  -------------------------------------------------------------------------*/
  /**
  * Find a Object by ID
  */
  public static Report findByID(
        java.math.BigDecimal reportId)
  throws Exception{
    Report obj = new Report();
    obj.setReportId(reportId);
    return (Report) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = Report.recuperaProximoID(this, transID);
    this.setReportId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    Report obj = new Report();
    return getList(obj, null);
  }

}


/************************************************************/
// Hwork, 2005
/************************************************************/
