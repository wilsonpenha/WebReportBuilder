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

import java.awt.Cursor;

import javax.swing.JCheckBox;
import javax.swing.tree.DefaultMutableTreeNode;

import nickyb.sqleonardo.common.gui.AbstractDialogConfirm;
import nickyb.sqleonardo.environment.Application;

public class DialogDatasource extends AbstractDialogConfirm
{
	private boolean editmode;
	private SideNavigator navigator;
	
	private UoDatasource uoDs;
	private MaskDatasource mDs;
	
	private JCheckBox cbxConnect;
	
	public DialogDatasource(SideNavigator navigator, boolean editmode)
	{
		super(Application.application,"datasource." + (editmode?"edit":"new"));
		
		this.editmode = editmode;
		this.navigator = navigator;
		
		getContentPane().add(mDs = new MaskDatasource());
		bar.add(cbxConnect = new JCheckBox("connect"),0);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)navigator.getSelectionPath().getLastPathComponent();
		if(editmode)
			mDs.load(uoDs = (UoDatasource)node.getUserObject());
		else
			mDs.load(uoDs = new UoDatasource((UoDriver)node.getUserObject()));
		
		mDs.setEnabled(!uoDs.isConnected());
		cbxConnect.setSelected(uoDs.isConnected());
	}
	
	protected boolean onConfirm()
	{
		boolean bContinue;
		
		if(bContinue = mDs.unload(uoDs))
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			try
			{
				if(cbxConnect.isSelected() && !uoDs.isConnected())
				{
					uoDs.connect();
					Application.application.connectionOpened(uoDs.getKey());
				}
				else if(!cbxConnect.isSelected() && uoDs.isConnected())
				{
					uoDs.disconnect();
					Application.application.connectionClosed(uoDs.getKey());
				}
				
				if(!editmode)
					navigator.add(uoDs);
				else
					navigator.reloadSelection();
			}
			catch(Exception e)
			{
				Application.println(e,true);
				bContinue = false;
			}
			finally
			{
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
		
		return bContinue;
	}

	protected void onOpen()
	{
	}
}