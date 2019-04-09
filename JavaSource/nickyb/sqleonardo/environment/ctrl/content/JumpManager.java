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

import java.awt.Cursor;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.ContentPane;
import nickyb.sqleonardo.querybuilder.syntax.SQLFormatter;
import nickyb.sqleonardo.environment.mdi.ClientContent;

public class JumpManager implements Runnable
{
	private ContentPane owner;

	private Hashtable h = new Hashtable();
	private Vector v = new Vector();
	
	public static void perform(ContentPane control)
	{
		JumpManager j = new JumpManager();
		j.owner = control;
		
		new Thread(j).start();
	}
	
	public void run()
	{
		owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		TableMetaData tmd = (TableMetaData)owner.getTaskSource();
		
		int row = owner.getView().getCurrentRow();
		int col = owner.getView().getCurrentColumn();
			
		String column = owner.getView().getColumnName(col);
		
		for(int i=0; i<tmd.getImportedKeys().size(); i++)
		{
			if(tmd.getImportedKeyProperty(i,TableMetaData.IDX_REL_FKCOLUMN_NAME).equals(column))
			{
				TableMetaData tmdPK = new TableMetaData(tmd.getHandlerKey(),
														tmd.getImportedKeyProperty(i,TableMetaData.IDX_REL_PKTABLE_SCHEM),
														tmd.getImportedKeyProperty(i,TableMetaData.IDX_REL_PKTABLE_NAME));
				
				String pk = tmd.getImportedKeyProperty(i,TableMetaData.IDX_REL_PKCOLUMN_NAME);
				String id = ">> " + tmdPK.getIdentifier() + " (" + pk + ")";

				h.put(id,tmdPK);

				if(!v.contains(id))
					v.addElement(id);
			}
		}
		
		if(tmd.isPrimaryKey(column))
		{
			for(int i=0; i<tmd.getExportedKeys().size(); i++)
			{
				if(tmd.getExportedKeyProperty(i,TableMetaData.IDX_REL_PKCOLUMN_NAME).equals(column))
				{
					TableMetaData tmdFK = new TableMetaData(tmd.getHandlerKey(),
															tmd.getExportedKeyProperty(i,TableMetaData.IDX_REL_FKTABLE_SCHEM),
															tmd.getExportedKeyProperty(i,TableMetaData.IDX_REL_FKTABLE_NAME));
					
					String fk = tmd.getExportedKeyProperty(i,TableMetaData.IDX_REL_FKCOLUMN_NAME);
					String id = "<< " + tmdFK.getIdentifier() + " (" + fk + ")";
					
					h.put(id,tmdFK);

					if(!v.contains(id))
						v.addElement(id);
				}	
			}
		}
		
		owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		Object jumpTo = null;
		if(v.size() == 1)
		{
			if(JOptionPane.showConfirmDialog(Application.application,"jump from '" + column + "' to:\n" + v.elementAt(0),Application.PROGRAM,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION)
				jumpTo = v.elementAt(0);
		}
		else if(v.size() > 1)
		{
			jumpTo = JOptionPane.showInputDialog(Application.application,"jump from '" + column + "' to:",Application.PROGRAM,JOptionPane.PLAIN_MESSAGE,null,v.toArray(),null);
		}
		else
		{
			JOptionPane.showMessageDialog(Application.application,"column '" + column + "' has no references",Application.PROGRAM,JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		if(jumpTo!=null)
		{
			owner.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			TableMetaData tmdJ = (TableMetaData)h.get(jumpTo);
			
			int pos = jumpTo.toString().indexOf('(');
			String filter = jumpTo.toString().substring(pos+1,jumpTo.toString().length()-1);
			
			int sqltype = 0;
			for(int i=0; i<tmdJ.getColumns().size(); i++)
			{
				if(tmdJ.getColumnProperty(i,TableMetaData.IDX_COL_COLUMN_NAME).equals(filter))
					sqltype = Integer.valueOf(tmdJ.getColumnProperty(i,TableMetaData.IDX_COL_DATA_TYPE)).intValue();
			}

			ConnectionHandler ch = ConnectionAssistant.getHandler(tmdJ.getHandlerKey());
			filter = SQLFormatter.ensureQuotes(filter,ch.getObject("$identifierQuoteString").toString(),false);
			
			Object value = owner.getView().getValueAt(row,col);
			tmdJ.setFilter(filter + " = " + SQLFormatter.toJdbcValue(value,sqltype));
			
			String title = "CONTENT : " + tmdJ.getIdentifier() + " : " + tmdJ.getHandlerKey();
			Application.application.add(new ClientContent(title, tmdJ,!tmdJ.getType().equals("TABLE"),true));
			
			owner.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}