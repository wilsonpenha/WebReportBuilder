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
import java.util.Locale;

import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import nickyb.sqleonardo.common.gui.AbstractDialogConfirm;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.common.util.LocaleAdapter;
import nickyb.sqleonardo.environment.Application;
import nickyb.sqleonardo.environment.Preferences;
import nickyb.sqleonardo.querybuilder.QueryBuilder;

public class DialogPreferences extends AbstractDialogConfirm
{
	private JCheckBox optQbAutoJoin;
	private JCheckBox optQbAutoAlias;
	private JCheckBox optQbUseQuote;
	private JCheckBox optQbUseSchema;
	private JCheckBox optQbLoadAtOnce;
        
    private JLabel jLabelLanguage = new JLabel();
    private JComboBox jComboBoxLanguage = new JComboBox();
    
	public DialogPreferences()
	{
		super(Application.application, Application.PROGRAM + ".preferences",350,INITIAL_HEIGHT);
		
		JPanel pnlQB = new JPanel(new GridLayout(0,1));
		pnlQB.setBorder(new EmptyBorder(10,5,90,5));
		
		pnlQB.add(optQbAutoJoin = new JCheckBox(I18n.getString("application.preferences.autoJoin","auto join"),Preferences.getBoolean("querybuilder.auto-join",true)));
		pnlQB.add(optQbAutoAlias = new JCheckBox(I18n.getString("application.preferences.autoAlias","auto alias"),Preferences.getBoolean("querybuilder.auto-alias",true)));
		pnlQB.add(new JSeparator());
		pnlQB.add(optQbUseQuote = new JCheckBox(I18n.getString("application.preferences.alwaysQuoteIdentifiers","always quote identifiers"),Preferences.getBoolean("querybuilder.use-quote",true)));
		pnlQB.add(optQbUseSchema = new JCheckBox(I18n.getString("application.preferences.useSchemaName","use schema name in syntax definition"),Preferences.getBoolean("querybuilder.use-schema",true)));
		pnlQB.add(new JSeparator());
		pnlQB.add(optQbLoadAtOnce = new JCheckBox(I18n.getString("application.preferences.loadObjectsAtOnce","load table objects list at once"),Preferences.getBoolean("querybuilder.load-objects-at-once",true)));
                
		jLabelLanguage.setText( I18n.getString("application.preferences.language","language") );
		
        // Add all the available languages...
        java.util.List list = I18n.getListOfAvailLanguages();
        list.add(0, Locale.getDefault());
        
        if (!Locale.getDefault().equals( Locale.ENGLISH ))
        {
            list.add(1, Locale.ENGLISH);
        }
        
        int selectedItem = 0;
        for (int i=0; i<list.size(); ++i)
        {
            java.util.Locale loc = (java.util.Locale)list.get(i);
            jComboBoxLanguage.addItem( new LocaleAdapter( loc ) );
            
            System.out.println(I18n.getCurrentLocale().toString() + " compare to " + loc.toString());

            if (I18n.getCurrentLocale().toString().equals(loc.toString() ))
            {
                selectedItem = i;
            }
            else
            {
                System.out.println(I18n.getCurrentLocale().toString() + " != " + loc.toString());
            }
        }
        
        jComboBoxLanguage.setSelectedIndex( selectedItem );
        
		JPanel pnlGeneral = new JPanel(new GridLayout(0,1));
		pnlGeneral.setBorder(new EmptyBorder(10,5,215,5));
		
        pnlGeneral.add( jLabelLanguage );
		pnlGeneral.add( jComboBoxLanguage );
		
		JTabbedPane options = new JTabbedPane();
		options.addTab("general",pnlGeneral);
		options.addTab("query builder",pnlQB);
		
		getContentPane().add(options);
	}

	protected void onOpen()
	{
		Object[] items = Application.application.menubar.getMenu(MDIMenubar.IDX_WINDOW).getMenuComponents();
		for(int i=0; i<items.length; i++)
		{
			if(items[i] instanceof JMenuItem)
			{
				String txt = ((JMenuItem)items[i]).getText();
				if(txt.indexOf(" - QUERY : ")!=-1)
				{
					optQbAutoAlias.setEnabled(false);
					optQbAutoJoin.setEnabled(false);
					optQbUseQuote.setEnabled(false);
					optQbUseSchema.setEnabled(false);
					optQbLoadAtOnce.setEnabled(false);
					
					break;
				}
			}
		}
	}

	protected boolean onConfirm()
	{
		QueryBuilder.autoJoin = optQbAutoJoin.isSelected();
		QueryBuilder.autoAlias = optQbAutoAlias.isSelected();
		QueryBuilder.useAlwaysQuote	= optQbUseQuote.isSelected();
		QueryBuilder.loadObjectsAtOnce	= optQbLoadAtOnce.isSelected();
		
		Preferences.set("querybuilder.auto-join"			,new Boolean(optQbAutoJoin.isSelected()));
		Preferences.set("querybuilder.auto-alias"			,new Boolean(optQbAutoAlias.isSelected()));
		Preferences.set("querybuilder.use-quote"			,new Boolean(optQbUseQuote.isSelected()));
		Preferences.set("querybuilder.use-schema"			,new Boolean(optQbUseSchema.isSelected()));
		Preferences.set("querybuilder.load-objects-at-once"	,new Boolean(optQbLoadAtOnce.isSelected()));
        
        if (jComboBoxLanguage.getSelectedIndex() >= 0)
        {
            LocaleAdapter la = (LocaleAdapter)jComboBoxLanguage.getSelectedItem();
            I18n.setCurrentLocale( la.getLocale() );
            Preferences.set("app.locale", la.getLocale().toString() );
        }
                
		return true;
	}    
}