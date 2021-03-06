package reports.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import reports.Columns;
import reports.Fields;
import reports.GroupColumns;
import reports.Groups;
import reports.Parameters;
import reports.Report;
import reports.Variables;
import reports.bean.BeanObject;
import reports.bean.Main;
import br.com.hwork.persistencia.ErrorLog;
import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.persistencia.exception.ServiceLocatorException;

public class GraphicServlet extends HttpServlet implements Servlet {

	private static final String TASK_PRINT = "print";
	private static final String TASK_PDF = "pdf";
	private static final String TASK_XML = "xml";  
	private static final String TASK_HTML = "html";
	private static final String TASK_HTMLnoBreaks = "htmlNoBreaks";
	private static final String TASK_XLS = "xls";
	private static final String TASK_CSV = "csv";

	private DataSource ds;
	private Map reportsProps = new HashMap();
	private Report report;
	HttpSession session = null;

	private Main beanMain = new Main();

	// Class for reading the output that is generated
	// by the Velocity engine; this ouput is sent
	// to the Jasper XML template compiler
	private class TemplateCompiler implements Runnable {

		private PipedInputStream inStream;
		private JasperReport jasperReport;
		private HttpServletRequest req;
		private HttpServletResponse resp;

		public TemplateCompiler(PipedInputStream pipedStream, HttpServletRequest req, HttpServletResponse resp) {
			this.inStream = pipedStream;
			this.req = req;
			this.resp = resp;
		}

		/*
		 * This method should be called after the thread that is executing this
		 * Runnable instance has finished.
		 */
		public JasperReport getJasperReport() {
			return jasperReport;
		}

		public void run() { 
			try {
				// If no input stream was provided, return
				if (inStream == null) {
					return; 
				}
 
//				fis.close();
//				fout.close();

				jasperReport = JasperCompileManager.compileReport(inStream);
			} catch (Exception e) { 
//				io.printStackTrace();
				ErrorLog.println("error in Buildding report by JRException on Compile!", e);
				forwardToErrorJSP(req, resp, e);
				//log it
			}
		}
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			ServletContext context = getServletContext();

			// Set class path for compiling XML templates
			System.setProperty("jasper.reports.compile.class.path", context.getRealPath("/WEB-INF/lib/jasperreports-0.6.8.jar")
					+ System.getProperty("path.separator")
					+ context.getRealPath("/WEB-INF/classes/"));

