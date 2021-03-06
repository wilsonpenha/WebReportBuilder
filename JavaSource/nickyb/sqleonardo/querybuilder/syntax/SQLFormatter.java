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

package nickyb.sqleonardo.querybuilder.syntax;

import java.sql.Types;

import nickyb.sqleonardo.querybuilder.QueryBuilder;

public class SQLFormatter implements _ReservedWords
{
	public static final char BREAK	= '\n';
	public static final char COMMA	= ',';
	public static final char DOT	= '.';
	public static final char SPACE	= ' ';
	
	private static final String INDENT	= "     ";
	
	private static String concatCommaDelimited(Object tokens[], boolean wrap)
	{
		if(tokens.length == 0) return "<empty>";
		
		String delimiter = String.valueOf(COMMA) + (wrap ? String.valueOf(BREAK) : String.valueOf(SPACE));
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<tokens.length; i++)
		{
			if(wrap) buffer.append(INDENT);
			buffer.append(tokens[i] + delimiter);
		}
		
		return buffer.substring(0,buffer.length()-delimiter.length());
	}

	public static String concat(QueryTokens._Expression tokens[], boolean wrap)
	{
		return concatCommaDelimited(tokens,wrap);
	}
	
	public static String concat(QueryTokens.Group tokens[], boolean wrap)
	{
		return concatCommaDelimited(tokens,wrap);
	}
	
	public static String concat(QueryTokens.Sort tokens[], boolean wrap)
	{
		return concatCommaDelimited(tokens,wrap);
	}
	
	public static String concat(QueryTokens.Condition tokens[], boolean wrap)
	{
		if(tokens.length == 0) return "<empty>";
		
		String delimiter = (wrap ? String.valueOf(BREAK) : String.valueOf(SPACE));
		StringBuffer buffer = new StringBuffer();
		
		for(int i=0; i<tokens.length; i++)
		{
			if(wrap)
			{
				String indentation = INDENT;
				
				if(tokens[i].toString().startsWith("AND "))
					indentation = " ";
				else if (tokens[i].toString().startsWith("OR "))
					indentation = "  ";
				
				buffer.append(indentation);
			}
			buffer.append(tokens[i] + delimiter);
		}
		
		return buffer.substring(0,buffer.length()-delimiter.length());
	}
	
	public static String concat(QueryTokens._TableReference tokens[], boolean wrap)
	{
		if(tokens.length == 0) return "<empty>";
		
		String delimiter = (wrap ? String.valueOf(BREAK) + INDENT : String.valueOf(SPACE));
		StringBuffer buffer = new StringBuffer();
		
		sort(tokens);
		
		for(int i=0; i<tokens.length; i++)
		{
			if(tokens[i] instanceof QueryTokens.Join)
			{
				QueryTokens.Join token = (QueryTokens.Join)tokens[i];
				
				QueryTokens.Column cL = token.getPrimary();
				QueryTokens.Column cR = token.getForeign();
				
				boolean bLeft = buffer.toString().indexOf(cL.getTable().toString() + SPACE) == 0 || buffer.toString().indexOf(SPACE + cL.getTable().toString() + SPACE) > 0;
				boolean bRight = buffer.toString().indexOf(cR.getTable().toString() + SPACE) == 0 || buffer.toString().indexOf(SPACE + cR.getTable().toString() + SPACE) > 0;
				
				if(!bLeft && !bRight)
				{
					if(buffer.length() > 0)
						buffer.insert(buffer.toString().lastIndexOf(delimiter),COMMA);
					else if(wrap)
						buffer.append(INDENT);
					
					buffer.append(token.toString() + delimiter);
				}
				else if(bLeft && bRight)
				{
					buffer.append(_ReservedWords.AND + SPACE + token.getCondition().toString() + delimiter);
				}
				else
				{
					if(!bLeft)
					{
						if(token.getType() == QueryTokens.Join.LEFT_OUTER)
							token.setType(QueryTokens.Join.RIGHT_OUTER);
						else if(token.getType() == QueryTokens.Join.RIGHT_OUTER)
							token.setType(QueryTokens.Join.LEFT_OUTER);
						
						token.getCondition().setLeft(cR);
						token.getCondition().setRight(cL);
						
						cR = cL;
					}
					buffer.append(token.getTypeName() + SPACE + cR.getTable().toString() + SPACE + _ReservedWords.ON + SPACE + token.getCondition() + delimiter);
				}
			}
		}

		for(int i=0; i<tokens.length; i++)
		{
			if(tokens[i] instanceof QueryTokens.Table)
			{
				if(buffer.length() > 0)
					buffer.insert(buffer.toString().lastIndexOf(delimiter),COMMA);
				else if(wrap)
					buffer.append(INDENT);
					
				buffer.append(tokens[i] + delimiter);
			}
		}
		
		return buffer.length() > 0 ? buffer.substring(0,buffer.length()-delimiter.length()) : "<empty>";
	}
	
	private static void sort(QueryTokens._TableReference tokens[])
	{
		for(int i=0; i<tokens.length-1; i++)
		{
			if(tokens[i] instanceof QueryTokens.Join)
			{
				QueryTokens.Join joinI = (QueryTokens.Join)tokens[i];
				for(int j=i+1,k=i; j<tokens.length; j++)
				{
					if(tokens[j] instanceof QueryTokens.Join)
					{
						QueryTokens.Join joinJ = (QueryTokens.Join)tokens[j];
						
						if((joinI.getPrimary().getTable().getReference().equals(joinJ.getPrimary().getTable().getReference())
						&& joinI.getForeign().getTable().getReference().equals(joinJ.getForeign().getTable().getReference()))
						|| (joinI.getPrimary().getTable().getReference().equals(joinJ.getForeign().getTable().getReference())
						&& joinI.getForeign().getTable().getReference().equals(joinJ.getPrimary().getTable().getReference())))
						{
							moveUp(tokens,j,i+1);
							k++;
						}
						else if(joinI.getPrimary().getTable().getReference().equals(joinJ.getPrimary().getTable().getReference())
						|| joinI.getForeign().getTable().getReference().equals(joinJ.getForeign().getTable().getReference())
						|| joinI.getPrimary().getTable().getReference().equals(joinJ.getForeign().getTable().getReference())
						|| joinI.getForeign().getTable().getReference().equals(joinJ.getPrimary().getTable().getReference()))
						{
							moveUp(tokens,j,++k);
						}
					}
				}				
			}
		}
	}

	private static void moveUp(Object tokens[], int idxOld, int idxNew)
	{
		Object token = tokens[idxOld];
		for(int i=idxOld-1; i>=idxNew; i--)
		{
			tokens[i+1] = tokens[i];
		}
		tokens[idxNew] = token;
	}
	
	public static String ensureQuotes(String identifier, boolean asNeeded)
	{
		return ensureQuotes(identifier, QueryBuilder.identifierQuoteString ,asNeeded);
	}
	
	public static String ensureQuotes(String identifier, String quoteString, boolean asNeeded)
	{
		if(quoteString.equals(String.valueOf(SPACE)))
			return identifier;
		
		/* clean quote chars */
		if(identifier.startsWith(quoteString)) identifier = identifier.substring(quoteString.length());
		if(identifier.endsWith(quoteString)) identifier = identifier.substring(0,identifier.length() - quoteString.length());
		for(int i; (i=identifier.indexOf(quoteString)) !=-1;)
		{
			String left = identifier.substring(0,i);
			String right = identifier.substring(i+1);
			
			identifier = left + right;
		}
		
		/* put quote chars */
		StringBuffer quoted = new StringBuffer(identifier);
		if(asNeeded)
		{
			int iQuote = 0;
			int iSpace = 0;
			while( (iSpace = quoted.toString().indexOf(SPACE,iQuote)) != -1 )
			{
				int iDot = quoted.toString().indexOf(DOT,iQuote);
				if(iDot == -1)
				{
					quoted.insert(iQuote,quoteString);
					quoted.append(quoteString);
					break;
				}
				else if(iSpace < iDot)
				{
					quoted.insert(iQuote,quoteString);
					quoted.insert(++iDot,quoteString);
					iQuote = iDot+2;
				}
				else
				{
					iQuote = iDot+1;
				}
			}
		}
		else
		{
			quoted.insert(0,quoteString);
			quoted.append(quoteString);
		
			if(identifier.indexOf(DOT)!=-1)
			{
				int point = identifier.indexOf(DOT);
				quoted.insert(point+1,quoteString);
				quoted.insert(point+3,quoteString);
			}
		}
		
		return quoted.toString();
	}
	
	public static String toJdbcValue(Object value, int sqltype)
	{
		if(value==null) return "null";
		
		switch(sqltype)
		{
			case Types.CHAR:
			case Types.VARCHAR:
				return "'" + value.toString() + "'";
			case Types.DATE:
				return "{d '" + value.toString() + "'}";
			case Types.TIME:
				return "{t '" + value.toString() + "'}";
			case Types.TIMESTAMP:
				return "{ts '" + value.toString() + "'}";
			default:
				return value.toString();
		}
	}		
}