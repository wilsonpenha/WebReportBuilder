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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.common.util.Text;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.environment.ctrl.CommandEditor;
import nickyb.sqleonardo.environment.ctrl.editor.DialogCommand;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.SQLParser;

public class ClientCommandEditor extends MDIClient implements _ConnectionListener
{
	public static final String DEFAULT_TITLE = "command editor";
		
	private CommandEditor control;
	private JMenuItem[] m_actions;
	private Toolbar toolbar;
	private JComboBox cbx;
	
	public ClientCommandEditor()
	{
		super(DEFAULT_TITLE);
		
		setComponentCenter(control = new CommandEditor());
		control.setBorder(new EmptyBorder(2,2,2,2));
		
		createToolbar();
		initMenuActions();
		
		Application.application.addListener(this);
	}

	private void createToolbar()
	{
		cbx = new JComboBox(ConnectionAssistant.getHandlers().toArray());
		
		toolbar = new Toolbar(Toolbar.HORIZONTAL);
		toolbar.add(new ActionOpen());
		toolbar.add(new ActionSave());
		toolbar.addSeparator();
		toolbar.add(control.getActionMap().get("start-task"));
		toolbar.add(control.getActionMap().get("stop-task"));
		toolbar.addSeparator();
		toolbar.add(new JLabel("use connection: "));
		toolbar.add(cbx);
        
		setComponentEast(toolbar);
	}
	
	private void initMenuActions()
	{
		m_actions = new JMenuItem[]
		{
			new JMenuItem(new ActionCommand()),
			null,
			new JMenuItem(new ActionClearInput()),
			new JMenuItem(new ActionClearOutput()),
			null,
			new JMenuItem(new ActionReverseSyntax()),
		};
	}
	
	public final CommandEditor getControl()
	{
		return control;
	}	

	public JMenuItem[] getMenuActions()
	{
		return m_actions;
	}

	public Toolbar getSubToolbar()
	{
		return toolbar;
	}

	public final String getName()
	{
		return DEFAULT_TITLE;
	}

	protected void setPreferences()
	{
	}

	public void onConnectionClosed(String keycah)
	{
		cbx.removeItem(keycah);
	}
	
	public void onConnectionOpened(String keycah)
	{
		cbx.addItem(keycah);
	}

	public String getActiveConnection()
	{
		return cbx.getSelectedIndex()==-1 ? null : cbx.getSelectedItem().toString();
	}

	public void setActiveConnection(String keycah)
	{
		cbx.setSelectedItem(keycah);
	}
	
	private class ActionCommand extends AbstractAction
	{
		ActionCommand()
		{
			this.putValue(NAME,"new command...");
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			if(ClientCommandEditor.this.getActiveConnection()!=null)
				new DialogCommand(ClientCommandEditor.this.getActiveConnection(),null).show();
			else
				Application.alert(Application.PROGRAM,"No connection!");
		}
	}
	
	private class ActionClearInput extends AbstractAction
	{
		ActionClearInput()
		{
			this.putValue(NAME, "clear request area");
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			ClientCommandEditor.this.control.setDocument(new PlainDocument());
		}
	}
	
	private class ActionClearOutput extends AbstractAction
	{
		ActionClearOutput()
		{
			this.putValue(NAME, "clear response area");
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			ClientCommandEditor.this.control.clearResponse();
		}
	}
	
	private class ActionOpen extends MDIActions.AbstractBase
	{
		private ActionOpen()
		{
			setIcon(Application.ICON_EDITOR_OPEN);
			setTooltip("open");
		}
    
