/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 6/21/2005
/************************************************************/
package reports.bean;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.DataSource;

import reports.QueryMetaCols;
import reports.Report;
import reports.Table;
import reports.TableColumn;
import br.com.hwork.persistencia.PObject;
import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.persistencia.exception.ServiceLocatorException;

public class BeanParameters extends RptBeanObject{

	/*-------------------------------------------------------------------------
						Support Attributes
	-------------------------------------------------------------------------*/
	private java.lang.String parametersId = "";
	private java.lang.String reportId = "";
	private java.lang.String parameterName = "";
	private java.lang.String classType = "";
	private java.lang.String parameterDescription = "";
	private java.lang.String isRequired = "";
	private java.lang.String inputType = "";
	private java.lang.String tableInput = "";
	private java.lang.String fieldKey = "";
	private java.lang.String fieldDisplay = ""; 
	private java.lang.String sql = "";
	private java.lang.String radioOptions = "";

	private String jndiName = "";
	private String contextType = "";
	private String sqlStr = "";
	private ResultSet retorno;
	private String executeQuery = "";
	private String viewData = "";
	private String createColumns = "";
	private String maxRows = "50";
	private String[] titles = new String[0];
	private String errorMsg = "";

	private Connection connection;
	private Statement SQLQuery;

	public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String string) {
		sqlStr = string;
	}
	public String getContextType() {
		return contextType;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setContextType(String string) {
		contextType = string;
	}
	public void setJndiName(String string) {
		jndiName = string;
	}

	/*-------------------------------------------------------------------------
						Access Methods
	-------------------------------------------------------------------------*/
	public java.lang.String getParametersId(){return this.parametersId;}
	public void setParametersId(java.lang.String parametersId){this.parametersId = parametersId;}
	public java.lang.String getReportId(){return this.reportId;}
	public void setReportId(java.lang.String reportId){this.reportId = reportId;}
	public java.lang.String getParameterName(){return this.parameterName;}
	public void setParameterName(java.lang.String parameterName){this.parameterName = parameterName;}
	public java.lang.String getClassType(){return this.classType;}
	public void setClassType(java.lang.String classType){this.classType = classType;}
	public java.lang.String getParameterDescription(){return this.parameterDescription;}
	public void setParameterDescription(java.lang.String parameterDescription){this.parameterDescription = parameterDescription;}
	public java.lang.String getIsRequired(){return this.isRequired;}
	public void setIsRequired(java.lang.String isRequired){this.isRequired = isRequired;}
	public java.lang.String getInputType(){return this.inputType;}
	public void setInputType(java.lang.String inputType){this.inputType = inputType;}
	public java.lang.String getTableInput(){return this.tableInput;}
	public void setTableInput(java.lang.String tableInput){this.tableInput = tableInput;}
	public java.lang.String getFieldKey(){return this.fieldKey;}
	public void setFieldKey(java.lang.String fieldKey){this.fieldKey = fieldKey;}
	public java.lang.String getFieldDisplay(){return this.fieldDisplay;}
	public void setFieldDisplay(java.lang.String fieldDisplay){this.fieldDisplay = fieldDisplay;} 
	public java.lang.String getSql() {return sql;}
	public void setSql(java.lang.String string) {sql = string;}
	public java.lang.String getRadioOptions() {return radioOptions;}
	public void setRadioOptions(java.lang.String string) {radioOptions = string;}
 

 /*-------------------------------------------------------------------------
                      Constructors
  -------------------------------------------------------------------------*/
  public BeanParameters(){super(); this.blockSize = 20;}


 /*-------------------------------------------------------------------------
                      Business process
  -------------------------------------------------------------------------*/
  protected PObject getPObjectInstance(){return new reports.Parameters();}
  protected void selectList() throws Exception {
	Report report = new Report();
	report.setReportId(new BigDecimal(this.getReportId()));
  	this.list = reports.Parameters.findByReport(report);
  }
  protected void selectObject(String[] idValues) throws Exception{this.pobject = reports.Parameters.findByID(new java.math.BigDecimal(idValues[0]));}

  public Vector getColumns(String tableName) throws Exception{
	  Report report = Report.findByID(new BigDecimal(this.reportId));
	  String jndiName = report.getJndiName()!=null ? report.getJndiName():"";
	  String contextType = report.getContextType()!=null ? report.getContextType():"";
	  Connection conn = this.getConnection(contextType, jndiName);
	  DatabaseMetaData databaseMetaData = conn.getMetaData(); 

	  String s = null;
	  String schemaFilter = conn.getMetaData().getUserName().toUpperCase();
	  String tableFilter = tableName;
	  String[] as = null;
	  String s3 = null;
	  String s4 = null;
	  String s5 = null;
	  String s6 = null;
		
	  if(databaseMetaData.supportsCatalogsInTableDefinitions() && !s.equals(conn.getCatalog()) && !s.equals(""))
		  conn.setCatalog(s);
	  ResultSet resultset = databaseMetaData.getColumns(null, schemaFilter, tableFilter, null);

	  Vector vColumns = new Vector();
	  while (resultset.next())
	  {
		  TableColumn tableColumn = new TableColumn();
		  tableColumn.setTableName(tableFilter);
		  tableColumn.setColumnName(resultset.getString(4));
		  vColumns.add(tableColumn);
	  }
	  resultset.close();
	  conn.close();
	  return vColumns;
  }

  public Vector getColumnsFromSQL() throws Exception{
	Vector vObj = new Vector();
	Report report = Report.findByID(new BigDecimal(this.reportId));
	this.setJndiName(report.getJndiName());
	this.setContextType((report.getContextType()==null?"":report.getContextType()));
	
	try {
		if (!this.getSql().equals("")) {
			this.connection = getConnection();
			this.SQLQuery = connection.createStatement();
			if (getSql().toUpperCase().indexOf("UPDATE") > -1
				|| this.getSql().toUpperCase().indexOf("DELETE") > -1
				|| this.getSql().toUpperCase().indexOf("INSERT") > -1
				|| this.getSql().toUpperCase().indexOf("CREATE") > -1
				|| this.getSql().toUpperCase().indexOf("DROP") > -1) {
				//SQLQuery.executeUpdate(this.sql);
				//System.out.println("JQuery: Executando Query: dentro");
				//System.out.println(this.getTableInput());
				//System.out.println("");
			} else {
				this.retorno = SQLQuery.executeQuery(this.getSql());
			}

			//System.out.println("JQuery: Executando Query: fora");
			//System.out.println(this.getTableInput());
			//System.out.println("");
		}
	
		boolean exist = false;
		if (this.retorno != null) {
			for (int i = 1; i <= this.retorno.getMetaData().getColumnCount(); i++) {
				QueryMetaCols queryMetaCols = new QueryMetaCols();
				queryMetaCols.setFieldName(this.retorno.getMetaData().getColumnName(i));
				queryMetaCols.setFieldComment(this.retorno.getMetaData().getColumnLabel(i));

				if (this.retorno.getMetaData().getScale(i)>0 && 
				this.retorno.getMetaData().getColumnClassName(i).equals("java.math.BigDecimal"))
					queryMetaCols.setFieldType("java.lang.Double");
				else
					queryMetaCols.setFieldType(this.retorno.getMetaData().getColumnClassName(i));
				
				for (int k = 0; k < vObj.size(); k++) {
					if (((QueryMetaCols) vObj.elementAt(k)).getFieldName().equals(queryMetaCols.getFieldName())){
						exist = true;
						break;
					}
				}
				if (!exist)
					vObj.add(queryMetaCols);
				else
					exist = false;
			}
		}

		try{
			if (this.connection!=null && !this.connection.isClosed()){
				this.SQLQuery.close();
				this.connection.close();
				//System.out.println("Connection is closed!");
			}
		}catch (SQLException es){}

		return vObj;
	} catch (SQLException e) {
		try{
			if (this.connection!=null && !this.connection.isClosed()){
				this.SQLQuery.close();
				this.connection.close();
				//System.out.println("Connection is closed!");
			}
		}catch (SQLException es){}
		this.errorMsg = "JQuery: Statement error : " + e.getMessage();
	} catch (Exception e) {
		try{
			if (this.connection!=null && !this.connection.isClosed()){
				this.SQLQuery.close();
				this.connection.close();
				//System.out.println("Connection is closed!");
			}
		}catch (SQLException es){}
		this.errorMsg = e.getMessage();
		//System.out.println("JQuery: ClassNotFoundException: " + e.getMessage());
		//System.out.println("");
	}
	return vObj;
  }
	
	private Connection getConnection() {
	
		try{
			if (this.connection!=null && !this.connection.isClosed()){
				//System.out.println("Connection is closed!");
				this.connection.close();
			}
		}catch (SQLException es){}
			
		DataSource ds;
		Connection conn = null;
	
		//System.out.println("JNDI Name : " + this.getJndiName());
		//System.out.println("Context Type : " + this.getContextType());
		try {
			ds = ServiceLocator.getInstance(this.getContextType()).getDataSource(this.getJndiName());
			conn = ds.getConnection();
		} catch (ServiceLocatorException e) {
			this.errorMsg = "JQuery: JNDI error : " + e.getMessage();
			//		  e.printStackTrace();
		} catch (SQLException e) {
			this.errorMsg = "JQuery: SQL error : " + e.getMessage();
			//		  e.printStackTrace();
		}
		return conn;
	}


}





/************************************************************/
// Hwork, 2005
/************************************************************/
