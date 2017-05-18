/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/30/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Parameters extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal parametersId;
  private java.math.BigDecimal reportId;
  private java.lang.String parameterName;
  private java.lang.String classType;
  private java.lang.String parameterDescription;
  private java.lang.String isRequired;
  private java.lang.String inputType;
  private java.lang.String tableInput;
  private java.lang.String fieldKey;
  private java.lang.String fieldDisplay;
  private java.lang.String sql;
  private java.lang.String radioOptions;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public Parameters() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access parametersId: Parameters Id
  */
  public java.math.BigDecimal getParametersId(){return this.parametersId;}
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access prameterName: Prameter Name
  */
  public java.lang.String getParameterName(){return this.parameterName;}
  /**
  * Access classType: Class Type
  */
  public java.lang.String getClassType(){return this.classType;}
  /**
  * Access parameterDescription: Parameter Description
  */
  public java.lang.String getParameterDescription(){return this.parameterDescription;}
  public java.lang.String getIsRequired(){return this.isRequired;}
  public java.lang.String getInputType(){return this.inputType;}
  public java.lang.String getTableInput(){return this.tableInput;}
  public java.lang.String getFieldKey(){return this.fieldKey;}
  public java.lang.String getFieldDisplay(){return this.fieldDisplay;}
  public java.lang.String getSql() {return sql;}
  public java.lang.String getRadioOptions() {return radioOptions;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change parametersId: Parameters Id
  */
  public void setParametersId(java.math.BigDecimal parametersId){this.parametersId = parametersId;}
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change prameterName: Prameter Name
  */
  public void setParameterName(java.lang.String prameterName){this.parameterName = prameterName;}
  /**
  * Change classType: Class Type
  */
  public void setClassType(java.lang.String classType){this.classType = classType;}
  /**
  * Change parameterDescription: Parameter Description
  */
  public void setParameterDescription(java.lang.String parameterDescription){this.parameterDescription = parameterDescription;}
  public void setIsRequired(java.lang.String isRequired){this.isRequired = isRequired;}
  public void setInputType(java.lang.String inputType){this.inputType = inputType;}
  public void setTableInput(java.lang.String tableInput){this.tableInput = tableInput;}
  public void setFieldKey(java.lang.String fieldKey){this.fieldKey = fieldKey;}
  public void setFieldDisplay(java.lang.String fieldDisplay){this.fieldDisplay = fieldDisplay;}
  public void setSql(java.lang.String string) {sql = string;}  
  public void setRadioOptions(java.lang.String string) {radioOptions = string;}  
 

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
  public static Parameters findByID(
        java.math.BigDecimal parametersId)
  throws Exception{
    Parameters obj = new Parameters();
    obj.setParametersId(parametersId);
    return (Parameters) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = Parameters.recuperaProximoID(this, transID);
    this.setParametersId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    Parameters obj = new Parameters();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Report provide  */  public static Vector findByReport(Report object)  throws Exception{    Parameters obj = new Parameters();    obj.setReport(object);    return getList(obj, null);  }
}


/************************************************************/
// Hwork, 2005
/************************************************************/
