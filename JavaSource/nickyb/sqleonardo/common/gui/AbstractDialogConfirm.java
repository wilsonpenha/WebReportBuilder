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

package nickyb.sqleonardo.common.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import nickyb.sqleonardo.common.util.I18n;

public abstract class AbstractDialogConfirm extends AbstractDialogModal
{
	protected CommandButton btnConfirm;
	
	protected AbstractDialogConfirm(Component owner, String title)
	{
		this(owner, title, INITIAL_WIDTH, INITIAL_HEIGHT);
	}
	
	protected AbstractDialogConfirm(Component owner, String title, int width, int height)
	{
		super(owner, title, width, height);
		btnConfirm = insertButton(1,I18n.getString("application.ok","ok"));
	}
	
	protected abstract boolean onConfirm();
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource() == btnConfirm)
		{
			if(!onConfirm()) return;
		}
		
		super.actionPerformed(ae);
	}
}
