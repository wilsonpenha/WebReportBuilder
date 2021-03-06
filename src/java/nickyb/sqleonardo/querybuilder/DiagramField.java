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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.querybuilder.dnd.DragMouseAdapter;
import nickyb.sqleonardo.querybuilder.dnd.RelationDropTargetListener;
import nickyb.sqleonardo.querybuilder.dnd.RelationTransferHandler;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax._ReservedWords;

public class DiagramField extends JPanel implements ItemListener, MouseListener, PopupMenuListener
{
	int position;
	static ImageIcon keyIcon = null;

	QueryTokens.Column querytoken;
	private DiagramEntity owner;

	private JCheckBox checkboxComponent = null;
	private JLabel labelComponent = null;

	private MouseListener listener;

	DiagramField(DiagramEntity entity, String name, boolean iskey)
	{
		super();
		setOwner(entity);

		if (keyIcon == null)
			keyIcon = new javax.swing.ImageIcon(getClass().getResource("/images/bullet_key.png"));

		this.setLayout(new BorderLayout());
		checkboxComponent = new JCheckBox();

		labelComponent = new JLabel(name);
		labelComponent.setHorizontalTextPosition(JLabel.LEFT);
		
		Font f = labelComponent.getFont();
		if (!iskey)
		{
			f = new Font(f.getName(), Font.PLAIN, f.getSize());
		}
		else
		{
			f = new Font(f.getName(), Font.BOLD, f.getSize());
			labelComponent.setIcon(keyIcon);
		}
		labelComponent.setFont(f);

		this.add(getCheckboxComponent(), BorderLayout.WEST);
		this.add(labelComponent, BorderLayout.CENTER);

		getLabelComponent().addMouseListener(this);
		getLabelComponent().addMouseListener(listener = new DragMouseAdapter());
		getLabelComponent().setTransferHandler(new RelationTransferHandler());
		getLabelComponent().setDropTarget(new DropTarget(this, new RelationDropTargetListener(getOwner().builder.diagram)));

		getCheckboxComponent().addItemListener(this);
		getCheckboxComponent().addMouseListener(this);
		getCheckboxComponent().setPreferredSize(new Dimension(20, 8));
		getCheckboxComponent().setBorderPainted(false);
		getCheckboxComponent().setFocusPainted(false);
		getCheckboxComponent().setOpaque(false);
		
		setOpaque(true);
		setName(name);
		setBorder(new LineBorder(UIManager.getColor("List.background")));
		setDefaultQueryToken();

	}

	public void setToolTipText(String text)
	{
		labelComponent.setToolTipText(text);
	}

	void setDefaultQueryToken()
	{
		querytoken = new QueryTokens.Column(getOwner().getQuerytoken(), this.getName());

		if (QueryBuilder.autoAlias)
		{
			String alias = querytoken.getTable().getAlias() + "." + querytoken.getName();

			if (alias.length() > QueryBuilder.maxColumnNameLength && QueryBuilder.maxColumnNameLength > 0)
				alias = alias.substring(0, QueryBuilder.maxColumnNameLength);

			querytoken.setAlias(alias);
		}
	}

	void setQueryToken(QueryTokens.Column token)
	{
		querytoken = token;
		checkboxComponent.setSelected(true);
	}

	public void itemStateChanged(ItemEvent ie)
	{
		getOwner().onSelectionChanged(this);
	}

	public void mouseReleased(MouseEvent me)
	{
		if (SwingUtilities.isRightMouseButton(me))
		{
			JPopupMenu popup = new JPopupMenu(this.getName());
			popup.addPopupMenuListener(this);

			popup.add(new MenuItemSelect());
			popup.addSeparator();
			popup.add(new ActionAddWhere());
			popup.add(new ActionAddHaving());
			popup.addSeparator();
			popup.add(new ActionAddExpression());

			popup.show(this, me.getX(), me.getY());
		}
		else if (!this.getOwner().builder.isDragAndDropEnabled())
		{
			getOwner().builder.diagram.join(this.getOwner(),this);
		}
	}

	public void mouseExited(MouseEvent me)
	{
	}
	public void mouseClicked(MouseEvent me)
	{
	}
	public void mouseEntered(MouseEvent me)
	{
	}
	public void mousePressed(MouseEvent me)
	{
	}

