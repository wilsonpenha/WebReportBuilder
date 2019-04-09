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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import nickyb.sqleonardo.common.util.I18n;

public abstract class QueryActions
{
	public static final String COPY_SYNTAX		= "copy-syntax";
	public static final String FIELDS_DRAGGABLE	= "fields-draggable";
	public static final String ENTITIES_ARRANGE	= "entities-arrange";
	public static final String ENTITIES_PACK	= "entities-pack";
	public static final String ENTITIES_REMOVE	= "entities-remove";
	
	static void init(QueryBuilder builder)
	{
		builder.getActionMap().put(COPY_SYNTAX		,new ActionCopySyntax(builder));
		builder.getActionMap().put(FIELDS_DRAGGABLE	,new ActionDragAndDrop(builder));
		builder.getActionMap().put(ENTITIES_ARRANGE	,new ActionArrangeEntities(builder));
		builder.getActionMap().put(ENTITIES_PACK	,new ActionPackEntities(builder));
		builder.getActionMap().put(ENTITIES_REMOVE	,new ActionRemoveEntities(builder));
	}	
	
	abstract static class AbstractQueryAction extends AbstractAction
	{
		QueryBuilder builder;
		AbstractQueryAction(QueryBuilder builder){this.builder = builder;}
	}
	
	static class ActionDragAndDrop extends AbstractQueryAction
	{
		ActionDragAndDrop(QueryBuilder builder)
		{
			super(builder);
			putValue(NAME,I18n.getString("querybuilder.action.join","join by Drag&Drop"));
		}
        
		public void actionPerformed(ActionEvent ae)
		{
			builder.setDragAndDropEnabled(!builder.isDragAndDropEnabled());
		}
	}

	static class ActionCopySyntax extends AbstractQueryAction implements ClipboardOwner
	{
		ActionCopySyntax(QueryBuilder builder)
		{
			super(builder);
			putValue(NAME,I18n.getString("querybuilder.action.copySyntax","copy syntax"));
		}
		
		public void actionPerformed(ActionEvent ae)
		{
			String value = builder.getModel().toString(true);
			
			Clipboard cb = builder.getToolkit().getSystemClipboard();
			StringSelection contents = new StringSelection(value);
			cb.setContents(contents,this);
		}

		public void lostOwnership(Clipboard clipboard, Transferable contents)
		{
		}
	}

	static class ActionArrangeEntities extends AbstractQueryAction
	{
		ActionArrangeEntities(QueryBuilder builder)
		{
			super(builder);
			putValue(NAME,I18n.getString("querybuilder.action.arrangeEntities","arrange entities"));
		}
        
		public void actionPerformed(ActionEvent e)
		{
			builder.diagram.doArrangeEntities();
		}
	}
	
	static class ActionPackEntities extends AbstractQueryAction
	{
		ActionPackEntities(QueryBuilder builder)
		{
			super(builder);
			putValue(NAME,I18n.getString("querybuilder.action.packEntities","pack entities"));
		}
        
		public void actionPerformed(ActionEvent e)
		{
			DiagramEntity[] entities = builder.diagram.getEntities();
			for(int i=0; i<entities.length; i++)
				entities[i].setPack(true);
		}
	}
	
	static class ActionRemoveEntities extends AbstractQueryAction
	{
		ActionRemoveEntities(QueryBuilder builder)
		{
			super(builder);
			putValue(NAME,I18n.getString("querybuilder.action.removeEntities","remove entities"));
		}
        
		public void actionPerformed(ActionEvent e)
		{
			if(JOptionPane.showConfirmDialog(builder,I18n.getString("querybuilder.message.continue","do you want to continue?"), I18n.getString("querybuilder.action.removeEntities","remove entities"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return;
			
			DiagramEntity[] entities = builder.diagram.getEntities();
			for(int i=0; i<entities.length; i++)
				entities[i].doDefaultCloseAction();
		}
	}
}