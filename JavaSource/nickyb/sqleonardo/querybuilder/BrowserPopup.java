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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import nickyb.sqleonardo.common.util.I18n;

import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QuerySpecification;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.SubQuery;
import nickyb.sqleonardo.querybuilder.syntax._ReservedWords;

public class BrowserPopup extends JPopupMenu implements MouseListener
{
	private QueryBuilder builder;
	private QueryTokens._Base token;
	private DefaultMutableTreeNode node; 
	
	private final static int POP_IDX_DISTINCT = 0;
	// - 1
	private final static int POP_IDX_ADD_E	= 2; // expression
	private final static int POP_IDX_ADD_S	= 3; // sub query
	private final static int POP_IDX_ADD_W	= 4; // where
	private final static int POP_IDX_ADD_G	= 5; // group by
	private final static int POP_IDX_ADD_H	= 6; // having
	private final static int POP_IDX_ADD_O	= 7; // order by
	private final static int POP_IDX_ADD_U	= 8; // union
	// - 9
	private final static int POP_IDX_EDIT	= 10;
	// - 11
	private final static int POP_IDX_REM	= 12;
	// - 13
	private final static int POP_IDX_REM_ALL= 14;
	
	BrowserPopup(QueryBuilder builder)
	{
		this.builder = builder;
		
		add(new MenuItemDistinct());
		addSeparator();
		add(new ActionAddExpression());
		add(new ActionAddSubquery());
		add(new ActionAddWhere());
		add(new ActionAddGroupBy());
		add(new ActionAddHaving());
		add(new ActionAddOrderBy());
		add(new ActionUnion());
		addSeparator();
		add(new ActionEditNode());
		addSeparator();
		add(new ActionRemoveNode());
		addSeparator();
		add(new ActionRemoveAll());
	}

	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	
	public void mouseClicked(MouseEvent me)
	{
		if(me.getClickCount() == 2)
		{
			if(token == null) return;
			((JMenuItem)getComponent(POP_IDX_EDIT)).getAction().actionPerformed(null);
		}
	}
	
	public void mousePressed(MouseEvent me)
	{
		JTree tree = (JTree)me.getSource();
		tree.putClientProperty("JTree.lineStyle", "Angled");
		if(SwingUtilities.isRightMouseButton(me))
		{
			int row = tree.getRowForLocation(me.getX(), me.getY());
			if(row != -1) tree.setSelectionRow(row);
		}

		node = null;
		token = null;

		TreePath path = tree.getSelectionPath();
		if(path != null)
		{
			node = (DefaultMutableTreeNode)path.getLastPathComponent();
			if(node.getUserObject() instanceof QueryTokens._Base)
			{
				token = (QueryTokens._Base)node.getUserObject();
			}
			else if(node instanceof BrowserItems.ConditionQueryTreeItem)
			{
				token = ((BrowserItems.ConditionQueryTreeItem)node).getCondition();
			}
			else if(node instanceof BrowserItems.AbstractQueryTreeItem)
			{
				QueryExpression qe = ((BrowserItems.AbstractQueryTreeItem)node).getQueryExpression();
				if(qe instanceof SubQuery) token = (SubQuery)qe;
			}
		}
	}

