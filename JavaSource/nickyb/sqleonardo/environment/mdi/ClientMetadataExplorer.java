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

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;

import nickyb.sqleonardo.common.gui.Toolbar;
import nickyb.sqleonardo.environment.ctrl.MetadataExplorer;

public class ClientMetadataExplorer extends MDIClient
{
	public static final String DEFAULT_TITLE = "metadata explorer";
	
	private MetadataExplorer control;
	private JMenuItem[] m_actions;
	
	public ClientMetadataExplorer()
	{
		super(DEFAULT_TITLE);
		
		setComponentCenter(control = new MetadataExplorer());
		control.setBorder(new EmptyBorder(2,2,2,2));
		
		System.out.println("Looad actions...");
		initMenuActions();
	}

	private void initMenuActions()
	{
		Action aDel	= control.getActionMap().get("delete");
		Action aPro = control.getActionMap().get("properties");
		
		aDel.setEnabled(false);
		aPro.setEnabled(false);
		
		JMenu m_lv = new JMenu("objects view");
		m_lv.add(control.getActionMap().get("choose-columns")).setEnabled(false);
		m_lv.addSeparator();
		m_lv.add(MDIMenubar.createItem(control.getActionMap().get("list-copy"))).setEnabled(false);
		m_lv.add(MDIMenubar.createItem(control.getActionMap().get("list-export"))).setEnabled(false);
		m_lv.addSeparator();
		m_lv.add(MDIMenubar.createItem(control.getActionMap().get("list-refresh"))).setEnabled(false);
		
		m_actions = new JMenuItem[]
		{
			m_lv,
			null,
			new JMenuItem(control.getActionMap().get("new-driver")),
			new JMenuItem(control.getActionMap().get("new-datasorce")),
			null,
			new JMenuItem(aDel),
			null,
			new JMenuItem(aPro)
		};
	}
	
	public final void dispose()
	{
		control.unloadNavigator();
		super.dispose();
	}

	public final MetadataExplorer getControl()
	{
		return control;
	}
	
	public final String getName()
	{
		return DEFAULT_TITLE;
	}

	public JMenuItem[] getMenuActions()
	{
		return m_actions;
	}

	public Toolbar getSubToolbar()
	{
		return null;
	}
    
	protected void setPreferences()
	{
		control.setPreferences();
	}
}