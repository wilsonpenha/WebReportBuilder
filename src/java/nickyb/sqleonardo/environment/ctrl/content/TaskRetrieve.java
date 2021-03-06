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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.ContentPane;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;

public class TaskRetrieve implements Runnable
{
	private ContentPane target;
	private _TaskSource source;

	private Statement stmt = null;
	private ResultSet rs = null;
	
	public TaskRetrieve(ContentPane target, _TaskSource source)
	{
		this.target = target;
		this.source = source;
	}
	
	public void run()
	{
		try
		{
			String syntax = source.getSyntax().trim();
			if(source.getHandlerKey()!=null)
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(source.getHandlerKey());
				stmt = ch.get().createStatement();
				rs = stmt.executeQuery(syntax);
				
				for(int i=1; i<=this.getColumnCount(); i++)
				{
					String l = this.getColumnLabel(i);
					target.getView().addColumn(l,this.getColumnType(i));
				}
				target.getView().onTableChanged(false);

				for(int i=1; i<=this.getColumnCount(); i++)
				{
					String t = this.getColumnLabel(i) + " : " + this.getColumnTypeName(i) + this.getColumnSize(i) + " " + this.getColumnNullable(i);
					target.getView().setToolTipText(i-1,t);
				}

				for(int row=1;target.isBusy() && rs.next();row++)
				{
					Object[] rowdata = new Object[this.getColumnCount()];
					for(int i=1; i<=this.getColumnCount(); i++)
					{
						rowdata[i-1] = rs.getString(i);
					}
					target.getView().addRow(rowdata,false);
					
					if(row == ContentModel.MAX_BLOCK_RECORDS)
						target.getView().onTableChanged(true);
					
					if(row%ContentModel.MAX_BLOCK_RECORDS == 0)
						target.doRefreshStatus();
				}
				rs.close();
			}
		}
		catch(SQLException sqle)
		{
			Application.println(sqle,true);
		}
		finally
		{
			target.getView().onTableChanged(true);
			
			target.onEndTask();
			target.doRefreshStatus();
		}
	}
	
	private int getColumnCount() throws SQLException
	{
		return rs.getMetaData().getColumnCount();
	}

	private String getColumnLabel(int index) throws SQLException
	{
		return rs.getMetaData().getColumnLabel(index);
	}
	
	private String getColumnSize(int index) throws SQLException
	{
		String size;
		if(isNumberType(getColumnType(index)))
		{
			int p = rs.getMetaData().getPrecision(index);
			int s = rs.getMetaData().getScale(index);
			
			size = s > 0 ? p + "," + s : Integer.toString(p);
		}
		else
			size = Integer.toString(rs.getMetaData().getColumnDisplaySize(index));
		
		return "(" + size + ")";
	}
	
	private String getColumnNullable(int index) throws SQLException
	{
		return rs.getMetaData().isNullable(index) == ResultSetMetaData.columnNullable ? "null" : "not null";
	}
	
	private int getColumnType(int index) throws SQLException
	{
		return rs.getMetaData().getColumnType(index);
	}

	private String getColumnTypeName(int index) throws SQLException
	{
		return rs.getMetaData().getColumnTypeName(index).toLowerCase();
	}

	public static boolean isNumberType(int type)
	{
		return type == Types.BIGINT || type == Types.BIT || type == Types.DECIMAL
			|| type == Types.DOUBLE || type == Types.FLOAT || type == Types.INTEGER
			|| type == Types.NUMERIC || type == Types.REAL || type == Types.SMALLINT;
	}	
}