	public void mouseReleased(MouseEvent me)
	{
		if(SwingUtilities.isRightMouseButton(me))
		{
			JTree tree = (JTree)me.getSource();
			tree.putClientProperty("JTree.lineStyle", "Angled");
			TreePath path = tree.getSelectionPath();
			
			if(path!=null)
			{
				for(int i=0; i<getComponentCount(); i++)
					getComponent(i).setVisible(false);
				
				if(node instanceof BrowserItems.DefaultTreeItem && ((BrowserItems.DefaultTreeItem)node).isQueryToken())
				{
					if(token instanceof QueryTokens._TableReference) return;
					
					boolean isEditable = token instanceof QueryTokens._Expression || token instanceof QueryTokens.Condition || token instanceof QueryTokens.Sort;
					boolean isRemovable = !(token instanceof QueryTokens.Column);
					
					getComponent(POP_IDX_ADD_G).setVisible(token instanceof QueryTokens._Expression);
					getComponent(POP_IDX_ADD_O).setVisible(token instanceof QueryTokens._Expression);
					getComponent(POP_IDX_EDIT-1).setVisible(token instanceof QueryTokens._Expression);
					getComponent(POP_IDX_EDIT).setVisible(isEditable);
					getComponent(POP_IDX_REM-1).setVisible(isEditable);
					getComponent(POP_IDX_REM).setVisible(true);
	
					getComponent(POP_IDX_ADD_O).setEnabled(builder.browser.getQueryItem().toString().indexOf("ROOT")!=-1);
					
					getComponent(POP_IDX_EDIT).setEnabled(isRemovable);
					getComponent(POP_IDX_REM).setEnabled(isRemovable);
				}
				
				if(node instanceof BrowserItems.AbstractQueryTreeItem)
				{
					getComponent(POP_IDX_ADD_U).setVisible(true);
					getComponent(POP_IDX_EDIT-1).setVisible(getComponent(POP_IDX_EDIT).isVisible());
					getComponent(POP_IDX_REM-1).setVisible(true);
					getComponent(POP_IDX_REM).setVisible(true);
					
					getComponent(POP_IDX_ADD_U).setEnabled(builder.browser.getQueryExpression().getUnion()==null);
					getComponent(POP_IDX_REM).setEnabled(node.toString().indexOf("ROOT")==-1);
				}
				
				if(node instanceof BrowserItems.ClauseTreeItem)
				{
					if(node.getUserObject().toString().indexOf(_ReservedWords.SELECT)!=-1)
					{
						getComponent(POP_IDX_DISTINCT).setVisible(true);
						getComponent(POP_IDX_DISTINCT+1).setVisible(true);
						getComponent(POP_IDX_ADD_E).setVisible(true);
						getComponent(POP_IDX_ADD_S).setVisible(true);
	
						((JCheckBoxMenuItem)getComponent(POP_IDX_DISTINCT)).setSelected(node.getUserObject().toString().indexOf(_ReservedWords.DISTINCT)!=-1);
					}
					
					getComponent(POP_IDX_ADD_H).setVisible(node.getUserObject().toString().indexOf(_ReservedWords.HAVING)!=-1);
					getComponent(POP_IDX_ADD_W).setVisible(node.getUserObject().toString().indexOf(_ReservedWords.WHERE)!=-1);
					
					boolean oneAddVisible = getComponent(POP_IDX_ADD_W).isVisible() || getComponent(POP_IDX_ADD_H).isVisible();
					boolean areAllRemovable = !getComponent(POP_IDX_ADD_E).isVisible();
					
					getComponent(POP_IDX_REM_ALL-1).setVisible(oneAddVisible);
					getComponent(POP_IDX_REM_ALL).setVisible(areAllRemovable);
					getComponent(POP_IDX_REM_ALL).setEnabled(node.getChildCount()>0);
					
				}
				
				show(tree,me.getX(),me.getY());
			}
		}
	}

	class MenuItemDistinct extends JCheckBoxMenuItem implements ActionListener
	{
		MenuItemDistinct()
		{
			super("distinct");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			short q = QuerySpecification.NONE;
			String uo = "<html><b>" + _ReservedWords.SELECT;
			
			if(isSelected())
			{
				uo += " " + _ReservedWords.DISTINCT;
				q = QuerySpecification.DISTINCT;
			}
			
			BrowserPopup.this.node.setUserObject(uo);
			BrowserPopup.this.builder.browser.getQuerySpecification().setQuantifier(q);
			BrowserPopup.this.builder.browser.refreshSelection();
		}
	}

	class ActionAddGroupBy extends AbstractAction
	{
		ActionAddGroupBy(){super(I18n.getString("querybuilder.menu.addToGroupBy","add to group-by"));}
		
