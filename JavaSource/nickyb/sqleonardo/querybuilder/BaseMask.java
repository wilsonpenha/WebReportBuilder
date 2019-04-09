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

import nickyb.sqleonardo.common.gui.AbstractDialogConfirm;
import nickyb.sqleonardo.common.gui.BorderLayoutPanel;

public abstract class BaseMask extends BorderLayoutPanel
{
	private boolean okpressed;
	
	private String title;
	private QueryBuilder builder;

	BaseMask(String title, QueryBuilder builder)
	{
		this.title = title;
		this.builder = builder;
	}
	
	protected abstract boolean onConfirm();
	protected abstract void onShow();
	
    public boolean showDialog()
    {
    	DialogConfirm dlg = new DialogConfirm();
		dlg.pack();
    	dlg.show();
    	
    	return okpressed;
    }
    
    class DialogConfirm extends AbstractDialogConfirm
    {
    	DialogConfirm()
    	{
    		super(BaseMask.this.builder,BaseMask.this.title);
			getContentPane().add(BaseMask.this);
    	}
    	
		public void show()
		{
			setLocationRelativeTo(BaseMask.this.builder);
			super.show();
		}
    	
		protected boolean onConfirm()
		{
			return BaseMask.this.okpressed = BaseMask.this.onConfirm();
		}

		protected void onOpen()
		{
			BaseMask.this.onShow();
		}    	
    }
}