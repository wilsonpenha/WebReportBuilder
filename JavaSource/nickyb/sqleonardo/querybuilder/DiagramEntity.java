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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import nickyb.sqleonardo.common.util.I18n;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class DiagramEntity extends JInternalFrame
{
	private JMenu header;
	private JPanel fields;
	
	Vector filterdFields = new Vector();
	
	QueryBuilder builder;
	private QueryTokens.Table querytoken;

	DiagramEntity(QueryBuilder builder, QueryTokens.Table qtoken)
	{
		super(qtoken.getName(), false, true);
		this.builder = builder;
		setQuerytoken(qtoken);
		
		setLayer(JDesktopPane.PALETTE_LAYER);
		putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
		
		getContentPane().add(fields = new JPanel(new GridLayout(0,1,0,0)));
		
		this.setJMenuBar(new JMenuBar());
		this.getJMenuBar().add(header = new JMenu(getQuerytoken().getReference()));
		
		header.add(new MenuItemSortByName());
		header.add(new MenuItemPack());
		header.addSeparator();
		header.add(new ActionSelectAll());
		header.add(new ActionDeselectAll());
		header.addSeparator();
		header.add(new ActionOpenAllForeignTables());
		header.add(new ActionOpenAllPrimaryTables());
		header.addSeparator();
		header.add(new ActionReferences());
	}
        
	DiagramField addField(int ordinalPosition, String label, Object key, String tip)
	{
		DiagramField field = new DiagramField(this,label,key!=null);
		field.position = ordinalPosition;
		
		field.setToolTipText(label + (tip!=null ? " : " + tip : ""));
		field.setBackground(ViewDiagram.BGCOLOR_DEFAULT);
		
		fields.add(field);		
		return field;
	}
	
	private int findField(String label, boolean unpack)
	{
		if(unpack)
		{
			for(int i=0; i<filterdFields.size(); i++)
			{
				DiagramField field = (DiagramField)filterdFields.get(i);
				if(field.getName().equalsIgnoreCase(label))
				{
					fields.add(field);
					filterdFields.remove(field);
					
					doSort();
					pack();
					break;
				}
			}
		}
		
		for(int i=0; i<fields.getComponentCount(); i++)
		{
			DiagramField field = (DiagramField)fields.getComponent(i);
			if(field.getName().equalsIgnoreCase(label)) return i;
		}
		
		return -1;		
	}

    /**
     * Returns a DiagramField for the given name or null is no matching field is found...
     */
	public DiagramField getField(String label)
	{
		return getField(label,false);
	}
	
	// argument : unpack = true -> remove field from filter
	DiagramField getField(String label, boolean unpack)
	{
		int index = findField(label,unpack);
		return index!=-1?(DiagramField)fields.getComponent(index):null;
	}
	
	void setDragAndDropEnabled(boolean b)
	{
		for(int i=0; i<fields.getComponentCount(); i++)
		{
			DiagramField field = (DiagramField)fields.getComponent(i);
			field.setDragAndDropEnabled(b);
		}		
	}	
	
	void onCreate()
	{
		builder.browser.addFromClause(getQuerytoken());
		doFlush();
	}
	
	void onDestroy()
	{
		((JMenuItem)header.getMenuComponent(4)).getAction().actionPerformed(null);

		builder.diagram.removeAllRelation(this);
		builder.browser.removeFromClause(getQuerytoken());
	}
	
	void onSelectionChanged(DiagramField field)
	{
		if(field.isSelected())
			builder.browser.addSelectList(field.querytoken);
		else
			builder.browser.removeSelectList(field.querytoken);
		
		doPack();
	}

	void doFlush()
	{
		for(int i=0; i<fields.getComponentCount(); i++)
		{
			DiagramField field = (DiagramField)fields.getComponent(i);
			field.setBackground(field.isJoined() ? ViewDiagram.BGCOLOR_JOINED : ViewDiagram.BGCOLOR_DEFAULT);
			
			// see: setDragAndDropEnabled()
			// field.setEnabled(!builder.isDragAndDropEnabled());
		}
	}

	void doSort()
	{
		JCheckBoxMenuItem mItem = (JCheckBoxMenuItem)header.getMenuComponent(0);
		if(mItem.isSelected())
		{
			for(int i=0; i<fields.getComponentCount()-1; i++)
			{
				String master = ((DiagramField)fields.getComponent(i)).getName();
				for(int j=i+1; j<fields.getComponentCount(); j++)
				{
					String slave = ((DiagramField)fields.getComponent(j)).getName();
					if(master.compareTo(slave) > 0)
					{
						DiagramField field = (DiagramField)fields.getComponent(j);
						fields.remove(field);
						fields.add(field,i);
							
						master = slave;
					}
				}
			}
		}
		else
		{
			for(int i=0; i<fields.getComponentCount()-1; i++)
			{
				int master = ((DiagramField)fields.getComponent(i)).position;
				for(int j=i+1; j<fields.getComponentCount(); j++)
				{
					int slave = ((DiagramField)fields.getComponent(j)).position;
					if(master > slave)
					{
						DiagramField field = (DiagramField)fields.getComponent(j);
						fields.remove(field);
						fields.add(field,i);
							
						master = slave;
					}
				}
			}
		}
		
		builder.diagram.repaint();
		builder.diagram.validate();
		builder.diagram.doResize();
	}
	
	void setSort(boolean b)
	{
		JCheckBoxMenuItem mItem = (JCheckBoxMenuItem)header.getMenuComponent(0);
		mItem.setSelected(b);
		
		doSort();
	}
	
	void doPack()
	{
		boolean changed = false;		
		
		JCheckBoxMenuItem mItem = (JCheckBoxMenuItem)header.getMenuComponent(1);
		if(mItem.isSelected())
		{
			for(int i=0; i<fields.getComponentCount(); i++)
			{
				DiagramField field = (DiagramField)fields.getComponent(i);
				if(!field.isSelected() && !field.isJoined())
				{
					filterdFields.add(field);
					fields.remove(field);
					i--;
						
					changed = true;
				}
			}
		}
		else
		{
			while(filterdFields.size()>0)
			{
				fields.add((DiagramField)filterdFields.get(0));
				filterdFields.remove(0);
					
				changed = true;
			}
			doSort();
		}
			
		if(changed)
		{
			pack();
			builder.diagram.doResize();
		}
	}
	
	void setPack(boolean b)
	{
		JCheckBoxMenuItem mItem = (JCheckBoxMenuItem)header.getMenuComponent(1);
		mItem.setSelected(b);
		
		doPack();
	}
	
	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		header.setEnabled(b);
	}
	
