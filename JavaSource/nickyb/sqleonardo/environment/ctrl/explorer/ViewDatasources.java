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

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

import nickyb.sqleonardo.common.gui.ListView;

public class ViewDatasources extends ListView
{
	public ViewDatasources()
	{
		addColumn("name");
		addColumn("url");
		addColumn("status");
	}
	
	public void list(DefaultMutableTreeNode node)
	{
		removeAllRows();
		
		for(Enumeration e = node.children(); e.hasMoreElements();)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)e.nextElement();
			UoDatasource uoDs = (UoDatasource)child.getUserObject();
			
			Object[] rowdata = new Object[3];
			rowdata[0] = uoDs.name;
			rowdata[1] = uoDs.url;
			rowdata[2] = uoDs.isConnected() ? "connected" : "disconnected";
			
			addRow(rowdata);
		}
	}
}
