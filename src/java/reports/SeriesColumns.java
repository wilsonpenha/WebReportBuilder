/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 9/16/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class SeriesColumns extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal seriesColumnsId;
  private java.math.BigDecimal chartGraphicId;
  private java.lang.String columnName;
  private java.lang.String columnType;
  private java.math.BigDecimal order;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public SeriesColumns() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access seriesColumnsId: Series Columns Id
  */
  public java.math.BigDecimal getSeriesColumnsId(){return this.seriesColumnsId;}
  /**
  * Access chartGraphicId: Chart Graphic id
  */
  public java.math.BigDecimal getChartGraphicId(){return this.chartGraphicId;}
  public java.lang.String getColumnName() {return columnName;}
  /**
  * Access columnType: Column Type
  */
  public java.lang.String getColumnType(){return this.columnType;}
  /**
  * Access order: Order
  */
  public java.math.BigDecimal getOrder(){return this.order;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change seriesColumnsId: Series Columns Id
  */
  public void setSeriesColumnsId(java.math.BigDecimal seriesColumnsId){this.seriesColumnsId = seriesColumnsId;}
  /**
  * Change chartGraphicId: Chart Graphic id
  */
  public void setChartGraphicId(java.math.BigDecimal chartGraphicId){this.chartGraphicId = chartGraphicId;}
  public void setColumnName(java.lang.String string) {columnName = string;}
  /**
  * Change columnType: Column Type
  */
  public void setColumnType(java.lang.String columnType){this.columnType = columnType;}
  /**
  * Change order: Order
  */
  public void setOrder(java.math.BigDecimal order){this.order = order;}


  /*-------------------------------------------------------------------------
                      Navigation Methods
  -------------------------------------------------------------------------*/

  public ChartGraphic getChartGraphic() throws Exception{if ((this.getChartGraphicId()==null)) return null; return ChartGraphic.findByID(this.getChartGraphicId());}
  public void setChartGraphic(ChartGraphic obj) throws Exception{this.setChartGraphicId(obj!=null ? obj.getChartGraphicId() : null);}

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
  public static SeriesColumns findByID(
        java.math.BigDecimal seriesColumnsId)
  throws Exception{
    SeriesColumns obj = new SeriesColumns();
    obj.setSeriesColumnsId(seriesColumnsId);
    return (SeriesColumns) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = SeriesColumns.recuperaProximoID(this, transID);
    this.setSeriesColumnsId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    SeriesColumns obj = new SeriesColumns();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with ChartGraphic provide  */  public static Vector findByChartGraphic(ChartGraphic object)  throws Exception{    SeriesColumns obj = new SeriesColumns();    obj.setChartGraphic(object);    return getList(obj, null);  }


}


/************************************************************/
// Hwork, 2005
/************************************************************/
