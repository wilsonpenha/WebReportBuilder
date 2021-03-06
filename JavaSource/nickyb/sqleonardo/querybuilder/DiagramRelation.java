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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import nickyb.sqleonardo.common.util.I18n;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class DiagramRelation extends JPanel
{
	private static Stroke highlightStroke = new BasicStroke((float) (2f));
	private static Stroke normalStroke = new BasicStroke((float) (2f));

	QueryTokens.Join querytoken;

	DiagramEntity primaryEntity;
	DiagramField primaryField;
	DiagramEntity foreignEntity;
	DiagramField foreignField;

	Anchor anchor;
	ViewDiagram owner;

	private boolean highlight = false;

	DiagramRelation(ViewDiagram owner)
	{
		this.owner = owner;
		setLayout(null);
		setOpaque(false);

		anchor = new Anchor();
	}

	public void setName(String name)
	{
		super.setName(name);
		onPropertyChanged();
	}

	public boolean isHighlight()
	{
		return highlight;
	}
	
	public void setHighlight(boolean b)
	{
		// A black border is better visible.
		// A selected relation is not black drawn with a 2 points stroke
		// setForeground(b ? Color.lightGray : Color.black);
		// anchor.setBorder(new LineBorder(getForeground(),1));

		this.highlight = b;
		this.repaint();
	}

	void setQueryToken(QueryTokens.Join token)
	{
		querytoken = token;
		onPropertyChanged();
	}

	void setValues(int jointype, String operator)
	{
		querytoken.setType(jointype);
		querytoken.getCondition().setOperator(operator);
		onPropertyChanged();
	}

	private void onPropertyChanged()
	{
		String tip = querytoken.getCondition().toString();
		if (this.getName() != null)
			tip = "[ " + this.getName() + " ] " + tip;

		anchor.setToolTipText(tip);

		switch (querytoken.getType())
		{
		case QueryTokens.Join.LEFT_OUTER:
		case QueryTokens.Join.RIGHT_OUTER:
			anchor.setBackground(Color.yellow);
			break;
		case QueryTokens.Join.FULL_OUTER:
			anchor.setBackground(Color.green);
			break;
		default:
			anchor.setBackground(Color.red);
		}
	}

	void onCreate(QueryBuilder builder)
	{
		setQueryToken(new QueryTokens.Join(primaryField.querytoken, "=", foreignField.querytoken));
		builder.browser.addFromClause(querytoken);
	}

	void onDestroy(QueryBuilder builder)
	{
		primaryField.unjoined();
		foreignField.unjoined();

		primaryEntity.doFlush();
		foreignEntity.doFlush();

		builder.browser.removeFromClause(querytoken);
	}

	/**
	 * Updates the serie array accordely to the fields positions.
	 * 
	 */
	void doResize()
	{
		int yFieldP = (int) primaryField.getLocationOnScreen().getY() - (int) primaryEntity.getLocationOnScreen().getY() + primaryEntity.getLocation().y;
		int yFieldF = (int) foreignField.getLocationOnScreen().getY() - (int) foreignEntity.getLocationOnScreen().getY() + foreignEntity.getLocation().y;

		int py = yFieldP + (primaryField.getSize().height / 2);
		int fy = yFieldF + (foreignField.getSize().height / 2);

		// 1. Check for space between the two fields...
		//        ____ ____
		//        | P |__ ____ ____ __| P |
		//        |____| \__ | F | | F |__/ |____|
		//             |____| |____|
		// px1 px2 fx1 fx2 fx1 fx2 px1 px2

		int px1 = primaryEntity.getLocation().x;
		int px2 = px1 + primaryEntity.getSize().width;

		int fx1 = foreignEntity.getLocation().x;
		int fx2 = fx1 + foreignEntity.getSize().width;

		int xMin = 0, yMin = 0, xMax = 0, yMax = 0;

		if (px2 < fx1)
		{
			serie[0].x = px2;
			serie[0].y = py;
			serie[2].x = fx1;
			serie[2].y = fy;
			serie[1].x = (px2 + fx1) / 2;
			serie[1].y = (py + fy) / 2;

			xMin = px1;
			xMax = fx2;
		}
		else if (px1 > fx2)
		{
			serie[0].x = fx2;
			serie[0].y = fy;
			serie[2].x = px1;
			serie[2].y = py;
			serie[1].x = (fx2 + px1) / 2;
			serie[1].y = (py + fy) / 2;

			xMin = fx1;
			xMax = px2;
		}
		else
		{
			serie[0].x = px2;
			serie[0].y = py;
			serie[2].x = fx2;
			serie[2].y = fy;
			serie[1].x = Math.max(px2, fx2) + 30;
			serie[1].y = (py + fy) / 2;

			xMin = Math.min(px2, fx2);
			xMax = serie[1].x;
		}

		yMin = Math.min(py, fy);
		yMax = Math.max(py, fy);

		Rectangle area = new Rectangle(xMin, yMin, xMax - xMin + 1, yMax - yMin + 1);

		for (int i = 0; i < serie.length; ++i)
		{
			serie[i].x -= area.x;
			serie[i].y -= area.y;
		}
		setBounds(area);
		anchor.setLocation(serie[1].x + area.x - (anchor.getSize().width / 2), serie[1].y + area.y - (anchor.getSize().height / 2));
	}

	/**
	 * array of points to draw the connection line. It is updated by the method
	 * doResize()
	 * 
	 */
	private Point[] serie = new Point[] { new Point(0, 0), new Point(0, 0), new Point(0, 0) };

	protected void paintChildren(Graphics g)
	{
		g.setColor(isHighlight() ? Color.black : Color.lightGray);
		((Graphics2D) g).setStroke(isHighlight() ? highlightStroke : normalStroke);

		int arc_w = serie[2].x - serie[0].x;
		int arc_h = serie[2].y - serie[0].y;

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// -\_ and _/-
		if (serie[0].x > serie[1].x || serie[2].x > serie[1].x)
		{
			if (arc_h == 0)
			{
				g.drawLine(serie[0].x, serie[0].y, serie[2].x, serie[2].y);
			}
			else if (arc_h < 0) // __/--
			{
				g.drawArc(serie[0].x - arc_w / 2, serie[0].y + arc_h, arc_w, -arc_h, 270, 90);
				g.drawArc(serie[0].x + arc_w / 2, serie[0].y + arc_h, arc_w, -arc_h, 180, -90);
			}
			else if (arc_h > 0) // --\_
			{
				g.drawArc(serie[0].x - arc_w / 2, serie[0].y, arc_w, arc_h, 0, 90);
				g.drawArc(serie[0].x + arc_w / 2, serie[0].y, arc_w, arc_h, 180, 90);
			}
		}
		else
		{

			if (arc_h == 0)
			{
				g.drawLine(serie[0].x, serie[0].y, serie[2].x, serie[2].y);
			}
			else if (arc_h < 0) // ]
			{
				arc_w = serie[1].x - serie[0].x;
				arc_h = serie[0].y - serie[1].y;

				g.drawArc(serie[0].x - arc_w, serie[1].y - arc_h, arc_w * 2, arc_h * 2, 270, 90);

				arc_w = serie[1].x - serie[2].x;
				arc_h = serie[1].y - serie[2].y;

				g.drawArc(serie[2].x - arc_w, serie[2].y, arc_w * 2, arc_h * 2, 90, -90);

			}
			else if (arc_h > 0) // ]
			{
				arc_w = serie[1].x - serie[0].x;
				arc_h = serie[1].y - serie[0].y;
				g.drawArc(serie[0].x - arc_w, serie[0].y, arc_w * 2, arc_h * 2, 90, -90);

				arc_w = serie[1].x - serie[2].x;
				arc_h = serie[2].y - serie[1].y;

				g.drawArc(serie[2].x - arc_w, serie[1].y - arc_h, arc_w * 2, arc_h * 2, 270, 90);

			}
		}

		// g.drawLine(serie[0].x,serie[0].y,serie[1].x,serie[1].y);
		// g.drawLine(serie[1].x,serie[1].y,serie[2].x,serie[2].y);
		// g.drawLine(serie[2].x,serie[2].y,serie[3].x,serie[3].y);

		super.paintChildren(g);
	}

	private class Anchor extends JPanel implements MouseListener
	{
		Anchor()
		{
			addMouseListener(this);
			setBorder(LineBorder.createBlackLineBorder());
			setBackground(Color.red);
			setOpaque(true);
			setSize(10, 10);
		}

		public void mouseClicked(MouseEvent me)
		{
			if (SwingUtilities.isRightMouseButton(me))
			{
				JPopupMenu popup = new JPopupMenu();
				popup.add(new ActionEdit());
				popup.addSeparator();
				popup.add(new ActionRemove());

				popup.show(this, me.getX(), me.getY());
			}
                        else if (me.getClickCount() == 2)
                        {
                            new  ActionEdit().actionPerformed(new ActionEvent(this,0,""));
                        }
			// else // We want the highlight always...
			DiagramRelation.this.owner.setHighlight(DiagramRelation.this);
		}

		public void mouseEntered(MouseEvent e)
		{
		}

		public void mouseExited(MouseEvent e)
		{
		}

		public void mousePressed(MouseEvent e)
		{
		}

		public void mouseReleased(MouseEvent e)
		{
		}
	}

	private class ActionEdit extends AbstractAction
	{
		ActionEdit()
		{
			super(I18n.getString("querybuilder.menu.edit", "edit..."));
		}

		public void actionPerformed(ActionEvent e)
		{
			new MaskJoin(DiagramRelation.this,DiagramRelation.this.owner.getBuilder()).showDialog();
		}
	}

	private class ActionRemove extends AbstractAction
	{
		ActionRemove()
		{
			super(I18n.getString("querybuilder.menu.remove", "remove"));
		}

		public void actionPerformed(ActionEvent e)
		{
			DiagramRelation.this.owner.removeRelation(DiagramRelation.this);
		}
	}
}