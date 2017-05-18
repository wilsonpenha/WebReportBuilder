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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.environment._Constants;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public abstract class MDIActions implements _Constants
{
    public static abstract class AbstractBase extends AbstractAction
    {
		public AbstractBase(){super();}
		public AbstractBase(String text){super(text);}
    	
		protected void setAccelerator(KeyStroke stroke)
		{
			putValue(ACCELERATOR_KEY,stroke);
		}

        protected void setIcon(String iconkey)
        {
        	putValue(SMALL_ICON,Application.resources.getIcon(iconkey));
        }
        
        protected void setText(String text)
        {
        	putValue(NAME,text);
        }
        
        protected void setTooltip(String text)
        {
        	putValue(SHORT_DESCRIPTION,text);
        }
    }

	public final static class Dummy extends AbstractAction
	{
		public Dummy(String text){super(text);}
		
		public void actionPerformed(ActionEvent ae)
		{
			Application.alert(Application.PROGRAM,"not implemented!");
		}
	}
    
	public static class NewQuery extends AbstractBase
	{
		public NewQuery(){super(I18n.getString("application.menu.newQuery","new query"));}
		public void actionPerformed(ActionEvent ae)
		{
			if(!ConnectionAssistant.getHandlers().isEmpty())
			{
				Object keycah = null;
				if(ConnectionAssistant.getHandlers().size() > 1)
					keycah = JOptionPane.showInputDialog(Application.application,I18n.getString("application.message.useConnection","use connection:"),Application.PROGRAM,JOptionPane.PLAIN_MESSAGE,null,ConnectionAssistant.getHandlers().toArray(),null);
				else
					keycah = ConnectionAssistant.getHandlers().toArray()[0];
				
				if(keycah != null)
				{
					QueryModel qm = new QueryModel();
					if(!Preferences.getBoolean("querybuilder.use-schema"))
					{
						ConnectionHandler ch = ConnectionAssistant.getHandler(keycah.toString());
						ArrayList schemas = (ArrayList)ch.getObject("$schema_names");
						if(schemas.size()>0)
						{
							Object schema = JOptionPane.showInputDialog(Application.application,I18n.getString("application.message.schema","schema:"),Application.PROGRAM,JOptionPane.PLAIN_MESSAGE,null,schemas.toArray(),null);
							if(schema == null) return;
							qm.setSchema(schema.toString());
						}
					}
				
					ClientQueryBuilder cqb = new ClientQueryBuilder(keycah.toString());
					cqb.setModel(qm);
					Application.application.add(cqb);
				}
			}
			else
				Application.alert(Application.PROGRAM,I18n.getString("application.message.noConnection","No connection!"));
		}
	}
    
	public static class LoadQuery extends AbstractBase
	{
		public LoadQuery(){super(I18n.getString("application.menu.loadQuery","load query..."));}
		
		private void setSchema(String schema, QueryExpression qe)
		{
			if(qe == null) return;
			
			QueryTokens._Base[] tokens = qe.getQuerySpecification().getSelectList();
			for(int i=0; i<tokens.length; i++)
			{
				if(tokens[i] instanceof QueryTokens.Column)
				{
					((QueryTokens.Column)tokens[i]).getTable().setSchema(schema);
				}
			}
			
			tokens = qe.getQuerySpecification().getFromClause();
			for(int i=0; i<tokens.length; i++)
			{
				if(tokens[i] instanceof QueryTokens.Join)
				{
					((QueryTokens.Join)tokens[i]).getPrimary().getTable().setSchema(schema);
					((QueryTokens.Join)tokens[i]).getForeign().getTable().setSchema(schema);
				}
				else
					((QueryTokens.Table)tokens[i]).setSchema(schema);
			}
			
			setSchema(schema,qe.getUnion());
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			Object[] ret = DialogQuery.showLoad();
			if(ret[0]!=null && ret[1]!=null && ret[2]!=null)
			{
				ClientQueryBuilder cqb = new ClientQueryBuilder(ret[2].toString());
				cqb.setFileName(ret[0].toString());
				
				QueryModel qm = (QueryModel)ret[1];
				
				/* gestire schema */
				if(Preferences.getBoolean("querybuilder.use-schema"))
				{
					if(qm.getSchema()==null)
					{
						if(ret[3]!=null)
						{
							int option = JOptionPane.showConfirmDialog(Application.application,"do you want to apply '" + ret[3] + "' schema on all elements?",Application.PROGRAM,JOptionPane.YES_NO_CANCEL_OPTION);
						
							if(option == JOptionPane.YES_OPTION)
								setSchema(ret[3].toString(),qm.getQueryExpression());
							else if(option == JOptionPane.CANCEL_OPTION)
								return;
						}
					}
				}
				else
				{
					if(qm.getSchema()==null)
					{
						if(ret[3]!=null)
						{
							qm.setSchema(ret[3].toString());
							setSchema(null,qm.getQueryExpression());
						}
					}
					else if(ret[3]!=null)
						qm.setSchema(ret[3].toString());
				}

				Application.application.add(cqb);
				cqb.setModel(qm);
			}
		}
	}
	
	public static class Exit extends AbstractBase
	{
		public Exit(){super(I18n.getString("application.menu.exit","exit"));}
        
		public void actionPerformed(ActionEvent ae)
		{
			Application.shutdown();
		}
	}

	public static class ShowContent extends AbstractBase
	{
		public ShowContent(){super(I18n.getString("application.menu.content","show content..."));}
        
		public void actionPerformed(ActionEvent ae)
		{
		}
	}
	
	public static class ShowDefinition extends AbstractBase
	{
		public ShowDefinition(){super(I18n.getString("application.menu.definition","show definition..."));}
        
		public void actionPerformed(ActionEvent ae)
		{
		}
	}
	
		
	public static class ShowPreferences extends AbstractBase
	{
		public ShowPreferences()
		{
			super("preferences...");
			setIcon(ICON_PREFERENCES);
			setTooltip("edit preferences");
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			new DialogPreferences().show();
		}
	}
    
    public static abstract class AbstractShow extends AbstractBase
    {
		public abstract String getMDIClientName();
        
        public void actionPerformed(ActionEvent ae)
        {
        	Application.application.showClient(this.getMDIClientName());
        }
    }
    
    public static abstract class AbstractShowTool extends AbstractShow
    {
		public AbstractShowTool(KeyStroke ks, String iconKey)
		{
			setAccelerator(ks);
			setIcon(iconKey);
			setTooltip(this.getMDIClientName());
			setText("show " + this.getMDIClientName());
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			if(!Application.application.showClient(this.getMDIClientName()))
				Application.application.add(create());
		}
		
		protected abstract MDIClient create();
    }
    
    public static class ShowMetadataExplorer extends AbstractShowTool
    {
        public ShowMetadataExplorer()
        {
			super(KeyStroke.getKeyStroke(KeyEvent.VK_1,InputEvent.CTRL_MASK),ICON_EXPLORER);
        }
        
        public String getMDIClientName()
        {
			return ClientMetadataExplorer.DEFAULT_TITLE;
        }
        
		protected MDIClient create()
		{
			return new ClientMetadataExplorer();
		}        
    }
    
	public static class ShowCommandEditor extends AbstractShowTool
	{
		public ShowCommandEditor()
		{
			super(KeyStroke.getKeyStroke(KeyEvent.VK_2,InputEvent.CTRL_MASK),ICON_EDITOR);
		}
        
		public String getMDIClientName()
		{
			return ClientCommandEditor.DEFAULT_TITLE;
		}
		
		protected MDIClient create()
		{
			return new ClientCommandEditor();
		}        
	}

	public static class ShowSchemaComparer extends AbstractShowTool
	{
		public ShowSchemaComparer()
		{
			super(KeyStroke.getKeyStroke(KeyEvent.VK_3,InputEvent.CTRL_MASK),ICON_COMPARER);
		}
        
		public String getMDIClientName()
		{
			return ClientSchemaComparer.DEFAULT_TITLE;
		}
		
		protected MDIClient create()
		{
			return new ClientSchemaComparer();
		}        
	}

	public static class CascadeClients extends AbstractBase
	{
		public CascadeClients()
		{
			super(I18n.getString("application.menu.cascade","cascade"));
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			Application.application.cascadeClients();  
		}
	}
    
	public static class TileClients extends AbstractBase
	{
		public TileClients()
		{
			super(I18n.getString("application.menu.tileHorizontal","tile horizontal"));
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			Application.application.tileClients();
		}
	}
    
	public static class CloseAllClients extends AbstractBase
	{
		public CloseAllClients()
		{
			super(I18n.getString("application.menu.closeAll","close all"));
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			Application.application.closeAllClients();  
		}
	}
	
	public static class About extends AbstractBase
	{
		public About(){super(I18n.getFormattedString("application.menu.about","about {0}...", new Object[]{""+Application.PROGRAM}));}
        
		public void actionPerformed(ActionEvent ae)
		{
			  new DialogAbout().show();
		}
	}
}