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

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;

public class UoDatasource
{
	private UoDriver uoDv;
	
	public String name	= new String("new database name");
	public String url	= new String();
	public String uid	= new String();
	public String pwd	= new String();
	
	/* tree filter */
	public String schema = null;
	
	public boolean remember = false;
	
	public UoDatasource(UoDriver uoDv)
	{
		this.uoDv	= uoDv;
		this.url	= uoDv.example;
	}

	public void connect() throws Exception
	{
		ConnectionAssistant.open(uoDv.getKey(),this.getKey(),url,uid,pwd);
	}
	
	public boolean isConnected()
	{
		return ConnectionAssistant.getHandler(this.getKey())!=null;
	}
	
	public void disconnect() throws Exception
	{
		ConnectionHandler ch = ConnectionAssistant.getHandler(this.getKey());
		ConnectionAssistant.removeHandler(this.getKey());
		
		ch.close();
	}
	
	public String getKey()
	{
		return uoDv.name +"."+ name +"@"+ (uid==null || uid.length() == 0 ?"<null>" : uid);
	}
	
	public String toString()
	{
		if(schema!=null)
			return name + " | " + schema;
		
		return name;
	}
}