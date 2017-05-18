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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Types;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import nickyb.sqleonardo.common.util.Text;
import nickyb.sqleonardo.environment.Application;

public class MaskExport extends AbstractMaskPerform
{
	private AbstractChoice eChoice;

	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		for(int i=0; i<eChoice.getComponentCount();i++)
			eChoice.getComponent(i).setEnabled(b);
	}
	
	void setType(short type, String tname, String fname)
	{
		if(eChoice!=null) remove(eChoice);
		
		progress.setValue(0);
		progress.setMaximum(0);
		
		if(type == WEB)
		{
			if(!fname.endsWith(".htm") && !fname.endsWith(".html")) fname = fname + ".html"; 
			setComponentCenter(eChoice = new WebChoice());
		}
		else if(type == SQL)
		{
			if(!fname.endsWith(".sql")) fname = fname + ".sql"; 
			setComponentCenter(eChoice = new SqlChoice(tname));
		}
		else if(type == TXT)
		{
			if(!fname.endsWith(".txt")) fname = fname + ".txt"; 
			setComponentCenter(eChoice = new TxtChoice());
		}
			
		lblFile.setText("file: " + fname);
	}
	
//	-----------------------------------------------------------------------------------------
//	?????????????????????????????????????????????????????????????????????????????????????????
//	-----------------------------------------------------------------------------------------
	void init(ContentView view)
	{
		super.init(view);
		
		progress.setValue(0);
		progress.setMaximum(view.getFlatRowCount());
		
		eChoice.open();
	}
	
	void next()
	{
		eChoice.handle(view.getValues(progress.getValue()));
		progress.setValue(progress.getValue()+1);
	}
	
	boolean finished()
	{
		if(progress.getValue() == progress.getMaximum())
		{
			eChoice.close();
			
			btnStop.setEnabled(false);
			lblMsg.setText("ready!");
			
			return true;
		}
		
		return false;
	}
