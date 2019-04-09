/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 9/16/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class ChartGraphic extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal chartGraphicId;
  private java.lang.String chartName;
  private java.math.BigDecimal reportId;
  private java.lang.String xLabel;
  private java.lang.String yLabel;
  private java.lang.String legendLocation;
  private java.math.BigDecimal chartHeight;
  private java.math.BigDecimal chartWidth;
  private java.lang.String chartType;
  private java.lang.String chartSubtype;
  private java.lang.String dataRangeColumn;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public ChartGraphic() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access chartGraphicId: Chart Graphic id
  */
  public java.math.BigDecimal getChartGraphicId(){return this.chartGraphicId;}
  public java.lang.String getChartName(){return this.chartName;}
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access xLabel: X Label
  */
  public java.lang.String getXLabel(){return this.xLabel;}
  /**
  * Access yLabel: Y Label
  */
  public java.lang.String getYLabel(){return this.yLabel;}
  /**
  * Access legendLocation: Legend Location
  */
  public java.lang.String getLegendLocation(){return this.legendLocation;}
  /**
  * Access chartHeight: Chart Height
  */
  public java.math.BigDecimal getChartHeight(){return this.chartHeight;}
  /**
  * Access chartWidth: Chart Width
  */
  public java.math.BigDecimal getChartWidth(){return this.chartWidth;}
  /**
  * Access chartType: Chart Type
  */
  public java.lang.String getChartType(){return this.chartType;}
  /**
  * Access chartSubtype: Chart SubType
  */
  public java.lang.String getChartSubtype(){return this.chartSubtype;}
  /**
  * Access dataRangeColumn: Data Range Column
  */
  public java.lang.String getDataRangeColumn(){return this.dataRangeColumn;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change chartGraphicId: Chart Graphic id
  */
  public void setChartGraphicId(java.math.BigDecimal chartGraphicId){this.chartGraphicId = chartGraphicId;}
  public void setChartName(java.lang.String chartName){this.chartName = chartName;}
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change xLabel: X Label
  */
  public void setXLabel(java.lang.String xLabel){this.xLabel = xLabel;}
  /**
  * Change yLabel: Y Label
  */
  public void setYLabel(java.lang.String yLabel){this.yLabel = yLabel;}
  /**
  * Change legendLocation: Legend Location
  */
  public void setLegendLocation(java.lang.String legendLocation){this.legendLocation = legendLocation;}
  /**
  * Change chartHeight: Chart Height
  */
  public void setChartHeight(java.math.BigDecimal chartHeight){this.chartHeight = chartHeight;}
  /**
  * Change chartWidth: Chart Width
  */
  public void setChartWidth(java.math.BigDecimal chartWidth){this.chartWidth = chartWidth;}
  /**
  * Change chartType: Chart Type
  */
  public void setChartType(java.lang.String chartType){this.chartType = chartType;}
  /**
  * Change chartSubtype: Chart SubType
  */
  public void setChartSubtype(java.lang.String chartSubtype){this.chartSubtype = chartSubtype;}
  /**
  * Change dataRangeColumn: Data Range Column
  */
  public void setDataRangeColumn(java.lang.String dataRangeColumn){this.dataRangeColumn = dataRangeColumn;}


  /*-------------------------------------------------------------------------
                      Navigation Methods
  -------------------------------------------------------------------------*/

  public Report getReport() throws Exception{if ((this.getReportId()==null)) return null; return Report.findByID(this.getReportId());}
  public void setReport(Report obj) throws Exception{this.setReportId(obj!=null ? obj.getReportId() : null);}

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
  public static ChartGraphic findByID(
        java.math.BigDecimal chartGraphicId)
  throws Exception{
    ChartGraphic obj = new ChartGraphic();
    obj.setChartGraphicId(chartGraphicId);
    return (ChartGraphic) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = ChartGraphic.recuperaProximoID(this, transID);
    this.setChartGraphicId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    ChartGraphic obj = new ChartGraphic();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Report provide  */  public static Vector findByReport(Report object)  throws Exception{    ChartGraphic obj = new ChartGraphic();    obj.setReport(object);    return getList(obj, null);  }

}


/************************************************************/
// Hwork, 2005
/************************************************************/
