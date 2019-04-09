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

package nickyb.sqleonardo.environment;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import nickyb.sqleonardo.common.gui.ListView;
import nickyb.sqleonardo.querybuilder.QueryBuilder;

public class Preferences
{
	/* BAD */
	public static void loadDefaults()
	{
		Application.println("loading Defaults...");
		if(Application.session.mount(Application.ENTRY_PREFERENCES).size() == 0)
		{
			Application.session.mount(Application.ENTRY_PREFERENCES).add(new Hashtable());
			
			set("window.height"	,new Integer(600));
			set("window.width"	,new Integer(800));
	        
			set("explorer.navigator.datasources.name.width"		,new Integer(30));
			set("explorer.navigator.datasources.url.width"		,new Integer(250));
			set("explorer.navigator.datasources.status.width"	,new Integer(30));
			
			set("querybuilder.auto-join"	,new Boolean(true));
			set("querybuilder.auto-alias"	,new Boolean(true));
			set("querybuilder.use-quote"	,new Boolean(true));
			set("querybuilder.use-schema"	,new Boolean(true));
		}
		
		QueryBuilder.autoJoin = getBoolean("querybuilder.auto-join",true);
		QueryBuilder.autoAlias = getBoolean("querybuilder.auto-alias",true);
		QueryBuilder.useAlwaysQuote	= getBoolean("querybuilder.use-quote",true);
		QueryBuilder.loadObjectsAtOnce = getBoolean("querybuilder.load-objects-at-once",true);
	}
	
	private static Hashtable get()
	{
		return (Hashtable)Application.session.mount(Application.ENTRY_PREFERENCES).get(0);
	}
	
    public static void set(String key,Object value)
    {
		get().put(key,value);
    }
    
	public static boolean getBoolean(String key)
	{
		return ((Boolean)get().get(key)).booleanValue();
	}
	
	public static boolean getBoolean(String key, boolean defaultValue)
	{
		if(!get().containsKey(key)){
			Application.println("Setting value : <"+defaultValue+"> to <"+key+">");
			set(key,new Boolean(defaultValue));
		}
		
		return getBoolean(key);
	}
	
	public static int getInteger(String key)
	{
		return ((Integer)get().get(key)).intValue();
	}
	
	public static int getInteger(String key, int defaultValue)
	{
		if(!get().containsKey(key))
			set(key,new Integer(defaultValue));
		
		return getInteger(key);
	}
	
	public static String getString(String key)
	{
		return get().get(key).toString();
	}

	public static String getString(String key, String defaultValue)
	{
		if(!get().containsKey(key))
			set(key,defaultValue);
		
		return getString(key);
	}
	
	public static void listMetadata(String dvname,String metaview,ListView lv,ResultSet rs) throws SQLException
	{
		Application.session.mount(Application.ENTRY_PREFERENCES);
		Application.session.home();
		Application.session.jump("metaview." + dvname);
		
		ArrayList cols = Application.session.jump(metaview);
		lv.reset();
		
		if(cols.size() == 0)
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i=1; i<=rsmd.getColumnCount(); i++)
			{
				String name = rsmd.getColumnName(i);
				lv.addColumn(name);
				
				cols.add(new Object[]{name,new Boolean(true)});
			}
		}
		else
		{
			for(int i=0; i<cols.size(); i++)
			{
				Object[] col = (Object[])cols.get(i);
				if(((Boolean)col[1]).booleanValue())
					lv.addColumn(col[0].toString());
			}
		}
		
		while(rs.next())
		{
			String[] rowdata = new String[cols.size()];
			for(int i=0,j=0; i<rowdata.length; i++)
			{
				Object[] col = (Object[])cols.get(i);
				if(((Boolean)col[1]).booleanValue())
				{
					rowdata[j++] = rs.getString(i+1);
					if(rowdata[j-1]!=null) rowdata[j-1] = rowdata[j-1].trim();
				}
			}
			lv.addRow(rowdata);
		}
		
		rs.close();
	}
}