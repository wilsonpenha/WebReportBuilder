/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 9/16/2005
/************************************************************/
package reports.bean;

import java.sql.Timestamp;
import java.util.Date;
import br.com.hwork.persistencia.PObject;
import reports.bean.RptBeanObject;
import br.com.hwork.text.Formatter;

import reports.SeriesColumns;

public class BeanSeriesColumns extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String seriesColumnsId = "";  private java.lang.String chartGraphicId = "";  private java.lang.String columnName = "";  private java.lang.String columnType = "";
  private java.lang.String order = "";

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getSeriesColumnsId(){return this.seriesColumnsId;}  public void setSeriesColumnsId(java.lang.String seriesColumnsId){this.seriesColumnsId = seriesColumnsId;}  public java.lang.String getChartGraphicId(){return this.chartGraphicId;}  public void setChartGraphicId(java.lang.String chartGraphicId){this.chartGraphicId = chartGraphicId;}  public java.lang.String getColumnName() {return columnName;}
  public void setColumnName(java.lang.String string) {columnName = string;}
  public java.lang.String getColumnType(){return this.columnType;}  public void setColumnType(java.lang.String columnType){this.columnType = columnType;}  public java.lang.String getOrder(){return this.order;}  public void setOrder(java.lang.String order){this.order = order;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanSeriesColumns(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.SeriesColumns();}
  protected void selectList() throws Exception {this.list = reports.SeriesColumns.findAll();}
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.SeriesColumns.findByID(new java.math.BigDecimal(idValues[0]));}

}





/************************************************************/
// Hwork, 2005
/************************************************************/
