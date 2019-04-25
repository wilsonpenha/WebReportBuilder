<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%
	String report = PropertiesManager.getString("app.tab.report",request);
	String queryB = PropertiesManager.getString("app.tab.queryBuilder",request);
	String variables = PropertiesManager.getString("app.tab.variables",request);
	String params = PropertiesManager.getString("app.tab.parameters",request);
	String fields = PropertiesManager.getString("app.tab.fields",request);
	String detailC = PropertiesManager.getString("app.tab.detailCols",request);
	String titleC = PropertiesManager.getString("app.tab.titleCols",request);
	String pageC = PropertiesManager.getString("app.tab.pageCols",request);
	String summaryC = PropertiesManager.getString("app.tab.summaryCols",request);
	String groups = PropertiesManager.getString("app.tab.groups",request);
	String groupsC = PropertiesManager.getString("app.tab.groupsCols",request);
	String charts = PropertiesManager.getString("app.tab.chartGraphic",request);
	String[] tabSheetOff = {report,queryB,variables,params,fields,detailC,titleC,pageC,summaryC,groups,groupsC,charts};
	String[] tabSheetOffAction = {"javascript:setObjIds(document.forms[0],"+request.getParameter("reportId")+");setAction(document.forms[0],'"+BeanObject.ACTION_FORM_UPDATE+"');document.forms[0].action='../beanReport.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../queryBuilder.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanVariables.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanParameters.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanFields.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanColumns.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanReportColumns.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanPageColumns.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanSummaryColumns.do';document.forms[0].submit();",
								  "javascript:setObjIds(document.forms[0],"+request.getParameter("groupsId")+");setAction(document.forms[0],'"+BeanObject.ACTION_FORM_UPDATE+"');document.forms[0].action='../beanGroups.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanGroupColumns.do';document.forms[0].submit();",
								  "javascript:setAction(document.forms[0],'"+BeanObject.ACTION_SELECT_LIST+"');document.forms[0].action='../beanChartGraphic.do';document.forms[0].submit();"};
	String[] tabSheetOffSize = {"40","85","55","70","35","65","60","60","82","40","75","60"};
%>
<%=BeanObject.getTabSheet(request.getParameter("tabSheetOn"),tabSheetOff,tabSheetOffAction,tabSheetOffSize)%>
