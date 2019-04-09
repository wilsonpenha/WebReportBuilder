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

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nickyb.sqleonardo.common.gui.AbstractDialogModal;
import nickyb.sqleonardo.common.util.Text;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.querybuilder.syntax.SQLFormatter;

public class DialogPreview extends AbstractDialogModal
{
	private JTextArea syntaxes;
	
	private ContentView view;
	private TableMetaData tmd;
	
	public DialogPreview(ContentView view,TableMetaData tmd)
	{
		super(Application.application,"changes");
		this.view = view;
		this.tmd = tmd;
			
		getContentPane().add(new JScrollPane(syntaxes=new JTextArea()));
		syntaxes.setEditable(false);
	}

	protected void onOpen()
	{
		int whereCols = tmd.getPrimaryKeys().size();
		String preparedWhere = new String(" WHERE ");
		
		ArrayList whereColsIDX	= new ArrayList();
		for(int i=0; i<whereCols; i++)
		{
			String colName = tmd.getPrimaryKeyProperty(i,TableMetaData.IDX_PK_COLUMN_NAME);
			
			int idx = view.getColumnIndex(colName);
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
		
		for(int i=0; i<view.getChanges().count(); i++)
		{
			ContentChanges.Handler handler = view.getChanges().getHandlerAt(i);
			Object[] rowdata = view.getValues(handler.rid);
			
			String sql = null;
			if(handler.type.equals(ContentChanges.INSERT))
			{
				String columns = new String();
				String values = new String();
				
				for(int j=0; j<rowdata.length; j++)
				{
					Object cell = rowdata[j];
					if(rowdata[j] instanceof Object[])
						cell = ((Object[])rowdata[j])[0];

					columns += view.getColumnName(j) + ",";
					values += toJdbcValue(cell,j) + ",";
				}
				columns = columns.substring(0,columns.length()-1);
				values = values.substring(0,values.length()-1);
				
				sql = "INSERT INTO " + tmd.getIdentifier() + " (" + columns + ") VALUES (" + values + ")";
			}
			else
			{
				String where = preparedWhere;
				for(int j=0; j<whereCols; j++)
				{
					int pos = where.indexOf("?");
					int col = ((Integer)whereColsIDX.get(j)).intValue();
					
					Object cell = rowdata[col];
					if(cell instanceof Object[])
						cell = ((Object[])cell)[1];
					
					where = Text.replaceText(where,toJdbcValue(cell,col),pos,1);
				}
				
				if(handler.type.equals(ContentChanges.DELETE))
				{
					sql = "DELETE FROM " + tmd.getIdentifier() + where;
				}
				else if(handler.type.equals(ContentChanges.UPDATE))
				{
					sql = "UPDATE " + tmd.getIdentifier() + " SET ";
					
					for(int j=0; j<rowdata.length; j++)
					{
						if(rowdata[j] instanceof Object[])
						{
							Object cell = ((Object[])rowdata[j])[0];
							sql += view.getColumnName(j) + " = " + toJdbcValue(cell,j) + ",";
						}
					}
					sql = sql.substring(0,sql.length()-1) + where;
				}
			}
			syntaxes.append(sql.trim() + ";\n");
		}
	}
	
	private String toJdbcValue(Object value, int col)
	{
		return SQLFormatter.toJdbcValue(value,view.getColumnType(col));
	}	
}
