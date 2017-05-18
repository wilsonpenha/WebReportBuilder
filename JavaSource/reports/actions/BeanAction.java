package reports.actions;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import reports.bean.Main;

/**
 * @version 	1.0
 * @author
 */
public class BeanAction extends Action

{

	private Main beanMain = new Main();
	private String encoding;

	HttpSession session = null;

	public void init() throws ServletException {
        ServletConfig config = getServlet().getServletConfig();
        encoding = config.getInitParameter("PARAMETER_ENCODING");
    }
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        if (encoding != null) {
            request.setCharacterEncoding(encoding);
        }
    }

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward(); // return value

        try {

            // do something here
        	processRequest(request, response);

			//System.out.println("title : "+request.getParameter("title"));
        	this.session = request.getSession();
			beanMain = null;
			synchronized (session) {
				beanMain = new reports.bean.Main();
				beanMain.setRootDIR(request);
			}

        } catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add("name", new ActionMessage("id"));

        }

        // If a message is required, save the specified key(s)
        // into the request for use by the <struts:errors> tag.

        if (!errors.isEmpty()) {
            saveErrors(request, errors);

            // Forward control to the appropriate 'failure' URI (change name as desired)
            //	forward = mapping.findForward("failure");

        } else {

            // Forward control to the appropriate 'success' URI (change name as desired)
            // forward = mapping.findForward("success");

        }

        // Finish with
        return (forward);

    }
}
