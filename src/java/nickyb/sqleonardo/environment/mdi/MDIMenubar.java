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
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import nickyb.sqleonardo.environment.Application;

public class MDIMenubar extends JMenuBar implements InternalFrameListener
{
    static final int IDX_ACTIONS	= 1;
    static final int IDX_TOOLS		= 2;
    static final int IDX_WINDOW		= 3;
    
    private WindowGroup winGropu;
    
    public MDIMenubar()
    {
		winGropu = new WindowGroup();
    	
		JMenu menu = add("file");
		menu.setMnemonic('f');
		menu.add(createItem(MDIActions.ACTION_NEW_QUERY));
		menu.add(createItem(MDIActions.ACTION_LOAD_QUERY));
		menu.addSeparator();
		menu.add(createItem(new MDIActions.Dummy("print..."))).setEnabled(false);
		//menu.addSeparator();
		//menu.add(createItem(MDIActions.ACTION_EXIT));
        
		menu = add("actions");
		menu.setMnemonic('a');
		menu.add("<empty>").setEnabled(false);
        
		menu = add("tools");
		menu.setMnemonic('t');
		menu.add(winGropu.add(MDIActions.ACTION_MDI_SHOW_EXPLORER));
		menu.add(winGropu.add(MDIActions.ACTION_MDI_SHOW_EDITOR));
		menu.add(winGropu.add(MDIActions.ACTION_MDI_SHOW_COMPARER));
		menu.addSeparator();
		menu.add(createItem(MDIActions.ACTION_SHOW_CONTENT));
		menu.add(createItem(MDIActions.ACTION_SHOW_DEFINITION));
		menu.addSeparator();
		menu.add(createItem(MDIActions.ACTION_SHOW_PREFERENCES));
        
        menu = add("window");
		menu.setMnemonic('w');
        menu.add(createItem(MDIActions.ACTION_MDI_CASCADE));
		menu.add(createItem(MDIActions.ACTION_MDI_TILEH));
        menu.addSeparator();
		menu.add(createItem(MDIActions.ACTION_MDI_CLOSE_ALL));
		menu.addSeparator();
		menu.add("<empty>").setEnabled(false);
		
        menu = add("?");
		menu.add(createItem(new MDIActions.Dummy("how to..."))).setEnabled(false);
		menu.addSeparator();
		menu.add(createItem(MDIActions.ACTION_ABOUT));
    }
    
    private JMenu add(String text)
    {
        return add(new JMenu(text));
    }

	private static JMenuItem createItem(String actionkey)
	{
		return createItem(Application.application.getAction(actionkey));
	}
    
    public static JMenuItem createItem(Action a)
    {
        JMenuItem item = new JMenuItem(a);
        item.setToolTipText(null);
        item.setIcon(null);
        
        if(a.getValue(Action.ACCELERATOR_KEY)!=null)
        	item.setAccelerator((KeyStroke)a.getValue(Action.ACCELERATOR_KEY));

        return item;
    }
    
    public void internalFrameActivated(InternalFrameEvent ife)
    {
        this.getMenu(IDX_ACTIONS).getMenuComponent(0).setVisible(true);
        while(this.getMenu(IDX_ACTIONS).getMenuComponentCount()>1)
            this.getMenu(IDX_ACTIONS).remove(1);
        
        MDIClient client = (MDIClient)ife.getInternalFrame(); 
        if(client.getMenuActions()!=null)
        {
	        for(int i=0; i<client.getMenuActions().length; i++)
	        {
	        	if(client.getMenuActions()[i]==null)
					this.getMenu(IDX_ACTIONS).addSeparator();
				else
					this.getMenu(IDX_ACTIONS).add(client.getMenuActions()[i]);
	        }
	
	        this.getMenu(IDX_ACTIONS).getMenuComponent(0).setVisible(false);
        }

		for(int i=0;i<this.getMenu(IDX_TOOLS).getMenuComponentCount();i++)
		{
			if(this.getMenu(IDX_TOOLS).getMenuComponent(i) instanceof JRadioButtonMenuItem)
			{
				JRadioButtonMenuItem item = (JRadioButtonMenuItem)this.getMenu(IDX_TOOLS).getMenuComponent(i);
				if(((MDIActions.AbstractShow)item.getAction()).getMDIClientName().equals(client.getName()))
				{
					item.setSelected(true);
				}
			}
		}
        
		for(int i=0;i<this.getMenu(IDX_WINDOW).getMenuComponentCount();i++)
		{
			if(this.getMenu(IDX_WINDOW).getMenuComponent(i) instanceof JRadioButtonMenuItem)
			{
				JRadioButtonMenuItem item = (JRadioButtonMenuItem)this.getMenu(IDX_WINDOW).getMenuComponent(i);
				if(((MDIActions.AbstractShow)item.getAction()).getMDIClientName().equals(client.getName()))
				{
					item.setSelected(true);
				}
			}
		}
		
		Application.application.toolbar.onMDIClientActivated(client);
    }
    
    public void internalFrameClosed(InternalFrameEvent ife)
    {
        MDIClient client = (MDIClient)ife.getInternalFrame();
        
        for(int i=6;i<this.getMenu(IDX_WINDOW).getMenuComponentCount();i++)
        {
            JMenuItem item = (JMenuItem)this.getMenu(IDX_WINDOW).getMenuComponent(i);
            if(((ActionShowMDIClient)item.getAction()).getMDIClientName().equals(client.getName()))
            {
                this.getMenu(IDX_WINDOW).remove(i--);
				if(i==5) i++;
                
                if(this.getMenu(IDX_WINDOW).getMenuComponentCount()>6)
					item = (JMenuItem)this.getMenu(IDX_WINDOW).getMenuComponent(i);
                else
					item = (JMenuItem)this.getMenu(IDX_TOOLS).getMenuComponent(0);
				
				item.getAction().actionPerformed(null);
                break;
            }
        }
        this.getMenu(IDX_WINDOW).getMenuComponent(5).setVisible(this.getMenu(IDX_WINDOW).getMenuComponentCount()==6);
    }
    
    public void internalFrameOpened(InternalFrameEvent ife)
    {
        MDIClient client = (MDIClient)ife.getInternalFrame();
        if(!client.isClosable()) return;
        
        this.getMenu(IDX_WINDOW).add(winGropu.add(client));
        this.getMenu(IDX_WINDOW).getMenuComponent(5).setVisible(false);
    }
    
    public void internalFrameClosing(InternalFrameEvent ife){}
    public void internalFrameDeactivated(InternalFrameEvent ife){}
    public void internalFrameDeiconified(InternalFrameEvent ife){}
    public void internalFrameIconified(InternalFrameEvent ife){}
    
    /* generic action */
	private class ActionShowMDIClient extends MDIActions.AbstractShow
	{
		private String clientname;
        
		ActionShowMDIClient(MDIClient client)
		{
			clientname = client.getName();
			setText(client.getTitle());
		}
        
		public String getMDIClientName(){return clientname;}
	}
	
	private class WindowGroup extends ButtonGroup
	{
		JRadioButtonMenuItem add(MDIClient client)
		{
			return createItem(new ActionShowMDIClient(client));			
		}
		
		JRadioButtonMenuItem add(String actionkey)
		{
			return createItem(Application.application.getAction(actionkey));			
		}
		
		private JRadioButtonMenuItem createItem(Action a)
		{
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(a);
			item.setToolTipText(null);
			item.setIcon(null);
			add(item);
        
			if(a.getValue(Action.ACCELERATOR_KEY)!=null)
				item.setAccelerator((KeyStroke)a.getValue(Action.ACCELERATOR_KEY));

			return item;
		}
	}
}