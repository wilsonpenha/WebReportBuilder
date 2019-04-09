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

package nickyb.sqleonardo.querybuilder;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JSplitPane;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class QueryBuilder extends BorderLayoutPanel
{
	public static boolean autoJoin = true;
	public static boolean autoAlias = true;
	public static boolean useAlwaysQuote = true;

	/* querybuilder.objetctype.TABLE */
	public static boolean loadObjectsAtOnce = true;
	
	public static String identifierQuoteString 	= "\"";
	public static int maxColumnNameLength = 0;
	
	private Connection connection;
	private QueryModel model = new QueryModel();

	ViewBrowser browser;
	ViewDiagram diagram;
	ViewObjects objects;
	
	public QueryBuilder()
	{
		this(null);
	}

	public QueryBuilder(Connection connection)
	{
		super(2,2);
		
		QueryActions.init(this);
		
		this.initComponents();
		this.setConnection(connection);
		
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt)
			{
				JSplitPane split = (JSplitPane)QueryBuilder.this.getComponent(0);
				JSplitPane split2 = (JSplitPane)split.getLeftComponent();
                // Value changed to 0.5 by Giulio Toffoli
				split2.setDividerLocation(0.5);
				split2.validate();
			}
		});
		
		this.transferFocus();
	}
	
	private void initComponents()
	{
		browser = new ViewBrowser(this);
		diagram = new ViewDiagram(this);
		objects = new ViewObjects(this);
		
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split2.setOneTouchExpandable(true);
		split2.setDividerLocation(250);
		split2.setLeftComponent(browser);
		split2.setRightComponent(objects);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setTopComponent(split2);
		split.setBottomComponent(diagram);
		split.setOneTouchExpandable(true);
		
		setComponentCenter(split);
	}
	
	public boolean isDragAndDropEnabled()
	{
		return diagram.isDragAndDropEnabled();
	}
	
	public void setDragAndDropEnabled(boolean b)
	{
		diagram.setDragAndDropEnabled(b);
	}
	
	public QueryModel getModel()
	{
		return model;
	}
	
	public void setModel(QueryModel qm)
	{
		model = qm;
		diagram.onModelChanged();
		browser.onModelChanged();
		objects.onModelChanged();
	}
	
	public Connection getConnection()
	{
		return connection;
	}
	
	public void setConnection(Connection connection)
	{
		try
		{
			this.connection = connection;
			
			if(connection!=null)
			{
				QueryBuilder.identifierQuoteString = connection.getMetaData().getIdentifierQuoteString();
				QueryBuilder.maxColumnNameLength = connection.getMetaData().getMaxColumnNameLength();
			}
			
			objects.onConnectionChanged();
		}
		catch(SQLException sqle)
		{
			System.out.println("[ QueryBuilder::setConnection ]\n" + sqle);
		}
	}
	
	void onLoad()
	{
		load(browser.getQuerySpecification().getFromClause());
		load(browser.getQuerySpecification().getSelectList());
	}
	
	private void load(QueryTokens._Expression[] tokens)
	{
		for(int i=0; i<tokens.length; i++)
		{
			if(tokens[i] instanceof QueryTokens.Column)
			{
				QueryTokens.Column token = (QueryTokens.Column)tokens[i];
				
				DiagramEntity entity = diagram.getEntity(token.getTable());
				if(entity!=null)
				{
					DiagramField field = entity.getField(token.getName());
					if(field!=null)
						field.setQueryToken(token);
				}				
			}
		}
	}
	
	private void load(QueryTokens._TableReference[] tokens)
	{
		for(int i=0; i<tokens.length; i++)
		{
			if(tokens[i] instanceof QueryTokens.Table)
			{
				DiagramLoader.run(DiagramLoader.DEFAULT,this,(QueryTokens.Table)tokens[i],false);
			}
			else
			{
				QueryTokens.Join token = (QueryTokens.Join)tokens[i];
				
				DiagramEntity entityP = diagram.getEntity(token.getPrimary().getTable());
				if(entityP == null)
				{
					DiagramLoader.run(DiagramLoader.DEFAULT,this,token.getPrimary().getTable(),false);
					entityP = diagram.getEntity(token.getPrimary().getTable());
				}
				
				DiagramEntity entityF = diagram.getEntity(token.getForeign().getTable());
				if(entityF == null)
				{
					DiagramLoader.run(DiagramLoader.DEFAULT,this,token.getForeign().getTable(),false);
					entityF = diagram.getEntity(token.getForeign().getTable());
				}
				
				DiagramField fieldP = entityP.getField(token.getPrimary().getName());
				DiagramField fieldF = entityF.getField(token.getForeign().getName());
				
				if(fieldP!=null && fieldF!=null)
				{
					diagram.join(entityP,fieldP);
					diagram.join(entityF,fieldF);
					
					DiagramRelation relation = diagram.getRelation(token);
					if(relation!=null)
						relation.setQueryToken(token);
				}
			}
		}
	}
}