			// Init velocity
			initVelocity(config);
		} catch (Exception e) {
			throw new ServletException("Error initializing ReportServlet!", e);
		}
	}

	private void initVelocity(ServletConfig config) throws Exception {

		String propsFile = config.getInitParameter("velocity.properties");
		Properties props = new Properties();
		String path = getServletContext().getRealPath(propsFile);
		props.load(new FileInputStream(path));
		Velocity.init(props);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException { 
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			// Populate context

			String format = req.getParameter("format");
			String[] paramName = req.getParameterValues("paramName");
			String[] paramValue = req.getParameterValues("paramValue");
		
			byte[] output = null;
			JasperPrint jasperPrint = (JasperPrint) req.getSession().getAttribute("JASPER_PRINT");

			if (req.getParameter("reload").equals("true") || jasperPrint == null) {
				this.session = req.getSession();
				beanMain = null;
				synchronized (session) {
					beanMain = new reports.bean.Main();
					this.report = Report.findByID(new BigDecimal(req.getParameter("reportId")));
				}
	
				VelocityContext context = new VelocityContext();
				addDataToContext(context, req, resp);
	
				// Get template
				Template template = Velocity.getTemplate("BasicGraphic.vm"); 
				JasperReport jasperReport = compileTemplate(template, context, req, resp);
	
				Map parameters = new HashMap();    
				JRParameter param = null;
				if (!paramName[0].equals("")){   
					for (int i=0;i<paramName.length;i++){
						param = getJRParam(paramName[i], jasperReport);   
						if (param!=null){
							Class type = param.getValueClass();
							Object value = null; 
								
							if (!paramValue[i].equals("")){
								if (type == java.sql.Timestamp.class){
									long dataLong = br.com.hwork.text.Formatter.getDateTimeLong(paramValue[i]);
									value = new java.sql.Timestamp(dataLong);
								}
								else{
								  Constructor constructor = type.getConstructor(new Class[]{java.lang.String.class});
								  value = constructor.newInstance(new Object[] {paramValue[i]});
								} 
							}
							parameters.put(paramName[i], value);
						}
					}
				}

				// Create JasperPrint object using the fillReport() method in
				// JasperFillManager class
				jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, getConnection());
	
				if (format.equals(TASK_HTML)){
					//req.getSession().setAttribute("JASPER_PRINT", jasperPrint);
				}
	
	
			}
	
			if (format.equals(TASK_PDF)) {
				output = JasperExportManager.exportReportToPdf(jasperPrint);
				resp.setContentType("application/pdf");
				resp.setHeader("Content-Disposition", "inline; filename=report.pdf"); 
				ServletOutputStream outputStream = resp.getOutputStream();
				resp.setContentLength(output.length);
				outputStream = resp.getOutputStream();
				outputStream.write(output, 0, output.length);
				outputStream.flush();
				outputStream.close();
			} else {
				JRExporter exporter = null;
				/*************************************************************** 
				* Processing to generate the html format content type is 
				* text/html content-disposition is report.html 
				**************************************************************/
				if (format.equals(TASK_HTML)) {
	
					ServletOutputStream outputStream; 
					outputStream = resp.getOutputStream();

					//JspWriter out =
					//	((PageContext) JspFactory
					//		.getDefaultFactory()
					//		.getPageContext(this, req, resp, "jsp/common/errorpge.jsp", true, 8192, true))
					//		.getOut();
	
					outputStream.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\r\n");
					outputStream.println("<html>\r\n");
					outputStream.println("<head>\r\n");
					outputStream.println("\r\n");
					outputStream.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n");
					outputStream.println("<meta http-equiv=\"Expires\" content=\"-1\">\r\n");
					outputStream.println("<meta http-equiv=\"Pragma\" content=\"no-cache\">\r\n");
					outputStream.println("<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\r\n");
	
					exporter = new JRHtmlExporter();
	
					int pageIndex = 0;
					int lastPageIndex = 0;
					if (jasperPrint.getPages() != null) {
						lastPageIndex = jasperPrint.getPages().size() - 1;
					}
	
					String pageStr = req.getParameter("page");
	
					if (pageStr != null && !pageStr.equals(""))
						pageIndex = Integer.parseInt(pageStr);
					else
						pageIndex = 0;
	
					if (pageIndex < 0) {
						pageIndex = 0;
					}
	
					if (pageIndex > lastPageIndex) {
						pageIndex = lastPageIndex;
					}
	
					StringBuffer sbuffer = new StringBuffer();
	
					Map imagesMap = new HashMap();
					req.getSession().setAttribute("IMAGES_MAP", imagesMap);
	
					resp.setHeader("Content-Disposition", "inline; filename=report.html"); 
	
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					//exporter.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, sbuffer);
//					exporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR, "jsp/common/images");
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
					exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, "image?image=");
					exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
					exporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
					exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
					exporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
	
					if (!jasperPrint.getPages().isEmpty()){
						output = exportReportToBytes(jasperPrint, exporter);
					}
	
					//exporter.exportReport();
	
					outputStream.println("\r\n");
					outputStream.println("\r\n");
	
					outputStream.println("<script>");
					outputStream.println("function openZoomPopUp() {");
					outputStream.println("		 window.showModalDialog(\"jsp/common/zoomControl.jsp\",window,\"status:false;dialogWidth:220px;dialogHeight:200px;scroll:no\");");
					outputStream.println("}");
					outputStream.println("</script>");
	
					outputStream.println("<html>\r\n");
					outputStream.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\r\n");
					outputStream.println("<head>\r\n");
					outputStream.println("  <style type=\"text/css\">\r\n");
					outputStream.println("    a {text-decoration: none}\r\n");
					outputStream.println("  </style>\r\n");
					outputStream.println("</head>\r\n");
					outputStream.println("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\r\n");
					outputStream.println("<form method=\"post\" action=\""+req.getContextPath()+"/factory.do\" name=\"reportForm\">\r\n");
					outputStream.println("<input type=\"hidden\" name=\"JASPER_FILE\" value=\""+req.getParameter("JASPER_FILE")+"\">");
					outputStream.println("<input type=\"hidden\" name=\"reportId\" value=\""+req.getParameter("reportId")+"\">");
					outputStream.println("<input type=\"hidden\" name=\"format\" value=\""+req.getParameter("format")+"\">");
					for (int i=0;i<paramName.length;i++){
						outputStream.println("<input type=\"hidden\" name=\"paramName\" value=\""+paramName[i]+"\">");
						outputStream.println("<input type=\"hidden\" name=\"paramValue\" value=\""+paramValue[i]+"\">");
					}
					outputStream.println("<input type=\"hidden\" id=\"reload\" name=\"reload\">");
					outputStream.println("<input type=\"hidden\" id=\"page\" name=\"page\">");
					outputStream.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n");
					outputStream.println("<tr>\r\n");
					outputStream.println("  <td width=\"50%\">&nbsp;</td>\r\n");
					outputStream.println("  <td align=\"left\">\r\n");
					outputStream.println("    <hr size=\"1\" color=\"#000000\">\r\n");
					outputStream.println("    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n");
					outputStream.println("      <tr>\r\n");
					outputStream.println("        <td><a href=\"#\" onClick=\"javascript:document.getElementById(\'reload\').value=true;document.forms[0].submit();\">"+
										 "<img src=\"jsp/common/images/reload.GIF\" border=\"0\"></a></td>\r\n");
					outputStream.println("        <td>&nbsp;&nbsp;&nbsp;</td>\r\n");
	
					if (pageIndex > 0) { 
	
						outputStream.println("\r\n");
						outputStream.println("        <td><a href=\"#\" onClick=\"javascript:document.getElementById(\'page\').value=0;document.forms[0].submit();\">"+
											 "<img src=\"jsp/common/images/first.GIF\" border=\"0\"></a></td>\r\n");
						outputStream.println("        <td><a href=\"#\" onClick=\"javascript:document.getElementById(\'page\').value=");
						outputStream.print(pageIndex - 1);
						outputStream.println(";document.forms[0].submit();\"><img src=\"jsp/common/images/previous.GIF\" border=\"0\"></a></td>\r\n");
	
					} else {
	
						outputStream.println("\r\n");
						outputStream.println("        <td><img src=\"jsp/common/images/first_grey.GIF\" border=\"0\"></td>\r\n");
						outputStream.println(
							"        <td><img src=\"jsp/common/images/previous_grey.GIF\" border=\"0\"></td>\r\n");
	
					}
	
					if (pageIndex < lastPageIndex) {
	
						outputStream.println("\r\n");
						outputStream.println("        <td><a href=\"#\" onClick=\"javascript:document.getElementById(\'page\').value=");
						outputStream.print(pageIndex + 1);
						outputStream.println(";document.forms[0].submit();\"><img src=\"jsp/common/images/next.GIF\" border=\"0\"></a></td>\r\n");
						outputStream.println("        <td><a href=\"#\" onClick=\"javascript:document.getElementById(\'page\').value=");
						outputStream.print(lastPageIndex);
						outputStream.println(";document.forms[0].submit();\"><img src=\"jsp/common/images/last.GIF\" border=\"0\"></a></td>\r\n");
	
					} else {
	
						outputStream.println("\r\n");
						outputStream.println("        <td><img src=\"jsp/common/images/next_grey.GIF\" border=\"0\"></td>\r\n");
						outputStream.println("        <td><img src=\"jsp/common/images/last_grey.GIF\" border=\"0\"></td>\r\n");
	
					}
	
					outputStream.println("        <td>&nbsp;&nbsp;&nbsp;</td>\r\n");
					outputStream.println("        <td><a href=\"javascript:openZoomPopUp();\"><img src=\"jsp/common/images/zoom.gif\" border=\"0\"></a></td>\r\n");
	
					outputStream.println("\r\n");
					outputStream.println("        <td width=\"100%\">&nbsp;</td>\r\n");
					outputStream.println("      </tr>\r\n");
					outputStream.println("    </table>\r\n");
					outputStream.println("    <hr size=\"1\" color=\"#000000\">\r\n");
					outputStream.println("  </td>\r\n");
					outputStream.println("  <td width=\"50%\">&nbsp;</td>\r\n");
					outputStream.println("</tr>\r\n");
					outputStream.println("<tr>\r\n");
					outputStream.println("  <td width=\"50%\">&nbsp;</td>\r\n");
					outputStream.println("  <td align=\"center\">\r\n");
					outputStream.println("\r\n");
						
					if (!jasperPrint.getPages().isEmpty())
						outputStream.write(output);
					else{
						outputStream.println("<span style='color: red; font-size: 12pt'> No records found for this criteria!</span><br><br><input type='button' name='Close' value='Close' style=\"color: \" onclick=\"javascript:window.close();\">");
					}
						
					outputStream.println("\r\n");
					outputStream.println("\r\n");
					outputStream.println("  </td>\r\n");
					outputStream.println("  <td width=\"50%\">&nbsp;</td>\r\n");
					outputStream.println("</tr>\r\n");
					outputStream.println("</table>\r\n");
					outputStream.println("</form>\r\n");
					outputStream.println("</body>\r\n");
					outputStream.println("</html>\r\n");
					outputStream.println("\r\n");
					//resp.setContentLength(output.length+outputStream.toString().length());
					outputStream.flush();
					outputStream.close();
				}
				if (!format.equals(TASK_HTML)) {  
					output = exportReportToBytes(jasperPrint, exporter);
					resp.setContentLength(output.length);
					ServletOutputStream outputStream;
					outputStream = resp.getOutputStream();
					outputStream.write(output);
					outputStream.flush();
					outputStream.close();
				}
			}
			

		} catch (JRException e) {
			ErrorLog.println("error in Buildding report by JRException reason!", e);
			forwardToErrorJSP(req, resp, e);
		} catch (Exception e) {
			ErrorLog.println("error in Buildding report by unknow reason!", e);
			forwardToErrorJSP(req, resp, e);
		}
	}

	private JRParameter getJRParam(String paramName, JasperReport jasperReport){
		JRParameter[] jrParams = jasperReport.getParameters();
					
		for (int i=0;i<jrParams.length;i++){
			if (paramName.equals(jrParams[i].getName()))
				return jrParams[i];
		}
		return null;
	}

	private byte[] exportReportToBytes(JasperPrint jasperPrint, JRExporter exporter) throws JRException {
		byte[] output;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		output = baos.toByteArray();
		return output;
	}

	private void forwardToErrorJSP(HttpServletRequest req, HttpServletResponse res, Exception e) {
		try {
			
			ServletOutputStream outputStream = res.getOutputStream();
			PrintStream printOut = new PrintStream(outputStream);
			//e.printStackTrace(printOut);
			//System.out.println("Error : "+e.getMessage());
			outputStream.println("Error on try to RUN the Report : "+e.getMessage()); 
//			outputStream.flush();
//			outputStream.close();
			
		} catch (Exception ex) {

		}
	}

	private void addDataToContext(VelocityContext context,
			HttpServletRequest req, HttpServletResponse resp) throws Exception{

		String[] paramName = req.getParameterValues("paramName");
		String[] paramValue = req.getParameterValues("paramValue");
		String[] sGroups = req.getParameterValues("groupsIds");
		String[] sGroupColumns = req.getParameterValues("gColsIds");
		String[] sTitleColumns = req.getParameterValues("titleIds");
		String[] sPageColumns = req.getParameterValues("pageIds");
		String[] sDetailColumns = req.getParameterValues("detailIds");
		String[] sSummaryColumns = req.getParameterValues("summaryIds");
		
		String strWhere = this.report.getSql().toUpperCase();
		
		Vector vParams  = Parameters.findByReport(this.report);
		Vector vVariables = Variables.findByReport(this.report);
		Vector vReportList = new Vector();
		Vector vPageList = new Vector();
		Vector vSummaryList = new Vector();
		Vector vGroups = new Vector();
		Vector vColumnList = new Vector();
		Vector vFooterColumns = new Vector();
		Vector vHeaderColumns = new Vector();
		
		String clause = "";
		
		for (int i=0;i<paramName.length;i++){
			if (paramValue[i]!=null && !paramValue[i].equals("")){
				clause = getParameter(paramName[i],vParams).getSql().toUpperCase();
				clause = BeanObject.replaceAll(clause,"$PI","$P{"+paramName[i]+"}");
				int posWhere = strWhere.indexOf(" WHERE "); 
				if (posWhere>0){
					strWhere = BeanObject.replace(strWhere," WHERE "," WHERE "+clause+" AND ");
				}else{
					int posGroup = strWhere.indexOf(" GROUP ");
					int posOrder = strWhere.indexOf(" ORDER ");
					if (posGroup>0){
						strWhere = BeanObject.replaceAll(strWhere," GROUP "," WHERE "+clause+" GROUP ");
					}else if (posOrder>0){
						strWhere = BeanObject.replaceAll(strWhere," ORDER "," WHERE "+clause+" ORDER ");
					}else{
						strWhere += " WHERE "+clause;
					}
				}
			}
		}  
		
		if (req.getParameter("designer").equals("false")){
			if (sGroups!=null){
				for (int i=0;i<sGroups.length;i++){
					Groups groups = Groups.findByID(new BigDecimal(sGroups[i]));
					vGroups.add(groups);
				}
			}
		}else{
			vGroups = Groups.findByReportOrderByOrder(this.report);
		}
		
		if (req.getParameter("designer").equals("false")){
			int qtd = vVariables.size();
			for (int i=0;i<qtd;i++){
				for (int j=0;j<vVariables.size();j++){
					boolean exist = false;
					if (((Variables)vVariables.elementAt(j)).getResetGroupId()!=null){
						Groups variableGroup = ((Variables)vVariables.elementAt(j)).getResetGroups();
						for (int k=0;k<vGroups.size();k++){
							if (variableGroup.getGroupName().equals(((Groups)vGroups.elementAt(k)).getGroupName())){
								exist = true;
								break;
							}
						}
						if (!exist){
							vVariables.remove(j);
							break;
						}
					}
				}
			}
		}
		
		if (req.getParameter("designer").equals("false")){
			if (sGroupColumns!=null){
				for (int i=0;i<sGroupColumns.length;i++){ 
					int pos = sGroupColumns[i].indexOf("|");
					GroupColumns groupColumns = GroupColumns.findByID(new BigDecimal(sGroupColumns[i].substring(pos+1)));
					if (groupColumns.getBandType().equals("Header"))
						vHeaderColumns.add(groupColumns);
					else
						vFooterColumns.add(groupColumns);
				}
			}
		}  
		
		if (req.getParameter("designer").equals("false")){  
			vColumnList = Columns.findByReportBandType(this.report,"Detail");
			Vector tmpColumns = new Vector(); 
			for (int i=0;i<vColumnList.size();i++){
				for (int k=0;k<sDetailColumns.length;k++){
					if (((Columns)vColumnList.elementAt(i)).getColumnsId().toString().equals(sDetailColumns[k])){
						tmpColumns.add((Columns)vColumnList.elementAt(i));
						break;
					}
				}
			}
			vColumnList=tmpColumns;
		}else{
			vColumnList = Columns.findByReportBandType(this.report,"Detail");
		}

		if (req.getParameter("designer").equals("false")){
			if (sSummaryColumns!=null){
				vSummaryList = Columns.findByReportBandType(this.report,"Summary");
				Vector tmpColumns = new Vector();
				for (int i=0;i<vSummaryList.size();i++){
					for (int k=0;k<sSummaryColumns.length;k++){
						if (((Columns)vSummaryList.elementAt(i)).getColumnsId().toString().equals(sSummaryColumns[k])){
							tmpColumns.add((Columns)vSummaryList.elementAt(i));
							break;
						}
					}
				}
				vSummaryList=tmpColumns;
			}
		}else{
			vSummaryList = Columns.findByReportBandType(this.report,"Summary");
		}

		if (req.getParameter("designer").equals("false")){
			if (sPageColumns!=null){
				vPageList = Columns.findByReportBandType(this.report,"Page"); 
				Vector tmpColumns = new Vector();
				for (int i=0;i<vPageList.size();i++){ 
					for (int k=0;k<sPageColumns.length;k++){
						if (((Columns)vPageList.elementAt(i)).getColumnsId().toString().equals(sPageColumns[k])){
							tmpColumns.add((Columns)vPageList.elementAt(i));
							break;
						}
					}
				}
				vPageList=tmpColumns;
			}
		}else{
			vPageList = Columns.findByReportBandType(this.report,"Page");
		}

		if (req.getParameter("designer").equals("false")){
			if (sTitleColumns!=null){
				vReportList = Columns.findByReportBandType(this.report,"Report");
				Vector tmpColumns = new Vector();
				for (int i=0;i<vReportList.size();i++){
					for (int k=0;k<sTitleColumns.length;k++){
						if (((Columns)vReportList.elementAt(i)).getColumnsId().toString().equals(sTitleColumns[k])){
							tmpColumns.add((Columns)vReportList.elementAt(i));
							break;
						}
					}
				}
				vReportList=tmpColumns;
			} 
		}else{
			vReportList = Columns.findByReportBandType(this.report,"Report");
		}

		System.out.println(strWhere);
		List fieldList = new ArrayList(Fields.findByReport(this.report));
		List columnList = new ArrayList(vColumnList);
		List pageList = new ArrayList(vPageList); 
		List reportList = new ArrayList(vReportList);      
		List summaryList = new ArrayList(vSummaryList);     
		List groupList = new ArrayList(vGroups);    
		List headerColumns = new ArrayList(vHeaderColumns);   
		List footerColumns = new ArrayList(vFooterColumns);       
		List variableList = new ArrayList(vVariables);
		List parameterList = new ArrayList(vParams);     
  
		// add title, fields, columns, sql query, header string to 
		// VelocityContext

		String fmt = "htmlNoBreaks";
		
		if (req.getParameter("format").equals("html") || 
			req.getParameter("format").equals("pdf"))   
			fmt = "html"; 
		
		context.put("designer", req.getParameter("designer"));       
		context.put("headerPos", req.getParameter("headerOrientation"));    
		context.put("footerPos", req.getParameter("footerOrientation"));       
		context.put("title", report.getTitle());  
		context.put("titleBgColor", report.getTitleBgColor());  
		context.put("titleFgColor", report.getTitleFgColor());  
		context.put("detailHeadBgColor", report.getDetailHeadBgColor());  
		context.put("detailHeadFgColor", report.getDetailHeadFgColor());  
		context.put("detailColorOn", report.getDetailColorOn());   
		context.put("detailColorOff", report.getDetailColorOff());  
		context.put("reportList", reportList);  
		context.put("pageList", pageList); 
		context.put("summaryList", summaryList); 
		context.put("imageFile", report.getImage());        
		context.put("format", fmt);  
		if (req.getParameter("designer").equals("true"))
			context.put("orientation", report.getOrientation());
		else  
			context.put("orientation", req.getParameter("orientation"));
		context.put("fieldList", fieldList); 
		context.put("columnList", columnList);
		context.put("groupList", groupList);
		context.put("headerColumns", headerColumns);
		context.put("footerColumns", footerColumns);
		context.put("variableList", variableList);  
		context.put("parameterList", parameterList); 
		context.put("sql", strWhere);
		context.put("headerString", report.getPageHeader()!=null?report.getPageHeader():"");
		context.put("footerString", report.getSummary()!=null?report.getSummary():"");
	}

	private Parameters getParameter(String paramName, Vector vParams){
		for (int i=0;i<vParams.size();i++){
			if (paramName.equals(((Parameters)vParams.elementAt(i)).getParameterName()))
				return (Parameters)vParams.elementAt(i);
		}
		return null;
	}

	private JasperReport compileTemplate(Template template, Context context, HttpServletRequest req, HttpServletResponse resp)
			throws Exception { 

		PipedInputStream inStream = null;
		PipedInputStream oldinStream = null;
		try {
			BufferedWriter writer = null;
			Thread thread = null;
			TemplateCompiler compiler = null;
			try {
				PipedOutputStream outStream = new PipedOutputStream();
				writer = new BufferedWriter(new OutputStreamWriter(outStream));
				String rootDir = BeanObject.replaceAll(req.getRealPath(""), "\\", "//");
				Writer wout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rootDir+ "//WEB-INF//TraceReport.jrxml")));
				

				
				// Connect input stream to output stream
				inStream = new PipedInputStream(outStream);
				compiler = new TemplateCompiler(inStream, req, resp); 
				thread = new Thread(compiler);
				thread.start();
				template.merge(context, wout);
				template.merge(context, writer); 

				try{
					PrintWriter fout = new PrintWriter(wout, true);
					fout.println();
				} catch (Exception io) {
					ErrorLog.println("error in Buildding report by JRException reason!", io);
					forwardToErrorJSP(req, resp, io);
					//log it
				}			

			} catch (Exception e) {
				ErrorLog.println("error in Buildding report by JRException reason!", e);
				forwardToErrorJSP(req, resp, e);
			} finally {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			}
			// Wait for thread to finish executing
			thread.join();
			// Get compiled report
			JasperReport jasperReport = compiler.getJasperReport();
			return jasperReport;
		} catch (Exception e) {
			ErrorLog.println("error in Buildding report by JRException reason!", e);
			forwardToErrorJSP(req, resp, e);
			return null;
		} finally {
			// InputStream cannot be closed before thread
			// is finished executing
			if (inStream != null) {
				inStream.close();
			}
		}
	}

	private void generateHtmlOutput(JasperPrint jasperPrint,
			HttpServletRequest req, HttpServletResponse resp)
			throws IOException, JRException {

		Map imagesMap = new HashMap();
		req.getSession().setAttribute("IMAGES_MAP", imagesMap); 
		JRHtmlExporter exporter = new JRHtmlExporter();

		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, resp.getWriter());
		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, imagesMap);
		exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
				"image?image=");

		exporter.exportReport();       
	}

	private Connection getConnection() {

		Connection conn = null;
		String jndiName = this.report.getJndiName()!=null? this.report.getJndiName():"";
		String contextType = this.report.getContextType()!=null?this.report.getContextType():"";

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