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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ListView extends BorderLayoutPanel
{
	private static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();
	private JTable table;
    
	public ListView()
    {
    	super(2,2);
	    JScrollPane scroll = new JScrollPane(table = new JTable());
		scroll.getViewport().setBackground(Color.white);
	    setComponentCenter(scroll);
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reset();
		
		table.getActionMap().put("copy",new ActionCopyCell());
		table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK),"copy");
    }
    
	protected JTable getJavaComponent()
	{
		return table;
	}    
    
	public void addListSelectionListener(ListSelectionListener l)
	{
		table.getSelectionModel().addListSelectionListener(l);
	}
	
	public synchronized void addMouseListener(MouseListener l)
	{
		table.addMouseListener(l);
	}
    
	public void addColumn(String text)
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.addColumn(text);
	}
    
	public void setColumnWidth(String text,int size)
	{
		TableColumn tableColumn = table.getColumn(text);
		tableColumn.setPreferredWidth(size);
		tableColumn.setWidth(size);
	}
	
	public void setHeaderVisible(boolean aFlag)
	{
		table.getTableHeader().setPreferredSize(new Dimension(0,0));
		table.getTableHeader().setVisible(aFlag);
	}
    
	public void addRow(Object[] rowdata)
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.addRow(rowdata);
	}
	
	public boolean isSelectionEmpty()
	{
		return table.getSelectedRowCount() < 1;
	}	

	public String getColumnName(int col)
	{
		return table.getColumnName(col);
	}
	
    public int getColumnCount()
    {
        return table.getColumnCount();
    }
    
    public int getRowCount()
    {
        return table.getRowCount();
    }
    
    public int getSelectedRow()
    {
    	return table.getSelectedRow();
    }
    
	public Object getValueAt(int row,int col)
	{
		return table.getValueAt(row,col);
	}
    
	public void setValueAt(Object value,int row,int col)
	{
		table.setValueAt(value,row,col);
	}
    
	public void removeRow(int row)
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.removeRow(row);
	}
	
	public void removeSelectedRow()
	{
		removeRow(getSelectedRow());
	}
	
	public void removeAllRows()
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
	}
	
	public void copyAllRows()
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<table.getRowCount(); i++)
		{
			for(int j=0; j<table.getColumnCount(); j++)
			{
				String cell = table.getValueAt(i,j) == null ? null : table.getValueAt(i,j).toString();
				if(j>0) sb.append("\t");
				sb.append(cell);
			}
			sb.append("\n");
		}

		Clipboard cb = this.getToolkit().getSystemClipboard();
		StringSelection contents = new StringSelection(sb.toString());
		cb.setContents(contents, defaultClipboardOwner);
	}
	
	public void reset()
	{
		DefaultTableModel model = new DefaultTableModel()
		{
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		table.setModel(model);
	}
	
	public void tableDataChanged()
	{
		table.tableChanged(new TableModelEvent(table.getModel()));
	}
	
	private class ActionCopyCell extends AbstractAction
	{
		public void actionPerformed(ActionEvent ae)
		{
			int col = ListView.this.table.getSelectedColumn();
			int row = ListView.this.table.getSelectedRow();
			
			if(row!=-1 && col!=-1)
			{
				if(ListView.this.table.getValueAt(row,col)==null) return;
				
				Clipboard cb = ListView.this.getToolkit().getSystemClipboard();
				
				String value = ListView.this.table.getValueAt(row,col).toString();
				StringSelection contents = new StringSelection(value);
				cb.setContents(contents, defaultClipboardOwner);
			}
		}
	}
	
	static class ClipboardObserver implements ClipboardOwner
	{
		public void lostOwnership(Clipboard clipboard, Transferable contents)
		{
		}
	}
}
