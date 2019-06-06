/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 6/21/2005
/************************************************************/
package reports.bean;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.DataSource;

import reports.Columns;
import reports.Fields;
import reports.Report;
import br.com.hwork.persistencia.PCol;
import br.com.hwork.persistencia.PObject;
import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.persistencia.SessionObject;
import br.com.hwork.persistencia.exception.ServiceLocatorException;
import br.com.hwork.servlet.PropertiesManager;

public class BeanFields extends RptBeanObject {

	/*-------------------------------------------------------------------------
	                    Support Attributes
	-------------------------------------------------------------------------*/
	private java.lang.String fiedlsId = "";
	private java.lang.String reportId = "";
	private java.lang.String fieldName = "";
	private java.lang.String fieldDescription = "";
	private java.lang.String classType = "";

	private String jndiName = "";
	private String contextType = "";
	private String sql = "";
	private ResultSet retorno;
	private String executeQuery = "";
	private String viewData = "";
	private String createColumns = "";
	private String maxRows = "50";
	private String[] titles = new String[0];
	private String errorMsg = "";

	private Connection connection;
	private Statement SQLQuery;

	public void setCreateColumns(String string) {
		createColumns = string;
	}

	public void setErrorMsg(String string) {
		errorMsg = string;
	}

	public void setMaxRows(String maxRows) {
		this.maxRows = maxRows;
	}

	public void setViewData(String viewData) {
		this.viewData = viewData;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public void setContextType(String contextType) {
		this.contextType = contextType;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public void setExecuteQuery(String string) {
		executeQuery = string;
	}

	public String getCreateColumns() {
		return createColumns;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public String getExecuteQuery() {
		return executeQuery;
	}

	public String getMaxRows() {
		return this.maxRows;
	}

	public String getViewData() {
		return this.viewData;
	}

	public String getJndiName() {
		return this.jndiName;
	}

	public String getContextType() {
		return this.contextType;
	}

	public String getSql() {
		return this.sql;
	}

	public Connection getOpenConn() {
		return this.connection;
	}

	/*-------------------------------------------------------------------------
	                    Access Methods
	-------------------------------------------------------------------------*/
	public java.lang.String getFiedlsId() {
		return this.fiedlsId;
	}

	public void setFiedlsId(java.lang.String fiedlsId) {
		this.fiedlsId = fiedlsId;
	}

	public java.lang.String getReportId() {
		return this.reportId;
	}

	public void setReportId(java.lang.String reportId) {
		this.reportId = reportId;
	}

	public java.lang.String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(java.lang.String fieldName) {
		this.fieldName = fieldName;
	}

	public java.lang.String getFieldDescription() {
		return this.fieldDescription;
	}

	public void setFieldDescription(java.lang.String fieldDescription) {
		this.fieldDescription = fieldDescription;
	}

	public java.lang.String getClassType() {
		return this.classType;
	}

	public void setClassType(java.lang.String classType) {
		this.classType = classType;
	}

	/*-------------------------------------------------------------------------
	                     Constructors
	 -------------------------------------------------------------------------*/
	public BeanFields() {
		super();
		this.blockSize = 20;
	}

	/*-------------------------------------------------------------------------
	                     Business process
	 -------------------------------------------------------------------------*/
	protected PObject getPObjectInstance() {
		return new reports.Fields();
	}

	protected void selectList() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.getReportId()));
		this.list = reports.Fields.findByReport(report);
	}

	protected void selectObject(String[] idValues) throws Exception {
		this.pobject = reports.Fields.findByID(new java.math.BigDecimal(idValues[0]));
	}

	public ResultSet getRetorno() {
		return this.retorno;
	}

	public void execute() {
		if (this.dbAction == ACTION_FORM_INSERT) {
			try {
				Report report = Report.findByID(new BigDecimal(this.getReportId()));
				if (!this.executeQuery.equals("Yes")) {
					this.sql = report.getSql();
					this.viewData = "Yes";
				}
				this.jndiName = report.getJndiName();
				this.contextType = report.getContextType() == null ? "" : report.getContextType();
			} catch (Exception e) {
				this.errorMsg = "Erro on try to get some values from Report object!";
				this.dbAction = ACTION_SELECT_LIST;
			}
			this.open();
		}
		super.execute();
	}

	public void insert() throws Exception {
		Report report = Report.findByID(new BigDecimal(this.reportId));
		report.setSql(this.sql);
		FieldsInsertCtrl fieldsInsertCtrl = new FieldsInsertCtrl(report, this.objIds, this.createColumns);
		fieldsInsertCtrl.execute();
	}

