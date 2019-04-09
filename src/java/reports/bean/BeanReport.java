/************************************************************/
//Titulo.....: Projeto reports
//Empresa....: Hwork
//Versao.....: 1.0
//Alterado em: 6/20/2005
/************************************************************/
package reports.bean;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.Vector;

import reports.Columns;
import reports.Fields;
import reports.GroupColumns;
import reports.Groups;
import reports.Parameters;
import reports.Report;
import reports.Variables;
import br.com.hwork.persistencia.PObject;
import br.com.hwork.persistencia.SessionObject;

public class BeanReport extends RptBeanObject {

	/*-------------------------------------------------------------------------
	                    Support Attributes
	-------------------------------------------------------------------------*/
	private java.lang.String reportId = "";
	private java.lang.String reportName = "";
	private java.lang.String description = "";
	private java.lang.String title = "";
	private java.lang.String image = "";
	private java.lang.String header = "";
	private java.lang.String footer = "";
	private java.lang.String sql = "";
	private java.lang.String orientation = "";
	private java.lang.String pageHeader = "";
	private java.lang.String pageFooter = "";
	private java.lang.String summary = "";
	private java.lang.String template = "";
	private String jndiName = "";
	private String contextType = "";
	private String titleBgColor = "";
	private String titleFgColor = "";
	private String detailHeadBgColor = "";
	private String detailHeadFgColor = "";
	private String detailColorOn = "";
	private String detailColorOff = "";

	private int ACTION_COPY_REPORT = 31;

	/*-------------------------------------------------------------------------
	                    Access Methods
	-------------------------------------------------------------------------*/
	public String getDetailColorOff() {return detailColorOff;}
	public String getDetailColorOn() {return detailColorOn;}
	public String getDetailHeadBgColor() {return detailHeadBgColor;}
	public String getDetailHeadFgColor() {return detailHeadFgColor;}
	public String getTitleBgColor() {return titleBgColor;}
	public String getTitleFgColor() {return titleFgColor;}
	public void setDetailColorOff(String string) {detailColorOff = string;}
	public void setDetailColorOn(String string) {detailColorOn = string;}
	public void setDetailHeadBgColor(String string) {detailHeadBgColor = string;}
	public void setDetailHeadFgColor(String string) {detailHeadFgColor = string;}
	public void setTitleBgColor(String string) {titleBgColor = string;}
	public void setTitleFgColor(String string) {titleFgColor = string;}

