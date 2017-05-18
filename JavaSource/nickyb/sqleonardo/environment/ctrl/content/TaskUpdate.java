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

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.common.util.Text;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.ContentPane;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;

public class TaskUpdate implements Runnable
{
	private ContentPane target;
	private _TaskSource source;

	private PreparedStatement pstmt = null;

	public TaskUpdate(ContentPane target, _TaskSource source)
	{
		this.target = target;
		this.source = source;		
	}
	
	private TableMetaData getTableMetaData()
	{
		return (TableMetaData)source;
	}
	
	public void run()
	{
		int whereCols = getTableMetaData().getPrimaryKeys().size();
		String preparedWhere = new String(" WHERE ");
		
		ArrayList whereColsIDX	= new ArrayList();
		for(int i=0; i<whereCols; i++)
		{
			String colName = getTableMetaData().getPrimaryKeyProperty(i,TableMetaData.IDX_PK_COLUMN_NAME);
			
			int idx = target.getView().getColumnIndex(colName);
			if(idx >= 0)
			{
				preparedWhere += colName + " = ? AND ";
				whereColsIDX.add(new Integer(idx));
			}
			else
			{
				whereCols--;
			}
		}
		preparedWhere = preparedWhere.substring(0,preparedWhere.length()-5);
		
		for(int exceptions=0; target.getView().getChanges().count()>exceptions ;)
		{
			StringBuffer sql = new StringBuffer();
			Vector data = new Vector();			
			
			ContentChanges.Handler handler = target.getView().getChanges().getHandlerAt(exceptions);
			Object[] rowdata = target.getView().getValues(handler.rid);
			
			if(handler.type.equals(ContentChanges.INSERT))
			{
				sql.append(") VALUES (");
				for(int i=rowdata.length-1; i>=0 ; i--)
				{
					Object cell = rowdata[i];
					if(rowdata[i] instanceof Object[])
						cell = ((Object[])rowdata[i])[0];
					
					sql.insert(0, "," + target.getView().getColumnName(i));
					sql.append("?,");
					
					data.insertElementAt(new Object[]{cell, new Integer(target.getView().getColumnType(i))},0);
				}
				sql.replace(0,1,"INSERT INTO " + getTableMetaData().getIdentifier() + " (");
				sql.setCharAt(sql.length()-1,')');
			}
			else
			{
				if(handler.type.equals(ContentChanges.DELETE))
				{
					sql.append("DELETE FROM " + getTableMetaData().getIdentifier());
				}
				else if(handler.type.equals(ContentChanges.UPDATE))
				{
					sql.append("UPDATE " + getTableMetaData().getIdentifier() + " SET ");
					
					for(int i=0; i<rowdata.length ; i++)
					{
						if(rowdata[i] instanceof Object[])
						{
							Object cell = ((Object[])rowdata[i])[0];
							data.addElement(new Object[]{cell, new Integer(target.getView().getColumnType(i))});
							
							sql.append(target.getView().getColumnName(i) + " = ?,");
						}
					}
					sql.deleteCharAt(sql.length()-1);
				}
				sql.append(preparedWhere);

				for(int i=0; i<whereCols ; i++)
				{
					int col = ((Integer)whereColsIDX.get(i)).intValue();

					Object cell = rowdata[col];
					if(cell instanceof Object[])
						cell = ((Object[])cell)[1];
					
					data.addElement(new Object[]{cell, new Integer(target.getView().getColumnType(i))});
				}	
			}
			
			try
			{
				this.close();
			}
			catch(Exception e)
			{
				Application.println(e,true);
			}
				
			try
			{
				execute(sql.toString(),data);
					
				for(int i=0; i<rowdata.length; i++)
				{
					if(rowdata[i] instanceof Object[])
					{
						Object cell = ((Object[])rowdata[i])[0];
						rowdata[i] = cell;
					}
				}
		
				target.getView().getChanges().removeHandlerAt(exceptions);
			}
			catch(Exception e)
			{
				if(!alert(e)) break;
				exceptions++;
			}			
		}
		target.getView().onTableChanged(true);
		target.onEndTask();
	}
	
	private boolean alert(Exception e)
	{
		String title = e.getClass().getName();
		String message = Text.wrap(e.toString(),75);
		message += "\ndo you want continue?";
		
		return JOptionPane.showConfirmDialog(Application.application,message,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;		
	}
	
	private void close() throws Exception
	{
		if(pstmt!=null)
		{
			pstmt.close();
			pstmt = null;
		}		
	}
	
	private void execute(String sql, Vector data) throws Exception
	{
		ConnectionHandler ch = ConnectionAssistant.getHandler(source.getHandlerKey());	
		pstmt = ch.get().prepareStatement(sql);
		
		for(int i=0; i<data.size(); i++)
		{
			Object[] param = (Object[])data.elementAt(i);
			pstmt.setObject(i+1,param[0],((Integer)param[1]).intValue());
		}
		pstmt.executeUpdate();
		this.close();		
	}
}