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

package nickyb.sqleonardo.environment.ctrl.content;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.SQLFormatter;

public class TableMetaData extends QueryTokens.Table implements _TaskSource
{
	private String filter;
	private String keycah;
	private String type;
	
	private ArrayList alColumns;
	private ArrayList alPrimaryKeys;
	private ArrayList alImportedKeys;
	private ArrayList alExportedKeys;
	
	public TableMetaData(String keycah, String schema, String name)
	{
		this(keycah,schema,name,null);
	}
	
	public TableMetaData(String keycah, String schema, String name, String type)
	{
		super(schema,name);
		this.keycah = keycah;
		this.type = type;
	}

	public String getHandlerKey()
	{
		return keycah;
	}

	public String getSyntax()
	{
		String syntax = "SELECT * FROM " + this.getIdentifier();
		if(filter != null) syntax += " WHERE " + filter;
		
		return  syntax;
	}
	
	public void setFilter(String s)
	{
		filter = s;
	}
	
	public String getIdentifier()
	{
		ConnectionHandler ch = ConnectionAssistant.getHandler(keycah);
		
		String declare = super.getIdentifier();
		return SQLFormatter.ensureQuotes(declare,ch.getObject("$identifierQuoteString").toString(),false);
	}
	
	public boolean isPrimaryKey(String column)
	{
		for(int i=0; i<this.getPrimaryKeys().size(); i++)
		{
			if(this.getPrimaryKeyProperty(i,TableMetaData.IDX_PK_COLUMN_NAME).equals(column))
				return true;
		}
		
		return false;
	}
	
