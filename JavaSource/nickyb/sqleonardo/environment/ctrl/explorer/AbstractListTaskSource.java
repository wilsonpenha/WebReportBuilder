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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.gui.ListView;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.environment.ctrl.editor.DialogCommand;
import nickyb.sqleonardo.environment.ctrl.content.AbstractActionContent;
import nickyb.sqleonardo.environment.ctrl.content.TableMetaData;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.environment.mdi.ClientCommandEditor;
import nickyb.sqleonardo.environment.mdi.ClientContent;
import nickyb.sqleonardo.environment.mdi.ClientDefinition;
import nickyb.sqleonardo.environment.mdi.ClientMetadataExplorer;
import nickyb.sqleonardo.environment.mdi.ClientQueryBuilder;
import nickyb.sqleonardo.environment.mdi.MDIActions;

public abstract class AbstractListTaskSource extends ListView implements MouseListener
{
	private JLabel info;
	private JLabel counter;
	
	protected AbstractListTaskSource()
	{
		addMouseListener(this);
		
		BorderLayoutPanel statusbar = new BorderLayoutPanel(3,0);
		setComponentSouth(statusbar);
		
		statusbar.setComponentCenter(info = new JLabel("..."));
		info.setBorder(new CompoundBorder(LineBorder.createGrayLineBorder(), new EmptyBorder(2,4,2,4)));
		
		statusbar.setComponentEast(counter = new JLabel("objects : 0"));
		counter.setBorder(new CompoundBorder(LineBorder.createGrayLineBorder(), new EmptyBorder(2,4,2,4)));
	}
	
	public void addRow(Object[] rowdata)
	{
		super.addRow(rowdata);
		counter.setText("objects : " + this.getRowCount());
	}
	
	public void reset()
	{
		super.reset();
		
		if(counter!=null)
			counter.setText("objects : 0");
	}
	
	protected void setInfo(String s)
	{
		info.setText(s);
	}
	
	protected abstract String getHandlerKey();
	
	/* bad */
	protected Connection getConnection()
	{
		return ConnectionAssistant.hasHandler(getHandlerKey()) ? ConnectionAssistant.getHandler(getHandlerKey()).get() : null;
	}
	
	private int findColumn(String name)
	{
		for(int i=0; i<getJavaComponent().getColumnModel().getColumnCount(); i++)
		{
			if(getJavaComponent().getColumnModel().getColumn(i).getHeaderValue().equals(name)) return i;
		}
		
		return -1;
	}
	
	private static DefaultMutableTreeNode findChild(DefaultMutableTreeNode parent, String label, boolean like)
	{
		for(int i=0; i<parent.getChildCount(); i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)parent.getChildAt(i);
			
			if(like && child.getUserObject().toString().startsWith(label)) return child;
			if(!like && child.getUserObject().toString().equals(label)) return child;
		}
			
		return null;			
	}	
	
	protected String getTableSchema()
	{
		int idx = findColumn("TABLE_SCHEM");
		if(idx == -1) return null;
		
		Object schema	= this.getValueAt(this.getSelectedRow(),idx);
		return schema == null ? null : schema.toString();
	}
	
	protected String getTableName()
	{
		int idx = findColumn("TABLE_NAME");
		if(idx == -1) return null;
		
		Object name	= this.getValueAt(this.getSelectedRow(),idx);
		return name == null ? null : name.toString();
	}
	
	protected String getTableType()
	{
		int idx = findColumn("TABLE_TYPE");
		if(idx == -1) return null;
		
		Object type	= this.getValueAt(this.getSelectedRow(),idx);
		return type == null ? null : type.toString();
	}
	
	public void mouseClicked(MouseEvent me)
	{
		if(me.getClickCount() == 2)
		{
			if(this.getSelectedRow() == -1) return;
			
			if(this.getConnection() == null)
				Application.alert(Application.PROGRAM,"No connection!");
			else
				new ActionShowContent().actionPerformed(null);
		}
	}

	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mousePressed(MouseEvent me){}

	public void mouseReleased(MouseEvent me)
	{
		if(SwingUtilities.isRightMouseButton(me))
		{
			int row = getJavaComponent().rowAtPoint(me.getPoint());
			getJavaComponent().setRowSelectionInterval(row,row);
			
			JPopupMenu popup = new JPopupMenu();
			popup.add(new ActionQuery());
			popup.add(new ActionAddLink());
			popup.addSeparator();
			popup.add(new ActionCommand());
			popup.addSeparator();
			popup.add(new ActionDeleteContent());
			popup.add(new ActionDropObject());
			popup.addSeparator();
			popup.add(new ActionShowContent());
			popup.add(new ActionShowDefinition());
			
			popup.show(getJavaComponent(),me.getX(),me.getY());
		}
	}
	
