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

import java.util.ArrayList;

import nickyb.sqleonardo.querybuilder.syntax.QueryExpression;
import nickyb.sqleonardo.querybuilder.syntax.QueryTokens;
import nickyb.sqleonardo.querybuilder.syntax.SQLFormatter;
import nickyb.sqleonardo.querybuilder.syntax._ReservedWords;

public class QueryModel
{
	private QueryExpression queryExpression;
	private ArrayList orderClause;
	
	private String schema;
	
	public QueryModel()
	{
		this(null);
	}
	
	public QueryModel(String schema)
	{
		this.schema = schema;
		
		queryExpression = new QueryExpression();
		orderClause = new ArrayList();
	}
	
	public String getSchema()
	{
		return schema == null ? null : new String(schema);
	}

	public void setSchema(String schema)
	{
		this.schema = schema;
	}

	public void addOrderByClause(QueryTokens.Sort token)
	{
		orderClause.add(token);
	}
	
	public QueryTokens.Sort[] getOrderByClause()
	{
		QueryTokens.Sort[] a = new QueryTokens.Sort[orderClause.size()];
		return (QueryTokens.Sort[])orderClause.toArray(a);
	}
	
	public void removeOrderByClause(QueryTokens.Sort token)
	{
		orderClause.remove(token);
	}
	
	public QueryExpression getQueryExpression()
	{
		return queryExpression;
	}
		
	public void setQueryExpression(QueryExpression qe)
	{
		queryExpression = qe;
	}
	
	public String toString(boolean wrap)
	{
		String syntax = queryExpression.toString(wrap);
		
		if(orderClause.size() > 0)
			syntax = syntax + (wrap ? SQLFormatter.BREAK : SQLFormatter.SPACE) + _ReservedWords.ORDER_BY + (wrap ? SQLFormatter.BREAK : SQLFormatter.SPACE) + SQLFormatter.concat(this.getOrderByClause(),wrap);
		
		return syntax;
	}
	
	public String toString()
	{
		return toString(false);
	}
}