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

package nickyb.sqleonardo.environment;

import java.awt.BorderLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import nickyb.sqleonardo.common.gui.BorderLayoutPanel;
import nickyb.sqleonardo.common.util.Appearance;
import nickyb.sqleonardo.common.util.I18n;
import nickyb.sqleonardo.common.util.Resources;
import nickyb.sqleonardo.common.util.Store;
import nickyb.sqleonardo.common.util.Text;
import nickyb.sqleonardo.environment.mdi.ClientMetadataExplorer;
import nickyb.sqleonardo.environment.mdi.MDIActions;
import nickyb.sqleonardo.environment.mdi.MDIClient;
import nickyb.sqleonardo.environment.mdi.MDIMenubar;
import nickyb.sqleonardo.environment.mdi.MDIToolbar;
import nickyb.sqleonardo.environment.mdi.MDIWindow;
import nickyb.sqleonardo.environment.mdi._ConnectionListener;

public class Application extends Appearance implements _Constants,_Version
{
	public static final ClipboardOwner defaultClipboardOwner;

    private JDesktopPane desktop;
    private ArrayList connectionListeners;
    private JInternalFrame internalFrame;    
    public MDIMenubar menubar;
    public MDIToolbar toolbar;
    
    JFrame frame = new JFrame();
    public static Application application = new Application();

	//private static JDesktopPane desktop;

	private static String webServerStr = null;
    private static String hostName = "localhost";
    private static String serverPath = "";
	private static int port = 8080;
    private static String servletPath = "applet2servlet.do";
    private static String reportId = "";
    
	static
	{
		defaultClipboardOwner = new ClipboardOwner()
		{	
			public void lostOwnership(Clipboard clipboard, Transferable contents)
			{
			}
		};	
	}
	
    public static MDIWindow window = null;
    
	public static final Store session		= new Store();
    public static final Resources resources	= new Resources();
    
    private static void loadIcons()
    {
		resources.loadIcon(ICON_CONNECT		,"/images/connect.png");
		resources.loadIcon(ICON_DISCONNECT	,"/images/disconnect.png");

    	resources.loadIcon(ICON_SAVE,"/images/disk.png");
		resources.loadIcon(ICON_STOP,"/images/stop.png");		

		resources.loadIcon(ICON_COMPARER	,"/images/table_error.png");
        resources.loadIcon(ICON_EXPLORER	,"/images/database_lightning.png");
        resources.loadIcon(ICON_EDITOR		,"/images/page_edit.png");
		resources.loadIcon(ICON_PREFERENCES	,"/images/wrench.png");
        
		resources.loadIcon(ICON_EXPLORER_DRIVER			,"/images/database_gear.png");
		resources.loadIcon(ICON_EXPLORER_DATASOURCE_OK	,"/images/database_connect.png");
		resources.loadIcon(ICON_EXPLORER_DATASOURCE_KO	,"/images/database.png");
		resources.loadIcon(ICON_EXPLORER_SCHEMA			,"/images/folder_database.png");
		resources.loadIcon(ICON_EXPLORER_TYPES			,"/images/folder_table.png");
		resources.loadIcon(ICON_EXPLORER_ALL			,"/images/table_multiple.png");
		resources.loadIcon(ICON_EXPLORER_LINKS			,"/images/table_link.png");
		resources.loadIcon(ICON_EXPLORER_ADD_GROUP		,"/images/cart_add.png");
		resources.loadIcon(ICON_EXPLORER_REMOVE_GROUP	,"/images/cart_delete.png");

		resources.loadIcon(ICON_EDITOR_OPEN ,"/images/folder_page.png");
		resources.loadIcon(ICON_EDITOR_SAVE ,"/images/page_save.png");
		resources.loadIcon(ICON_EDITOR_RUN	,"/images/page_gear.png");
		
		resources.loadIcon(ICON_CONTENT_UPDATE,"/images/database_save.png");
		resources.loadIcon(ICON_CONTENT_DELETE,"/images/table_row_delete.png");
		resources.loadIcon(ICON_CONTENT_INSERT,"/images/table_row_insert.png");
		
		resources.loadIcon(ICON_QUERY_LAUNCH,"/images/table_gear.png");		
	}
	
