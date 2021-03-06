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

import java.awt.Cursor;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.gui.ListView;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class DefinitionPane extends BorderLayoutPanel implements ChangeListener
{
	private ListView lvColumns,lvPrimaryKeys,lvIndexInfo,lvExportedKeys,lvImportedKeys;
	private JTabbedPane tp;
	
	private String keycah;
	private QueryTokens.Table table;

	public DefinitionPane(String keycah,QueryTokens.Table table)
	{
		this.keycah = keycah;
		this.table = table;
		
		tp = new JTabbedPane();
		tp.addChangeListener(this);
		tp.setBorder(LineBorder.createGrayLineBorder());
		
		lvColumns = new ListView();
		tp.addTab("columns",lvColumns);
			
		lvPrimaryKeys = new ListView();
		tp.addTab("primary keys",lvPrimaryKeys);
		
		lvIndexInfo = new ListView();
		tp.addTab("indices",lvIndexInfo);
		
		lvExportedKeys = new ListView();
		tp.addTab("exported keys",lvExportedKeys);
		
		lvImportedKeys = new ListView();
		tp.addTab("imported keys",lvImportedKeys);
		
		setComponentCenter(tp);
	}
	
	private ConnectionHandler getConnection()
	{
		return ConnectionAssistant.getHandler(keycah);
	}
	
	private void list(String metaview, ListView lv, ResultSet rs) throws SQLException
	{
		int i = keycah.lastIndexOf('.');
		Preferences.listMetadata(keycah.substring(0,i),metaview,lv,rs);
	}
	
	private void listColumns() throws SQLException
	{
		ConnectionHandler ch = this.getConnection();
		
		String catalog = table.getSchema() == null ? null : ch.get().getCatalog();
		ResultSet rs = ch.get().getMetaData().getColumns(catalog,table.getSchema(),table.getName(),"%");
		
		list("columns",lvColumns,rs);
		tp.setTitleAt(0,"columns (" + lvColumns.getRowCount() + ")");
	}
	
	private void listPrimaryKeys() throws SQLException
	{
		ConnectionHandler ch = this.getConnection();
		
		String catalog = table.getSchema() == null ? null : ch.get().getCatalog();
		ResultSet rs = ch.get().getMetaData().getPrimaryKeys(catalog,table.getSchema(),table.getName());
		
		list("primary keys",lvPrimaryKeys,rs);
		tp.setTitleAt(1,"primary keys (" + lvPrimaryKeys.getRowCount() + ")");
	}
	
	private void listIndexInfo() throws SQLException
	{
		ConnectionHandler ch = this.getConnection();
		
		String catalog = table.getSchema() == null ? null : ch.get().getCatalog();
		ResultSet rs = ch.get().getMetaData().getIndexInfo(catalog,table.getSchema(),table.getName(),false,false);
		
		list("indices",lvIndexInfo,rs);
		tp.setTitleAt(2,"indices (" + lvIndexInfo.getRowCount() + ")");
	}
	
	private void listExportedKeys() throws SQLException
	{
		ConnectionHandler ch = this.getConnection();
		
		String catalog = table.getSchema() == null ? null : ch.get().getCatalog();
		ResultSet rs = ch.get().getMetaData().getExportedKeys(catalog,table.getSchema(),table.getName());
		
		list("exported keys",lvExportedKeys,rs);
		tp.setTitleAt(3,"exported keys (" + lvExportedKeys.getRowCount() + ")");
	}
	
	private void listImportedKeys() throws SQLException
	{
		ConnectionHandler ch = this.getConnection();
		
		String catalog = table.getSchema() == null ? null : ch.get().getCatalog();
		ResultSet rs = ch.get().getMetaData().getImportedKeys(catalog,table.getSchema(),table.getName());
		
		list("imported keys",lvImportedKeys,rs);
		tp.setTitleAt(4,"imported keys (" + lvImportedKeys.getRowCount() + ")");
	}
	
	public String getSelectedTitle()
	{
		int idx = tp.getSelectedIndex();
		if(tp.getTitleAt(idx).endsWith(")"))
		{
			String title = tp.getTitleAt(idx);
			return title.substring(0,title.indexOf(" ("));
		}
		
		return tp.getTitleAt(idx);
	}
	
	public ListView getSelectedView()
	{
		return getViewAt(tp.getSelectedIndex());
	}
	
	public ListView getViewAt(int idx)
	{
		return (ListView)tp.getComponentAt(idx);
	}
	
	public void stateChanged(ChangeEvent ce)
	{
		int idx = tp.getSelectedIndex();
		if(getViewAt(idx).getColumnCount() > 0) return;
		
		try
		{
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			switch(idx)
			{
				case 0: listColumns(); break;
				case 1: listPrimaryKeys(); break;
				case 2: listIndexInfo(); break;
				case 3: listExportedKeys(); break;
				case 4: listImportedKeys(); break;
			}
		}
		catch (SQLException e)
		{
			String title = tp.getTitleAt(idx);
			tp.setTitleAt(idx,title + " (?)");
			
			Application.println(e,true);
		}
		finally
		{
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}		
	}
}