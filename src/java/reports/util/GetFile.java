package reports.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import reports.Report;
import reports.bean.Main;

/**
 * @version 	1.0
 * @author
 */
public class GetFile extends HttpServlet implements Servlet {

	private static final String TASK_PDF = "pdf";
	private static final String TASK_XML = "xml";
	private static final String TASK_HTM = "htm";
	private static final String TASK_XLS = "xls";
	private static final String TASK_CSV = "csv";
	private static final String TASK_DOC = "doc";
	private static final String TASK_RTF = "rtf";
	private static final String TASK_TXT = "txt";
	private static final String TASK_JPG = "jpg";
	private static final String TASK_BMP = "bmp";
	private static final String TASK_TIF = "tif";
	
	private Main beanMain = new Main();
	HttpSession session; 

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		OutputStream outputStream; 
		outputStream = resp.getOutputStream();
		String id = req.getParameter("id");
		try {
			this.session = req.getSession();
					beanMain = null;
					synchronized (session) {
						beanMain = new reports.bean.Main();

					}

			beanMain.setRootDIR(req);
			
			Report attachmentFiles = Report.findByID(new BigDecimal(req.getParameter("reportId")));
			String extFile = ".jrxml"; 
			
			resp.setContentType("text/plain; charset=ISO-8859-1");
			resp.setHeader("Content-Length", new Long(attachmentFiles.getJasperJrxml().length).toString());
			resp.setHeader("Content-Disposition", "inline; filename="+TextUtil.urlDecodeUTF8(attachmentFiles.getReportName()+extFile));
			outputStream.write(attachmentFiles.getJasperJrxml(), 0, attachmentFiles.getJasperJrxml().length);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {

	}

}