//	/////////////////////////////////////////////////////////////////////////////
//	Menu Actions
//	/////////////////////////////////////////////////////////////////////////////
	private class MenuItemSortByName extends JCheckBoxMenuItem implements ActionListener
	{
		private MenuItemSortByName()
		{
			super(I18n.getString("querybuilder.menu.sortByName","sort by name"));
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramEntity.this.doSort();
		}
	}

	private class MenuItemPack extends JCheckBoxMenuItem implements ActionListener
	{
		private MenuItemPack()
		{
			super(I18n.getString("querybuilder.menu.pack","pack"));
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramEntity.this.doPack();
		}
	}
	
	private class ActionSelectAll extends AbstractAction
	{
		private ActionSelectAll()
		{
			super(I18n.getString("querybuilder.menu.selectAll","select all"));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramEntity.this.setPack(false);
			
			for(int i=0; i<fields.getComponentCount(); i++)
				((DiagramField)fields.getComponent(i)).setSelected(true);
		}
	}
	
	private class ActionDeselectAll extends AbstractAction
	{
		private ActionDeselectAll()
		{
			super(I18n.getString("querybuilder.menu.deselectAll","deselect all"));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramEntity.this.setPack(false);			
			
			for(int i=0; i<fields.getComponentCount(); i++)
				((DiagramField)fields.getComponent(i)).setSelected(false);
		}
	}
	
	private class ActionOpenAllForeignTables extends AbstractAction
	{
		private ActionOpenAllForeignTables()
		{
			super(I18n.getString("querybuilder.menu.openAllForeignTables","open all foreign tables"));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramLoader.run(DiagramLoader.ALL_FOREIGN_TABLES, DiagramEntity.this.builder, DiagramEntity.this.getQuerytoken(), true);
		}
	}
	
	private class ActionOpenAllPrimaryTables extends AbstractAction
	{
		private ActionOpenAllPrimaryTables()
		{
			super(I18n.getString("querybuilder.menu.openAllPrimaryTables","open all primary tables"));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			DiagramLoader.run(DiagramLoader.ALL_PRIMARY_TABLES, DiagramEntity.this.builder, DiagramEntity.this.getQuerytoken(), true);
		}
	}
	
	private class ActionReferences extends AbstractAction
	{
		private ActionReferences()
		{
			super(I18n.getString("querybuilder.menu.references","references..."));
		}
		
		public void actionPerformed(ActionEvent e)
		{
			new MaskReferences(DiagramEntity.this,DiagramEntity.this.builder).showDialog();
		}
	}

    public QueryTokens.Table getQuerytoken() {
        return querytoken;
    }

    public void setQuerytoken(QueryTokens.Table querytoken) {
        this.querytoken = querytoken;
    }
}