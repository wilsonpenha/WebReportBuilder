package reports.bean;
import java.sql.*;
import java.util.*;
import java.io.*;

import javax.sql.DataSource;

import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.persistencia.exception.ServiceLocatorException;

import reports.Report;

public class BeanQuery {
	//Atributos públicos
	private String jndiName = "";
	private String contextType = "";
	private String sql = "";
	private ResultSet retorno;
	private String errorMessage = "";
	private String fileConf = "";

	//Atributos privados
	private Connection connection;
	private Statement SQLQuery;

	private Main beanMain = new Main();

	//Métodos Set
	public void setJndiName(String jndiName) {this.jndiName = jndiName;}
	public void setContextType(String contextType) {this.contextType = contextType;}
	public void setSql(String sql) {this.sql = sql;}
	//Métodos Get
	public String getJndiName() {return this.jndiName;}
	public String getContextType() {return this.contextType;}
	public String getSql() {return this.sql;}
	public String getErrorMessage() {return this.errorMessage;}
	public ResultSet getRetorno() {return this.retorno;}
	
	public Vector getQueryMetaCols() throws Exception{
		Vector vObj = new Vector();
		if (getRetorno()!=null){
			for (int i=1;i<=getRetorno().getMetaData().getColumnCount();i++){
				QueryMetaCols queryMetaCols = new QueryMetaCols();
				queryMetaCols.setFieldName(getRetorno().getMetaData().getColumnName(i));  
				queryMetaCols.setFieldComment(getRetorno().getMetaData().getColumnLabel(i));  
				queryMetaCols.setFieldType(getRetorno().getMetaData().getColumnClassName(i));  
				vObj.add(queryMetaCols);
			}
		}

		return vObj;
	}

	public class QueryMetaCols{
		private String fieldName;
		private String fieldComment;
		private String fieldType;
		
		public void setFieldName(String fieldName){this.fieldName=fieldName;}
		public void setFieldComment(String fieldComment){this.fieldComment=fieldComment;}
		public void setFieldType(String fieldType){this.fieldType=fieldType;}
		public String getFieldName(){return this.fieldName;}
		public String getFieldComment(){return this.fieldComment;}
		public String getFieldType(){return this.fieldType;}
		
		public QueryMetaCols(){
		}

		public QueryMetaCols(String fieldName, String fieldComment, String fieldType){
			this.fieldName=fieldName;
			this.fieldComment=fieldComment;
			this.fieldType=fieldType;
		}
	}
	//Métodos para conexão, desconexão e acesso a dados

	public boolean open() {
		try {
			if (!this.sql.equals("")){
				this.connection = getConnection();
				this.SQLQuery = connection.createStatement();
				if (this.sql.toUpperCase().indexOf("UPDATE") > -1
					|| this.sql.toUpperCase().indexOf("DELETE") > -1
					|| this.sql.toUpperCase().indexOf("INSERT") > -1
					|| this.sql.toUpperCase().indexOf("CREATE") > -1
					|| this.sql.toUpperCase().indexOf("DROP") > -1) {
					//SQLQuery.executeUpdate(this.sql);
					System.out.println("JQuery: Executando Query:");
					System.out.println(this.getSql());
					System.out.println("");
					return false;
				} else {
					this.retorno = SQLQuery.executeQuery(this.sql);
				}
	
				System.out.println("JQuery: Executando Query:");
				System.out.println(this.getSql());
				System.out.println("");
			}

			return true;
		} catch (SQLException e) {
			this.errorMessage = "JQuery: Statment error : " + e.getMessage();
			return false;
		} catch (Exception e) {
			this.errorMessage = e.getMessage();
			System.out.println("JQuery: ClassNotFoundException: " + e.getMessage());
			System.out.println("");
			return false;
		}
	}
	private Connection getConnection() {
 
		DataSource ds;
		Connection conn = null;

		System.out.println("JNDI Name : "+this.getJndiName());
		System.out.println("Context Type : "+this.getContextType());
		try {
			ds = ServiceLocator.getInstance(this.getContextType()).getDataSource(this.getJndiName());
			conn = ds.getConnection();
		} catch (ServiceLocatorException e) {
			this.errorMessage = "JQuery: JNDI error : " + e.getMessage();
//			e.printStackTrace();
		} catch (SQLException e) {
			this.errorMessage = "JQuery: SQL error : " + e.getMessage();
//			e.printStackTrace();
		}
		return conn;
	}
}