//	-----------------------------------------------------------------------------------------
//	-----------------------------------------------------------------------------------------
	private abstract class AbstractChoice extends JPanel
	{
		private PrintStream stream;

		AbstractChoice(String title)
		{
			setBorder(new TitledBorder(title));
		}
		
		void open()
		{
			try
			{
				stream = new PrintStream(new FileOutputStream(MaskExport.this.lblFile.getText().substring(6)));
			}
			catch (FileNotFoundException e)
			{
				Application.println(e,true);
			}
		}
		
		abstract void handle(Object[] vals);
		
		void close()
		{
			stream.close();
		}
		
		void print(String s)
		{
			stream.print(s);
		}
		
		void println(String s)
		{
			stream.println(s);
		}
	}

	private class WebChoice extends AbstractChoice
	{
		JCheckBox cbxHeader;
		
		WebChoice()
		{
			super("options");
			setLayout(new FlowLayout(FlowLayout.LEFT));
			add(cbxHeader = new JCheckBox("with header"));
		}

		void open()
		{
			super.open();
			println("<html><body><table border=1>");
			
			if(cbxHeader.isSelected())
			{
				print("<tr>");
				for(int col=0; col<view.getColumnCount(); col++)
				{
					print("<th>" + view.getColumnName(col) + "</th>");
				}		
				println("</tr>");
			}
		}

		void handle(Object[] vals)
		{
			print("<tr>");
			for(int i=0; i<vals.length; i++)
			{
				String val = vals[i] == null ? "null" : vals[i].toString();
				print("<td>" + val + "</td>");
			}		
			println("</tr>");
		}

		void close()
		{
			println("</table></body></html>");
			super.close();
		}
	}

	private class SqlChoice extends AbstractChoice
	{
		JCheckBox cbxDelete;
		JTextField txtTable;
		
		String insert = null;
		
		SqlChoice(String tname)
		{
			super("options");
			
			GridBagLayout gbl = new GridBagLayout();
			setLayout(gbl);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			
			cbxDelete = new JCheckBox("with delete statement");
			gbl.setConstraints(cbxDelete, gbc);
			add(cbxDelete);

			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.insets = new Insets(5,3,0,3);
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
				
			JLabel lbl = new JLabel("table name:");
			gbl.setConstraints(lbl, gbc);
			add(lbl);
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			txtTable = new JTextField(tname,10);
			gbl.setConstraints(txtTable, gbc);
			add(txtTable);
		}

		void open()
		{
			super.open();
			
			if(cbxDelete.isSelected())
			{
				println("DELETE FROM " + txtTable.getText() + ";");
			}
			
			StringBuffer buffer = new StringBuffer("INSERT INTO " + txtTable.getText() + " (");
			for(int col=0; col<view.getColumnCount(); col++)
			{
				buffer.append(view.getColumnName(col) + ",");
			}
			buffer.deleteCharAt(buffer.length()-1);
			insert = buffer.toString() + ")";
		}

		void handle(Object[] vals)
		{
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<vals.length; i++)
			{
//				String val = vals[i] == null ? "null" : vals[i].toString();
				buffer.append(toSQLValue(vals[i],i+1) + ",");
			}
			buffer.deleteCharAt(buffer.length()-1);		
			println(insert + " VALUES (" + buffer.toString() + ");");
		}
		
		private String toSQLValue(Object value,int col)
		{
			if(value==null) return "null";
		
			switch(MaskExport.this.view.getColumnType(col))
			{
				case Types.CHAR:
				case Types.VARCHAR:
					value = Text.replaceText(value.toString(),"\'","\\\'");
					return "'" + value.toString() + "'";
				case Types.DATE:
					return "{d '" + value.toString() + "'}";
				case Types.TIME:
					return "{t '" + value.toString() + "'}";
				case Types.TIMESTAMP:
					return "{ts '" + value.toString() + "'}";
				default:
					return value.toString();
			}
		}
	}

	private class TxtChoice extends AbstractChoice
	{
		JCheckBox cbxHeader;
		JCheckBox cbxNull;
		JCheckBox cbxTrim;
		
		JRadioButton rbTab;
		JRadioButton rbOther;
		
		JTextField txtDelimiter;
		
		TxtChoice()
		{
			super("options");
			
			GridBagLayout gbl = new GridBagLayout();
			setLayout(gbl);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.NORTHWEST;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			
			cbxHeader = new JCheckBox("with header");
			gbl.setConstraints(cbxHeader, gbc);
			add(cbxHeader);

			cbxNull = new JCheckBox("null if blanks");
			cbxNull.setEnabled(false);
			gbl.setConstraints(cbxNull, gbc);
			add(cbxNull);
			
			cbxTrim = new JCheckBox("trim value");
			cbxTrim.setEnabled(false);
			gbl.setConstraints(cbxTrim, gbc);
			add(cbxTrim);			
			
			gbc = new GridBagConstraints();
			gbc.insets = new Insets(5,3,0,0);
			gbc.weighty = 1.0;
			
			JLabel lbl = new JLabel("delimiter:");
			gbl.setConstraints(lbl, gbc);
			add(lbl);
			
			rbTab = new JRadioButton("tab",true);
			gbl.setConstraints(rbTab, gbc);
			add(rbTab);
			
			rbOther = new JRadioButton("other");
			gbl.setConstraints(rbOther, gbc);
			add(rbOther);
			
			gbc.anchor = GridBagConstraints.WEST;
			txtDelimiter = new JTextField(";",5);
			txtDelimiter.setEditable(false);
			txtDelimiter.setEnabled(false);
			gbl.setConstraints(txtDelimiter, gbc);
			add(txtDelimiter);
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(rbTab);
			bg.add(rbOther);

			rbTab.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					txtDelimiter.setEditable(!rbTab.isSelected());
					txtDelimiter.setEnabled(!rbTab.isSelected());
				}
			});
		}
		
		private String getDelimiter()
		{
			if(rbTab.isSelected()) return "\t";
			return txtDelimiter.getText();
		}

		void open()
		{
			super.open();
			
			if(cbxHeader.isSelected())
			{
				StringBuffer buffer = new StringBuffer();
				for(int col=0; col<view.getColumnCount(); col++)
				{
					buffer.append(view.getColumnName(col) + getDelimiter());
				}
				if(buffer.length() > 0) buffer.deleteCharAt(buffer.length()-1);
				println(buffer.toString());
			}
		}

		void handle(Object[] vals)
		{
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<vals.length; i++)
			{
				String val = vals[i] == null ? "null" : vals[i].toString();
				buffer.append(val + getDelimiter());
			}
			if(buffer.length() > 0) buffer.deleteCharAt(buffer.length()-1);
			println(buffer.toString());
		}
	}
}
