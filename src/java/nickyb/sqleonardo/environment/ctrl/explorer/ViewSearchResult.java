/*
 * SQLeonardo :: java database frontend
 * Copyright (C) 2004 nickyb@users.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package nickyb.sqleonardo.environment.ctrl.explorer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.querybuilder.QueryBuilder;

public class ViewSearchResult extends AbstractListTaskSource
{
	private String keycah = null;
	private boolean tableSearch = true;
	
	protected String getHandlerKey()
	{
		if(ConnectionAssistant.hasHandler(keycah))
		{
			QueryBuilder.identifierQuoteString = ConnectionAssistant.getHandler(keycah).getObject("$identifierQuoteString").toString();
			QueryBuilder.maxColumnNameLength = ((Integer)ConnectionAssistant.getHandler(keycah).getObject("$maxColumnNameLength")).intValue();
		}
		
		return keycah;
	}
	
	protected String getTableType()
	{
		if(tableSearch) return super.getTableType();
		
		try
		{
			String catalog	= getConnection().getCatalog();
			String schema	= getTableSchema();
			String table	= getTableName();
			String type		= null;
			
			ResultSet rs = getConnection().getMetaData().getTables(catalog,schema,table,null);
			if(rs.next())
			{
				type = rs.getString(4);
			}
			rs.close();
			return type;
		}
		catch (SQLException e)
		{
			Application.println(e,false);
		}
				
		return null;
	}
	
    protected void list(String keycah,String schema,String table,String column)
    {
		this.reset();
    	this.keycah = keycah;
    	
		try
		{
			if(tableSearch = (column==null || column.length()==0))
				listTables(schema,table);
			else
				listColumns(schema,table,column);
		}
		catch(SQLException e)
		{
			Application.println(e,true);
		}
    }
    
    private void list(ResultSet rs) throws SQLException
    {
		ResultSetMetaData rsmd = rs.getMetaData();
		
		for(int i=1; i<=4; i++)
		{
			addColumn(rsmd.getColumnName(i));
		}
		
		while(rs.next())
		{
			String[] rowdata = new String[4];
			for(int i=1; i<=4; i++)
			{
				rowdata[i-1] = rs.getString(i);
			}
			addRow(rowdata);
		}
		rs.close();
    }
    
    private void listTables(String schema,String table) throws SQLException
    {
    	String catalog = schema == null ? null : this.getConnection().getCatalog();
    	list(this.getConnection().getMetaData().getTables(catalog,schema,table,null));
    }
    
    private void listColumns(String schema,String table,String column) throws SQLException
    {
		String catalog = schema == null ? null : this.getConnection().getCatalog();
		list(this.getConnection().getMetaData().getColumns(catalog,schema,table,column));
    }
}