		public void actionPerformed(ActionEvent e)
		{
			BrowserPopup.this.builder.browser.addGroupByClause(new QueryTokens.Group((QueryTokens._Expression)token));
		}
	}
	
	class ActionAddOrderBy extends AbstractAction
	{
		ActionAddOrderBy(){super(I18n.getString("querybuilder.menu.addToOrderBy","add to order-by"));}
		
		public void actionPerformed(ActionEvent e)
		{
			BrowserPopup.this.builder.browser.addOrderByClause(new QueryTokens.Sort((QueryTokens._Expression)token));
		}
	}
	
	class ActionAddExpression extends AbstractAction
	{
		ActionAddExpression(){super(I18n.getString("querybuilder.menu.addExpression","add expression..."));}
		
		public void actionPerformed(ActionEvent e)
		{
			QueryTokens.DefaultExpression token = new QueryTokens.DefaultExpression(null);
			if(new MaskExpression(token,BrowserPopup.this.builder).showDialog())
				BrowserPopup.this.builder.browser.addSelectList(token);
		}
	}
	
	abstract class ActionAddCondition extends AbstractAction
	{
		ActionAddCondition(){super(I18n.getString("querybuilder.menu.addCondition","add condition..."));}
		
		abstract void add(QueryTokens.Condition token);
		abstract QueryTokens.Condition createQueryToken();
		
		public void actionPerformed(ActionEvent e)
		{
			QueryTokens.Condition token = createQueryToken();
			if(new MaskCondition(token,BrowserPopup.this.builder).showDialog()) add(token);
		}
	}
	
	class ActionAddWhere extends ActionAddCondition
	{
		void add(QueryTokens.Condition token)
		{
			BrowserPopup.this.builder.browser.addWhereClause(token);
		}
		
		QueryTokens.Condition createQueryToken()
		{
			QueryTokens.Condition token = new QueryTokens.Condition();
			if(BrowserPopup.this.builder.browser.getQuerySpecification().getWhereClause().length > 0)
				token.setAppend(_ReservedWords.AND);
			
			return token;
		}
	}
	
	class ActionAddHaving extends ActionAddCondition
	{
		void add(QueryTokens.Condition token)
		{
			BrowserPopup.this.builder.browser.addHavingClause(token);
		}
		
		QueryTokens.Condition createQueryToken()
		{
			QueryTokens.Condition token = new QueryTokens.Condition();
			if(BrowserPopup.this.builder.browser.getQuerySpecification().getHavingClause().length > 0)
				token.setAppend(_ReservedWords.AND);
			
			return token;
		}
	}

	class ActionAddSubquery extends AbstractAction
	{
		ActionAddSubquery(){super(I18n.getString("querybuilder.menu.addSubquery","add subquery"));}
		
		public void actionPerformed(ActionEvent e)
		{
			BrowserPopup.this.builder.browser.addSelectList(new SubQuery());
		}
	}
	
	class ActionUnion extends AbstractAction
	{
		ActionUnion(){super(I18n.getString("querybuilder.menu.union","union"));}
		
		public void actionPerformed(ActionEvent e)
		{
			BrowserPopup.this.builder.browser.setUnion(new QueryExpression());
		}
	}
	
	class ActionEditNode extends AbstractAction
	{
		ActionEditNode(){super(I18n.getString("querybuilder.menu.edit","edit..."));}

