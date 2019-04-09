/************************************************************/
//Description: Project pge
//Company....: Bright-Ideas
//Version....: 1.0
//Last change: 3/18/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class MenuControl extends PObject{

  /*-------------------------------------------------------------------------
                      Business Attributes
  -------------------------------------------------------------------------*/
  private java.math.BigDecimal menuControlId;
  private java.lang.String name;
  private java.lang.String description;
  private java.lang.String active;
  private java.lang.String url;
  private java.lang.String category;

  /*-------------------------------------------------------------------------
                      Constructor
  -------------------------------------------------------------------------*/
  public MenuControl() {super();}

  /*-------------------------------------------------------------------------
                      Access Methods GET
  -------------------------------------------------------------------------*/
  /**
  * Access menuControlId: Menu Option
  */
  public java.math.BigDecimal getMenuControlId(){return this.menuControlId;}
  /**
  * Access name: Display Name
  */
  public java.lang.String getName(){return this.name;}
  /**
  * Access description: Description
  */
  public java.lang.String getDescription(){return this.description;}
  /**
  * Access active: Is Active?
  */
  public java.lang.String getActive(){return this.active;}
  /**
  * Access url: URL
  */
  public java.lang.String getUrl(){return this.url;}
  /**
  * Access category: CATEGORY
  */
  public java.lang.String getCategory(){return this.category;}

  /*-------------------------------------------------------------------------
                      Access Methods SET
  -------------------------------------------------------------------------*/
  /**
  * Change menuControlId: Menu Option
  */
  public void setMenuControlId(java.math.BigDecimal menuControlId){this.menuControlId = menuControlId;}
  /**
  * Change name: Display Name
  */
  public void setName(java.lang.String name){this.name = name;}
  /**
  * Change description: Description
  */
  public void setDescription(java.lang.String description){this.description = description;}
  /**
  * Change active: Is Active?
  */
  public void setActive(java.lang.String active){this.active = active;}
  /**
  * Change url: URL
  */
  public void setUrl(java.lang.String url){this.url = url;}
  /**
  * Change category: Category
  */
  public void setCategory(java.lang.String category){this.category = category;}


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
  public static MenuControl findByID(
        java.math.BigDecimal menuControlId)
  throws Exception{
    MenuControl obj = new MenuControl();
    obj.setMenuControlId(menuControlId);
    return (MenuControl) getObject(obj, null);
  }

  /**
  * Create the next ID
  */
  protected void beforeInsert(String transID)
  throws Exception{
    BigDecimalValue bd = MenuControl.recuperaProximoID(this, transID);
    this.setMenuControlId(bd.getValue());
  }
  

  /**
  * Bring all Obejct for this class
  */
  public static Vector findAll()
  throws Exception{
    MenuControl obj = new MenuControl();
    return getList(obj, null);
  }
  
  public static Vector findIdGreaterThanZero()
   throws Exception{
	 MenuControl obj = new MenuControl();
	 Vector v = new Vector();
	 return getList("findIdGreaterThanZero", v, obj, null);
   }

}


/************************************************************/
// Bright-Ideas, 2005
/************************************************************/
