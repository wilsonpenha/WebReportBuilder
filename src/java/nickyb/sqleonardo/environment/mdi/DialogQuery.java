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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nickyb.sqleonardo.common.gui.AbstractDialogWizard;
import nickyb.sqleonardo.common.gui.AbstractMaskChooser;
import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.common.jdbc.ConnectionHandler;
import nickyb.sqleonardo.querybuilder.QueryModel;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.io.FileStreamLQY;
import nickyb.sqleonardo.environment.io.FileStreamSQL;
import nickyb.sqleonardo.environment.io.FileStreamXLQ;

public class DialogQuery extends AbstractDialogWizard
{
	private final static String LOAD = "query.load";
	private final static String SAVE = "query.save";
	
	private AbstractMaskChooser mkc;
	private MaskPreview mkp;
	
	private boolean terminated;

	private DialogQuery(String title)
	{
		super(Application.application,title);
		mkp = new MaskPreview();
	}

	private DialogQuery(String title,QueryModel model)
	{
		this(title);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		mkp.setModel(model);
	}
	
	public static Object[] showLoad()
	{
		DialogQuery dlg = new DialogQuery(LOAD);
		dlg.show();
		
		return onDispose(dlg);
	}
	
	public static Object[] showSave(QueryModel model)
	{
		DialogQuery dlg = new DialogQuery(SAVE,model);
		dlg.show();
		
		return onDispose(dlg);
	}
	
	private static Object[] onDispose(DialogQuery dlg)
	{
		Object[] ret = new Object[4];
		
		if(dlg.terminated)
		{
			ret[0] = dlg.mkc.getSelectedFile();
			ret[1] = dlg.mkp.getModel();
			ret[2] = dlg.mkp.cbxConnections.getSelectedItem();
			ret[3] = dlg.mkp.cbxSchemas.getSelectedItem();
		}
		
		dlg.dispose();
		return ret;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		terminated = ae.getSource() == btnNext && getStep() == 1;
		super.actionPerformed(ae);
	}

	protected boolean onNext()
	{
		if(this.getTitle().equals(LOAD))
		{
			if(getStep()==0)
			{
				return doLoad();
			}
			else if(mkp.cbxConnections.getSelectedItem()==null)
			{
				Application.alert(Application.PROGRAM,"Please, choose a connection.");
				return false;
			}
		}
		else if(getStep()==1)
		{
			return doSave();
		}
		
		return true;
	}
	
	protected void onOpen()
	{
		if(this.getTitle().equals(LOAD))
		{
			mkc = new DefaultMaskChooser(AbstractMaskChooser.OPEN_DIALOG,AbstractMaskChooser.FILES_ONLY,false);
			addStep(mkc);
			addStep(mkp);
			mkc.setFileFilter(new LQYFilter());
		}
		else
		{
			mkc = new DefaultMaskChooser(AbstractMaskChooser.SAVE_DIALOG,AbstractMaskChooser.FILES_ONLY,false);
			addStep(mkp);
			addStep(mkc);
		}
		mkc.setFileFilter(new XLQFilter());
		mkc.setFileFilter(new SQLFilter());
		
		getContentPane().validate();		
	}

	private boolean doLoad()
	{
		if(mkc.getSelectedFile()==null) return false;
		String filename = mkc.getSelectedFile().toString();
		
		try
		{
			QueryModel model;
			
			if(filename.endsWith(".xlq"))
				model = FileStreamXLQ.read(filename);
			else if(filename.endsWith(".lqy"))
				model = FileStreamLQY.read(filename);
			else if(filename.endsWith(".sql"))
				model = FileStreamSQL.read(filename);
			else
				return false;

			mkp.setModel(model);
			return true;
		}
		catch(Exception e)
		{
			Application.println(e,true);
			e.printStackTrace();
			
			this.show();
			
			return false;
		}
	}

	private boolean doSave()
	{
		if(mkc.getSelectedFile()==null) return false;
		String filename = mkc.getSelectedFile().toString();
		
		AbstractMaskChooser.AbstractFileFilter filter = (AbstractMaskChooser.AbstractFileFilter)mkc.getFileFilter();
		if(filter.getPerformType() == 1 && !filename.endsWith(".xlq"))
			mkc.setSelectedFile(new File(filename += ".xlq"));
		else if(filter.getPerformType() == 2 && !filename.endsWith(".sql"))
			mkc.setSelectedFile(new File(filename += ".sql"));
		
		try
		{
			if(filter.getPerformType() == 1) // xlq
				FileStreamXLQ.write(filename,mkp.qmodel);
			else if(filter.getPerformType() == 2) // sql
				FileStreamSQL.write(filename,mkp.qmodel);
			else
				return false;
				
			return true;
		}
		catch(Exception e)
		{
			Application.println(e,true);
			e.printStackTrace();
			
			return false;
		}
	}

	private static class LQYFilter extends AbstractMaskChooser.AbstractFileFilter
	{
		LQYFilter(){super("sqleonardo query < 2006.08, (.lqy)",new String[]{".lqy"});}
		public short getPerformType(){return 0;}
	}
	
	private static class XLQFilter extends AbstractMaskChooser.AbstractFileFilter
	{
		XLQFilter(){super("sqleonardo query, (.xlq)",new String[]{".xlq"});}
		public short getPerformType(){return 1;}
	}
	
	private static class SQLFilter extends AbstractMaskChooser.AbstractFileFilter
	{
		SQLFilter(){super("statement, (.sql)",new String[]{".sql"});}
		public short getPerformType(){return 2;}
	}
	
	private class MaskPreview extends BorderLayoutPanel implements ItemListener
	{
		private QueryModel qmodel;
		
		JComboBox cbxConnections;
		JComboBox cbxSchemas;
		
		JTextArea txt;
		
		MaskPreview()
		{
			super(5,5);
			
			txt = new JTextArea();
			txt.setEditable(false);

			cbxConnections = new JComboBox(ConnectionAssistant.getHandlers().toArray());
			cbxSchemas = new JComboBox();
			
			cbxConnections.setSelectedItem(null);
			cbxSchemas.setEnabled(false);
			
			cbxConnections.addItemListener(this);
			
			JPanel pnlLabels = new JPanel(new GridLayout(2,1,0,3));
			JPanel pnlCbxs = new JPanel(new GridLayout(2,1,0,3));
			
			pnlLabels.add(new JLabel("connection:"));
			pnlLabels.add(new JLabel("schema:"));
			
			pnlCbxs.add(cbxConnections);
			pnlCbxs.add(cbxSchemas);
			
			BorderLayoutPanel pnlSouth = new BorderLayoutPanel(3,3);
			pnlSouth.setComponentWest(pnlLabels);
			pnlSouth.setComponentCenter(pnlCbxs);
			pnlSouth.setVisible(DialogQuery.this.getTitle().equals(LOAD));
			
			setComponentCenter(new JScrollPane(txt));
			setComponentSouth(pnlSouth);
		}

		QueryModel getModel()
		{
			return qmodel;
		}
		
		void setModel(QueryModel model)
		{
			qmodel = model;
			if(model!=null)
				txt.setText(qmodel.toString(true));			
		}

		public void itemStateChanged(ItemEvent ie)
		{
			ConnectionHandler ch = ConnectionAssistant.getHandler(ie.getItem().toString());
			ArrayList schemas = (ArrayList)ch.getObject("$schema_names");
			
			cbxSchemas.setModel(new DefaultComboBoxModel(schemas.toArray()));
			cbxSchemas.setEnabled(cbxSchemas.getItemCount()>0);
			cbxSchemas.setSelectedItem(qmodel.getSchema());
		}
	}
}