	public ArrayList getColumns()
	{
		if(alColumns == null)
		{
			alColumns = new ArrayList();

			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getHandlerKey());
				DatabaseMetaData dbmd = ch.get().getMetaData();
				
				String catalog = this.getSchema() == null ? null : ch.get().getCatalog();
				copy(dbmd.getColumns(catalog,this.getSchema(),this.getName(),"%"),alColumns);
			}
			catch (SQLException e)
			{
				Application.println("[ TableMetaData::getColumns() ]\n" + e);
			}
		}
		
		return alColumns;
	}

	public String getColumnProperty(int row, int idx)
	{
		return ((String[])this.getColumns().get(row))[idx];
	}

	public String getPrimaryKeyProperty(int row, int idx)
	{
		return ((String[])this.getPrimaryKeys().get(row))[idx];
	}
	
	public ArrayList getPrimaryKeys()
	{
		if(alPrimaryKeys == null)
		{
			alPrimaryKeys = new ArrayList();

			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getHandlerKey());
				DatabaseMetaData dbmd = ch.get().getMetaData();
				
				String catalog = this.getSchema() == null ? null : ch.get().getCatalog();
				copy(dbmd.getPrimaryKeys(catalog,this.getSchema(),this.getName()),alPrimaryKeys);
			}
			catch (SQLException e)
			{
				Application.println("[ TableMetaData::getPrimaryKeys() ]\n" + e);
			}
		}

		return alPrimaryKeys;
	}

	public String getImportedKeyProperty(int row, int idx)
	{
		return ((String[])this.getImportedKeys().get(row))[idx];
	}
	
	public ArrayList getImportedKeys()
	{
		if(alImportedKeys == null)
		{
			alImportedKeys = new ArrayList();

			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getHandlerKey());
				DatabaseMetaData dbmd = ch.get().getMetaData();
				
				String catalog = this.getSchema() == null ? null : ch.get().getCatalog();
				copy(dbmd.getImportedKeys(catalog,this.getSchema(),this.getName()),alImportedKeys);
			}
			catch (SQLException e)
			{
				Application.println("[ TableMetaData::getImportedKeys() ]\n" + e);
			}
		}

		return alImportedKeys;
	}

	public String getExportedKeyProperty(int row, int idx)
	{
		return ((String[])this.getExportedKeys().get(row))[idx];
	}
	
	public ArrayList getExportedKeys()
	{
		if(alExportedKeys == null)
		{
			alExportedKeys = new ArrayList();

			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getHandlerKey());
				DatabaseMetaData dbmd = ch.get().getMetaData();
				
				String catalog = this.getSchema() == null ? null : ch.get().getCatalog();
				copy(dbmd.getExportedKeys(catalog,this.getSchema(),this.getName()),alExportedKeys);
			}
			catch (SQLException e)
			{
				Application.println("[ TableMetaData::getExportedKeys() ]\n" + e);
			}
		}

		return alExportedKeys;
	}
	
	public String getType()
	{
		if(type==null)
		{
			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getHandlerKey());
				DatabaseMetaData dbmd = ch.get().getMetaData();
					
				String catalog = this.getSchema() == null ? null : ch.get().getCatalog();
				ResultSet rs = dbmd.getTables(catalog,this.getSchema(),this.getName(),null);
				
				if(rs.next())
				{
					type = rs.getString(4);
				}
			}
			catch (SQLException e)
			{
				Application.println("[ TableMetaData::getExportedKeys() ]\n" + e);
			}
		}
		
		return type;
	}
	
	private static void copy(ResultSet rs,ArrayList al) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		while(rs.next())
		{
			String[] row = new String[rsmd.getColumnCount()];
			al.add(row);
			
			for(int i=0; i<row.length; i++)
				row[i] = rs.getString(i+1);
		}
		rs.close();
	}
	
	/* COLUMN */
	public static final int IDX_COL_TABLE_CAT			= 0;
	public static final int IDX_COL_TABLE_SCHEM			= 1;
	public static final int IDX_COL_TABLE_NAME			= 2;
	public static final int IDX_COL_COLUMN_NAME			= 3;
	public static final int IDX_COL_DATA_TYPE			= 4;
	public static final int IDX_COL_TYPE_NAME			= 5;
	public static final int IDX_COL_COLUMN_SIZE			= 6;
	public static final int IDX_COL_BUFFER_LENGTH		= 7;
	public static final int IDX_COL_DECIMAL_DIGITS		= 8;
	public static final int IDX_COL_NUM_PREC_RADIX		= 9;
	public static final int IDX_COL_NULLABLE			= 10;
	public static final int IDX_COL_REMARKS				= 11;
	public static final int IDX_COL_COLUMN_DEF			= 12;
	public static final int IDX_COL_SQL_DATA_TYPE		= 13;
	public static final int IDX_COL_SQL_DATETIME_SUB	= 14;
	public static final int IDX_COL_CHAR_OCTET_LENGTH	= 15;
	public static final int IDX_COL_ORDINAL_POSITION	= 16;
	public static final int IDX_COL_IS_NULLABLE			= 17;
	
	/* PRiMARY KEY */
	public static final int IDX_PK_TABLE_CAT	= 0;
	public static final int IDX_PK_TABLE_SCHEM	= 1;
	public static final int IDX_PK_TABLE_NAME	= 2;
	public static final int IDX_PK_COLUMN_NAME	= 3;
	public static final int IDX_PK_KEY_SEQ		= 4;
	public static final int IDX_PK_PK_NAME		= 5;

	/* RELATiONSHiP (iMPORTED&EXPORTED KEY) */
	public static final int IDX_REL_PKTABLE_CAT		= 0;
	public static final int IDX_REL_PKTABLE_SCHEM	= 1;
	public static final int IDX_REL_PKTABLE_NAME	= 2;
	public static final int IDX_REL_PKCOLUMN_NAME	= 3;
	public static final int IDX_REL_FKTABLE_CAT		= 4;
	public static final int IDX_REL_FKTABLE_SCHEM	= 5;
	public static final int IDX_REL_FKTABLE_NAME	= 6;
	public static final int IDX_REL_FKCOLUMN_NAME	= 7;
	public static final int IDX_REL_KEY_SEQ			= 8;
	public static final int IDX_REL_UPDATE_RULE		= 9;
	public static final int IDX_REL_DELETE_RULE		= 10;
	public static final int IDX_REL_FK_NAME			= 11;
	public static final int IDX_REL_PK_NAME			= 12;
	public static final int IDX_REL_DEFERRABILITY	= 13;
}