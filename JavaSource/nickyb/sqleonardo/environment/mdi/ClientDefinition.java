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

package nickyb.sqleonardo.environment.mdi;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.DefinitionPane;
import nickyb.sqleonardo.environment.ctrl.content.AbstractActionContent;
import nickyb.sqleonardo.environment.ctrl.content.TableMetaData;
import nickyb.sqleonardo.environment.ctrl.explorer.DialogChooseColumns;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class ClientDefinition extends MDIClient
{
	public static final String DEFAULT_TITLE = "DEFINITION";
	
	private DefinitionPane control;
	private JMenuItem[] m_actions;
	
	private String type;
	
	public ClientDefinition(String keycah, QueryTokens.Table table, String type)
	{
		super(DEFAULT_TITLE + " : " + table.getIdentifier() + " : " + keycah);
		setMaximizable(true);
		setResizable(true);
		
		this.type = type;
		
		setComponentCenter(control = new DefinitionPane(keycah,table));
		control.setBorder(new EmptyBorder(2,2,2,2));
		
		initMenuActions();
	}

	private void initMenuActions()
	{
		m_actions = new JMenuItem[]
		{
			new JMenuItem(new ActionChooseColumns()),
			null,
			new JMenuItem(new ActionCopyList()),
			new JMenuItem(new ActionRefreshList()),
			null,			
//			MDIMenubar.createItem(new MDIActions.Dummy("export lists...")),
//			null,
			new JMenuItem(new ActionShowContent())
		};
	}

	public JMenuItem[] getMenuActions()
	{
		return m_actions;
	}

	public Toolbar getSubToolbar()
	{
		return null;
	}
    
	protected void setPreferences()
	{
	}

	private class ActionChooseColumns extends AbstractAction
	{
		private ActionChooseColumns()
		{
			putValue(NAME,"choose columns...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			int i = ClientDefinition.this.getTitle().lastIndexOf(':');
			int j = ClientDefinition.this.getTitle().lastIndexOf('.');
			
			String dvname = ClientDefinition.this.getTitle().substring(i+2,j);
			String mvname = ClientDefinition.this.control.getSelectedTitle();
			
			new DialogChooseColumns(dvname,mvname).show();
		}
	}
	
	private class ActionCopyList extends AbstractAction
	{
		private ActionCopyList()
		{
			putValue(NAME,"copy list");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			ClientDefinition.this.control.getSelectedView().copyAllRows();
		}
	}

	private class ActionRefreshList extends AbstractAction
	{
		private ActionRefreshList()
		{
			putValue(NAME,"refresh list");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			ClientDefinition.this.control.getSelectedView().reset();
			ClientDefinition.this.control.stateChanged(null);
		}
	}

//	private class ActionExportList extends AbstractAction
//	{
//		private ActionExportList()
//		{
//			putValue(NAME,"export list...");
//		}
//        
//		public void actionPerformed(ActionEvent ae)
//		{
//			ListView lv = null;
//			if(MetadataExplorer.this.tp.getSelectedIndex() == 0)
//			{
//				int last = navigator.getRightView().getComponentCount() - 1;
//				lv = (ListView)navigator.getRightView().getComponent(last);
//			}
//			else
//				lv = (ListView)search.getRightView();
//			
//			new DialogExport(lv).show();
//		}
//	}	
	
	private class ActionShowContent extends AbstractActionContent
	{
		ActionShowContent(){this.putValue(NAME,"show content");}
		
		protected TableMetaData getTableMetaData()
		{
			int i = ClientDefinition.this.getTitle().indexOf(':');
			int j = ClientDefinition.this.getTitle().lastIndexOf(':');
			
			String keycah = ClientDefinition.this.getTitle().substring(j+2).trim();
			String name	= ClientDefinition.this.getTitle().substring(i+2,j).trim();
			
			String schema = null;
			if((i = name.indexOf('.')) != -1)
			{
				schema = name.substring(0,i);
				name = name.substring(i+1);
			}			
			
			return new TableMetaData(keycah,schema,name,ClientDefinition.this.type);
		}

		protected void onActionPerformed(int records, int option)
		{
			if(option == JOptionPane.CANCEL_OPTION || (records == 0 && option == JOptionPane.NO_OPTION)) return;
			boolean retrieve = records > 0 && option == JOptionPane.YES_OPTION;
			
			String title = "CONTENT : " + this.getTableMetaData().getIdentifier() + " : " + this.getTableMetaData().getHandlerKey();
			Application.application.add(new ClientContent(title, this.getTableMetaData(),!this.getTableMetaData().getType().equals("TABLE"),retrieve));
		}
		
		protected int showConfirmDialog(int records)
		{
			if(records == 0)
			{
				String message = this.getDefaultMessage(records) + "\ndo you want continue?";
				return JOptionPane.showConfirmDialog(Application.application,message,"show content",JOptionPane.YES_NO_OPTION);
			}
			else
			{
				String message = this.getDefaultMessage(records) + "\ndo you want retrieve?";
				return JOptionPane.showConfirmDialog(Application.application,message,"show content",JOptionPane.YES_NO_CANCEL_OPTION);
			}
		}
	}
}