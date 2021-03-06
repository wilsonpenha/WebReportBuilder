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
import java.sql.SQLException;

import javax.swing.tree.DefaultMutableTreeNode;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.querybuilder.QueryBuilder;
import nickyb.sqleonardo.querybuilder.ViewObjects;

public class ViewMetadata extends AbstractListTaskSource
{
	private String keycah = null;	
	private String schema = null;	

	protected String getHandlerKey()
	{
		if(ConnectionAssistant.hasHandler(keycah))
		{
			QueryBuilder.identifierQuoteString = ConnectionAssistant.getHandler(keycah).getObject("$identifierQuoteString").toString();
			QueryBuilder.maxColumnNameLength = ((Integer)ConnectionAssistant.getHandler(keycah).getObject("$maxColumnNameLength")).intValue();
		}
		
		return keycah;
	}

	protected String getTableSchema()
	{
		return schema;
	}

	protected void list(DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
		if(parent.getUserObject() instanceof UoDatasource)
		{
			UoDatasource uoDs = (UoDatasource)parent.getUserObject();
			schema = uoDs.schema;
		}
		else
		{
			schema	= parent.getUserObject().toString();
			parent	= (DefaultMutableTreeNode)parent.getParent();
		}
		keycah = ((UoDatasource)parent.getUserObject()).getKey();

		String[] tableType;
		if(node.getUserObject().toString().equals(ViewObjects.ALL_TABLE_TYPES))
			tableType = null;
		else
			tableType = new String[]{node.getUserObject().toString()};

		try
		{
			String dvname = node.getPath()[1].toString();
			String catalog = schema == null ? null : this.getConnection().getCatalog();
			
			ResultSet rs = this.getConnection().getMetaData().getTables(catalog,schema,"%",tableType);
			Preferences.listMetadata(dvname,"table types",this,rs);
		}
		catch (SQLException e)
		{
			Application.println(e,true);
		}
	}
}