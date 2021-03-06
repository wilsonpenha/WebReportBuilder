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

package nickyb.sqleonardo.environment.ctrl.content;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Types;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import nickyb.sqleonardo.common.gui.CustomLineBorder;
import nickyb.sqleonardo.common.util.Appearance;
import nickyb.sqleonardo.environment.ctrl.ContentPane;

public class ContentView extends JPanel implements ListSelectionListener
{
	private JTable data;
	private LineNumberView lines;
	
	private ContentModel model;
	private ContentPopup popup;
	private ContentPane control;
    
	public ContentView(ContentPane control)
	{
		super(new GridLayout(1,1));
	    this.control = control;
	    
		data = new JTable();
		data.setModel(model = new ContentModel());
		data.addMouseListener(popup = new ContentPopup(this));
		data.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent key)
			{
				if(key.getKeyCode() == KeyEvent.VK_DOWN)
				{
					if(ContentView.this.data.getSelectedRow() == ContentView.this.data.getRowCount()-1)
					{
						if(ContentView.this.getBlock() < ContentView.this.getBlockCount())
						{
							int col = ContentView.this.data.getSelectedColumn();
							
							ContentView.this.control.getSlider().setValue(ContentView.this.getBlock());
							
							ContentView.this.data.setRowSelectionInterval(0,0);
							ContentView.this.data.scrollRectToVisible(ContentView.this.data.getCellRect(0,col,true));
							key.consume();
						}
					}
				}
				else if(key.getKeyCode() == KeyEvent.VK_UP)
				{
					if(ContentView.this.data.getSelectedRow() == 0)
					{
						if(ContentView.this.getBlock() > 1)
						{
							int col = ContentView.this.data.getSelectedColumn();

							ContentView.this.control.getSlider().setValue(ContentView.this.getBlock()-2);
							int row = ContentView.this.data.getRowCount()-1;
							
							ContentView.this.data.setRowSelectionInterval(row,row);
							ContentView.this.data.scrollRectToVisible(ContentView.this.data.getCellRect(row,col,true));
							key.consume();
						}
					}

				}
			}
		});
		
		JScrollPane scroll = new JScrollPane(data);
		scroll.getViewport().setBackground(Color.white);
		add(scroll);	
		
		data.setRowSelectionAllowed(false);
		data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		data.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		data.setDefaultRenderer(Object.class,new InternalCellRenderer());
		
		data.getTableHeader().addMouseListener(popup);
		data.getTableHeader().setReorderingAllowed(false);
		
		lines = new LineNumberView();
		lines.addMouseListener(popup);
		lines.setSelectionModel(data.getSelectionModel());
		scroll.setRowHeaderView(lines);
		
		JLabel cUL = new JLabel("#",JLabel.CENTER);
		cUL.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		cUL.setFont(UIManager.getFont("TableHeader.font"));
		scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER,cUL);
		
		JLabel cLL = new JLabel();
		cLL.setBorder(new CustomLineBorder(true,false,false,false));
		scroll.setCorner(JScrollPane.LOWER_LEFT_CORNER,cLL);
		
		data.getColumnModel().getSelectionModel().addListSelectionListener(this);
		
		data.getActionMap().put("copy", ((JMenuItem)popup.getSubElementsAt(1)).getAction());
		data.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK),"copy");
		
		data.getActionMap().put("paste", ((JMenuItem)popup.getSubElementsAt(2)).getAction());
		data.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK),"paste");
				
		data.getActionMap().put("set-null", ((JMenuItem)popup.getSubElementsAt(3)).getAction());
		data.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0),"set-null");		
	}
	
	ContentPane getControl()
	{
		return control;
	}	
	
	public void addRow(Object[] rowdata, boolean newrow)
	{
		model.addRow(rowdata,newrow);
		lines.setRowCount(this.getRowCount());
	}
	
	public void addColumn(String text)
	{
		addColumn(text,Types.CHAR);
	}
    
	public void addColumn(String text,int type)
	{
		model.addColumn(text,type);
	}
    
	public void deleteRow(int row)
	{
		model.deleteRow(row);		
		lines.setRowCount(this.getRowCount());
		
		onTableChanged(true);
	}
	
	public void insertRow(int row)
	{
		model.insertRow(row);		
		lines.setRowCount(this.getRowCount());
		
		onTableChanged(true);
	}
	
	public int getCurrentRow()
	{
		return data.getSelectedRow();
	}
	
	public int getCurrentColumn()
	{
		return data.getSelectedColumn();
	}
	
	private InternalHeaderCellRenderer getHeaderRenderer(int idx)
	{
		TableColumn tc = data.getColumnModel().getColumn(idx);
		if(tc.getHeaderRenderer()!=null) return (InternalHeaderCellRenderer)tc.getHeaderRenderer();
		
		InternalHeaderCellRenderer ihcr = new InternalHeaderCellRenderer();
		tc.setHeaderRenderer(ihcr);

		return ihcr;
	}

	public void setToolTipText(int i,String text)
	{
		this.getHeaderRenderer(i).setToolTipText(text);
	}
