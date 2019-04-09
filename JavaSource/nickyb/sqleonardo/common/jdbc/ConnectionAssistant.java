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

package nickyb.sqleonardo.common.jdbc;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

public class ConnectionAssistant
{
    private static Hashtable drivers = new Hashtable();
	private static Hashtable connections = new Hashtable();
    
    /* connection */
    public static ConnectionHandler open(String keycad, String keycah, String url, String uid, String pwd) throws Exception
    {
        Driver d = (Driver)drivers.get(keycad);
        Properties info = new Properties();
        
    	if(uid != null)
    	    info.put("user", uid);

    	if(pwd != null)
    	    info.put("password", pwd);
    	
		ConnectionHandler ch = new ConnectionHandler(d.connect(url,info));
		connections.put(keycah,ch);
		
    	return ch;
    }
    
	public static boolean hasHandler(String keycah)
	{
		return keycah==null ? false : connections.containsKey(keycah);
	}    
    
	public static ConnectionHandler getHandler(String keycah)
	{
		return (ConnectionHandler)connections.get(keycah);
	}
	
	public static void removeHandler(String keycah)
	{
		connections.remove(keycah);
	}

    /* create driver instance */
    public static String declare(String library, String classname) throws Exception
    {
        return declare(library,classname,true);
    }
    
    public static String declare(String library, String classname, boolean classpath) throws Exception
    {
        String keycad = library +"$"+ classname;
        if(!drivers.containsKey(keycad))
        {
	        if(classpath)
	        {
	            declare(keycad,Class.forName(classname));
	        }
	        else
	        {
			    File file = new File(library);
			    ClassLoader cl = new URLClassLoader(new URL[] {file.toURL()},ClassLoader.getSystemClassLoader());
			    
			    declare(keycad,Class.forName(classname,true,cl));
			}
		}
        
        return new String(keycad);
    }

	private static void declare(String keycad, Class c) throws Exception
	{
		Driver d = (Driver)c.newInstance();        
		declare(keycad,d);
	}
    
	public static void declare(String keycad, Driver d) throws Exception
	{
		drivers.put(keycad,d);
	}
    
	public static Set getDeclarations()
	{
		return drivers.keySet();
	}
	
	public static Set getHandlers()
	{
		return connections.keySet();
	}	
}