		public void actionPerformed(ActionEvent ae)
		{
			String currentDirectory = Preferences.getString("lastDirectory",System.getProperty("user.home"));
			
			JFileChooser fc = new JFileChooser(currentDirectory);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setMultiSelectionEnabled(true);
		
			fc.setFileFilter(new FileFilter()
			{
				public boolean accept(File file)
				{
					return file.isDirectory() || file.getName().endsWith(".sql");
				}
				public String getDescription()
				{
					return "script files (*.sql)";
				}
			});
	
			if(fc.showOpenDialog(Application.application) == JFileChooser.APPROVE_OPTION)
			{
				Preferences.set("lastDirectory",fc.getCurrentDirectory().toString());
				PlainDocument doc = new PlainDocument();
				
				for(int i=0; i<fc.getSelectedFiles().length; i++)
				{
					String filename = fc.getSelectedFiles()[i].toString();
					try
					{
						load(doc,filename);
						doc.insertString(doc.getLength(),"\n",null);
					}
					catch(BadLocationException ble)
					{
						Application.println(ble,false);
					}
					catch(IOException ioe)
					{
						Application.println(ioe,false);
					}
				}
			
				ClientCommandEditor.this.control.setDocument(doc);
			}
		}
	
		private void load(PlainDocument doc, String filename) throws IOException, BadLocationException
		{
			Reader in = new FileReader(filename);
		
			int nch;
			char[] buff = new char[4096];
			while ((nch = in.read(buff, 0, buff.length)) != -1)
			{
				doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
			}
			in.close();
		}
	}
	
	private class ActionSave extends MDIActions.AbstractBase
	{
		private ActionSave()
		{
			setIcon(Application.ICON_EDITOR_SAVE);
			setTooltip("save");
		}
    
		public void actionPerformed(ActionEvent ae)
		{
			String currentDirectory = Preferences.getString("lastDirectory",System.getProperty("user.home"));
			
			JFileChooser fc = new JFileChooser(currentDirectory);
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
			fc.setFileFilter(new FileFilter()
			{
				public boolean accept(File file)
				{
					return file.isDirectory() || file.getName().endsWith(".sql");
				}
				public String getDescription()
				{
					return "script files (*.sql)";
				}
			});
		
			if(fc.showSaveDialog(Application.application) == JFileChooser.APPROVE_OPTION)
			{
				Preferences.set("lastDirectory",fc.getCurrentDirectory().toString());				
				String filename = fc.getSelectedFile().toString();
				
				if(fc.getFileFilter().getDescription().endsWith("(*.sql)"))
					if(!filename.endsWith(".sql")) filename += ".sql";
				
				save(filename);
			}
		}
	
		private void save(String filename)
		{
			try
			{
				Document doc = ClientCommandEditor.this.control.getDocument();
				Writer out = new FileWriter(filename);
				out.write(doc.getText(0,doc.getLength()));
				out.flush();
				out.close();
			}
			catch(BadLocationException ble)
			{
				Application.println(ble,false);
			}
			catch(IOException ioe)
			{
				Application.println(ioe,false);
			}
		}
	}
	
	private class ActionReverseSyntax extends AbstractAction
	{
		ActionReverseSyntax()
		{
			putValue(NAME,"reverse syntax");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			if(ClientCommandEditor.this.getActiveConnection()==null)
			{
				Application.alert(Application.PROGRAM,"No connection!");
				return;
			}
				
			String sql = ClientCommandEditor.this.control.getSelectedText();
			if(Text.isEmpty(sql))
			{
				Application.alert(Application.PROGRAM,"Nothing selected!");
				return;				
			}
			
			try
			{
				QueryModel qm = SQLParser.toQueryModel(sql);
				if(!Preferences.getBoolean("querybuilder.use-schema"))
				{
					ConnectionHandler ch = ConnectionAssistant.getHandler(ClientCommandEditor.this.getActiveConnection());
					ArrayList schemas = (ArrayList)ch.getObject("$schema_names");
					if(schemas.size()>0)
					{
						Object schema = JOptionPane.showInputDialog(Application.application,"schema:",Application.PROGRAM,JOptionPane.PLAIN_MESSAGE,null,schemas.toArray(),null);
						if(schema == null) return;
						qm.setSchema(schema.toString());
					}
				}
				
				ClientQueryBuilder cqb = new ClientQueryBuilder(ClientCommandEditor.this.getActiveConnection());				
				Application.application.add(cqb);
				cqb.setModel(qm);
			}
			catch (IOException e)
			{
				Application.println(e,true);
			}
		}
	}	
}
