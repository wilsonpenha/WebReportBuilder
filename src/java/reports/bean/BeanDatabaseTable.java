/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 7/13/2005
/************************************************************/
package reports.bean;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import br.com.hwork.persistencia.PObject;
import br.com.hwork.text.Formatter;

import reports.DatabaseTable;
import reports.Report;
import reports.Table;

public class BeanDatabaseTable extends RptBeanObject{

  /*-------------------------------------------------------------------------
                      Support Attributes
  -------------------------------------------------------------------------*/
  private java.lang.String tableId = "";  private java.lang.String tableName = "";  private java.lang.String schemaName = "";
  private java.lang.String reportId = "";
  

  /*-------------------------------------------------------------------------
                      Access Methods
  -------------------------------------------------------------------------*/
  public java.lang.String getTableId(){return this.tableId;}  public void setTableId(java.lang.String tableId){this.tableId = tableId;}  public java.lang.String getTableName(){return this.tableName;}  public void setTableName(java.lang.String tableName){this.tableName = tableName.toUpperCase();}  public java.lang.String getSchemaName(){return this.schemaName;}  public void setSchemaName(java.lang.String schemaName){this.schemaName = schemaName.toUpperCase();}  public java.lang.String getReportId() {return reportId;}
  public void setReportId(java.lang.String string) {reportId = string;}


 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanDatabaseTable(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.DatabaseTable();}
  protected void selectList() throws Exception {this.list = reports.DatabaseTable.findAll();}
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.DatabaseTable.findByID(new java.math.BigDecimal(idValues[0]));}

  public Vector getTables() throws Exception{
	  Report report = Report.findByID(new BigDecimal(this.reportId));
	  String jndiName = report.getJndiName()!=null ? report.getJndiName():"";
	  String contextType = report.getContextType()!=null ? report.getContextType():"";
	  Connection conn = this.getConnection(contextType, jndiName);
	  DatabaseMetaData databaseMetaData = conn.getMetaData(); 

	  String s = null;
	  String schemaFilter = conn.getMetaData().getUserName().toUpperCase();
	  String tableFilter = null;
	  String[] as = null;
	  String s3 = null;
	  String s4 = null;
	  String s5 = null;
	  String s6 = null;
		
//	  if(databaseMetaData.supportsCatalogsInTableDefinitions() && !s.equals(conn.getCatalog()) && !s.equals(""))
//		  conn.setCatalog(s);
	  if (conn.getMetaData().getDriverName().equals("HSQL Database Engine Driver"))
		  schemaFilter="PUBLIC";
	  ResultSet resultset = databaseMetaData.getTables(null, schemaFilter, null, null);

	  this.schemaName = schemaFilter;
	  Vector vTables = new Vector();
	  while (resultset.next())
	  {
		  DatabaseTable obj = DatabaseTable.findByName(resultset.getString(3), schemaFilter);
		  if (obj==null){
			Table table = new Table();
			table.setTableName(resultset.getString(3));
			vTables.add(table);
		  }
	  }
	  resultset.close();
	  conn.close();
	  return vTables;
  }

}





/************************************************************/
// Hwork, 2005
/************************************************************/
