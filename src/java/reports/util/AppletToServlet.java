/*
 * Created on Mar 9, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package reports.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.persistencia.exception.ServiceLocatorException;

import nickyb.sqleonardo.common.util.I18n;
import reports.Report;
import reports.bean.BeanObject;
import reports.bean.Main;

/**
 * @author Wilson
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AppletToServlet extends HttpServlet implements SingleThreadModel {

	protected static final int INDEX_DATA = 0;
	protected static final int INDEX_SUBS = 1;
	protected static final int INDEX_JUMP = 2;
	
	private String rootDir = "";

	/* mount-points */
	private Hashtable mountpoints;
	
	private Main beanMain = new Main();
	HttpSession session = null;
	private DataSource ds; 

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
		mountpoints = new Hashtable();
	
	}
	/**
	 *  This method is used for applets.
	 *
	 *  Receives and sends the data using object serialization.
	 *
	 *  Gets an input stream from the applet and reads a student object.  Then
	 *  registers the student using our data accessor.  Finally, sends a confirmation
	 *  message back to the applet.
	 */
    public void doPost(HttpServletRequest request,
                        HttpServletResponse response) 
           throws ServletException, IOException
    {
        ObjectInputStream inputFromApplet = null;
        ZipOutputStream zin = null;        
        PrintWriter out = null;
        BufferedReader inTest = null;
        

		try
        {  
			rootDir = BeanObject.replaceAll(request.getRealPath(""), "\\", "//");

			// get an input stream from the applet
	        inputFromApplet = new ObjectInputStream(request.getInputStream());
	        System.out.println("Applet has Connected");
	    
	        // read the serialized student data from applet        
	        System.out.println("Reading data...");
	        zin = (ZipOutputStream) inputFromApplet.readObject();
	        System.out.println("Finished reading.");
	        
	        inputFromApplet.close();
	        
	        System.out.println("Complete.");
	         
	        // send back a confirmation message to the applet
            //out = new PrintWriter(response.getOutputStream());
            //response.setContentType("text/plain");
            //out.println("confirmed");
            //out.flush();
            //out.close();          
            
        }
        catch (Exception e)
        {
			e.printStackTrace();    
        }
    }

	/**
	 *  This method is used by HTML clients and applets.
	 *
	 *  Handles a request and reads information from HTML form data.
	 *
	 *  Figures out if the HTML form is sending a request to register
	 *  or display the students.  Also, handles a request from an applet
	 *  to simply display the students.
	 */
    public void doGet(HttpServletRequest request,
                        HttpServletResponse response) 
           throws ServletException, IOException
    {

        ObjectOutputStream outputToApplet;

        try {
    	rootDir = BeanObject.replaceAll(request.getRealPath(""), "\\", "//");
		} catch (Exception e){
			e.printStackTrace();
		}finally{}
		
		System.out.println("in doGet(...)");

        String userOption = null;
        
        userOption = request.getParameterValues("UserOption")[0];
        
        System.out.println("userOption == " + userOption);
        
        if (userOption.equals("Save"))
        {
            // register a new student into the database
            registerStudent(request, response);
        }
        else if (userOption.equals("Load"))
        {
        	try {
            // begin applet communication to display students
            System.out.println("userOption = " + userOption);
            loadPrepare(response);
        	} catch (ClassNotFoundException e){
        		e.printStackTrace();
        	}
        }
        else if (userOption.equals("LoadResource"))
        {
        	try {
	            // begin applet communication to display students
	            System.out.println("userOption = " + userOption);
	            java.util.List list = I18n.getListOfAvailLanguages();
	
	            outputToApplet = new ObjectOutputStream(response.getOutputStream());
	            
	            System.out.println("Sending Object to applet...");
	
	            Vector v = new Vector();
	            System.out.println("List : "+list);
	            v.add(list);
	            outputToApplet.writeObject(v);
	            outputToApplet.flush();
	            outputToApplet.close();
	            System.out.println("Data transmission complete.");
	
        	} catch (Exception e){
        		e.printStackTrace();
        	}
        }
        else if (userOption.equals("LoadConnection"))
        {
        	try {
	            // begin applet communication to display students
	            System.out.println("userOption = " + userOption);

				this.session = request.getSession();
				beanMain = null;
				Report report = new Report();
				synchronized (session) {
					beanMain = new reports.bean.Main();
					report = Report.findByID(new BigDecimal(request.getParameterValues("reportId")[0]));
				}

				Connection conn = getConnection(report);
	
	            outputToApplet = new ObjectOutputStream(response.getOutputStream());
	            
	            System.out.println("Sending Object to applet...");
	
	            Vector v = new Vector();
	            v.add(conn);
	            outputToApplet.writeObject(v);
	            outputToApplet.flush();
	            outputToApplet.close();
	            System.out.println("Data transmission complete.");
	
        	} catch (Exception e){
        		e.printStackTrace();
        	}
        }
    
    }
 
    /**
	 *  This method is used for applets.
	 *
	 *  Displays the students to the applet.  This is accomplished by
	 *  getting the student list and returning a vector of students.  This
	 *  vector is returned to the applet using object serialization.
	 */
    protected void loadPrepare(HttpServletResponse response) throws IOException, ClassNotFoundException
    {
		ZipInputStream zin = new ZipInputStream(new FileInputStream(rootDir+"//applets//.sqleonardo"));
		
		for(ZipEntry entry = null; (entry = zin.getNextEntry())!=null;)
		{
			Object[] content = new Object[3];
			content[INDEX_DATA] = new ObjectInputStream(zin).readObject();
			content[INDEX_SUBS] = new ObjectInputStream(zin).readObject();
			System.out.println("Key : "+entry.getName());
			this.put(entry.getName(),content);
		}
			
		zin.close();
        
        // send the student vector back to the applet using 
        // object serialization
        System.out.println("sending response");
        sendInputStream(response, mountpoints);
    }
    
	protected Object[] get(String entry)
	{
		return (Object[])mountpoints.get(entry);
	}

	protected void put(String entry, Object[] content)
	{
		mountpoints.put(entry,content);
	}
	
	public Enumeration mounts()
	{
		return mountpoints.keys();
	}

	/**
	 *  This method is used for applets.
	 *
	 *  Performs the nuts and bolts of sending the student list.  The technique used is
	 *  object serialization.  The process is accomplished by opening an ObjectOutputStream
	 *  to the client connection.  This is accomplished via the HttpServletResponse parameter.
	 *  Next the object is written to the client followed by flushing and closing the stream.
	 *
	 */
    protected void sendInputStream(HttpServletResponse response, Hashtable hash)
    {
        ObjectOutputStream outputToApplet;
        
        try
        {
            outputToApplet = new ObjectOutputStream(response.getOutputStream());
            
            System.out.println("Sending Object to applet...");

            //File f = new File(rootDir+"//applets//.sqleonardo"); 
            //FileInputStream fis = new FileInputStream(f); 
            //BufferedInputStream bis = new BufferedInputStream(zin);  
            //DataInputStream dis = new DataInputStream(bis);  
            
            //String record = "";
            //String line = "";
            //while ( (line=dis.readLine()) != null ) {
            //	System.out.println(line);
            //	record+=dis.readLine();
            //}
            
            Vector v = new Vector();
            v.add(hash);
            outputToApplet.writeObject(v);
            outputToApplet.flush();
            outputToApplet.close();
            System.out.println("Data transmission complete.");
        }
        catch (IOException e)
        {
			e.printStackTrace(); 
        }
    }
    
	/**
	 *  This method is used for students.
	 *
	 *  Creates a new student based on the form data and registers the student
	 *  in the database.  Next, creates a confirmation page for the client.
	 */
    protected void registerStudent(HttpServletRequest request,
                                HttpServletResponse response)
    {
		// create a new student based on the form data

		try
		{
			ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(rootDir+"//applets//.sqleonardo"));
			
			for(Enumeration e = mounts(); e.hasMoreElements();)
			{
				String name = e.nextElement().toString();
				zout.putNextEntry(new ZipEntry(name));
				
				Object[] content = this.get(name);
				new ObjectOutputStream(zout).writeObject(content[INDEX_DATA]);
				new ObjectOutputStream(zout).writeObject(content[INDEX_SUBS]);
				
				zout.closeEntry();
			}
			zout.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();   
		}
    }
    
    
	/**
	 *  Returns servlet information
	 */
    public String getServletInfo()
    {
        return "<i>Input Output to Applet/Servlet, v1</i>";   
    }
    
	private Connection getConnection(Report report) {

		Connection conn = null;
		String jndiName = report.getJndiName()!=null? report.getJndiName():"";
		String contextType = "java:comp/env";
		if (System.getProperty("java.vendor").indexOf("IBM")>=0)
			contextType = "";

		try {
			ds = ServiceLocator.getInstance(contextType).getDataSource(jndiName);
			conn = ds.getConnection();
		} catch (ServiceLocatorException e) {
			System.out.println("JQuery: JNDI error : " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("JQuery: SQL error : " + e.getMessage());
		}
		return conn;
	}
}