	private static void loadSession()
	{
		try
		{
			//Application.println(System.getProperty("user.dir"));
			if(new File(sessionFilename()).exists())
			{
				session.load(sessionFilename());
				if(session.canMount(ENTRY_INFO))
				{
					session.mount(ENTRY_INFO);
					println("\tversion: " + session.jump("version").get(0));
				}
			}
			Preferences.loadDefaults();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void shutdown()
	{
		try
		{
			Application.application.dispose();
			session.mount(ENTRY_INFO);
			session.home();
			session.jump("version");
			
			if(session.jump().size() == 0)
				session.jump().add(getVersion());
			else
				session.jump().set(0,getVersion());
			
			//Application.save();
			session.save(sessionFilename());
		}
		catch (Exception e)
		{
			println(e,false);
		}
	}	
	
	private static String sessionFilename()
	{
		return  System.getProperty("user.home")+"/.sqleonardo";
	}
	
    public static String getVersion()
    {
        return MAJOR + "." + MINOR;
    }
    
    public static String getVersion2()
    {
        return PROGRAM + "." + getVersion();
    }
    
    public static String getVersion3()
    {
    	return getVersion2() + " [ " + WEB +" ]";
    }

	public static void alert(String title,String message)
    {
       	JOptionPane.showMessageDialog(Application.application,message,title,JOptionPane.WARNING_MESSAGE);
    }
    
	public static boolean confirm(String title,String message)
	{
		return JOptionPane.showConfirmDialog(Application.application,message,title,JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}    
    
	public static String input(String title,String message)
	{
		return JOptionPane.showInputDialog(Application.application,message,title,JOptionPane.PLAIN_MESSAGE);
	}    
    
    public static void println(Exception e,boolean alert)
    {
    	if(alert)
    		alert(e.getClass().getName(), Text.wrap(e.toString(),100));
    	else
        	System.out.println(e);
    }
    
    public static void println(String s)
    {
        System.out.println(s);
    }
    
    public static void initI18n()
    {
       String s = Preferences.getString("app.locale","en_EN");
       
       if (s != null && s.length() > 0)
       {
            String language = s;
            if (s.indexOf("_") > 0) {
                language = s.substring(0, s.indexOf("_") );
                s = s.substring(s.indexOf("_")+1);
            }
            else s ="";
            
            String country = s;
            if (s.indexOf("_") > 0) {
                country = s.substring(0, s.indexOf("_") );
                s = s.substring(s.indexOf("_")+1);
            }
            else s ="";
            
            String variant = s;
           
            if (language.length()>0 && country.length() >0 && variant.length()>0)
            {
                I18n.setCurrentLocale(new Locale(language, country, variant));
            }
            else if (language.length()>0 && country.length()>0)
            {
                I18n.setCurrentLocale(new Locale(language, country));
            }
            else
            {
                I18n.setCurrentLocale(new Locale(language));
            }
       }
    }
    
    public void initMDIWindow(){
        Application.println("loading resources..."); 
        Application.loadIcons();
        Application.println("loading session...");
        Application.loadSession();
		//Application.loadFromServlet();
        Application.println("loading initI18n..."); 
		Application.initI18n();
        //Application.println("loading MDIWindow..."); 
        //Application.window = new MDIWindow();
		//Application.println("loading window...");
		//Application.window.show();
    }
    public void init(){

    	// get the host name and port of the applet's web server
    	getContentPane().setLayout(new BorderLayout());
    	Application.reportId = this.getParameter("reportId");
        URL hostURL = getCodeBase();
		hostName = hostURL.getHost();
		serverPath = hostURL.getPath();
        port = hostURL.getPort();
		
		if (port == -1)
		{
			port = 80;
		}
		
        webServerStr = "http://" + hostName + ":" + port + serverPath + servletPath;

    	initMDIWindow();
		connectionListeners = new ArrayList();
    	
        initActions();
    	Application.application = this;

		initComponents();
		this.getContentPane().add(desktop, BorderLayout.CENTER);
		
    	setSize(Preferences.getInteger("window.width"),
				Preferences.getInteger("window.height"));

		show();
		
    	add(new ClientMetadataExplorer());
		
		setVisible(true);
		
        //setJMenuBar(menubar = new MDIMenubar());
		//app.add(new ClientMetadataExplorer());
    	//getContentPane().add(app.getContentPane());

		System.setProperty("sun.swing.enableImprovedDragGesture","");

/*        if (System.getProperty("nickyb.sqleonardo.laf.class") == null)
        {
            try {
            	System.out.println(javax.swing.UIManager.getLookAndFeel());
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getLookAndFeelDefaults());
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }else{
            try {
                javax.swing.UIManager.setLookAndFeel(System.getProperty("nickyb.sqleonardo.laf.class"));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        	
        }*/
		
    	WindowListener l = new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {Application.shutdown();;}
		};
		
    }

    public void stop(){
    	System.out.println("Stopping...");
    	Application.shutdown();
    }
    
    public static void main(String[] args)
    {
        
        // Fix for java Bug ID:  4521075  
        /*System.setProperty("sun.swing.enableImprovedDragGesture","");
        
        if (System.getProperty("nickyb.sqleonardo.laf.class") != null)
        {
            try {
                javax.swing.UIManager.setLookAndFeel(System.getProperty("nickyb.sqleonardo.laf.class"));
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }*/
        
        System.out.println(javax.swing.UIManager.getSystemLookAndFeelClassName());
        
    }

	/*protected static void loadFromServlet()
	{

        ObjectInputStream inputFromServlet = null;
        Vector studentVector = null;
        
	    try
	    {     
	        // build a GET url string w/ encoded name/value pairs
	        //
	        // Send over UserOption=AppletDisplay.  The servlet will interpret
	        // the user option and send back the student list as a serialized vector
	        // of student objects.
	        //
	        String servletGET = webServerStr + "?" 
	                            + URLEncoder.encode("UserOption") + "=" 
	                            + URLEncoder.encode("Load");
	        
            // connect to the servlet
            Application.println("Connecting...");
            URL fileFromServlet = new URL( servletGET );
            URLConnection servletConnection = fileFromServlet.openConnection();  
	        	    
	        // Read the input from the servlet.  
	        //
	        // The servlet will return a serialized vector containing
	        // student entries.
	        //
            Application.println("Getting input stream");
            
	        inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());

	        session.load(inputFromServlet);

	        if(session.canMount(ENTRY_INFO))
			{
				session.mount(ENTRY_INFO);
				println("\tversion: " + session.jump("version").get(0));
			}

			Preferences.loadDefaults();
	        
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }

	}*/
    
	public static java.util.List getListOfAvailLanguages(){

        ObjectInputStream inputFromServlet = null;
        
	    try {     
	        // build a GET url string w/ encoded name/value pairs
	        //
	        // Send over UserOption=AppletDisplay.  The servlet will interpret
	        // the user option and send back the student list as a serialized vector
	        // of student objects.
	        //
	        String servletGET = webServerStr + "?" 
	                            + URLEncoder.encode("UserOption") + "=" 
	                            + URLEncoder.encode("LoadResource");
	        
            // connect to the servlet
            Application.println("Connecting...");
            URL fileFromServlet = new URL( servletGET );
            URLConnection servletConnection = fileFromServlet.openConnection();  
	        	    
	        // Read the input from the servlet.  
	        //
	        // The servlet will return a serialized vector containing
	        // student entries.
	        //
            Application.println("Getting input stream");
            
	        inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());
	        Vector v = (Vector)inputFromServlet.readObject();
	        java.util.List list = (java.util.List)v.firstElement();
            System.out.println("List : "+list);
	        return list;
	        
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	return null;
	    }

	}
    
	public static Connection getConnection(){

        ObjectInputStream inputFromServlet = null;
        
	    try {     

			//Main beanMain = new reports.bean.Main();
			//Report report = Report.findByID(new BigDecimal(reportId));
			
	    	return null;
	        
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	return null;
	    }

	}

	/**
     *  Save the seesion data.
     */
	/*protected static void save()
	{
	    
		try
        {
            // connect to the servlet

			ZipOutputStream zin = session.save();
			
			Application.println("Connecting to servlet...");
            URL fileToServlet = new URL( webServerStr );
            URLConnection servletConnection = fileToServlet.openConnection();  
            Application.println("Connected");

            // inform the connection that we will send output and accept input
            servletConnection.setDoInput(true);          
	        servletConnection.setDoOutput(true);
	        
            // Don't used a cached version of URL connection.
            servletConnection.setUseCaches (false);

            // Specify the content type that we will send binary data
            servletConnection.setRequestProperty
                ("Content-Type", "application/octet-stream");
	        	        	                       
	        // send the student object to the servlet using serialization
            sendStreamToServlet(servletConnection, zin);
	        	        
	    }
	    catch (Exception e)
	    {
	    	Application.println(e.toString());    
	    }
	    
	}*/

	/**
	 *  Sends a object to a servlet.  The object is serialized over the URL connection
	 */
    protected static void sendStreamToServlet(URLConnection servletConnection, ZipOutputStream zin)
    {
        ObjectOutputStream outputToServlet = null;
        
        try
        {
	        // send the student object to the servlet using serialization
        	Application.println("Sending the student to the servlet...");
	        outputToServlet = new ObjectOutputStream(servletConnection.getOutputStream());
	        
	        // serialize the object
	        outputToServlet.writeObject(zin);
	        
	        outputToServlet.flush();	        
	        outputToServlet.close();
	        Application.println("Complete.");
        }
        catch (IOException e)
        {
        	Application.println(e.toString());    
        }
    }

	public final void dispose() throws Exception
	{
		ClientMetadataExplorer cme = (ClientMetadataExplorer)getClient(ClientMetadataExplorer.DEFAULT_TITLE);
		cme.dispose();
    	
		Preferences.set("window.height"	,new Integer(this.getSize().height));
		Preferences.set("window.width"	,new Integer(this.getSize().width));
    	
		//super.dispose();
		//throw new Exception("Undefined dispose method");
	}    
    
    public Action getAction(String key)
    {
        return this.getRootPane().getActionMap().get(key);
    }
    
    private void initActions()
    {
		this.getRootPane().getActionMap().put(ACTION_NEW_QUERY			, new MDIActions.NewQuery());
		this.getRootPane().getActionMap().put(ACTION_LOAD_QUERY			, new MDIActions.LoadQuery());
		this.getRootPane().getActionMap().put(ACTION_EXIT				, new MDIActions.Exit());
		this.getRootPane().getActionMap().put(ACTION_ABOUT				, new MDIActions.About());
		
		this.getRootPane().getActionMap().put(ACTION_SHOW_PREFERENCES	, new MDIActions.ShowPreferences());
		this.getRootPane().getActionMap().put(ACTION_SHOW_CONTENT		, new MDIActions.ShowContent());
		this.getRootPane().getActionMap().put(ACTION_SHOW_DEFINITION	, new MDIActions.ShowDefinition());
		
        this.getRootPane().getActionMap().put(ACTION_MDI_SHOW_EXPLORER	, new MDIActions.ShowMetadataExplorer());
		this.getRootPane().getActionMap().put(ACTION_MDI_SHOW_EDITOR	, new MDIActions.ShowCommandEditor());
		this.getRootPane().getActionMap().put(ACTION_MDI_SHOW_COMPARER	, new MDIActions.ShowSchemaComparer());
		
		this.getRootPane().getActionMap().put(ACTION_MDI_CLOSE_ALL	, new MDIActions.CloseAllClients());
		this.getRootPane().getActionMap().put(ACTION_MDI_CASCADE	, new MDIActions.CascadeClients());
		this.getRootPane().getActionMap().put(ACTION_MDI_TILEH		, new MDIActions.TileClients());
    }
    
    private void initComponents()
    {
		setTitle(null);
		setSize(Preferences.getInteger("window.width"),
				Preferences.getInteger("window.height"));

		//UWindow.centerOnScreen(this);

        BorderLayoutPanel content = new BorderLayoutPanel();
        content.setComponentNorth(toolbar = new MDIToolbar());
        content.setComponentCenter(desktop = new JDesktopPane());
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        
        setJMenuBar(menubar = new MDIMenubar());
        setContentPane(content);
    }
    
	public final void setTitle(String title)
	{
		if(title == null)
			title = Application.getVersion2();
		else
			title = Application.getVersion2() + " - " + title;
		
		//super.setTitle(title);
	}
	
	public void add(MDIClient client)
	{
		desktop.add(client);
		
		try
		{
			client.addInternalFrameListener(menubar);
			client.setVisible(true);
			client.setMaximum(true);
			client.setSelected(true);
			//client.setPreferences();
		}
		catch (PropertyVetoException e)
		{
			Application.println(e,false);
		}
	}

	public void closeAllClients()
	{
		JInternalFrame clients[] = desktop.getAllFrames();
	
		for (int i = 0; i < clients.length; i++)
		{
			if(clients[i].isClosable())
			{
				clients[i].doDefaultCloseAction();
			}
		}
	}
    
    public MDIClient getClient(String name)
    {
		JInternalFrame[] clients = desktop.getAllFrames();
		for(int i=0; i<clients.length; i++)
		{
			if(clients[i].getName()!=null && clients[i].getName().equals(name))	return (MDIClient)clients[i];
		}
		
		return null;
    }

    public boolean showClient(MDIClient client)
    {
    	if(client == null) return false;
    	
	    try
	    {
			client.setSelected(true);
	        client.toFront();
	    }
	    catch (PropertyVetoException e)
	    {
	        Application.println(e,false);
	    }
	    
		return true;
    }
    
    public boolean showClient(String name)
    {
		return showClient(this.getClient(name));
    }
    
    private MDIClient[] getClientsForResize()
    {
    	Vector clients = new Vector();
		JInternalFrame frames[] = desktop.getAllFrames();
		
		for(int i=0; i<frames.length; i++)
			if(frames[i].isMaximizable())
				clients.add(frames[i]);
		
		return (MDIClient[])clients.toArray(new MDIClient[clients.size()]);
    }
    
	public void cascadeClients()
	{
		int offset = 20;
		int x = 0;
		int y = 0;
		
		MDIClient[] clients = getClientsForResize();
		
		int h = (desktop.getBounds().height - 5) - clients.length * offset;
		int w = (desktop.getBounds().width - 5) - clients.length * offset;
        
		for (int i = clients.length - 1; i >= 0; i--)
		{
			if(clients[i].isMaximizable() || clients[i].isResizable())
			{
				try
				{
					clients[i].setMaximum(false);
				}
				catch (PropertyVetoException e)
				{
					Application.println(e,false);
				}
				
				clients[i].setSize(w,h);
				clients[i].setLocation(x,y);
			
				x = x + offset;
				y = y + offset;
				
				showClient(clients[i]);
			}
		}
	}

	public void tileClients()
	{
		MDIClient[] clients = getClientsForResize();
        
		if(clients.length > 0)
		{
			int h = desktop.getBounds().height/clients.length;
			int w = desktop.getBounds().width;
			int y = 0;
        
			for (int i = 0; i < clients.length; i++)
			{
				if(clients[i].isMaximizable() || clients[i].isResizable())
				{
					try
					{
						clients[i].setMaximum(false);
					}
					catch (PropertyVetoException e)
					{
						Application.println(e,false);
					}
					
					clients[i].setSize(w,h);
					clients[i].setLocation(0,y);
					
					y = y + h;
				
					showClient(clients[i]);
				}
			}
		}
	}
    
	public void addListener(_ConnectionListener l)
	{
		connectionListeners.add(l);
	}
    
	public void connectionClosed(String keycah)
	{
		for(Iterator iter = connectionListeners.iterator(); iter.hasNext();)
			((_ConnectionListener)iter.next()).onConnectionClosed(keycah);
	}
	
	public void connectionOpened(String keycah)
	{
		for(Iterator iter = connectionListeners.iterator(); iter.hasNext();)
			((_ConnectionListener)iter.next()).onConnectionOpened(keycah);
	}

}
