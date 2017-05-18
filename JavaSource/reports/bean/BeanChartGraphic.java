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

import reports.ChartGraphic;

public class BeanChartGraphic extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String chartGraphicId = "";  private java.lang.String chartName = "";  private java.lang.String reportId = "";  private java.lang.String xLabel = "";  private java.lang.String yLabel = "";  private java.lang.String legendLocation = "";  private java.lang.String chartHeight = "";  private java.lang.String chartWidth = "";  private java.lang.String chartType = "";  private java.lang.String chartSubtype = "";  private java.lang.String dataRangeColumn = "";

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getChartGraphicId(){return this.chartGraphicId;}  public void setChartGraphicId(java.lang.String chartGraphicId){this.chartGraphicId = chartGraphicId;}  public java.lang.String getChartName(){return this.chartName;}  public void setChartName(java.lang.String chartName){this.chartName = chartName;}  public java.lang.String getReportId(){return this.reportId;}  public void setReportId(java.lang.String reportId){this.reportId = reportId;}  public java.lang.String getXLabel(){return this.xLabel;}  public void setXLabel(java.lang.String xLabel){this.xLabel = xLabel;}  public java.lang.String getYLabel(){return this.yLabel;}  public void setYLabel(java.lang.String yLabel){this.yLabel = yLabel;}  public java.lang.String getLegendLocation(){return this.legendLocation;}  public void setLegendLocation(java.lang.String legendLocation){this.legendLocation = legendLocation;}  public java.lang.String getChartHeight(){return this.chartHeight;}  public void setChartHeight(java.lang.String chartHeight){this.chartHeight = chartHeight;}  public java.lang.String getChartWidth(){return this.chartWidth;}  public void setChartWidth(java.lang.String chartWidth){this.chartWidth = chartWidth;}  public java.lang.String getChartType(){return this.chartType;}  public void setChartType(java.lang.String chartType){this.chartType = chartType;}  public java.lang.String getChartSubtype(){return this.chartSubtype;}  public void setChartSubtype(java.lang.String chartSubtype){this.chartSubtype = chartSubtype;}  public java.lang.String getDataRangeColumn(){return this.dataRangeColumn;}  public void setDataRangeColumn(java.lang.String dataRangeColumn){this.dataRangeColumn = dataRangeColumn;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanChartGraphic(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.ChartGraphic();}
  protected void selectList() throws Exception {this.list = reports.ChartGraphic.findAll();}
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.ChartGraphic.findByID(new java.math.BigDecimal(idValues[0]));}
}





/************************************************************/
// Hwork, 2005
/************************************************************/
