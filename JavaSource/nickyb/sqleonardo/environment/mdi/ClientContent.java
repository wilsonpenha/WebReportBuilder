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
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.ContentPane;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;
import nickyb.sqleonardo.environment.ctrl.content.DialogStream;
import nickyb.sqleonardo.environment.ctrl.content.TaskRetrieve;

public class ClientContent extends MDIClient
{
	public static final String DEFAULT_TITLE = "content";	
	
	private ContentPane control;
	private JMenuItem[] m_actions;
	private Toolbar toolbar;
	
	public ClientContent(String title, _TaskSource query, boolean readonly, boolean retrieve)
	{
		super((title == null ? DEFAULT_TITLE : title));
		setMaximizable(true);
		setResizable(true);
		
		setComponentCenter(control = new ContentPane(query,readonly,retrieve));
		control.setBorder(new EmptyBorder(2,2,2,2));
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		InternalFrameListener ifl = new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent evt)
			{
				if(!ClientContent.this.control.isBusy()
				&& !ClientContent.this.control.isReadOnly()
				&& ClientContent.this.control.getView().getChanges().count() > 0)
				{
					int option = JOptionPane.showConfirmDialog(Application.application,"do you want save changes?",Application.PROGRAM,JOptionPane.YES_NO_CANCEL_OPTION);
					
					if(option == JOptionPane.YES_OPTION)
						ClientContent.this.control.getActionMap().get("changes-save").actionPerformed(null);
					else if(option == JOptionPane.CANCEL_OPTION)
						return;
				}
				
				ClientContent.this.control.onEndTask();
				ClientContent.this.dispose();
			}
		};
		addInternalFrameListener(ifl);
		
		createToolbar();
		initMenuActions();
	}
    
	private void createToolbar()
	{
		toolbar = new Toolbar(Toolbar.HORIZONTAL);
		toolbar.add(control.getActionMap().get("changes-save"));
		toolbar.addSeparator();
		toolbar.add(control.getActionMap().get("record-insert"));
		toolbar.add(control.getActionMap().get("record-delete"));
		toolbar.addSeparator();
		toolbar.add(control.getActionMap().get("stop-task"));
        
		setComponentEast(toolbar);
	}

	private void initMenuActions()
	{
		m_actions = new JMenuItem[]
		{
			MDIMenubar.createItem(control.getActionMap().get("changes-show")),
			null,
			MDIMenubar.createItem(new ActionShowExport()),
			MDIMenubar.createItem(new ActionShowImport()),
			null,
			MDIMenubar.createItem(new ActionRelaunch())
		};
	}
	
	protected String getMessage()
	{
		int rows = control.getView().getRowCount();
		if(rows == 0) return null;
		
		String first = control.getView().getValueAt(0,0).toString();
		String last = control.getView().getValueAt(rows-1,0).toString();
		
		return "block " + control.getView().getBlock() + " of " + control.getView().getBlockCount() + " - record(s) " + first + " to " + last + " of " + control.getView().getFlatRowCount();
	}

	public JMenuItem[] getMenuActions()
	{
		return m_actions;
	}

	public Toolbar getSubToolbar()
	{
		return toolbar;
	}
    
	protected void setPreferences()
	{
	}
	
	private class ActionRelaunch extends AbstractAction
	{
		ActionRelaunch()
		{
			this.putValue(NAME, "relaunch query");
			this.putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
		}

		public void actionPerformed(ActionEvent ae)
		{
			if(!ClientContent.this.control.isBusy())
			{
				TaskRetrieve task = new TaskRetrieve(ClientContent.this.control,ClientContent.this.control.getTaskSource());
				
				ClientContent.this.control.getView().reset();
				ClientContent.this.control.onBeginTask(task);
			}
		}
	}
	
	private class ActionShowImport extends AbstractAction
	{
		ActionShowImport()
		{
			this.putValue(NAME, "import...");
			setEnabled(!ClientContent.this.control.isReadOnly());
		}

		public void actionPerformed(ActionEvent ae)
		{
			int pos1 = ClientContent.this.getTitle().indexOf(':');
			int pos2 = ClientContent.this.getTitle().lastIndexOf(':');
			
			String content = ClientContent.this.getTitle().substring(pos1+1,pos2);
			DialogStream.showImport(ClientContent.this.control.getView(),content.trim());
		}
	}
	
	private class ActionShowExport extends AbstractAction
	{
		ActionShowExport()
		{
			this.putValue(NAME, "export...");
		}

		public void actionPerformed(ActionEvent ae)
		{
			int pos1 = ClientContent.this.getTitle().indexOf(':');
			int pos2 = ClientContent.this.getTitle().lastIndexOf(':');
			
			String name = ClientContent.this.getTitle().substring(pos1+1,pos2);
			DialogStream.showExport(ClientContent.this.control.getView(),name.trim());
		}
	}
}
