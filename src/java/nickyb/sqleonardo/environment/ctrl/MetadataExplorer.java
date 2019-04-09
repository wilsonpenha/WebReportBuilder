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

package nickyb.sqleonardo.environment.ctrl;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.gui.ListView;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.util.Classpath;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.explorer.DialogChooseColumns;
import nickyb.sqleonardo.environment.ctrl.explorer.DialogDatasource;
import nickyb.sqleonardo.environment.ctrl.explorer.DialogDriver;
import nickyb.sqleonardo.environment.ctrl.explorer.DialogExport;
import nickyb.sqleonardo.environment.ctrl.explorer.SideNavigator;
import nickyb.sqleonardo.environment.ctrl.explorer.SideSearchCriteria;
import nickyb.sqleonardo.environment.ctrl.explorer.UoDatasource;
import nickyb.sqleonardo.environment.ctrl.explorer.UoDriver;
import nickyb.sqleonardo.environment.ctrl.explorer.UoLinks;

public class MetadataExplorer extends BorderLayoutPanel implements ChangeListener,TreeSelectionListener
{
	private CardLayout cv;
	private JTabbedPane tp;
	
	private SideNavigator navigator;
	private SideSearchCriteria search;
	
	public MetadataExplorer()
	{
		super(2,2);
		
		this.getActionMap().put("new-driver"	,new ActionNewDriver());
		this.getActionMap().put("new-datasorce"	,new ActionNewDatasource());
		this.getActionMap().put("delete"		,new ActionDelete());
		this.getActionMap().put("properties"	,new ActionProperties());
		
		/* objects view */
		this.getActionMap().put("choose-columns",new ActionChooseColumns());
		this.getActionMap().put("list-copy"		,new ActionCopyList());
		this.getActionMap().put("list-export"	,new ActionExportList());
		this.getActionMap().put("list-refresh"	,new ActionRefreshList());
		
		initComponents();
	    loadNavigator();
	}

	private void initComponents()
	{
		navigator = new SideNavigator();
		search = new SideSearchCriteria();
		
		JSplitPane split = new JSplitPane();
		split.setDividerLocation(250);
		split.setOneTouchExpandable(true);
		setComponentCenter(split);
		
		Container container = new Container();
		container.setLayout(cv = new CardLayout());
		container.add("first", navigator.getRightView());
		container.add("last", search.getRightView());
		split.setRightComponent(container);
		
		tp = new JTabbedPane(JTabbedPane.BOTTOM);
		tp.addTab("browse",navigator);
		tp.addTab("search",search);
		tp.addChangeListener(this);
		split.setLeftComponent(tp);        
	}
	
	public SideNavigator getNavigator()
	{
		return navigator;
	}
	
	private void loadNavigator()
	{
		Application.session.mount(Application.ENTRY_JDBC);
		Application.session.home();
		for(Enumeration eDv = Application.session.jumps(); eDv.hasMoreElements();)
		{
			UoDriver uoDv = new UoDriver();
			uoDv.name = eDv.nextElement().toString();
			
			Application.session.jump(uoDv.name);
			
			Object[] dvInfo = (Object[])Application.session.jump().get(0);
			uoDv.library	= dvInfo[0] == null ? "" : dvInfo[0].toString();
			uoDv.classname	= dvInfo[1] == null ? "" : dvInfo[1].toString();
			uoDv.example	= dvInfo[2] == null ? "" : dvInfo[2].toString();
			
			navigator.add(uoDv);
			
			try
			{
				ConnectionAssistant.declare(uoDv.library,uoDv.classname,!Classpath.isRuntime(uoDv.library));
			}
			catch (Exception e)
			{
				//Application.println(e,false);
				e.printStackTrace();
			}
			
			for(Enumeration eDs = Application.session.jumps(); eDs.hasMoreElements();)
			{
				UoDatasource uoDs = new UoDatasource(uoDv);
				uoDs.name = eDs.nextElement().toString();
				navigator.add(uoDs);
				
				Application.session.home();
				Application.session.jump(new String[]{uoDv.name,uoDs.name});
			
				Object[] dsInfo = (Object[])Application.session.jump().get(0);
				uoDs.url = dsInfo[0] == null ? "" : dsInfo[0].toString();
				uoDs.uid = dsInfo[1] == null ? "" : dsInfo[1].toString();
				
				/* reload password */
				if(dsInfo.length == 3 && (uoDs.remember = dsInfo[2]!=null) )
					uoDs.pwd = dsInfo[2].toString();
				else
					uoDs.pwd = "";
				
				/* links */
				UoLinks uoLk = (UoLinks)navigator.getSelectionNode().getLastLeaf().getUserObject();
				for(Enumeration eLk = Application.session.jumps(); eLk.hasMoreElements();)
				{
					String group = eLk.nextElement().toString();
				
					Application.session.home();
					Application.session.jump(new String[]{uoDv.name,uoDs.name,group});
					
					for(int i=0; i<Application.session.jump().size(); i++)
					{
						Object[] link = (Object[])Application.session.jump().get(i);
						uoLk.add(group, (link[0] == null ? null : link[0].toString()), link[1].toString(), link[2].toString());
					}
				}
			}
			Application.session.home();
		}
		
		navigator.clearSelection();
		navigator.addTreeSelectionListener(this);
		navigator.putClientProperty("JTree.lineStyle", "Angled");

	}

