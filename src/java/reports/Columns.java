/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Columns extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal columnsId;
  private java.math.BigDecimal reportId;
  private java.lang.String columnName;
  private java.math.BigDecimal labelWidth;
  private java.math.BigDecimal width;
  private java.lang.String alignment;
  private java.lang.String classType;
  private java.math.BigDecimal order;
  private java.lang.String bandType;
  private java.lang.String columnExpression;
  private java.lang.String evaluationTime;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public Columns() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access columnsId: Columns Id
  */
  public java.math.BigDecimal getColumnsId(){return this.columnsId;}
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access columnName: Column Name
  */
  public java.lang.String getColumnName(){return this.columnName;}
  /**
  * Access width: Width
  */
  public java.math.BigDecimal getWidth(){return this.width;}
  public int getIntWidth(){return this.width.intValue();} 
  /**
  * Access alignment: Alignment
  */
  public java.lang.String getAlignment(){return this.alignment;}
  /**
  * Access classType: Class Type
  */
  public java.lang.String getClassType(){return this.classType;}
  public java.math.BigDecimal getOrder(){return this.order;}
  public java.lang.String getBandType(){return this.bandType;}
  /**
  * Access columnExpression: Column Expression
  */
  public java.lang.String getColumnExpression(){return this.columnExpression;}
  public java.math.BigDecimal getLabelWidth() {return labelWidth;}
  public int getIntLabelWidth(){return this.labelWidth.intValue();} 
  public java.lang.String getEvaluationTime() {return evaluationTime;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change columnsId: Columns Id
  */
  public void setColumnsId(java.math.BigDecimal columnsId){this.columnsId = columnsId;}
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change columnName: Column Name
  */
  public void setColumnName(java.lang.String columnName){this.columnName = columnName;}
  /**
  * Change width: Width
  */
  public void setWidth(java.math.BigDecimal width){this.width = width;}
  /**
  * Change alignment: Alignment
  */
  public void setAlignment(java.lang.String alignment){this.alignment = alignment;}
  /**
  * Change classType: Class Type
  */
  public void setClassType(java.lang.String classType){this.classType = classType;}
  public void setOrder(java.math.BigDecimal order){this.order = order;}
  public void setBandType(java.lang.String bandType){this.bandType = bandType;}
  /**
  * Change columnExpression: Column Expression
  */
  public void setColumnExpression(java.lang.String columnExpression){this.columnExpression = columnExpression;}
  public void setLabelWidth(java.math.BigDecimal decimal) {labelWidth = decimal;}
  public void setEvaluationTime(java.lang.String string) {evaluationTime = string;}

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
  public static Columns findByID(
        java.math.BigDecimal columnsId)
  throws Exception{
    Columns obj = new Columns();
    obj.setColumnsId(columnsId);
    return (Columns) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = Columns.recuperaProximoID(this, transID);
    this.setColumnsId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    Columns obj = new Columns();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Report provide  */  public static Vector findByReportBandType(Report report, String bandType)  throws Exception{    Columns obj = new Columns();    Vector v = new Vector();
    v.add(report.getReportId());
    v.add(bandType);    return getList("findByReportBandType", v, obj, null);  }

  /**
  * Bring all Obejct for this class 
  * that relationaship with Report provide
  */
  public static Vector findByReport(Report report)
  throws Exception{
	Columns obj = new Columns();
	obj.setReport(report);
	return getList(obj, null);
  }
}


/************************************************************/
// Hwork, 2005
/************************************************************/
