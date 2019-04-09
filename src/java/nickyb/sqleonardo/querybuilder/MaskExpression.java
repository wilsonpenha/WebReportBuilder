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

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;

public class MaskExpression extends BaseMask
{
	private QueryTokens.DefaultExpression querytoken;
	private JTextArea value;
	
	public MaskExpression(QueryTokens.DefaultExpression token,QueryBuilder builder)
	{
		super("expression.edit",builder);
		querytoken = token;
		
		setComponentCenter(new JScrollPane(value = new JTextArea()));
		value.setWrapStyleWord(true);
		value.setLineWrap(true);
		value.setColumns(45);
		value.setRows(10);
	}

	protected boolean onConfirm()
	{
		querytoken.set(value.getText());
		return true;
	}

	protected void onShow()
	{
		if(querytoken.isEmpty())
			value.setText("");
		else
			value.setText(querytoken.toString());
	}
}