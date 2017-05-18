/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Variables extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal variablesId;
  private java.math.BigDecimal reportId;
  private java.lang.String variableName;
  private java.lang.String variableDescription;
  private java.lang.String classType;
  private java.lang.String resetType;
  private java.lang.String incrementType;
  private java.math.BigDecimal resetGroupId;
  private java.math.BigDecimal incrementGroupId;
  private java.lang.String calculation;
  private java.lang.String variableExpression;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public Variables() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access variablesId: Variables Id
  */
  public java.math.BigDecimal getVariablesId(){return this.variablesId;}
  /**
  * Access reportId: Report Id
  */
  public java.math.BigDecimal getReportId(){return this.reportId;}
  /**
  * Access variableName: Variable Name
  */
  public java.lang.String getVariableName(){return this.variableName;}
  /**
  * Access variableDescription: Variable Description
  */
  public java.lang.String getVariableDescription(){return this.variableDescription;}
  /**
  * Access classType: Class Type
  */
  public java.lang.String getClassType(){return this.classType;}
  /**
  * Access resetType: Reset Type
  */
  public java.lang.String getResetType(){return this.resetType;}
  public java.lang.String getIncrementType(){return this.incrementType;}
  /**
  * Access resetGroupId: Reset Group Id
  */
  public java.math.BigDecimal getResetGroupId(){return this.resetGroupId;}
  /**
  * Access incrementGroupId: Increment Group Id
  */
  public java.math.BigDecimal getIncrementGroupId(){return this.incrementGroupId;}
  /**
  * Access calculation: Calculation
  */
  public java.lang.String getCalculation(){return this.calculation;}
  /**
  * Access variableExpression: Variable Expression
  */
  public java.lang.String getVariableExpression(){return this.variableExpression;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change variablesId: Variables Id
  */
  public void setVariablesId(java.math.BigDecimal variablesId){this.variablesId = variablesId;}
  /**
  * Change reportId: Report Id
  */
  public void setReportId(java.math.BigDecimal reportId){this.reportId = reportId;}
  /**
  * Change variableName: Variable Name
  */
  public void setVariableName(java.lang.String variableName){this.variableName = variableName;}
  /**
  * Change variableDescription: Variable Description
  */
  public void setVariableDescription(java.lang.String variableDescription){this.variableDescription = variableDescription;}
  /**
  * Change classType: Class Type
  */
  public void setClassType(java.lang.String classType){this.classType = classType;}
  /**
  * Change resetType: Reset Type
  */
  public void setResetType(java.lang.String resetType){this.resetType = resetType;}
  public void setIncrementType(java.lang.String incrementType){this.incrementType = incrementType;}
  /**
  * Change resetGroupId: Reset Group Id
  */
  public void setResetGroupId(java.math.BigDecimal resetGroupId){this.resetGroupId = resetGroupId;}
  /**
  * Change incrementGroupId: Increment Group Id
  */
  public void setIncrementGroupId(java.math.BigDecimal incrementGroupId){this.incrementGroupId = incrementGroupId;}
  /**
  * Change calculation: Calculation
  */
  public void setCalculation(java.lang.String calculation){this.calculation = calculation;}
  /**
  * Change variableExpression: Variable Expression
  */
  public void setVariableExpression(java.lang.String variableExpression){this.variableExpression = variableExpression;}


  /*-------------------------------------------------------------------------
                      Navigation Methods
  -------------------------------------------------------------------------*/

  public Groups getIncrementGroups() throws Exception{if ((this.getIncrementGroupId()==null)) return null; return Groups.findByID(this.getIncrementGroupId());}
  public void setIncrementGroups(Groups obj) throws Exception{this.setIncrementGroupId(obj!=null ? obj.getGroupsId() : null);}
  public Groups getResetGroups() throws Exception{if ((this.getResetGroupId()==null)) return null; return Groups.findByID(this.getResetGroupId());}
  public void setResetGroups(Groups obj) throws Exception{this.setResetGroupId(obj!=null ? obj.getGroupsId() : null);}
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
  public static Variables findByID(
        java.math.BigDecimal variablesId)
  throws Exception{
    Variables obj = new Variables();
    obj.setVariablesId(variablesId);
    return (Variables) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = Variables.recuperaProximoID(this, transID);
    this.setVariablesId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    Variables obj = new Variables();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Groups provide  */  public static Vector findByGroupsIncrementGroups(Groups object)  throws Exception{    Variables obj = new Variables();    obj.setIncrementGroups(object);    return getList(obj, null);  }

  /**  * Bring all Obejct for this class   * that relationaship with Groups provide  */  public static Vector findByGroupsResetGroups(Groups object)  throws Exception{    Variables obj = new Variables();    obj.setResetGroups(object);    return getList(obj, null);  }

  /**  * Bring all Obejct for this class   * that relationaship with Report provide  */  public static Vector findByReport(Report object)  throws Exception{    Variables obj = new Variables();    obj.setReport(object);    return getList(obj, null);  }

}


/************************************************************/
// Hwork, 2005
/************************************************************/