		public void actionPerformed(ActionEvent e)
		{
			int ret=-1;
			
			if(BrowserPopup.this.token instanceof QueryTokens.Condition)
			{
				if(new MaskCondition((QueryTokens.Condition)BrowserPopup.this.token,BrowserPopup.this.builder).showDialog()) ret = 0;
			}
			else if(BrowserPopup.this.token instanceof QueryTokens.DefaultExpression)
			{
				if(new MaskExpression((QueryTokens.DefaultExpression)BrowserPopup.this.token,BrowserPopup.this.builder).showDialog()) ret = 0;
			}
			else if(BrowserPopup.this.token instanceof QueryTokens.Sort)
			{
				Object sort = JOptionPane.showInputDialog(	SwingUtilities.getWindowAncestor(BrowserPopup.this.builder),
															((QueryTokens.Sort)token).toString().substring(0,((QueryTokens.Sort)token).toString().lastIndexOf(' ')),
															"order.edit", JOptionPane.PLAIN_MESSAGE,null, new String[]{"ASC","DESC"},
															((QueryTokens.Sort)token).isAscending() ? "ASC" : "DESC");
				
				if(sort!=null)
				{
					((QueryTokens.Sort)token).setType(sort.toString().equals("ASC") ? QueryTokens.Sort.ASCENDING : QueryTokens.Sort.DESCENDING);
					ret=0;
				}
			}
			
			if(ret==0) BrowserPopup.this.builder.browser.refreshSelection();
		}
	}
	
	class ActionRemoveNode extends AbstractAction
	{
		ActionRemoveNode(){super(I18n.getString("querybuilder.menu.remove","remove"));}

		public void actionPerformed(ActionEvent e)
		{
			if(BrowserPopup.this.token instanceof QueryTokens.Condition)
			{
				if(node.getParent().toString().indexOf(_ReservedWords.WHERE)!=-1)
					BrowserPopup.this.builder.browser.removeWhereClause((QueryTokens.Condition)token);
				else
					BrowserPopup.this.builder.browser.removeHavingClause((QueryTokens.Condition)token);
			}
			else if(BrowserPopup.this.token instanceof QueryTokens.DefaultExpression)
			{
				BrowserPopup.this.builder.browser.removeSelectList((QueryTokens._Expression)token);
			}
			else if(BrowserPopup.this.token instanceof QueryTokens.Group)
			{
				BrowserPopup.this.builder.browser.removeGroupByClause((QueryTokens.Group)token);
			}
			else if(BrowserPopup.this.token instanceof QueryTokens.Sort)
			{
				BrowserPopup.this.builder.browser.removeOrderByClause((QueryTokens.Sort)token);
			}
			else if(BrowserPopup.this.node instanceof BrowserItems.AbstractQueryTreeItem)
			{
				if(BrowserPopup.this.token instanceof QueryTokens._Expression)
				{
					BrowserPopup.this.builder.browser.removeSelectList((QueryTokens._Expression)token);
				}
				else if(BrowserPopup.this.node instanceof BrowserItems.UnionQueryTreeItem)
				{
					BrowserPopup.this.builder.browser.setUnion(null);
				}
			}
		}
	}
	
	class ActionRemoveAll extends AbstractAction
	{
		ActionRemoveAll(){super(I18n.getString("querybuilder.menu.removeAll","remove all"));}

		public void actionPerformed(ActionEvent e)
		{
			if(node.toString().indexOf(_ReservedWords.WHERE)!=-1)
				removeWhereClause();
			else if(node.toString().indexOf(_ReservedWords.GROUP_BY)!=-1)
				removeGroupByClause();
			else if(node.toString().indexOf(_ReservedWords.HAVING)!=-1)
				removeHavingClause();
			else if(node.toString().indexOf(_ReservedWords.ORDER_BY)!=-1)
				removeOrderByClause();
		}
		
		private void removeWhereClause()
		{
			while(node.getChildCount()>0)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
				BrowserPopup.this.builder.browser.removeWhereClause((QueryTokens.Condition)child.getUserObject());
			}
		}

		private void removeGroupByClause()
		{
			while(node.getChildCount()>0)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
				BrowserPopup.this.builder.browser.removeGroupByClause((QueryTokens.Group)child.getUserObject());
			}
		}
	
		private void removeHavingClause()
		{
			while(node.getChildCount()>0)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
				BrowserPopup.this.builder.browser.removeHavingClause((QueryTokens.Condition)child.getUserObject());
			}
		}

		private void removeOrderByClause()
		{
			while(node.getChildCount()>0)
			{
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)node.getFirstChild();
				BrowserPopup.this.builder.browser.removeOrderByClause((QueryTokens.Sort)child.getUserObject());
			}
		}
	}
}
