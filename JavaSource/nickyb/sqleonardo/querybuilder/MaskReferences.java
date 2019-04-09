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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.util.I18n;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class MaskReferences extends BaseMask
{
	private DiagramEntity item;
	
	private JList foreignTables;
	private JList primaryTables;
	
	public MaskReferences(DiagramEntity item,QueryBuilder builder)
	{
		super(I18n.getFormattedString("querybuilder.message.references","{0} references", new Object[]{ "" +item.getQuerytoken().getReference() }),builder);
        this.item = item;

		foreignTables = new JList();
		primaryTables = new JList();
		
		BorderLayoutPanel contentL = new BorderLayoutPanel();
		contentL.setComponentNorth(new JLabel(I18n.getString("querybuilder.menu.foreignTables","foreign tables")));
		contentL.setComponentCenter(new JScrollPane(foreignTables));
		
		BorderLayoutPanel contentR = new BorderLayoutPanel();
		contentR.setComponentNorth(new JLabel(I18n.getString("querybuilder.menu.primaryTables","primary tables")));
		contentR.setComponentCenter(new JScrollPane(primaryTables));
		
		JPanel pnlCenter = new JPanel(new GridLayout(1,2,2,2));
		pnlCenter.add(contentL);
		pnlCenter.add(contentR);		
		setComponentCenter(pnlCenter);
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(400,220);		
	}

	private void addToSource(Object[] tables)
	{
		for(int i=0; i<tables.length; i++)
		{
			String schema = null;
			String table = tables[i].toString();
			
			if(table.indexOf('.')!=-1)
			{
				schema = table.substring(0,table.indexOf('.'));
				table = table.substring(table.indexOf('.')+1);
			}
			
			QueryTokens.Table token = new QueryTokens.Table(schema,table);
			DiagramLoader.run(DiagramLoader.DEFAULT,item.builder,token,true);
		}
	}

	protected boolean onConfirm()
	{
		addToSource(foreignTables.getSelectedValues());
		addToSource(primaryTables.getSelectedValues());
		
		return true;
	}
	
	protected void onShow()
	{
		try
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			loadExportedKeys();
			loadImportedKeys();
		}
		catch(SQLException sqle)
		{
			System.out.println("[ MaskReferences::onShowing ]\n" + sqle);
		}
		finally
		{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));			
		}
	}
	
	private void loadImportedKeys()
		throws SQLException
	{
		DefaultListModel model = new DefaultListModel();
		DatabaseMetaData dbmd = item.builder.getConnection().getMetaData();
		
		String schema = item.builder.getModel().getSchema() == null ? item.getQuerytoken().getSchema() : item.builder.getModel().getSchema();
		String catalog = schema == null ? null : dbmd.getConnection().getCatalog();
		
		ResultSet rs = dbmd.getImportedKeys(catalog, schema, item.getQuerytoken().getName());		
		while(rs.next())
		{
			String pkschema = rs.getString(2);
			String pktable	= rs.getString(3).trim();
			
			if(item.builder.getModel().getSchema()!=null) pkschema = null;
			if(pkschema!=null) pkschema = pkschema.trim();
			
			if(!pktable.equals(item.getQuerytoken().getName()))
			{
				String pkElement = pkschema == null ? pktable : pkschema + "." + pktable;
				if(!model.contains(pkElement)) model.addElement(pkElement);
			}
		}
		rs.close();
		primaryTables.setModel(model);
	}

	private void loadExportedKeys()
		throws SQLException
	{
		DefaultListModel model = new DefaultListModel();
		DatabaseMetaData dbmd = item.builder.getConnection().getMetaData();
		
		String schema = item.builder.getModel().getSchema() == null ? item.getQuerytoken().getSchema() : item.builder.getModel().getSchema();
		String catalog = schema == null ? null : dbmd.getConnection().getCatalog();
		
		ResultSet rs = dbmd.getExportedKeys(catalog, schema, item.getQuerytoken().getName());		
		while(rs.next())
		{
			String fkschema = rs.getString(6);
			String fktable	= rs.getString(7).trim();
			
			if(item.builder.getModel().getSchema()!=null) fkschema = null;
			if(fkschema!=null) fkschema = fkschema.trim();

			if(!fktable.equals(item.getQuerytoken().getName()))
			{
				String fkElement = fkschema == null ? fktable : fkschema + "." + fktable;
				if(!model.contains(fkElement)) model.addElement(fkElement);
			}
		}
		rs.close();
		foreignTables.setModel(model);
	}
}