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

package nickyb.sqleonardo.environment.ctrl.explorer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import nickyb.sqleonardo.common.gui.CommandButton;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.mdi._ConnectionListener;

public class SideSearchCriteria extends JPanel implements ActionListener, _ConnectionListener
{
    private String[] operators = {"equals","contains","starts with","ends with"};
    private ViewSearchResult rView;
    
	private JComboBox cbxConnections;
	
	private JComboBox[] cbx;
	private JTextField[] txt;
    
    public SideSearchCriteria()
    {
		Application.application.addListener(this);
		
        setBackground(Color.white);
        setBorder(LineBorder.createGrayLineBorder());
        
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        
		cbx = new JComboBox[3];
		txt = new JTextField[3];
		
        addCriteria(0,gbl,"schema",25);
        addCriteria(1,gbl,"table" ,5);
        addCriteria(2,gbl,"column",5);
        
		addConnection(gbl);
		addStarter(gbl);
        
        rView = new ViewSearchResult();
    }
    
    private void addCriteria(int idx, GridBagLayout gbl, String where, int top_gap)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(top_gap,5,1,5);
        
        JLabel lbl = new JLabel(where + ":");
        gbl.setConstraints(lbl,gbc);
        add(lbl);
        
        gbc.gridwidth	= GridBagConstraints.REMAINDER;
        gbc.fill		= GridBagConstraints.HORIZONTAL;
        gbc.weightx		= 1.0;
        
        cbx[idx] = new JComboBox(operators);
        gbl.setConstraints(cbx[idx],gbc);
        add(cbx[idx]);
        
        gbc.insets	= new Insets(0,5,0,5);
        
        txt[idx] = new JTextField();
		txt[idx].addActionListener(this);
        gbl.setConstraints(txt[idx],gbc);
        add(txt[idx]);
        
        cbx[idx].setPreferredSize(txt[idx].getPreferredSize());
    }
    
    private void addConnection(GridBagLayout gbl)
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth	= GridBagConstraints.REMAINDER;
        gbc.anchor		= GridBagConstraints.WEST;
        gbc.insets		= new Insets(5,5,1,5);
        
        JLabel lbl = new JLabel("use connection:");
        gbl.setConstraints(lbl,gbc);
        add(lbl);
        
        gbc.fill	= GridBagConstraints.BOTH;
        gbc.insets	= new Insets(0,5,8,5);
        
		cbxConnections = new JComboBox();
        gbl.setConstraints(cbxConnections,gbc);
        add(cbxConnections);
    }

	private void addStarter(GridBagLayout gbl)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor		= GridBagConstraints.CENTER;
		gbc.gridwidth	= GridBagConstraints.REMAINDER;
		gbc.insets		= new Insets(10,2,0,2);
        
		CommandButton cb = new CommandButton("start",this);
		gbl.setConstraints(cb,gbc);
		add(cb);
        
		/* blank */
		gbc = new GridBagConstraints();
		gbc.weighty	= 1.0;
		
		JPanel pnl = new JPanel();
		pnl.setOpaque(false);
		gbl.setConstraints(pnl,gbc);
		add(pnl);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(cbxConnections.getSelectedIndex() == -1) return;
		
		String schema	= getSearchString(0);
		String table	= getSearchString(1);
		String column	= getSearchString(2);
		
		rView.list(cbxConnections.getSelectedItem().toString(),schema,table,column);
		rView.setInfo("schema:" + schema + " table:" + table + " column:" + column + " on " + cbxConnections.getSelectedItem().toString());
	}
	
	private String getSearchString(int idx)
	{
		String operator = cbx[idx].getSelectedItem().toString();
		String value	= txt[idx].getText();
		
		if(value==null || value.length()==0) return null;
		
		if(operator.toString().equals(operators[1]))
		{
			if(!value.toString().startsWith("%")) value = "%" + value.toString();
			if(!value.toString().endsWith("%")) value = value.toString() + "%";
		}
		else if(operator.toString().equals(operators[2]))
		{
			if(!value.toString().endsWith("%")) value = value.toString() + "%";
		}
		else if(operator.toString().equals(operators[3]))
		{
			if(!value.toString().startsWith("%")) value = "%" + value.toString();
		}
		
		return value.toString().toUpperCase();
	}
	
    public JComponent getRightView()
    {
        return rView;
    }

	public void setPreferences()
	{
	}
	
	public void onConnectionClosed(String keycah)
	{
		cbxConnections.removeItem(keycah);
	}
	
	public void onConnectionOpened(String keycah)
	{
		cbxConnections.addItem(keycah);
	}
}