//	/////////////////////////////////////////////////////////////////////////////
//	Popup Actions
//	/////////////////////////////////////////////////////////////////////////////
	protected class ActionQuery extends AbstractAction implements Runnable
	{
		ActionQuery()
		{
			super("add to new query");
		}
		
		public void run()
		{
			AbstractListTaskSource.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			String schema	= AbstractListTaskSource.this.getTableSchema();
			String name		= AbstractListTaskSource.this.getTableName();
		
			QueryModel model = new QueryModel(Preferences.getBoolean("querybuilder.use-schema") ? null : schema);
			QueryTokens.Table table = new QueryTokens.Table(Preferences.getBoolean("querybuilder.use-schema") ? schema : null,name);
			
			model.getQueryExpression().getQuerySpecification().addFromClause(table);
			
			ClientQueryBuilder client = new ClientQueryBuilder(AbstractListTaskSource.this.getHandlerKey());
			Application.application.add(client);
			client.setModel(model);
			
			AbstractListTaskSource.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			new Thread(this).start();
		}
	}

	protected class ActionAddLink extends AbstractAction
	{
		ActionAddLink()
		{
			super("add to group...");
		}
		
		public void actionPerformed(ActionEvent e)
		{
			int iDot = AbstractListTaskSource.this.getHandlerKey().indexOf('.');
			int iAt = AbstractListTaskSource.this.getHandlerKey().indexOf('@');
			
			String driver = AbstractListTaskSource.this.getHandlerKey().substring(0,iDot);
			String datasource = AbstractListTaskSource.this.getHandlerKey().substring(iDot+1,iAt);
			
			ClientMetadataExplorer cme = (ClientMetadataExplorer)Application.application.getClient(ClientMetadataExplorer.DEFAULT_TITLE);
			
			DefaultMutableTreeNode parent = cme.getControl().getNavigator().getRootNode();
			parent = AbstractListTaskSource.findChild(parent,driver,false);
			
			DefaultMutableTreeNode node = AbstractListTaskSource.findChild(parent,datasource,false);
			if(node==null) node = AbstractListTaskSource.findChild(parent,datasource,true);
			UoLinks uoLk = (UoLinks)node.getLastLeaf().getUserObject();
			
			Object group = null;
			if(uoLk.getGroups().size() == 0)
				group = Application.input(Application.PROGRAM,"group name:");
			else
				group = JOptionPane.showInputDialog(Application.application,"choose group:",Application.PROGRAM,JOptionPane.PLAIN_MESSAGE,null,uoLk.getGroups().toArray(),null);
			
			if(group!=null)
				uoLk.add(group.toString(),AbstractListTaskSource.this.getTableSchema(),AbstractListTaskSource.this.getTableName(),AbstractListTaskSource.this.getTableType());
		}
	}

	protected class ActionDeleteContent extends AbstractActionContent
	{
		ActionDeleteContent(){this.putValue(NAME,"delete content");}
		
		protected TableMetaData getTableMetaData()
		{
			return new TableMetaData(AbstractListTaskSource.this.getHandlerKey(),
									 AbstractListTaskSource.this.getTableSchema(),
									 AbstractListTaskSource.this.getTableName(),
									 AbstractListTaskSource.this.getTableType());
		}

		protected void onActionPerformed(int records, int option)
		{
			if(option == JOptionPane.NO_OPTION) return;
			
			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getTableMetaData().getHandlerKey());
				Statement stmt = ch.get().createStatement();
				stmt.executeUpdate("DELETE FROM " + this.getTableMetaData());
				stmt.close();
			}
			catch(SQLException sqle)
			{
				Application.println(sqle,true);
			}
		}
	}

	protected class ActionDropObject extends AbstractActionContent
	{
		ActionDropObject(){this.putValue(NAME,"drop <object>");}
		
		protected TableMetaData getTableMetaData()
		{
			return new TableMetaData(AbstractListTaskSource.this.getHandlerKey(),
									 AbstractListTaskSource.this.getTableSchema(),
									 AbstractListTaskSource.this.getTableName(),
									 AbstractListTaskSource.this.getTableType());
		}

		protected void onActionPerformed(int records, int option)
		{
			if(option == JOptionPane.NO_OPTION) return;
			
			try
			{
				ConnectionHandler ch = ConnectionAssistant.getHandler(this.getTableMetaData().getHandlerKey());
				Statement stmt = ch.get().createStatement();
				stmt.execute("DROP " + this.getTableMetaData().getType() + " " + this.getTableMetaData());
				stmt.close();
				
				AbstractListTaskSource.this.removeSelectedRow();
			}
			catch(SQLException sqle)
			{
				Application.println(sqle,true);
			}
		}
	}

	protected class ActionShowContent extends AbstractActionContent
	{
		ActionShowContent(){this.putValue(NAME,"show content");}
		
		protected TableMetaData getTableMetaData()
		{
			return new TableMetaData(AbstractListTaskSource.this.getHandlerKey(),
									 AbstractListTaskSource.this.getTableSchema(),
									 AbstractListTaskSource.this.getTableName(),
									 AbstractListTaskSource.this.getTableType());
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

	protected class ActionShowDefinition extends AbstractAction
	{
		ActionShowDefinition()
		{
			super("show definition");
		}
		
		public void actionPerformed(ActionEvent e)
		{
			String type 	= AbstractListTaskSource.this.getTableType();
			String schema	= AbstractListTaskSource.this.getTableSchema();
			String name		= AbstractListTaskSource.this.getTableName();
		
			Application.application.add(new ClientDefinition(AbstractListTaskSource.this.getHandlerKey(), new QueryTokens.Table(schema,name), type));
		}
	}

	protected class ActionCommand extends MDIActions.ShowCommandEditor
	{
		ActionCommand()
		{
			setText("new command...");
			setAccelerator(null);
			setIcon(null);
		}

		public void actionPerformed(ActionEvent e)
		{
			super.actionPerformed(e);
			
			String schema	= getTableSchema();
			String name		= getTableName();
			String keycah	= AbstractListTaskSource.this.getHandlerKey();
		
			((ClientCommandEditor)Application.application.getClient(ClientCommandEditor.DEFAULT_TITLE)).setActiveConnection(keycah);			
			new DialogCommand(AbstractListTaskSource.this.getHandlerKey(),new QueryTokens.Table(schema,name)).show();
		}
	}
}