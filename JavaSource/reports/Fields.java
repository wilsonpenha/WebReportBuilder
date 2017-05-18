/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Fields extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal fiedlsId;
  private java.math.BigDecimal reportId;
  private java.lang.String fieldName;
  private java.lang.String fieldDescription;
  private java.lang.String classType;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public Fields() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access fiedlsId: Fiedls Id
  */
  public java.math.BigDecimal getFiedlsId(){return this.fiedlsId;}
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access fieldName: Field Name
  */
  public java.lang.String getFieldName(){return this.fieldName;}
  /**
  * Access fieldDescription: Field Description
  */
  public java.lang.String getFieldDescription(){return this.fieldDescription;}
  /**
  * Access classType: Class Type
  */
  public java.lang.String getClassType(){return this.classType;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change fiedlsId: Fiedls Id
  */
  public void setFiedlsId(java.math.BigDecimal fiedlsId){this.fiedlsId = fiedlsId;}
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change fieldName: Field Name
  */
  public void setFieldName(java.lang.String fieldName){this.fieldName = fieldName;}
  /**
  * Change fieldDescription: Field Description
  */
  public void setFieldDescription(java.lang.String fieldDescription){this.fieldDescription = fieldDescription;}
  /**
  * Change classType: Class Type
  */
  public void setClassType(java.lang.String classType){this.classType = classType;}


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
  public static Fields findByID(
        java.math.BigDecimal fiedlsId)
  throws Exception{
    Fields obj = new Fields();
    obj.setFiedlsId(fiedlsId);
    return (Fields) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = Fields.recuperaProximoID(this, transID);
    this.setFiedlsId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    Fields obj = new Fields();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Report provide  */  public static Vector findByReport(Report object)  throws Exception{    Fields obj = new Fields();    obj.setReport(object);    return getList(obj, null);  }

}


/************************************************************/
// Hwork, 2005
/************************************************************/
