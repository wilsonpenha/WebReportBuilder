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
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.querybuilder.QueryActions;
import nickyb.sqleonardo.querybuilder.QueryBuilder;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.SQLParser;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;
import nickyb.sqleonardo.environment.io.FileStreamSQL;
import nickyb.sqleonardo.environment.io.FileStreamXLQ;

public class ClientQueryBuilder extends MDIClient implements ChangeListener
{
	public static final String DEFAULT_TITLE = "QUERY";
	public static int counter = 0;
	
	private QueryBuilder builder;
	private JTextArea syntax;
	private JTabbedPane control;
	
    private JMenuItem[] m_actions;
	private Toolbar toolbar; 
    
    private String keycah = null;
    private String filename = null;
    
	public ClientQueryBuilder(String keycah)
    {
		super(DEFAULT_TITLE);
		setMaximizable(false);
		setResizable(false);
		
		this.keycah = keycah;
		
		setComponentCenter(control = new JTabbedPane(JTabbedPane.BOTTOM));
		control.add(I18n.getString("application.builder","builder"),builder = new QueryBuilder());
		control.add(I18n.getString("application.syntax","syntax"),new JScrollPane(syntax = new JTextArea()));
		control.addChangeListener(this);
		
		createToolbar();
		initMenuActions();
		
		if(keycah!=null) builder.setConnection(ConnectionAssistant.getHandler(keycah).get());
		setFileName(null);
		
		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameActivated(InternalFrameEvent ife)
			{
				ClientQueryBuilder.this.setQueryParameters();
			}
		});
    }
    
    private void createToolbar()
    {
		toolbar = new Toolbar(Toolbar.HORIZONTAL);
        toolbar.add(new ActionLaunch());
		toolbar.add(new ActionSave());
        
        setComponentEast(toolbar);
    }

	private void initMenuActions()
    {
		JCheckBoxMenuItem cbxm = new JCheckBoxMenuItem(builder.getActionMap().get(QueryActions.FIELDS_DRAGGABLE));
		cbxm.setSelected(builder.isDragAndDropEnabled());
    	
		m_actions = new JMenuItem[]
		{
			cbxm,
			null,
			new JMenuItem(builder.getActionMap().get(QueryActions.ENTITIES_ARRANGE)),
			new JMenuItem(builder.getActionMap().get(QueryActions.ENTITIES_PACK)),
			new JMenuItem(builder.getActionMap().get(QueryActions.ENTITIES_REMOVE)),
			null,
			new JMenuItem(builder.getActionMap().get(QueryActions.COPY_SYNTAX)),
		};
    }
    
	public JMenuItem[] getMenuActions()
	{
		return m_actions;
	}

	public Toolbar getSubToolbar()
	{
		return toolbar;
	}
    
	public void stateChanged(ChangeEvent ce)
	{
		m_actions[2].setEnabled(control.getSelectedIndex() == 0);
		m_actions[3].setEnabled(control.getSelectedIndex() == 0);
		m_actions[4].setEnabled(control.getSelectedIndex() == 0);
		
		if(control.getSelectedIndex() == 0)
		{
			String msql = builder.getModel().toString(true);
			String tsql = syntax.getText();
			
			if(!tsql.equals(msql))
			{
				if(Application.confirm(Application.PROGRAM,I18n.getString("application.syntaxChanged","syntax changed!\ndo you want to apply changes (builder need to reload)?")))
				{
					// this thread resolve: IllegalComponentStateException
				new Thread(new Runnable()
//					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							while(!builder.isVisible());
							try
							{
								QueryModel qm = SQLParser.toQueryModel(syntax.getText());
								builder.setModel(qm);
							}
							catch (IOException e)
							{
								Application.println(e,true);
							}
						}
//					});
				}).start();
				}
			}
		}
		else
			syntax.setText(builder.getModel().toString(true));
	}
	
    protected void setPreferences()
    {
    }

	public final void setFileName(String filename)
	{
		this.filename = filename;
		
		String filename2 = filename==null ? ("<untitled" + (++counter) + ">") : filename;
		super.setTitle(this.getID() + " - " + DEFAULT_TITLE + " : " + filename2 + " : " + keycah);
	}
	
	public final void setModel(QueryModel model)
	{
		builder.setModel(model);
	}

	private void setQueryParameters()
	{
		if(ConnectionAssistant.hasHandler(keycah))
		{
			QueryBuilder.identifierQuoteString = ConnectionAssistant.getHandler(keycah).getObject("$identifierQuoteString").toString();
			QueryBuilder.maxColumnNameLength = ((Integer)ConnectionAssistant.getHandler(keycah).getObject("$maxColumnNameLength")).intValue();
		}
	}
	
    private class ActionLaunch extends MDIActions.AbstractBase implements _TaskSource
    {
        private ActionLaunch()
        {
            super(I18n.getString("application.launchQuery","launch query"));
            setIcon(Application.ICON_QUERY_LAUNCH);
            setTooltip(I18n.getString("application.launchQuery","launch query"));
        }
        
        public void actionPerformed(ActionEvent ae)
        {
			if(ClientQueryBuilder.this.builder.getConnection()==null)
			{
				Application.alert(Application.PROGRAM,"no connection!");
			}
//			else if(ClientQueryBuilder.this.getModel()==null)
//			{
//				Application.alert(Application.PROGRAM,"query is empty!");
//			}
			else
			{
				int pos = ClientQueryBuilder.this.getTitle().indexOf(":");
				String subtitle = ClientQueryBuilder.this.getTitle().substring(pos);
				
				Application.application.add(new ClientContent("PREVIEW " + subtitle,this,true,true));
			}
        }
        
		public String getHandlerKey()
		{
			return ClientQueryBuilder.this.keycah;
		}
		
		public String getSyntax()
		{
			return ClientQueryBuilder.this.builder.getModel().toString();
		}
    }
    
	public class ActionSave extends MDIActions.AbstractBase
	{
		private ActionSave()
		{
			setText(I18n.getString("application.saveQuery","save query"));
			setIcon(Application.ICON_SAVE);
			setTooltip(I18n.getString("application.saveQuery","save query"));
		}
        
		private void saveAs()
		{
			Object[] ret = DialogQuery.showSave(ClientQueryBuilder.this.builder.getModel());
			if(ret[0]!=null) ClientQueryBuilder.this.setFileName(ret[0].toString());
		}
		
		private void replace()
		{
			try
			{
				String fn = ClientQueryBuilder.this.filename;
				if(fn.endsWith(".sql"))
				{
					FileStreamSQL.write(fn, ClientQueryBuilder.this.builder.getModel());
				}
				else
				{
					if(!fn.endsWith(".xlq")) fn += ".xlq";
					FileStreamXLQ.write(fn, ClientQueryBuilder.this.builder.getModel());
				}
				ClientQueryBuilder.this.setFileName(fn);
			}
			catch(Exception e)
			{
				Application.println(e,true);
				e.printStackTrace();
			}
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			if(ClientQueryBuilder.this.filename==null)
			{
				saveAs();
			}
			else
			{
				String message = I18n.getFormattedString("application.message.replaceFile","{0}\nreplace existing file?",new Object[]{""+ClientQueryBuilder.this.filename});
				int ret = JOptionPane.showConfirmDialog(Application.application,message,"query.save",JOptionPane.YES_NO_CANCEL_OPTION);
				
				if(ret == JOptionPane.YES_OPTION)
					replace();
				else if(ret == JOptionPane.NO_OPTION)
					saveAs();
			}
		}
	}
}