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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.ctrl.editor.Task;
import nickyb.sqleonardo.environment.ctrl.editor._TaskSource;
import nickyb.sqleonardo.environment.ctrl.editor._TaskTarget;
import nickyb.sqleonardo.environment.mdi.ClientCommandEditor;

public class CommandEditor extends BorderLayoutPanel implements _TaskTarget
{
	private boolean stopped;
	
	private Thread queryThread;
	
	private JTextArea request;
	private JTextArea response;
	
	public CommandEditor()
	{
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.setTopComponent(new JScrollPane(request = new JTextArea()));
		split.setBottomComponent(new JScrollPane(response = new JTextArea()));
		split.setOneTouchExpandable(true);
		
		request.setTabSize(4);
		response.setTabSize(4);
		response.setEditable(false);

		setComponentCenter(split);

		request.getActionMap().put("stop-task"	,new ActionStopTask());
		request.getActionMap().put("start-task",new ActionStartTask());
		request.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,KeyEvent.CTRL_MASK),"start-task");
		
		request.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,KeyEvent.SHIFT_MASK),DefaultEditorKit.pasteAction);
		request.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,KeyEvent.CTRL_MASK),DefaultEditorKit.copyAction);
		request.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,KeyEvent.SHIFT_MASK),DefaultEditorKit.cutAction);
				
		getActionMap().setParent(request.getActionMap());
		
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent evt)
			{
				JSplitPane split = (JSplitPane)CommandEditor.this.getComponent(0);
				split.setDividerLocation(0.5);
				split.validate();
			} 
		});
	}
	
	public void append(String text)
	{
		request.append(text);
	}
	
	public void clearResponse()
	{
		response.setText(null);
		request.requestFocus();
	}

	public String getSelectedText()
	{
		return request.getSelectedText();
	}
	
	public Document getDocument()
	{
		return request.getDocument();
	}
	
	public void setDocument(Document doc)
	{
		this.request.setDocument(doc);
		this.request.setCaretPosition(0);
		this.request.setTabSize(4);
		this.request.requestFocus();
	}
	
//	/////////////////////////////////////////////////////////////////////////////
//	Actions
//	/////////////////////////////////////////////////////////////////////////////
	private class ActionStartTask extends AbstractAction implements Runnable
	{
		ActionStartTask()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_EDITOR_RUN));
			this.putValue(SHORT_DESCRIPTION, "launch");
		}

		public void actionPerformed(ActionEvent ae)
		{
			CommandEditor.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			CommandEditor.this.getActionMap().get("stop-task").setEnabled(true);
			this.setEnabled(false);
			
			stopped = false;
			queryThread = new Thread(this);
			queryThread.start();
		}
		
		public void run()
		{
			String requestString = request.getSelectedText();
			if(requestString == null || requestString.trim().length() == 0)
			{
				// line
				try
				{
					int line = request.getLineOfOffset(request.getCaretPosition());
					
					request.setSelectionStart(request.getLineStartOffset(line));
					request.setSelectionEnd(request.getLineEndOffset(line));
					requestString = request.getSelectedText();
				}
				catch (BadLocationException e)
				{
					Application.println(e,false);
				}
			}
			
			if(requestString == null || requestString.trim().length() == 0)
			{
				// full
				request.setSelectionStart(0);
				request.setSelectionEnd(request.getText().length());
				requestString = request.getSelectedText();
			}
			
			if(requestString != null && requestString.trim().length() > 0)
			{
				request.requestFocus();
				StringTokenizer st = new StringTokenizer(requestString.trim(),";");
				while(!stopped && st.hasMoreTokens())
				{
					_TaskSource source = new TaskSource(st.nextToken());
					// rimuovere gli a capo da token!
					CommandEditor.this.response.append("\n" + source.getSyntax() + "\n");
					
					new Task(source,CommandEditor.this).run();
				}
			}
			this.setEnabled(true);
			CommandEditor.this.getActionMap().get("stop-task").setEnabled(false);
			CommandEditor.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private class ActionStopTask extends AbstractAction
	{
		ActionStopTask()
		{
			this.putValue(SMALL_ICON, Application.resources.getIcon(Application.ICON_STOP));
			this.putValue(SHORT_DESCRIPTION, "stop");
			this.setEnabled(false);
		}

		public void actionPerformed(ActionEvent ae)
		{
			this.setEnabled(false);
			CommandEditor.this.queryThread = null;
		}
	}
	
//	/////////////////////////////////////////////////////////////////////////////
//	_TaskTarget
//	/////////////////////////////////////////////////////////////////////////////
	public boolean continueRun()
	{
		return queryThread != null;
	}

	public void onTaskFinished()
	{
		response.append("\n");
	}

	public void onTaskStarting()
	{
	}

	public void message(String text)
	{
	}

	public void write(String text)
	{
		response.append(text);
		try
		{
			int line = response.getLineCount();
			int off = response.getLineStartOffset(line-1);
			
			response.setCaretPosition(off);
		}
		catch (BadLocationException e)
		{
			e.printStackTrace();
		}
	}
	
//	/////////////////////////////////////////////////////////////////////////////
//	TaskSource
//	/////////////////////////////////////////////////////////////////////////////
	private class TaskSource implements _TaskSource
	{
		private String query;
		
		private TaskSource(String query)
		{
			this.query = query;
		}
		
		public String getHandlerKey()
		{
			ClientCommandEditor client = (ClientCommandEditor)Application.application.getClient(ClientCommandEditor.DEFAULT_TITLE);
			return client.getActiveConnection();
		}
		public String getSyntax()
		{
			return query;
		}
	}
}