	public Vector getQueryMetaCols() throws Exception {
		Vector vObj = new Vector();
		boolean exist = false;
		if (getRetorno() != null) {
			for (int i = 1; i <= getRetorno().getMetaData().getColumnCount(); i++) {
				QueryMetaCols queryMetaCols = new QueryMetaCols();
				queryMetaCols.setFieldName(getRetorno().getMetaData().getColumnName(i));
				queryMetaCols.setFieldComment(getRetorno().getMetaData().getColumnLabel(i));
				// queryMetaCols.setFieldComment("Prec :
				// "+getRetorno().getMetaData().getPrecision(i)+" -
				// "+getRetorno().getMetaData().getScale(i));

				if (getRetorno().getMetaData().getColumnClassName(i).equals("java.math.BigDecimal"))
					queryMetaCols.setFieldType("java.lang.Double");
				else
					queryMetaCols.setFieldType(getRetorno().getMetaData().getColumnClassName(i));

				for (int k = 0; k < vObj.size(); k++) {
					if (((QueryMetaCols) vObj.elementAt(k)).getFieldName().equals(queryMetaCols.getFieldName())) {
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

		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.SQLQuery.close();
				this.connection.close();
			}
		} catch (SQLException es) {
		}

		return vObj;
	}

	public class QueryMetaCols {
		private String fieldName;
		private String fieldComment;
		private String fieldType;

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public void setFieldComment(String fieldComment) {
			this.fieldComment = fieldComment;
		}

		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}

		public String getFieldName() {
			return this.fieldName;
		}

		public String getFieldComment() {
			return this.fieldComment;
		}

		public String getFieldType() {
			return this.fieldType;
		}

		public QueryMetaCols() {
		}
	}

	public class QueryData {
		private String fieldName;
		private String fieldComment;
		private String fieldType;

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public void setFieldComment(String fieldComment) {
			this.fieldComment = fieldComment;
		}

		public void setFieldType(String fieldType) {
			this.fieldType = fieldType;
		}

		public String getFieldName() {
			return this.fieldName;
		}

		public String getFieldComment() {
			return this.fieldComment;
		}

		public String getFieldType() {
			return this.fieldType;
		}

		public QueryData() {
		}
	}

	public class FieldsInsertCtrl extends SessionObject {

		private Report report;
		private String[] fields;
		private String createColumns;

		public FieldsInsertCtrl(Report report, String[] fields, String createColumns) {
			this.report = report;
			this.fields = fields;
			this.createColumns = createColumns;
		}

		protected void executeTransaction() throws Exception {
			Vector vFields = Fields.findByReport(report);
			Vector vColumns = Columns.findByReportBandType(report, "Detail");

			for (int i = 0; i < vFields.size(); i++) {
				((Fields) vFields.elementAt(i)).delete(transID);
			}

			if (this.createColumns.equals("Yes")) {
				for (int i = 0; i < vColumns.size(); i++) {
					((Columns) vColumns.elementAt(i)).delete(transID);
				}
				for (int i = 1; i < fields.length; i++) {
					Columns columnsObj = new Columns();
					columnsObj.setReport(this.report);
					columnsObj.setColumnExpression("$F{" + getIDValues(this.fields[i])[0] + "}");
					columnsObj.setColumnName(getIDValues(this.fields[i])[1]);
					columnsObj.setClassType(getIDValues(this.fields[i])[2]);
					columnsObj.setWidth(new BigDecimal("100"));
					columnsObj.setAlignment("Left");
					columnsObj.setOrder(new BigDecimal(i));
					columnsObj.setBandType("Detail");
					columnsObj.insert(transID);
				}
			}

			for (int i = 1; i < fields.length; i++) {
				Fields fieldsObj = new Fields();
				fieldsObj.setReport(this.report);
				fieldsObj.setFieldName(getIDValues(this.fields[i])[0]);
				fieldsObj.setFieldDescription(getIDValues(this.fields[i])[1]);
				fieldsObj.setClassType(getIDValues(this.fields[i])[2]);
				fieldsObj.insert(transID);
			}

			this.report.update(transID);
		}
	}

	// Métodos para conexão, desconexão e acesso a dados

	public boolean open() {
		try {
			if (!this.sql.equals("")) {
				this.connection = getConnection();
				this.SQLQuery = connection.createStatement();
				if (this.sql.toUpperCase().indexOf("UPDATE") > -1 || this.sql.toUpperCase().indexOf("DELETE") > -1
						|| this.sql.toUpperCase().indexOf("INSERT") > -1
						|| this.sql.toUpperCase().indexOf("CREATE") > -1
						|| this.sql.toUpperCase().indexOf("DROP") > -1) {
					// SQLQuery.executeUpdate(this.sql);
					System.out.println(this.getSql());
					System.out.println("");
					return false;
				} else {
					String sql_tmp = this.sql.replaceAll("<WHERE>", "").replaceAll("</WHERE>", "");
					String sql_5_rows_only = " LIMIT 5";
					if (this.sql.toUpperCase().indexOf(" LIMIT ") < 0)
						this.retorno = SQLQuery
								.executeQuery(PCol.getConvertedStatement(sql_tmp + sql_5_rows_only, connection));
					else
						this.retorno = SQLQuery.executeQuery(PCol.getConvertedStatement(sql_tmp, connection));

				}

				// System.out.println("BeanFields :: "+this.getSql());
				// System.out.println("");
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if (this.connection != null && !this.connection.isClosed()) {
					this.SQLQuery.close();
					this.connection.close();
				}
			} catch (SQLException es) {
			}
			this.errorMsg = "JQuery: Statement error : " + e.getMessage();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (this.connection != null && !this.connection.isClosed()) {
					this.SQLQuery.close();
					this.connection.close();
				}
			} catch (SQLException es) {
			}
			this.errorMsg = e.getMessage();
			System.out.println("JQuery: " + e.getMessage());
			System.out.println("");
			return false;
		}
	}

	private Connection getConnection() {

		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException es) {
		}

		DataSource ds;
		Connection conn = null;

		this.contextType = PropertiesManager.getString("application.contextType");

		try {
			ds = ServiceLocator.getInstance(this.getContextType()).getDataSource(this.getJndiName());
			conn = ds.getConnection();
		} catch (ServiceLocatorException e) {
			e.printStackTrace();
			this.errorMsg = "JQuery: JNDI error : " + e.getMessage();
			// e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			this.errorMsg = "JQuery: SQL error : " + e.getMessage();
			// e.printStackTrace();
		}
		return conn;
	}
}

/************************************************************/
// Hwork, 2005
/************************************************************/
