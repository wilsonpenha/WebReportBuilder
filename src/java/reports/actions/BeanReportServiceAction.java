package reports.actions;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import reports.bean.BeanObject;
import reports.util.ReportServlet;

/**
 * @version 	1.0
 * @author
 */
public class BeanReportServiceAction extends BeanAction

{

    @SuppressWarnings("static-access")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward(); // return value

        try {

        	super.execute(mapping, form, request, response);
        	
			BeanObject beanObj = (BeanObject)form;
            beanObj.execute();
            
            RequestDispatcher rd = request.getRequestDispatcher("factory.do");
            rd.forward(request,response);
            
			if (beanObj.getDbAction().equals(String.valueOf(beanObj.ACTION_SELECT_LIST))){
				forward = mapping.findForward("showReportService");
			}else if (beanObj.getDbAction().equals(String.valueOf(beanObj.ACTION_SELECT_OBJECT))){
				forward = mapping.findForward("showReportService");
			}
            // do something here

        } catch (Exception e) {

            // Report the error using the appropriate name and ID.
            errors.add("name", new ActionMessage("id"));

        }

        // If a message is required, save the specified key(s)
        // into the request for use by the <struts:errors> tag.

        if (!errors.isEmpty()) {
            saveErrors(request, errors);

            // Forward control to the appropriate 'failure' URI (change name as desired)
            forward = mapping.findForward("showReportService");

        } else {

            // Forward control to the appropriate 'success' URI (change name as desired)
            forward = mapping.findForward("showReportService");

        }

        // Finish with
        return (forward);

    }
}
