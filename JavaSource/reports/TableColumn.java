/************************************************************/
//Description: Project reports
//Company....: Hwork
//Version....: 1.0
//Last change: 6/20/2005

package reports;

import java.util.*;

/************************************************************/

public class TableColumn {

	/*-------------------------------------------------------------------------
	                    Business Attributes
	-------------------------------------------------------------------------*/
	private java.lang.String tableName;
	private java.lang.String ColumnName;

	/*-------------------------------------------------------------------------
	                    Constructor
	-------------------------------------------------------------------------*/
	public TableColumn() {
	}

	/**
	 * @return
	 */
	public java.lang.String getTableName() {
		return tableName;
	}

	/**
	 * @param string
	 */
	public void setTableName(java.lang.String string) {
		tableName = string;
	}

	/**
	 * @return
	 */
	public java.lang.String getColumnName() {
		return ColumnName;
	}

	/**
	 * @param string
	 */
	public void setColumnName(java.lang.String string) {
		ColumnName = string;
	}

}

/************************************************************/
// Hwork, 2005
/************************************************************/
