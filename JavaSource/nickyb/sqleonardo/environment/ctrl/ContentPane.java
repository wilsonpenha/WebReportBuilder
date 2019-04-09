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

package nickyb.sqleonardo.environment.ctrl;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;
import nickyb.sqleonardo.environment.ctrl.content.ContentModel;
import nickyb.sqleonardo.environment.ctrl.content.ContentView;
import nickyb.sqleonardo.environment.ctrl.content.DialogPreview;
import nickyb.sqleonardo.environment.ctrl.content.TableMetaData;
import nickyb.sqleonardo.environment.ctrl.content.TaskRetrieve;
import nickyb.sqleonardo.environment.ctrl.content.TaskUpdate;

public class ContentPane extends BorderLayoutPanel implements ChangeListener
{
	private boolean readonly;
	
	private JSlider sld;
	private JLabel status;
	private JTextArea syntax;
	private ContentView view;
	
	private _TaskSource query;
	private Thread task;
	
	public ContentPane(_TaskSource query, boolean readonly, boolean retrieve)
	{
		super(2,2);

		this.query = query;
		this.readonly = readonly;
				
		this.getActionMap().put("changes-save"	,new ActionSaveChanges());
		this.getActionMap().put("changes-show"	,new ActionShowChanges());
		this.getActionMap().put("record-insert"	,new ActionInsertRecord());
		this.getActionMap().put("record-delete"	,new ActionDeleteRecord());
		this.getActionMap().put("stop-task"		,new ActionStopTask());
		
		sld = new JSlider(JSlider.VERTICAL);
		sld.addChangeListener(this);
		sld.setSnapToTicks(true);
		sld.setInverted(true);
		sld.setValue(0);
		sld.setMinimum(0);
		sld.setMaximum(0);
		
		status = new JLabel("...");
		status.setBorder(new CompoundBorder(LineBorder.createGrayLineBorder(), new EmptyBorder(2,4,2,4)));
		
		JScrollPane scroll = new JScrollPane(syntax = new JTextArea());
		syntax.setRows(3);
		
		syntax.setText(query.getSyntax());
		syntax.setWrapStyleWord(true);
		syntax.setLineWrap(true);
		syntax.setEditable(false);
		syntax.setOpaque(false);
		
		BorderLayoutPanel pnlSouth = new BorderLayoutPanel(2,2);
		pnlSouth.setComponentCenter(status);
		pnlSouth.setComponentNorth(scroll);
		
		setComponentWest(sld);
		setComponentSouth(pnlSouth);
		setComponentCenter(view = new ContentView(this));
		
		if(retrieve)
			onBeginTask(new TaskRetrieve(this,query));
		else
		{
			for(int i=0; i < ((TableMetaData)query).getColumns().size(); i++)
			{
				String name = ((TableMetaData)query).getColumnProperty(i,TableMetaData.IDX_COL_COLUMN_NAME);
				String type = ((TableMetaData)query).getColumnProperty(i,TableMetaData.IDX_COL_DATA_TYPE);
				
				view.addColumn(name,Integer.valueOf(type).intValue());
			}
			
			onEndTask();
			doRefreshStatus();
			view.onTableChanged(false);
		}
	}
	
	public boolean isReadOnly()
	{
		return readonly;
	}

	public _TaskSource getTaskSource()
	{
		return query;
	}
	
	public JSlider getSlider()
	{
		return sld;
	}
	
	public ContentView getView()
	{
		return view;
	}
	
	public void onBeginTask(Runnable r)
	{
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		this.getActionMap().get("changes-save").setEnabled(false);
		this.getActionMap().get("changes-show").setEnabled(false);
		this.getActionMap().get("record-insert").setEnabled(false);
		this.getActionMap().get("record-delete").setEnabled(false);
		
		task = new Thread(r);
		task.start();
		
		this.getActionMap().get("stop-task").setEnabled(true);
	}
	
	public boolean isBusy()
	{
		return task!=null;
	}
		
