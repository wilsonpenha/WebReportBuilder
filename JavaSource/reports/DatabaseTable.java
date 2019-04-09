/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 7/13/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class DatabaseTable extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal tableId;
  private java.lang.String tableName;
  private java.lang.String schemaName;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public DatabaseTable() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access tableId: Table ID
  */
  public java.math.BigDecimal getTableId(){return this.tableId;}
  /**
  * Access tableName: Table Name
  */
  public java.lang.String getTableName(){return this.tableName;}
  /**
  * Access schemaName: Schema Name
  */
  public java.lang.String getSchemaName(){return this.schemaName;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change tableId: Table ID
  */
  public void setTableId(java.math.BigDecimal tableId){this.tableId = tableId;}
  /**
  * Change tableName: Table Name
  */
  public void setTableName(java.lang.String tableName){this.tableName = tableName;}
  /**
  * Change schemaName: Schema Name
  */
  public void setSchemaName(java.lang.String schemaName){this.schemaName = schemaName;}


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
  public static DatabaseTable findByID(
        java.math.BigDecimal tableId)
  throws Exception{
    DatabaseTable obj = new DatabaseTable();
    obj.setTableId(tableId);
    return (DatabaseTable) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = DatabaseTable.recuperaProximoID(this, transID);
    this.setTableId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    DatabaseTable obj = new DatabaseTable();
    return getList(obj, null);
  }

  /**
  * Bring all Obejct for this class
  */
  public static DatabaseTable findByName(String tableName, String schemaName)
  throws Exception{
	DatabaseTable obj = new DatabaseTable();
	obj.setTableName(tableName);
	obj.setSchemaName(schemaName);
	return (DatabaseTable)getObject(obj, null);
  }
}


/************************************************************/
// Hwork, 2005
/************************************************************/
