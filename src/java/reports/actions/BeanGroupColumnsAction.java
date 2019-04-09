package reports.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import reports.bean.BeanObject;

/**
 * @version 	1.0
 * @author
 */
public class BeanGroupColumnsAction extends BeanAction

{

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward(); // return value

        try {

        	super.execute(mapping, form, request, response);
			BeanObject beanObj = (BeanObject)form;
            beanObj.execute();
			if (beanObj.getDbAction().equals(String.valueOf(BeanObject.ACTION_FORM_INSERT))){
	        	return mapping.findForward("showFormInsert");
			}else if (beanObj.getDbAction().equals(String.valueOf(BeanObject.ACTION_SELECT_LIST))){
	        	return mapping.findForward("showDisplayList");
			}else if (beanObj.getDbAction().equals(String.valueOf(BeanObject.ACTION_SELECT_OBJECT))){
	        	return mapping.findForward("showFormUpdate");
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
            //	forward = mapping.findForward("failure");

        } else {

            // Forward control to the appropriate 'success' URI (change name as desired)
            // forward = mapping.findForward("success");

        }

        // Finish with
        return (forward);

    }
}
