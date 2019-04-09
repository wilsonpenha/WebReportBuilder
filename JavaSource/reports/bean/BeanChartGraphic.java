/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 9/16/2005
/************************************************************/
package reports.bean;

import java.sql.Timestamp;
import java.util.Date;
import br.com.hwork.persistencia.PObject;
import reports.bean.RptBeanObject;
import br.com.hwork.text.Formatter;

import reports.ChartGraphic;

public class BeanChartGraphic extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String chartGraphicId = "";

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getChartGraphicId(){return this.chartGraphicId;}

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanChartGraphic(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.ChartGraphic();}
  protected void selectList() throws Exception {this.list = reports.ChartGraphic.findAll();}
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.ChartGraphic.findByID(new java.math.BigDecimal(idValues[0]));}
}





/************************************************************/
// Hwork, 2005
/************************************************************/