/*	
	public void setColumnWidth(String text,int size)
	{
		TableColumn tableColumn = data.getColumn(text);
		tableColumn.setPreferredWidth(size);
		tableColumn.setWidth(size);
	}
*/
	public int getBlockCount()
	{
		return model.getBlockCount();
	}
    
	public int getBlock()
	{
		return model.getBlock();
	}
    
	public void setBlock(int idx)
	{
		model.setBlock(idx);
		lines.setBlock(idx);
		lines.setRowCount(this.getRowCount());
		
		onTableChanged(true);
	}
	
	public ContentChanges getChanges()
	{
		return model.getChanges();
	}
	
	public int getColumnCount()
	{
		return model.getColumnCount();
	}
	
	public int getColumnIndex(String name)
	{
		return model.getColumnIndex(name);
	}
	
	public String getColumnName(int idx)
	{
		return model.getColumnName(idx);
	}	
    
	public int getColumnType(int idx)
	{
		return model.getColumnType(idx);
	}	
    
	public int getRowCount()
	{
		return model.getRowCount();
	}

	public Object getLineAt(int row)
	{
		return lines.getValueAt(row,0);
	}
	
	Object[] getValues(int row)
	{
		return model.getValues(row);
	}

	Object[] getValues(Long rid)
	{
		return model.getValues(rid);
	}
	
	public Object getValueAt(int row, int col)
	{
		return model.getValueAt(row, col);
	}

	public void setValueAt(Object aValue, int row, int col)
	{
		model.setValueAt(aValue,row,col);
	}

	public int getFlatRowCount()
	{
		return model.getFlatRowCount();
	}
	
	public Object getFlatValueAt(int row, int col)
	{
		return model.getFlatValueAt(row,col);
	}

	public void resetFlatValueAt(int row, int col)
	{
		model.resetFlatValueAt(row,col);
	}
	
	public void setFlatValueAt(Object aValue, int row, int col)
	{
		model.setFlatValueAt(aValue,row,col);
	}

	public void reset()
	{
		data.setModel(model = new ContentModel());
		lines.setRowCount(0);
		lines.setBlock(1);
	}
	
	public Object getCellValue()
	{
		return data.getValueAt(data.getSelectedRow(),data.getSelectedColumn());
	}
	
	public void resetCellValue()
	{
		model.resetValueAt(data.getSelectedRow(),data.getSelectedColumn());
		data.tableChanged(new TableModelEvent(data.getModel(),data.getSelectedRow()));
	}
	
	public void setCellValue(Object value)
	{
		data.setValueAt(value, data.getSelectedRow(),data.getSelectedColumn());
		data.tableChanged(new TableModelEvent(data.getModel(),data.getSelectedRow()));
	}
	
	public void setSelectedCell(int row,int col)
	{
		data.setRowSelectionInterval(row,row);
		data.setColumnSelectionInterval(col,col);
		data.scrollRectToVisible(data.getCellRect(row,col,true));
	}
	
	public void sort(int col,short type)
	{
		model.sort(col,type);
	}
	
	public void onTableChanged(boolean onlyData)
	{
		data.tableChanged(onlyData ? new TableModelEvent(model) : null);
		lines.tableChanged(null);
	}

	public void valueChanged(ListSelectionEvent lse)
	{
		if(lse.getValueIsAdjusting()) return;
		
		for(int i=0; i<data.getColumnModel().getColumnCount(); i++)
		{
			this.getHeaderRenderer(i).setSelected(this.getCurrentColumn()==i);
		}
		data.getTableHeader().repaint();
	}

	private class InternalHeaderCellRenderer extends DefaultTableCellRenderer
	{
		InternalHeaderCellRenderer()
		{
			setFont(Appearance.fontPLAIN);
			setHorizontalAlignment(JLabel.CENTER);
		}
		
		private void setSelected(boolean b)
		{
			setFont(b ? Appearance.fontBOLD : Appearance.fontPLAIN);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if(table!=null)
			{
				JTableHeader header = table.getTableHeader();
				if (header!=null)
				{
					setForeground(header.getForeground());
					setBackground(header.getBackground());
				}
			}
				
			setText((value == null) ? "" : value.toString());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			return this;
		}
	}
	
	private class InternalCellRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int col)
		{
			super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
			super.setFont(Appearance.fontPLAIN);
			super.setOpaque(true);
		
			if(isSelected && !hasFocus)
				super.setBackground(data.getSelectionBackground());
			else
				super.setBackground(UIManager.getDefaults().getColor("Table.background"));

			if(value==null)
			{
				super.setText("<null>");
				if(ContentView.this.model.isCellChanged(row,col))
					super.setForeground(Color.green);
				else
					super.setForeground(Color.lightGray);
			}
			else
			{
				if(ContentView.this.model.isCellChanged(row,col))
					super.setForeground(Color.blue);
				else
					super.setForeground(UIManager.getDefaults().getColor("Table.foreground"));
			}
			
			if(TaskRetrieve.isNumberType(ContentView.this.getColumnType(col)))
				super.setHorizontalAlignment(RIGHT);
			else
				super.setHorizontalAlignment(LEFT);
		
			return this;
		}
	}	
}