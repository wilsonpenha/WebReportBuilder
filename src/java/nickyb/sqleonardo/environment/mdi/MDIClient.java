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

import javax.swing.JMenuItem;

import nickyb.sqleonardo.common.gui.ClientFrame;
import nickyb.sqleonardo.common.gui.Toolbar;

public abstract class MDIClient extends ClientFrame
{
    private static int counter=0;
    private int id=-1;
    
    public MDIClient(String title)
    {
        super(title);
        setFrameIcon(null);
        
        if(getName() == null)
        {
        	setName("MDIClient_" + (++counter));
        	setTitle((id=counter) + " - " + title);
        	setClosable(true);
        }
    }
    
	protected int getID()
	{
		return id;
	}
	
	public abstract JMenuItem[] getMenuActions();
	public abstract Toolbar getSubToolbar();
	
	protected abstract void setPreferences();
}