	public java.lang.String getReportId() {
		return this.reportId;
	}
	public void setReportId(java.lang.String reportId) {
		this.reportId = reportId;
	}
	public java.lang.String getReportName() {
		return this.reportName;
	}
	public void setReportName(java.lang.String reportName) {
		this.reportName = reportName;
	}
	public java.lang.String getDescription() {
		return this.description;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}
	public java.lang.String getTitle() {
		return this.title;
	}
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	public java.lang.String getImage() {
		return this.image;
	}
	public void setImage(java.lang.String image) {
		this.image = image;
	}
	public java.lang.String getHeader() {
		return this.header;
	}
	public void setHeader(java.lang.String header) {
		this.header = header;
	}
	public java.lang.String getFooter() {
		return this.footer;
	}
	public void setFooter(java.lang.String footer) {
		this.footer = footer;
	}
	public java.lang.String getSql() {
		return this.sql;
	}
	public void setSql(java.lang.String sql) {
		this.sql = sql;
	}
	public java.lang.String getOrientation() {
		return this.orientation;
	}
	public void setOrientation(java.lang.String orientation) {
		this.orientation = orientation;
	}
	public java.lang.String getPageHeader() {
		return this.pageHeader;
	}
	public void setPageHeader(java.lang.String pageHeader) {
		this.pageHeader = pageHeader;
	}
	public java.lang.String getPageFooter() {
		return this.pageFooter;
	}
	public void setPageFooter(java.lang.String pageFooter) {
		this.pageFooter = pageFooter;
	}
	public java.lang.String getSummary() {
		return this.summary;
	}
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}
	public java.lang.String getTemplate() {
		return this.template;
	}
	public void setTemplate(java.lang.String template) {
		this.template = template;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public String getJndiName() {
		return this.jndiName;
	}
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}
	public String getContextType() {
		return this.contextType;
	}

	/*-------------------------------------------------------------------------
	                     Constructors
	 -------------------------------------------------------------------------*/
	public BeanReport() {
		super();
		this.blockSize = 20;
	}

	/*-------------------------------------------------------------------------
	                     Business process
	 -------------------------------------------------------------------------*/
	protected PObject getPObjectInstance() {
		return new reports.Report();
	}
	protected void selectList() throws Exception {
		this.list = reports.Report.findAll();
	}
	protected void selectObject(String[] idValues) throws Exception {
		this.pobject = reports.Report.findByID(new java.math.BigDecimal(idValues[0]));
	}

	/**
	 * This method it is for build the Parameter input in report form selection 
	 * @return
	 * @throws Exception
	 */
	public String getParametersInput() throws Exception {
		String strJSP = "";
		Report report = Report.findByID(new BigDecimal(this.reportId));
		Vector vParams = Parameters.findByReport(report);
		Connection connection;
		Statement sqlQuery;
		ResultSet query = null;

		for (int i = 0; i < vParams.size(); i++) {
			String start = ((Parameters) vParams.elementAt(i)).getIsRequired().equals("Yes") ? "*" : "";
			String checked = ((Parameters) vParams.elementAt(i)).getIsRequired().equals("Yes") ? "checked" : "";
			strJSP += "<tr>\r";
			strJSP += "         	  <td width=\"32%\" class=\"dataLabel\">"
				+ start
				+ ((Parameters) vParams.elementAt(i)).getParameterDescription()
				+ " </td>\r";
			strJSP += "         	  <td width=\"66%\" class=\"inputLocation\">\r";
			strJSP += "         	    <input type=\"hidden\" name=\"paramLabel\" value=\""
				+ ((Parameters) vParams.elementAt(i)).getParameterDescription()
				+ "\">\r";
			strJSP += "         	    <input type=\"hidden\" name=\"paramName\" value=\""
				+ ((Parameters) vParams.elementAt(i)).getParameterName()
				+ "\">\r";

			if (((Parameters) vParams.elementAt(i)).getInputType().equals("Text")) {
				// If the parameter there is a simple input value text like a name, date or whatever
				strJSP += "         	    <input type=\"hidden\" name=\"validation\" value=\""
					+ ((Parameters) vParams.elementAt(i)).getIsRequired()
					+ "|text|1\">\r";
				strJSP
					+= "         	    <input type=\"text\" class=\"dataInput\" name=\"paramValue\" tabIndex=\"4\"size=\"30\">\r";
			} else if (((Parameters) vParams.elementAt(i)).getInputType().equals("Radio")) {
				// If the parameter there is a Radio selection, this code will build the selection with the options
				Parameters param = (Parameters) vParams.elementAt(i);
				try {
					StringTokenizer options = new StringTokenizer(param.getRadioOptions(), ";");
					strJSP += "         	    <input type=\"hidden\" name=\"validation\" value=\""
						+ ((Parameters) vParams.elementAt(i)).getIsRequired()
						+ "|radio|"
						+ options.countTokens()
						+ "\">\r";
					strJSP += "         	    <input type=\"hidden\" name=\"paramValue\">\r";
					while (options.hasMoreTokens()) {
						StringTokenizer option = new StringTokenizer(options.nextToken(), "=");
						String strCheck = "";
						if (checked.equals("checked"))
							strCheck = "onClick=\"document.forms[1].paramValue[" + i + "].value=this.value;\"";
						else
							strCheck =
								"onMousedown=\"if(this.checked){alert('You just unselect this option!');this.checked=false;document.forms[1].paramValue["
									+ i
									+ "].value=''}else{this.checked=true;document.forms[1].paramValue["
									+ i
									+ "].value=this.value;}\"";
						strJSP += "<input type=\"radio\"  "
							+ checked
							+ " name=\""
							+ ((Parameters) vParams.elementAt(i)).getParameterName()
							+ "\" value=\""
							+ option.nextToken().trim()
							+ "\" "
							+ strCheck
							+ ">"
							+ option.nextToken()
							+ "<br>\r";
						checked = "";
					}
				} catch (Exception ex) {
					strJSP += "&nbsp;<font color=\"red\">This parameter name: "
						+ ((Parameters) vParams.elementAt(i)).getParameterName()
						+ ", its can not to be build, because may be you have a mistake in your options!</font>";
				}

			} else if (((Parameters) vParams.elementAt(i)).getInputType().equals("Data Combo")) {
				// If the parameter there is a Data selection from the target datasource for the selected report name
				String jndiName = report.getJndiName() != null ? report.getJndiName() : "";
				String contextType = report.getContextType() != null ? report.getContextType() : "";
				strJSP += "         	    <input type=\"hidden\" name=\"validation\" value=\""
					+ ((Parameters) vParams.elementAt(i)).getIsRequired()
					+ "|select|1\">\r";
				try { 
					connection = this.getConnection(contextType, jndiName);
					sqlQuery = connection.createStatement();

					Parameters param = (Parameters) vParams.elementAt(i);
					String strSQL = param.getTableInput(); 
  
					query = sqlQuery.executeQuery(strSQL);
					strJSP += "         	    <SELECT NAME=\"paramValue\">\r";
					if (start.equals(""))
						strJSP += "         	       <Option value=\"\">----- Select -----</option>\r";

					while (query.next()) {
						strJSP += "         	       <Option value=\""
							+ query.getString(param.getFieldKey()).toString()
							+ "\">"
							+ query.getString(param.getFieldDisplay()).toString()
							+ "</option>\r";
					}
					sqlQuery.close();
					connection.close();
					strJSP += "         	    </SELECT>\r";
				} catch (Exception ex) {
					strJSP += "&nbsp;<font color=\"red\">This parameter name: "
						+ ((Parameters) vParams.elementAt(i)).getParameterName()
						+ ", its can not to be build, because may be you have a mistake in connection properties from your Report!</font>";
				}
			}
			strJSP += "         	  </td>\r";
			strJSP += "            </tr>\r";
		}
		return strJSP;
	}

	public Vector getGroups() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.reportId));
		return Groups.findByReportOrderByOrder(report);
	}

	public Vector getGroupColumns(Groups group) throws Exception {
		return GroupColumns.findByGroups(group);
	}

	public String getTagGroups(Groups group) throws Exception {
		String strJSP = "";
		Vector columns = getGroupColumns(group);
		String cor = "";

		if (columns.size() > 0) {
			strJSP += "<table id=\""
				+ group.getGroupName()
				+ "\" class=\"tableTag\" width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\r";
			strJSP += "<tr id=\"linha-1\" style=\"{cursor:hand}\"  bgcolor=\"#D3DCE7\">\r";
			strJSP += "  <td id=\"\" width=\"20%\" nowrap=\"1\"><div  align=\"center\"><b>View?</b></div></td>\r";
			strJSP
				+= "  <td id=\"\" width=\"80%\" nowrap=\"1\"><b>Column Name</b><img src=\"common/images/asc.gif\" onclick=\"TableSortWait('"
				+ group.getGroupName()
				+ "', '1' ,'aa','1');\"><img src=\"common/images/desc.gif\" onclick=\"TableSortWait('"
				+ group.getGroupName()
				+ "', '1' ,'ad','1');\"></td>\r";
			strJSP += "</tr>\r";
			for (int i = 0; i < columns.size(); i++) {
				if (i % 2 == 0)
					cor = "#F7F4F2";
				else
					cor = "#D3DCE7";
				strJSP += "<tr id=\""
					+ group.getGroupName()
					+ i
					+ "\"  bgcolor=\""
					+ cor
					+ "\" style=\"{cursor:hand}\" onMouseover=\"setColorOn('"
					+ group.getGroupName()
					+ i
					+ "');\" onMouseout=\"setColorOff('"
					+ group.getGroupName()
					+ i
					+ "');\">\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"20%\" align=\"center\"><div  align=\"center\"><input checked type=\"checkbox\" class=\"checkList\" name=\"gColsIds\" value=\""
					+ group.getGroupsId().toString()
					+ "|"
					+ ((GroupColumns) columns.elementAt(i)).getGroupColumnsId().toString()
					+ "\" onClick=\"if (this.checked){setGroupChecked(this);}\"></div></td>\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"80%\" >"
					+ ((GroupColumns) columns.elementAt(i)).getDescription()
					+ "</td>\r";
				strJSP += "</tr>\r";
			}
			strJSP += "</table>\r";
		}
		return strJSP;
	}

	public String getTagReport() throws Exception {
		String strJSP = "";
		Vector columns = getTitleColumns();
		String cor = "";

		if (columns.size() > 0) {
			strJSP
				+= "<table id=\"tableTitle\" class=\"tableTag\" width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\r";
			strJSP += "<tr id=\"linha-1\" style=\"{cursor:hand}\"  bgcolor=\"#D3DCE7\">\r";
			strJSP += "  <td id=\"\" width=\"20%\" nowrap=\"1\"><div  align=\"center\"><b>View?</b></div></td>\r";
			strJSP
				+= "  <td id=\"\" width=\"80%\" nowrap=\"1\"><b>Column Name</b><img src=\"common/images/asc.gif\" onclick=\"TableSortWait('tableTitle', '1' ,'aa','1');\"><img src=\"common/images/desc.gif\" onclick=\"TableSortWait('tableTitle', '1' ,'ad','1');\"></td>\r";
			strJSP += "</tr>\r";
			for (int i = 0; i < columns.size(); i++) {
				if (i % 2 == 0)
					cor = "#F7F4F2";
				else
					cor = "#D3DCE7";
				strJSP += "<tr id=\"tableTitle"
					+ i
					+ "\"  bgcolor=\""
					+ cor
					+ "\" style=\"{cursor:hand}\" onMouseover=\"setColorOn('tableTitle"
					+ i
					+ "');\" onMouseout=\"setColorOff('tableTitle"
					+ i
					+ "');\">\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"20%\" align=\"center\"><div  align=\"center\"><input checked type=\"checkbox\" class=\"checkList\" name=\"titleIds\" value=\""
					+ ((Columns) columns.elementAt(i)).getColumnsId().toString()
					+ "\"></div></td>\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"80%\" >"
					+ ((Columns) columns.elementAt(i)).getColumnName()
					+ "</td>\r";
				strJSP += "</tr>\r";
			}
			strJSP += "</table>\r";
		}
		return strJSP;
	}

	public String getTagPage() throws Exception {
		String strJSP = "";
		Vector columns = getPageColumns();
		String cor = "";

		if (columns.size() > 0) {
			strJSP
				+= "<table id=\"tablePage\" class=\"tableTag\" width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\r";
			strJSP += "<tr id=\"linha-1\" style=\"{cursor:hand}\"  bgcolor=\"#D3DCE7\">\r";
			strJSP += "  <td id=\"\" width=\"20%\" nowrap=\"1\"><div  align=\"center\"><b>View?</b></div></td>\r";
			strJSP
				+= "  <td id=\"\" width=\"80%\" nowrap=\"1\"><b>Column Name</b><img src=\"common/images/asc.gif\" onclick=\"TableSortWait('tablePage', '1' ,'aa','1');\"><img src=\"common/images/desc.gif\" onclick=\"TableSortWait('tablePage', '1' ,'ad','1');\"></td>\r";
			strJSP += "</tr>\r";
			for (int i = 0; i < columns.size(); i++) {
				if (i % 2 == 0)
					cor = "#F7F4F2";
				else
					cor = "#D3DCE7";
				strJSP += "<tr id=\"tablePage"
					+ i
					+ "\"  bgcolor=\""
					+ cor
					+ "\" style=\"{cursor:hand}\" onMouseover=\"setColorOn('tablePage"
					+ i
					+ "');\" onMouseout=\"setColorOff('tablePage"
					+ i
					+ "');\">\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"20%\" align=\"center\"><div  align=\"center\"><input checked type=\"checkbox\" class=\"checkList\" name=\"pageIds\" value=\""
					+ ((Columns) columns.elementAt(i)).getColumnsId().toString()
					+ "\"></div></td>\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"80%\" >"
					+ ((Columns) columns.elementAt(i)).getColumnName()
					+ "</td>\r";
				strJSP += "</tr>\r";
			}
			strJSP += "</table>\r";
		}
		return strJSP;
	}

	public String getTagDetail() throws Exception {
		String strJSP = "";
		Vector columns = getDetailColumns();
		String cor = "";

		if (columns.size() > 0) {
			strJSP
				+= "<table id=\"tableDetail\" class=\"tableTag\" width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\r";
			strJSP += "<tr id=\"linha-1\" style=\"{cursor:hand}\"  bgcolor=\"#D3DCE7\">\r";
			strJSP += "  <td id=\"\" width=\"20%\" nowrap=\"1\"><div  align=\"center\"><b>View?</b></div></td>\r";
			strJSP
				+= "  <td id=\"\" width=\"80%\" nowrap=\"1\"><b>Column Name</b><img src=\"common/images/asc.gif\" onclick=\"TableSortWait('tableDetail', '1' ,'aa','1');\"><img src=\"common/images/desc.gif\" onclick=\"TableSortWait('tableDetail', '1' ,'ad','1');\"></td>\r";
			strJSP += "</tr>\r";
			for (int i = 0; i < columns.size(); i++) {
				if (i % 2 == 0)
					cor = "#F7F4F2";
				else
					cor = "#D3DCE7";
				strJSP += "<tr id=\"tableDetail"
					+ i
					+ "\"  bgcolor=\""
					+ cor
					+ "\" style=\"{cursor:hand}\" onMouseover=\"setColorOn('tableDetail"
					+ i
					+ "');\" onMouseout=\"setColorOff('tableDetail"
					+ i
					+ "');\">\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"20%\" align=\"center\"><div  align=\"center\"><input checked type=\"checkbox\" onClick=\"javascript:checkDetailComuns(this)\" class=\"checkList\" name=\"detailIds\" value=\""
					+ ((Columns) columns.elementAt(i)).getColumnsId().toString()
					+ "\"></div></td>\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"80%\" >"
					+ ((Columns) columns.elementAt(i)).getColumnName()
					+ "</td>\r";
				strJSP += "</tr>\r";
			}
			strJSP += "</table>\r";
		}
		return strJSP;
	}

	public String getTagSummary() throws Exception {
		String strJSP = "";
		Vector columns = getSummaryColumns();
		String cor = "";

		if (columns.size() > 0) {
			strJSP
				+= "<table id=\"tableSummary\" class=\"tableTag\" width=\"300\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\">\r";
			strJSP += "<tr id=\"linha-1\" style=\"{cursor:hand}\"  bgcolor=\"#D3DCE7\">\r";
			strJSP += "  <td id=\"\" width=\"20%\" nowrap=\"1\"><div  align=\"center\"><b>View?</b></div></td>\r";
			strJSP
				+= "  <td id=\"\" width=\"80%\" nowrap=\"1\"><b>Column Name</b><img src=\"common/images/asc.gif\" onclick=\"TableSortWait('tableSummary', '1' ,'aa','1');\"><img src=\"common/images/desc.gif\" onclick=\"TableSortWait('tableSummary', '1' ,'ad','1');\"></td>\r";
			strJSP += "</tr>\r";
			for (int i = 0; i < columns.size(); i++) {
				if (i % 2 == 0)
					cor = "#F7F4F2";
				else
					cor = "#D3DCE7";
				strJSP += "<tr id=\"tableSummary"
					+ i
					+ "\"  bgcolor=\""
					+ cor
					+ "\" style=\"{cursor:hand}\" onMouseover=\"setColorOn('tableSummary"
					+ i
					+ "');\" onMouseout=\"setColorOff('tableSummary"
					+ i
					+ "');\">\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"20%\" align=\"center\"><div  align=\"center\"><input checked type=\"checkbox\" class=\"checkList\" name=\"summaryIds\" value=\""
					+ ((Columns) columns.elementAt(i)).getColumnsId().toString()
					+ "\"></div></td>\r";
				strJSP += "<td id=\""
					+ i
					+ "\" width=\"80%\" >"
					+ ((Columns) columns.elementAt(i)).getColumnName()
					+ "</td>\r";
				strJSP += "</tr>\r";
			}
			strJSP += "</table>\r";
		}
		return strJSP;
	}

	public Vector getDetailColumns() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.reportId));
		return Columns.findByReportBandType(report, "Detail");
	}

	public Vector getTitleColumns() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.reportId));
		return Columns.findByReportBandType(report, "Report");
	}

	public Vector getPageColumns() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.reportId));
		return Columns.findByReportBandType(report, "Page");
	}

	public Vector getSummaryColumns() throws Exception {
		Report report = new Report();
		report.setReportId(new BigDecimal(this.reportId));
		return Columns.findByReportBandType(report, "Summary");
	}

	public String getAttachmentNames() throws Exception{
		String fileName="";
		String mimeType="";
		String fileId="";
		String strHTML="";
		fileName=this.getReportName();
		mimeType="text/plain";
		fileId=this.getReportId();
		if (((Report)this.pobject).getJasperJrxml()!=null)
			strHTML+="<a href=\""+request.getContextPath()+"/getFile.do?reportId="+fileId+"\" target=\"_new\"> <img src=\"../jsp/common/images/Reports.gif\" border=\"0\" alt=\"\"></a>\n";
		return strHTML;
	}
	
	public void execute() {
		if (this.dbAction == ACTION_COPY_REPORT) {
			try {
				Report report = Report.findByID(new BigDecimal(this.getReportId()));
				ReportCopyCtrl reportCopyCtrl = new ReportCopyCtrl(report);
				reportCopyCtrl.execute();
				this.dbAction = ACTION_SELECT_LIST;
				this.message = "Your copy has been conclude!";
			} catch (Exception e) {
				this.message = "Erro on try to copy the Report object!";
				this.dbAction = ACTION_SELECT_LIST;
			}
		}
		
/*		if (this.dbAction == ACTION_UPDATE) {
			try {
				String sql = "";
				sql += getInsertStmp(new Report());
				sql += getInsertStmp(new Variables());
				sql += getInsertStmp(new Parameters());
				sql += getInsertStmp(new Fields());
				sql += getInsertStmp(new Columns());
				sql += getInsertStmp(new Groups());
				sql += getInsertStmp(new GroupColumns());
				//sql += getInsertStmp(new Sequence());
				sql += getInsertStmp(new DatabaseTable());
				String rootDir = BeanObject.replaceAll(request.getRealPath(""), "\\", "//");
				Writer wout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rootDir+ "//WEB-INF//ReportSQL.sql")));
				wout.write(sql);
				wout.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}*/
		super.execute();
	}

	public void delete() throws Exception{
		ReportDeleteCtrl reportDeleteCtrl = new ReportDeleteCtrl((Report)this.pobject);
		reportDeleteCtrl.execute();
	}

	public class ReportCopyCtrl extends SessionObject {

		private Report report;

		public ReportCopyCtrl(Report report) {
			this.report = report;
		}

		protected void executeTransaction() throws Exception {
			Vector vParameters     = Parameters.findByReport(report);
			Vector vFields         = Fields.findByReport(report);
			Vector vColumns        = Columns.findByReport(report);
			Vector vGroups         = Groups.findByReport(report);
			Vector vVariables      = Variables.findByReport(report);

			this.report.setReportName(this.report.getReportName()+" Copy");
			this.report.insert(transID);
			for (int i = 0; i < vParameters.size(); i++) {
				((Parameters) vParameters.elementAt(i)).setReport(this.report);
				((Parameters) vParameters.elementAt(i)).insert(transID);
			}
			for (int i = 0; i < vFields.size(); i++) {
				((Fields) vFields.elementAt(i)).setReport(this.report);
				((Fields) vFields.elementAt(i)).insert(transID);
			}
			for (int i = 0; i < vColumns.size(); i++) {
				((Columns) vColumns.elementAt(i)).setReport(this.report);
				((Columns) vColumns.elementAt(i)).insert(transID);
			}
			for (int i = 0; i < vGroups.size(); i++) {
				Groups group = (Groups) vGroups.elementAt(i);
				group.setReport(this.report);
				Vector vGroupColumns = GroupColumns.findByGroups(group);
				group.insert(transID);
				for (int j = 0; j < vGroupColumns.size(); j++) {
					((GroupColumns) vGroupColumns.elementAt(j)).setGroups(group);
					((GroupColumns) vGroupColumns.elementAt(j)).insert(transID);
				}
			}
			for (int i = 0; i < vVariables.size(); i++) {
				((Variables) vVariables.elementAt(i)).setReport(this.report);
				((Variables) vVariables.elementAt(i)).setResetGroupId(null);
				((Variables) vVariables.elementAt(i)).setIncrementGroupId(null);
				((Variables) vVariables.elementAt(i)).insert(transID);
			}
		}

	}

	public class ReportDeleteCtrl extends SessionObject {

		private Report report;

		public ReportDeleteCtrl(Report report) {
			this.report = report;
		}

		protected void executeTransaction() throws Exception {
			Vector vParameters     = Parameters.findByReport(report);
			Vector vFields         = Fields.findByReport(report);
			Vector vColumns        = Columns.findByReport(report);
			Vector vGroups         = Groups.findByReport(report);
			Vector vVariables      = Variables.findByReport(report);

			for (int i = 0; i < vParameters.size(); i++) {
				((Parameters) vParameters.elementAt(i)).delete(transID);
			}
			for (int i = 0; i < vFields.size(); i++) {
				((Fields) vFields.elementAt(i)).delete(transID);
			}
			for (int i = 0; i < vColumns.size(); i++) {
				((Columns) vColumns.elementAt(i)).delete(transID);
			}
			for (int i = 0; i < vVariables.size(); i++) {
				((Variables) vVariables.elementAt(i)).delete(transID);
			}
			for (int i = 0; i < vGroups.size(); i++) {
				Groups group = (Groups) vGroups.elementAt(i);
				Vector vGroupColumns = GroupColumns.findByGroups(group, transID);
				for (int j = 0; j < vGroupColumns.size(); j++) {
					((GroupColumns) vGroupColumns.elementAt(j)).delete(transID);
				}
				group.delete(transID);
			}
			this.report.delete(transID);
		}

	}


}

/************************************************************/
// Hwork, 2005
/************************************************************/
