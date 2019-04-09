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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.gui.ListView;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;

public class ViewConnection extends BorderLayoutPanel
{
	private JLabel			lblUrl;
	private JTextField		txtUid;
	private JPasswordField	txtPwd;
	
	private ListView lvMetaInfos;
	private StatusButton btnStatus;
	private SideNavigator navigator;
	
	public ViewConnection(SideNavigator navigator)
	{
		super(2,3);
		this.navigator = navigator;
		
		initInformationPane();
		initConnectionPane();
	}
	
	private void initInformationPane()
	{
		lvMetaInfos = new ListView();
		lvMetaInfos.setHeaderVisible(false);
		lvMetaInfos.addColumn("property");
		lvMetaInfos.addColumn("value");
		
		setComponentCenter(lvMetaInfos);
	}

	private void initConnectionPane()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout gbl = new GridBagLayout();
		JPanel pnl = new JPanel(gbl);
		pnl.setBorder(new CompoundBorder(LineBorder.createGrayLineBorder(),new EmptyBorder(2,2,2,2)));

		gbc.gridwidth	= GridBagConstraints.REMAINDER;
		gbc.fill		= GridBagConstraints.HORIZONTAL;
		gbc.weightx		= 1.0;
		gbc.insets		= new Insets(0,0,2,0);
		gbl.setConstraints(lblUrl = new JLabel("<url>",JLabel.CENTER),gbc);
		pnl.add(lblUrl);
		
		lblUrl.setOpaque(true);
		lblUrl.setBackground(java.awt.Color.gray);
		lblUrl.setForeground(java.awt.Color.white);
		
		gbc.gridwidth	= 1;
		gbc.weightx		= 0.0;
		gbc.insets	= new Insets(0,0,0,0);
		JLabel lbl = new JLabel("uid:");
		gbl.setConstraints(lbl,gbc);
		pnl.add(lbl);
		
		gbc.weightx	= 1.0;
		gbl.setConstraints(txtUid = new JTextField(),gbc);
		pnl.add(txtUid);
		
		gbc.weightx	= 0.0;
		gbc.insets	= new Insets(0,5,0,0);
		lbl = new JLabel("pwd:");
		gbl.setConstraints(lbl,gbc);
		pnl.add(lbl);
		
		gbc.weightx	= 1.0;
		gbc.insets	= new Insets(0,0,0,0);
		gbl.setConstraints(txtPwd = new JPasswordField(),gbc);
		pnl.add(txtPwd);
		
		gbc.weightx	= 0.0;
		gbc.insets	= new Insets(0,5,0,0);
		btnStatus = new StatusButton();		
		gbl.setConstraints(btnStatus,gbc);
		pnl.add(btnStatus);
		
		setComponentNorth(pnl);
	}

	public synchronized void list(DefaultMutableTreeNode node)
	{
		lvMetaInfos.removeAllRows();
		
		UoDatasource uo = (UoDatasource)node.getUserObject();
		
		lblUrl.setText(uo.url);
		txtUid.setText(uo.uid);
		txtPwd.setText(uo.remember?uo.pwd:null);
		
		txtUid.setEnabled(!uo.isConnected());
		txtPwd.setEnabled(!uo.isConnected());
		btnStatus.setSelected(uo.isConnected());
		
		if(uo.isConnected())
		{
			ConnectionHandler ch = ConnectionAssistant.getHandler(uo.getKey());
			
			ArrayList al = ch.getArrayList("$connection_infos");
			for(Iterator i = al.iterator(); i.hasNext();)
				lvMetaInfos.addRow((String[])i.next());
		}
	}
	
	private class StatusButton extends JToggleButton implements ActionListener
	{
		StatusButton()
		{
			this.addActionListener(this);			
			this.setSelected(false);
		}
		
		public void setSelected(boolean b)
		{
			super.setSelected(b);
			
			String iconKey = b ? Application.ICON_CONNECT:Application.ICON_DISCONNECT;
			this.setIcon(Application.resources.getIcon(iconKey));
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			ViewConnection.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			UoDatasource uoDs = (UoDatasource)ViewConnection.this.navigator.getSelectionNode().getUserObject();
			uoDs.uid = ViewConnection.this.txtUid.getText();
			uoDs.pwd = String.valueOf(ViewConnection.this.txtPwd.getPassword());
			
			try
			{
				if(this.isSelected() && !uoDs.isConnected())
				{
					uoDs.connect();
					Application.application.connectionOpened(uoDs.getKey());			
				}
				else if(!this.isSelected() && uoDs.isConnected())
				{
					uoDs.disconnect();
					Application.application.connectionClosed(uoDs.getKey());
				}
			
				ViewConnection.this.navigator.reloadSelection();
			}
			catch(Exception e)
			{
				Application.println(e,true);
			}
			finally
			{
				this.setSelected(uoDs.isConnected());
				ViewConnection.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
}