	public void unloadNavigator()
	{
		Application.session.umount(Application.ENTRY_JDBC);
		Application.session.mount(Application.ENTRY_JDBC);
		
		DefaultMutableTreeNode root = navigator.getRootNode();
		for(int i=0; i<root.getChildCount();i++)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(i);
			UoDriver uoDv = (UoDriver)node.getUserObject();
			
			Application.session.jump(uoDv.name).add(new String[]{uoDv.library,uoDv.classname,uoDv.example});		
			for(int j=0; j<node.getChildCount();j++)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getChildAt(j);
				UoDatasource uoDs = (UoDatasource)child.getUserObject();
				
				if(uoDs.isConnected())
				{
					try
					{
						uoDs.disconnect();
					}
					catch(Exception e)
					{
						Application.println(e,false);
					}
				}
				
				Application.session.home();
				Application.session.jump(new String[]{uoDv.name,uoDs.name});
				Application.session.jump().add(new String[]{uoDs.url,uoDs.uid,(uoDs.remember?uoDs.pwd:null)});
				
				/* links */
				UoLinks uoLk = (UoLinks)child.getLastLeaf().getUserObject();
				Iterator iG = uoLk.getGroups().iterator();
				while(iG.hasNext())
				{
					String group = iG.next().toString();
				
					Application.session.home();
					Application.session.jump(new String[]{uoDv.name,uoDs.name,group});
					
					Iterator iK = uoLk.getLinks(group).iterator();
					while(iK.hasNext())
					{
						Application.session.jump().add(iK.next());
					}
				}
			}
			Application.session.home();
		}
	}
	
	public void setPreferences()
	{
		navigator.setPreferences();
		search.setPreferences();
	}

	private void onSomethingChanged()
	{
		boolean bUoSelected = !navigator.isSelectionEmpty() && navigator.getSelectionPath().getPathCount()<4;
		boolean bListLoaded = !navigator.isSelectionEmpty() && !navigator.getSelectionNode().getAllowsChildren();
		
		this.getActionMap().get("new-driver").setEnabled(tp.getSelectedIndex() == 0);
		this.getActionMap().get("new-datasorce").setEnabled(tp.getSelectedIndex() == 0);
		this.getActionMap().get("delete").setEnabled(tp.getSelectedIndex() == 0 && bUoSelected);
		this.getActionMap().get("properties").setEnabled(tp.getSelectedIndex() == 0 && bUoSelected);
		
		if(!navigator.isSelectionEmpty() && navigator.getSelectionNode().getUserObject() instanceof UoLinks)
			this.getActionMap().get("choose-columns").setEnabled(false);
		else
			this.getActionMap().get("choose-columns").setEnabled(tp.getSelectedIndex() == 0 && bListLoaded);
		
		boolean bList = (tp.getSelectedIndex() == 0 && bListLoaded) || (tp.getSelectedIndex() == 1);
		this.getActionMap().get("list-copy").setEnabled(bList);
		this.getActionMap().get("list-export").setEnabled(bList);
		
		this.getActionMap().get("list-refresh").setEnabled(tp.getSelectedIndex() == 0 && bListLoaded);

	}
	
	private void showFirst()
	{
		cv.first((Container)((JSplitPane)this.getComponent(0)).getRightComponent());
	}
    
	private void showLast()
	{
		cv.last((Container)((JSplitPane)this.getComponent(0)).getRightComponent());
	}
	
	public void stateChanged(ChangeEvent ce)
	{
		if(tp.getSelectedIndex() == 0)
			showFirst();
		else
			showLast();
		
		onSomethingChanged();
	}
	
	public void valueChanged(TreeSelectionEvent tse)
	{
		onSomethingChanged();
	}
	
	private class ActionNewDriver extends AbstractAction
	{
		private ActionNewDriver()
		{
			putValue(NAME,"new driver...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			DialogDriver dlg = new DialogDriver(navigator,false);
			dlg.show();
		}
	}
	
	private class ActionNewDatasource extends AbstractAction
	{
		private ActionNewDatasource()
		{
			putValue(NAME,"new datasource...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			if(navigator.isSelectionEmpty())
			{
				Application.alert(Application.PROGRAM,"select one driver!");
				return;
			}
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)navigator.getSelectionPath().getPathComponent(1);
			navigator.setSelectionNode(node);
		
			DialogDatasource dlg = new DialogDatasource(navigator,false);
			dlg.show();
		}
	}
	
	private class ActionDelete extends AbstractAction
	{
		private ActionDelete()
		{
			putValue(NAME,"delete");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			MetadataExplorer.this.navigator.remove();
		}
	}
	
	private class ActionProperties extends AbstractAction
	{
		private ActionProperties()
		{
			putValue(NAME,"properties...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			if(navigator.isSelectionEmpty()) return;
			
			DefaultMutableTreeNode node = navigator.getSelectionNode();
			if(node.getUserObject() instanceof UoDriver)
			{
				DialogDriver dlg = new DialogDriver(navigator,true);
				dlg.show();
			}
			else if(node.getUserObject() instanceof UoDatasource)
			{
				DialogDatasource dlg = new DialogDatasource(navigator,true);
				dlg.show();
			}
		}
	}
	
	private class ActionChooseColumns extends AbstractAction
	{
		private ActionChooseColumns()
		{
			putValue(NAME,"choose columns...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			String dvname = MetadataExplorer.this.navigator.getSelectionNode().getPath()[1].toString();
			new DialogChooseColumns(dvname,"table types").show();
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
			if(MetadataExplorer.this.tp.getSelectedIndex() == 0)
			{
				for(int i=0; i<navigator.getRightView().getComponentCount(); i++)
				{
					Component c = navigator.getRightView().getComponent(i);					
					if(c.isShowing() && c instanceof ListView) ((ListView)c).copyAllRows();
				}
			}
			else
				((ListView)search.getRightView()).copyAllRows();
		}
	}
	
	private class ActionExportList extends AbstractAction
	{
		private ActionExportList()
		{
			putValue(NAME,"export list...");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			ListView lv = null;
			if(MetadataExplorer.this.tp.getSelectedIndex() == 0)
			{
				int last = navigator.getRightView().getComponentCount() - 1;
				lv = (ListView)navigator.getRightView().getComponent(last);
			}
			else
				lv = (ListView)search.getRightView();
			
			new DialogExport(lv).show();
		}
	}
	
	private class ActionRefreshList extends AbstractAction
	{
		private ActionRefreshList()
		{
			putValue(NAME,"refresh");
			putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_F5,0));
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			TreePath path = MetadataExplorer.this.navigator.getSelectionPath();
			MetadataExplorer.this.navigator.clearSelection();
			MetadataExplorer.this.navigator.setSelectionPath(path);
		}
	}
}