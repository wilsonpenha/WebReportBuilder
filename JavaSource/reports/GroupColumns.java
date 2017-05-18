/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class GroupColumns extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal groupColumnsId;
  private java.lang.String description;
  private java.math.BigDecimal width;
  private java.lang.String alignment;
  private java.lang.String classType;
  private java.lang.String fieldExpression;
  private java.math.BigDecimal order;
  private java.lang.String bandType;
  private java.math.BigDecimal groupsId;
  private java.lang.String isPrintHeader;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public GroupColumns() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access groupColumnsId: Group Columns Id
  */
  public java.math.BigDecimal getGroupColumnsId(){return this.groupColumnsId;}
  /**
  * Access description: Description
  */
  public java.lang.String getDescription(){return this.description;}
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
  /**
  * Access fieldExpression: Field Expression
  */
  public java.lang.String getFieldExpression(){return this.fieldExpression;}
  public java.math.BigDecimal getOrder(){return this.order;}
  /**
  * Access bandType: Band Type
  */
  public java.lang.String getBandType(){return this.bandType;}
  /**
  * Access groupsId: Groups Id
  */
  public java.math.BigDecimal getGroupsId(){return this.groupsId;}
  public java.lang.String getIsPrintHeader() {return isPrintHeader;}
  
  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change groupColumnsId: Group Columns Id
  */
  public void setGroupColumnsId(java.math.BigDecimal groupColumnsId){this.groupColumnsId = groupColumnsId;}
  /**
  * Change description: Description
  */
  public void setDescription(java.lang.String description){this.description = description;}
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
  /**
  * Change fieldExpression: Field Expression
  */
  public void setFieldExpression(java.lang.String fieldExpression){this.fieldExpression = fieldExpression;}
  public void setOrder(java.math.BigDecimal order){this.order = order;}
  /**
  * Change bandType: Band Type
  */
  public void setBandType(java.lang.String bandType){this.bandType = bandType;}
  /**
  * Change groupsId: Groups Id
  */
  public void setGroupsId(java.math.BigDecimal groupsId){this.groupsId = groupsId;}
  public void setIsPrintHeader(java.lang.String string) {isPrintHeader = string;}


  /*-------------------------------------------------------------------------
                      Navigation Methods
  -------------------------------------------------------------------------*/

  public Groups getGroups() throws Exception{if ((this.getGroupsId()==null)) return null; return Groups.findByID(this.getGroupsId());}
  public void setGroups(Groups obj) throws Exception{this.setGroupsId(obj!=null ? obj.getGroupsId() : null);}

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
  public static GroupColumns findByID(
        java.math.BigDecimal groupColumnsId)
  throws Exception{
    GroupColumns obj = new GroupColumns();
    obj.setGroupColumnsId(groupColumnsId);
    return (GroupColumns) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = GroupColumns.recuperaProximoID(this, transID);
    this.setGroupColumnsId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    GroupColumns obj = new GroupColumns();
    return getList(obj, null);
  }

  /**  * Bring all Obejct for this class   * that relationaship with Groups provide  */  public static Vector findByGroups(Groups object)  throws Exception{    GroupColumns obj = new GroupColumns();    obj.setGroups(object);    return getList(obj, null);  }

  /**
  * Bring all Obejct for this class 
  * that relationaship with Groups provide
  */
  public static Vector findByGroups(Groups object, String transID)
  throws Exception{
	GroupColumns obj = new GroupColumns();
	obj.setGroups(object);
	return getList(obj, transID);
  }
}


/************************************************************/
// Hwork, 2005
/************************************************************/
