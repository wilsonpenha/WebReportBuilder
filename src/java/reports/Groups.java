/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class Groups extends PObject {

	/*-------------------------------------------------------------------------
	                  Business Attributes
	-------------------------------------------------------------------------*/
	private java.math.BigDecimal groupsId;
	private java.math.BigDecimal reportId;
	private java.lang.String groupName;
	private java.lang.String isReprintHeader;
	private java.lang.String groupExpression;
	private java.math.BigDecimal order;
	private java.lang.String bgColor;
	private java.lang.String fgColor;

	/*-------------------------------------------------------------------------
	                  Constructor
	-------------------------------------------------------------------------*/
	public Groups() {
		super();
	}

	/*-------------------------------------------------------------------------
	                  Access Methods GET
	-------------------------------------------------------------------------*/
	/**
	 * Access groupsId: Groups Id
	 */
	public java.math.BigDecimal getGroupsId() {
		return this.groupsId;
	}

	/**
	 * Access reportId: Report Id
	 */
	public java.math.BigDecimal getReportId() {
		return this.reportId;
	}

	/**
	 * Access groupName: Group Name
	 */
	public java.lang.String getGroupName() {
		return this.groupName;
	}

	/**
	 * Access isReprintHeader: Reprint Header On Each Page
	 */
	public java.lang.String getIsReprintHeader() {
		return this.isReprintHeader;
	}

	public boolean getIsReprintHeaderOnNewPage() {
		return this.isReprintHeader.equals("Yes");
	}

	/**
	 * Access groupExpression: Group Expression
	 */
	public java.lang.String getGroupExpression() {
		return this.groupExpression;
	}

	public java.math.BigDecimal getOrder() {
		return order;
	}

	public java.lang.String getBgColor() {
		return bgColor;
	}

	public java.lang.String getFgColor() {
		return fgColor;
	}

	/*-------------------------------------------------------------------------
	                  Access Methods SET
	-------------------------------------------------------------------------*/
	public void setOrder(java.math.BigDecimal decimal) {
		order = decimal;
	}

	/**
	 * Change groupsId: Groups Id
	 */
	public void setGroupsId(java.math.BigDecimal groupsId) {
		this.groupsId = groupsId;
	}

	/**
	 * Change reportId: Report Id
	 */
	public void setReportId(java.math.BigDecimal reportId) {
		this.reportId = reportId;
	}

	/**
	 * Change groupName: Group Name
	 */
	public void setGroupName(java.lang.String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Change isReprintHeader: Reprint Header On Each Page
	 */
	public void setIsReprintHeader(java.lang.String isReprintHeader) {
		this.isReprintHeader = isReprintHeader;
	}

	/**
	 * Change groupExpression: Group Expression
	 */
	public void setGroupExpression(java.lang.String groupExpression) {
		this.groupExpression = groupExpression;
	}

	public void setBgColor(java.lang.String string) {
		bgColor = string;
	}

	public void setFgColor(java.lang.String string) {
		fgColor = string;
	}

	/*-------------------------------------------------------------------------
	                  Navigation Methods
	-------------------------------------------------------------------------*/

	public Report getReport() throws Exception {
		if ((this.getReportId() == null))
			return null;
		return Report.findByID(this.getReportId());
	}

	public void setReport(Report obj) throws Exception {
		this.setReportId(obj != null ? obj.getReportId() : null);
	}

	/*-------------------------------------------------------------------------
	                  Events Methods
	-------------------------------------------------------------------------*/
	protected void beforePost(String transID) throws Exception {
	}

	/*-------------------------------------------------------------------------
	                  System Business Methods
	-------------------------------------------------------------------------*/
	/**
	 * Find a Object by ID
	 */
	public static Groups findByID(java.math.BigDecimal groupsId) throws Exception {
		Groups obj = new Groups();
		obj.setGroupsId(groupsId);
		return (Groups) getObject(obj, null);
	}

	/**
	 * Create the next ID
	 */
	protected void beforeInsert(String transID) throws Exception {
		BigDecimalValue bd = Groups.recuperaProximoID(this, transID);
		this.setGroupsId(bd.getValue());
	}

	/**
	 * Bring all Obejct for this class
	 */
	public static Vector findAll() throws Exception {
		Groups obj = new Groups();
		return getList(obj, null);
	}

	/**
	 * Bring all Obejct for this class that relationaship with Report provide
	 */
	public static Vector findByReport(Report object) throws Exception {
		Groups obj = new Groups();
		obj.setReport(object);
		return getList(obj, null);
	}

	/**
	 * Bring all Obejct for this class that relationaship with Report provide
	 */
	public ArrayList getHeaderColumns() throws Exception {
		return new ArrayList(GroupColumns.findByGroupsBandType(this, "Header"));
	}

	/**
	 * Bring all Obejct for this class that relationaship with Report provide
	 */
	public ArrayList getFooterColumns() throws Exception {
		return new ArrayList(GroupColumns.findByGroupsBandType(this, "Footer"));
	}

	/**
	 * Bring all Obejct for this class that relationaship with Report provide
	 */
	public static Vector findByReportOrderByOrder(Report report) throws Exception {
		Groups obj = new Groups();
		Vector v = new Vector();
		v.add(report.getReportId());
		return getList("findByReportOrderByOrder", v, obj, null);
	}
}

/************************************************************/
// Hwork, 2005
/************************************************************/
