/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/23/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class JavaClass extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String classType;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public JavaClass() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  public java.lang.String getClassType(){return this.classType;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  public void setClassType(java.lang.String classType){this.classType = classType;}


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
  public static JavaClass findByID(
        java.lang.String classType)
  throws Exception{
    JavaClass obj = new JavaClass();
    obj.setClassType(classType);
    return (JavaClass) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    //BigDecimalValue bd = JavaClass.recuperaProximoID(this, transID);
    //this.setClassType(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    JavaClass obj = new JavaClass();
    return getList(obj, null);
  }

}


/************************************************************/
// Hwork, 2005
/************************************************************/
