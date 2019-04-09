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

import java.awt.Frame;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ListIterator;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import nickyb.sqleonardo.common.util.I18n;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class DiagramLoader extends JDialog implements Runnable
{
	static public final int DEFAULT = 0;
	static public final int ALL_FOREIGN_TABLES = 1;
	static public final int ALL_PRIMARY_TABLES = 2;
	
	private JLabel message;
	
	private int mode;
	private boolean autoJoinRequested;
	
	private QueryBuilder builder;
	private QueryTokens.Table table;
	
	private DiagramLoader(int mode, QueryBuilder builder, QueryTokens.Table table, boolean autojoin)
	{
		super((Frame)SwingUtilities.getAncestorOfClass(Frame.class, builder));
		
		this.setModal(true);
		this.setSize(275,55);
		this.setTitle(I18n.getString("querybuilder.message.wait","wait..."));
		this.setResizable(false);
		this.setLocationRelativeTo(builder);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				
		this.getContentPane().add(message=new JLabel("",JLabel.CENTER));
		
		this.autoJoinRequested = autojoin;
		this.builder = builder;
		this.table = table;
		this.mode = mode;
	}
	
	public void show()
	{
		new Thread(this).start();
		super.show();
	}
	
	public void run()
	{
		try
		{
			switch(mode)
			{
				case ALL_FOREIGN_TABLES: addAllForeignTables();break;
				case ALL_PRIMARY_TABLES: addAllPrimaryTables();break;
				default: addTable(table);
			}
		}
		catch(SQLException sqle)
		{
			System.out.println("[ DiagramLoader::run ]\n" + sqle);
		}
		finally
		{
			this.dispose();
		}
	}

	public static void run(int mode, QueryBuilder builder, QueryTokens.Table table, boolean autojoin)
	{
		new DiagramLoader(mode, builder, table, autojoin).show();
	}
	
	private void addTable(QueryTokens.Table table)
		throws SQLException
	{
		message.setText( I18n.getFormattedString("querybuilder.message.loading","Loading: {0}", new Object[]{"" + table.getIdentifier()}));

		if(QueryBuilder.autoAlias && table.getAlias()==null)
		{
			table.setAlias(table.getName());
		
			for(int i=0; builder.diagram.getEntity(table)!=null; i++)
			{
				if(mode==DEFAULT)
					table.setAlias(table.getName() + "_" + (char)(65+i));
				else
					return;
			}
		}
		else if(builder.diagram.getEntity(table)!=null)
		{
			if(mode==DEFAULT)
			{
				this.hide();
				JOptionPane.showMessageDialog(this,I18n.getString("querybuilder.message.tableLoadedAliasDisabled","Table already loaded and aliasing disabled!"), table.getIdentifier(), JOptionPane.WARNING_MESSAGE);
			}
			return;		    		
		}
		
		DiagramEntity item = creatEntity(table);
		builder.diagram.addEntity(item);
		
		if(autoJoinRequested && QueryBuilder.autoJoin)
			doAutoJoin(item);
	}
	
	private void addTables(ResultSet rs, int rsSchemaIndex, int rsTableIndex)
		throws SQLException
	{
		ArrayList list = new ArrayList();
		
		while(rs.next())
		{
			String schemaName = rs.getString(rsSchemaIndex);
			String tableName = rs.getString(rsTableIndex).trim();
			
			if(builder.getModel().getSchema()!=null) schemaName = null;
			if(schemaName!=null) schemaName = schemaName.trim();
			
			list.add(new QueryTokens.Table(schemaName,tableName));
		}
		rs.close();
					
		for(ListIterator iter = list.listIterator(); iter.hasNext();)
		{
			addTable((QueryTokens.Table)iter.next());
		}
	}
	
	private void addAllForeignTables()
		throws SQLException
	{
		DatabaseMetaData dbmd = builder.getConnection().getMetaData();
		message.setText(I18n.getString("querybuilder.message.reading","reading...") );
		
		String schema = builder.getModel().getSchema() == null ? table.getSchema() : builder.getModel().getSchema();		
		String catalog = schema == null ? null : dbmd.getConnection().getCatalog();
		
		addTables(dbmd.getExportedKeys(catalog, schema, table.getName()) ,6,7);
	}
	
	private void addAllPrimaryTables()
		throws SQLException
	{
		DatabaseMetaData dbmd = builder.getConnection().getMetaData();
		message.setText(I18n.getString("querybuilder.message.reading","reading..."));
		
		String schema = builder.getModel().getSchema() == null ? table.getSchema() : builder.getModel().getSchema();
		String catalog = schema == null ? null : dbmd.getConnection().getCatalog();
		
		addTables(dbmd.getImportedKeys(catalog, schema, table.getName()) ,2,3);
	}
	
	private DiagramEntity creatEntity(QueryTokens.Table table)
		throws SQLException
	{
		DiagramEntity item = new DiagramEntity(builder,table);
		item.setEnabled(builder.getConnection()!=null);
		
		if(builder.getConnection()!=null)
		{
			DatabaseMetaData dbmetadata = builder.getConnection().getMetaData();
			Hashtable primary = this.getPrimaryKeys(dbmetadata,item);
			
			String name = item.getQuerytoken().getName();
			String schema = builder.getModel().getSchema() == null ? item.getQuerytoken().getSchema() : builder.getModel().getSchema();
			String catalog = schema == null ? null : dbmetadata.getConnection().getCatalog();

			if(dbmetadata.storesLowerCaseIdentifiers())
			{
				name = name!=null ? name.toLowerCase() : null;
				schema = schema!=null ? schema.toLowerCase() : null;
				catalog = catalog!=null ? catalog.toLowerCase() : null;
			}
			else if(dbmetadata.storesUpperCaseIdentifiers())
			{
				name = name!=null ? name.toUpperCase() : null;
				schema = schema!=null ? schema.toUpperCase() : null;
				catalog = catalog!=null ? catalog.toUpperCase() : null;
			}
			
			ResultSet rsColumns = dbmetadata.getColumns(catalog, schema, name, "%");
			while(rsColumns.next())
			{
				String columnName	= rsColumns.getString(4).trim();
				String typeName		= rsColumns.getString(6);
				int size	= rsColumns.getInt(7);
				int pos		= rsColumns.getInt(17);
				
				String tip = typeName + "(" + size + ")";
				item.addField(pos,columnName,primary.get(columnName),tip);
			}
			rsColumns.close();
		}
		item.pack();
		
		return item;
	}
	
	private Hashtable getPrimaryKeys(DatabaseMetaData dbmetadata, DiagramEntity item)
	{
		Hashtable primary = new Hashtable();
		
		try
		{
			String name = item.getQuerytoken().getName();
			String schema = builder.getModel().getSchema() == null ? item.getQuerytoken().getSchema() : builder.getModel().getSchema();
			String catalog = schema == null ? null : dbmetadata.getConnection().getCatalog();
			
			if(dbmetadata.storesLowerCaseIdentifiers())
			{
				name = name!=null ? name.toLowerCase() : null;
				schema = schema!=null ? schema.toLowerCase() : null;
				catalog = catalog!=null ? catalog.toLowerCase() : null;
			}
			else if(dbmetadata.storesUpperCaseIdentifiers())
			{
				name = name!=null ? name.toUpperCase() : null;
				schema = schema!=null ? schema.toUpperCase() : null;
				catalog = catalog!=null ? catalog.toUpperCase() : null;
			}
			
			ResultSet rsPK = dbmetadata.getPrimaryKeys(catalog, schema, name);
			while(rsPK.next())
				primary.put(rsPK.getString(4).trim(), rsPK.getString(6));
			rsPK.close();
		}
		catch (SQLException sqle)
		{
			System.out.println("[ DiagramLoader::getPrimaryKeys ]\n" + sqle);
		}
		finally
		{
			return primary;
		}
	}
	
	private void doAutoJoin(DiagramEntity source)
		throws SQLException
	{
		if(builder.diagram.getEntities().length > 0)
		{
			DatabaseMetaData dbmetadata = builder.getConnection().getMetaData();
			
			String name = source.getQuerytoken().getName();
			String schema = builder.getModel().getSchema() == null ? source.getQuerytoken().getSchema() : builder.getModel().getSchema();
			String catalog = schema == null ? null : dbmetadata.getConnection().getCatalog();

			if(dbmetadata.storesLowerCaseIdentifiers())
			{
				name = name!=null ? name.toLowerCase() : null;
				schema = schema!=null ? schema.toLowerCase() : null;
				catalog = catalog!=null ? catalog.toLowerCase() : null;
			}
			else if(dbmetadata.storesUpperCaseIdentifiers())
			{
				name = name!=null ? name.toUpperCase() : null;
				schema = schema!=null ? schema.toUpperCase() : null;
				catalog = catalog!=null ? catalog.toUpperCase() : null;
			}

			join(dbmetadata.getImportedKeys(catalog, schema, name) , source, false);
			join(dbmetadata.getExportedKeys(catalog, schema, name) , source, true);
		}
	}
	
	private void join(ResultSet rs, DiagramEntity source, boolean ispk)
		throws SQLException
	{
		while(rs.next())
		{
			String pkschema = rs.getString(2);
			String pktable	= rs.getString(3).trim();
			String pkcolumn = rs.getString(4).trim();
			String fkschema = rs.getString(6);
			String fktable	= rs.getString(7).trim();
			String fkcolumn = rs.getString(8).trim();
			String fkname	= rs.getString(12);
			
			if(builder.getModel().getSchema()!=null)
				pkschema = fkschema = null;
			
			if(pkschema!=null) pkschema = pkschema.trim();
			if(fkschema!=null) fkschema = fkschema.trim();
			
			DiagramEntity itemP = ispk ? source : builder.diagram.getEntity(pkschema, pktable);
			DiagramEntity itemF = ispk ? builder.diagram.getEntity(fkschema, fktable) : source;
			
			if(itemP!=null && itemF!=null && !itemP.getQuerytoken().toString().equalsIgnoreCase(itemF.getQuerytoken().toString()))
			{
				DiagramField fP = itemP.getField(pkcolumn,true);
				DiagramField fF = itemF.getField(fkcolumn,true);
					
				builder.diagram.join(itemP,fP,itemF,fF);
				builder.diagram.getRelations()[builder.diagram.getRelationCount()-1].setName(fkname);
			}
		}
		rs.close();
	}
}