	public void popupMenuCanceled(PopupMenuEvent pme)
	{
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent pme)
	{
		//setBorder(new EmptyBorder(0,1,0,1));
		setBorder(new LineBorder(UIManager.getColor("List.background")));
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent pme)
	{
		setBorder(new LineBorder(UIManager.getColor("List.selectionBackground")));
		//setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
	}

	//	/////////////////////////////////////////////////////////////////////////////
	//	Join Manager
	//	/////////////////////////////////////////////////////////////////////////////
	private int joins;
	boolean isJoined()
	{
		return joins > 0;
	}

	void joined()
	{
		joins++;
	}

	void unjoined()
	{
		joins--;
	}

	void setDragAndDropEnabled(boolean b)
	{
		if(b)
			getLabelComponent().addMouseListener(listener);
		else
			getLabelComponent().removeMouseListener(listener);
	}

	//	/////////////////////////////////////////////////////////////////////////////
	//	Popup Actions
	//	/////////////////////////////////////////////////////////////////////////////
	private class MenuItemSelect extends JCheckBoxMenuItem implements ActionListener
	{
		private MenuItemSelect()
		{
			super("select");
			addActionListener(this);
			setState(DiagramField.this.isSelected());
		}

		public void actionPerformed(ActionEvent ae)
		{
			DiagramField.this.setSelected(this.getState());
		}
	}

	private class ActionAddExpression extends AbstractAction
	{
		private ActionAddExpression()
		{
			super(I18n.getString("querybuilder.menu.addExpression", "add expression..."));
		}

		public void actionPerformed(ActionEvent e)
		{
			String[] functions = new String[] { "count", "max", "min", "sum" };
			Object choose =
				JOptionPane.showInputDialog(
					DiagramField.this.getOwner().builder,
					I18n.getString("querybuilder.message.chooseFunction", "choose function:"),
					I18n.getString("querybuilder.menu.addExpression", "add expression..."),
					JOptionPane.PLAIN_MESSAGE,
					null,
					functions,
					null);

			if (choose != null)
			{
				String expr = choose.toString() + "(" + DiagramField.this.querytoken.getIdentifier() + ")";
				QueryTokens.DefaultExpression token = new QueryTokens.DefaultExpression(expr);
				getOwner().builder.browser.addSelectList(token);
			}
		}
	}

	private abstract class ActionAddCondition extends AbstractAction
	{
		abstract void add(QueryTokens.Condition token);
		abstract boolean isFirst();

		public void actionPerformed(ActionEvent e)
		{
			QueryTokens.Condition token = new QueryTokens.Condition();
			token.setLeft(new QueryTokens.DefaultExpression(DiagramField.this.querytoken.getIdentifier()));

			if (!isFirst())
				token.setAppend(_ReservedWords.AND);
			if (new MaskCondition(token, DiagramField.this.owner.builder).showDialog())
				add(token);
		}
	}

	private class ActionAddWhere extends ActionAddCondition
	{
		private ActionAddWhere()
		{
			putValue(NAME, I18n.getString("querybuilder.menu.addWhereCondition", "add where condition..."));
		}

		void add(QueryTokens.Condition token)
		{
			getOwner().builder.browser.addWhereClause(token);
		}

		boolean isFirst()
		{
			return getOwner().builder.browser.getQuerySpecification().getWhereClause().length == 0;
		}
	}

	private class ActionAddHaving extends ActionAddCondition
	{
		private ActionAddHaving()
		{
			putValue(NAME, I18n.getString("querybuilder.menu.addHavingCondition", "add having condition..."));
		}

		void add(QueryTokens.Condition token)
		{
			getOwner().builder.browser.addHavingClause(token);
		}

		boolean isFirst()
		{
			return getOwner().builder.browser.getQuerySpecification().getHavingClause().length == 0;
		}
	}

	public JCheckBox getCheckboxComponent()
	{
		return checkboxComponent;
	}

	public void setCheckboxComponent(JCheckBox checkboxComponent)
	{
		this.checkboxComponent = checkboxComponent;
	}

	public JLabel getLabelComponent()
	{
		return labelComponent;
	}

	public void setLabelComponent(JLabel labelComponent)
	{
		this.labelComponent = labelComponent;
	}

	public boolean isSelected()
	{
		return getCheckboxComponent().isSelected();
	}

	public void setSelected(boolean b)
	{
		getCheckboxComponent().setSelected(b);
	}

	public DiagramEntity getOwner()
	{
		return owner;
	}

	public void setOwner(DiagramEntity owner)
	{
		this.owner = owner;
	}
}