	public void onEndTask()
	{
		task = null;		
		
		this.getActionMap().get("stop-task").setEnabled(false);
		this.getActionMap().get("changes-save").setEnabled(true && !readonly);
		this.getActionMap().get("changes-show").setEnabled(true && !readonly);
		this.getActionMap().get("record-insert").setEnabled(true && !readonly);
		this.getActionMap().get("record-delete").setEnabled(true && !readonly);
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void doRefreshStatus()
	{
		sld.setMaximum(view.getBlockCount()==0?0:view.getBlockCount()-1);
		
		if(view.getRowCount() > 0)
			status.setText(	"block " + view.getBlock() + " of " + view.getBlockCount() +
							" | record " + view.getLineAt(0) + " to " + view.getLineAt(view.getRowCount()-1) + " of " + view.getFlatRowCount() +
							" | changes " + view.getChanges().count());
		else
			status.setText("0 records");
	}
	
	public void stateChanged(ChangeEvent e)
	{
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting())
		{
			int block = source.getValue()+1;
			
			if(view!=null && block!=view.getBlock())
			{
				view.setBlock(block);
				doRefreshStatus();
			}
	    }
	}
	
	private class ActionSaveChanges extends AbstractAction
	{
		ActionSaveChanges()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_CONTENT_UPDATE));
			this.putValue(SHORT_DESCRIPTION, "save changes");
		}

		public void actionPerformed(ActionEvent ae)
		{
			/* BAD */
			if(((TableMetaData)query).getPrimaryKeys().size() == 0 )
			{
				Application.alert(Application.PROGRAM,"no primary keys founded!");
				return;
			}			
			
			onBeginTask(new TaskUpdate(ContentPane.this,query));
		}
	}
	
	private class ActionShowChanges extends AbstractAction
	{
		ActionShowChanges()
		{
			super("show changes...");
		}

		public void actionPerformed(ActionEvent ae)
		{
			/* BAD */
			if(((TableMetaData)query).getPrimaryKeys().size() == 0 )
			{
				Application.alert(Application.PROGRAM,"no primary keys founded!");
				return;
			}			
			
			new DialogPreview(ContentPane.this.view,(TableMetaData)ContentPane.this.query).show();
		}
	}
	
	private class ActionInsertRecord extends AbstractAction
	{
		ActionInsertRecord()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_CONTENT_INSERT));
			this.putValue(SHORT_DESCRIPTION, "insert record");
			this.putValue(NAME, "insert record");
		}

		public void actionPerformed(ActionEvent ae)
		{
			int row = ContentPane.this.view.getCurrentRow();
			int col = ContentPane.this.view.getCurrentColumn();
			
			ContentPane.this.view.insertRow(++row);
			ContentPane.this.doRefreshStatus();
			
			if(row == ContentModel.MAX_BLOCK_RECORDS)
			{
				row = 0;
				ContentPane.this.sld.setValue(ContentPane.this.sld.getValue()+1);
			}
			ContentPane.this.view.setSelectedCell(row,(col == -1 ? 0 : col));
		}
	}
	
	private class ActionDeleteRecord extends AbstractAction
	{
		ActionDeleteRecord()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_CONTENT_DELETE));
			this.putValue(SHORT_DESCRIPTION, "delete record");
			this.putValue(NAME, "delete record");
		}

		public void actionPerformed(ActionEvent ae)
		{
			int row = ContentPane.this.view.getCurrentRow();
			int col = ContentPane.this.view.getCurrentColumn();
			
			if(row==-1) return;
			
			ContentPane.this.view.deleteRow(row);
			ContentPane.this.doRefreshStatus();
			
			if(ContentPane.this.view.getRowCount() == 0) return;

			if(row >= ContentPane.this.view.getRowCount()) row = ContentPane.this.view.getRowCount()-1;
			ContentPane.this.view.setSelectedCell(row,(col == -1 ? 0 : col));
		}
	}
	
	private class ActionStopTask extends AbstractAction
	{
		ActionStopTask()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_STOP));
		}

		public void actionPerformed(ActionEvent ae)
		{
			ContentPane.this.onEndTask();
		}
	}
}