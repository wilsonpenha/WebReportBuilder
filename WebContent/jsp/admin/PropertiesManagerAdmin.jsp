<%@ page language="java" autoFlush="true" isThreadSafe="true" buffer="10kb" contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import="java.util.Locale, java.util.ArrayList, java.util.HashMap, java.util.Iterator" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head> 
<html:base/> 
<title>"Hwork Administration | Properties Manager"</title> 

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">



<%
    Locale currentLocale = null;
	String currLocaleString = request.getParameter("currlocale");
	if (currLocaleString==null) 
	{
		currLocaleString = "";
		currentLocale = new Locale("en","");
	}
	else if (currLocaleString.indexOf("_")!=-1)
	{
		currentLocale = new Locale(currLocaleString.substring(0,currLocaleString.indexOf("_")),currLocaleString.substring(currLocaleString.indexOf("_")+1,currLocaleString.length()));
	}
	else{
	    currentLocale = new Locale(currLocaleString,"");
	}
	HashMap supportedLocales = PropertiesManager.get_SupportedLocale();
	Iterator localeIter = supportedLocales.keySet().iterator();
    
	
	String searchForKey = request.getParameter("searchForKey");
	if (searchForKey == null) searchForKey = "";
	String searchForValue = request.getParameter("searchForValue");
	if (searchForValue == null) searchForValue = "";

	String refreshSuccess = (String) request.getAttribute("refreshSuccess");
	
%>
<!-- start content //////////////////////////////////////////// -->
<div id="content">
<h1>Properties Manager</h1>
<div id="content-main">
<form action="admin" method="get" name="GCForm">
<h2 class="bar-blue-med">Display Properties</h2>

		<p>This page allows you to test/view the content stored in property files.  Please fill in the following information 
		aobut the content that you would like to test. The content will then be loaded below.
		</p>
		<form action="admin" method="get" name="refreshForm">
			<input type="hidden" name="action" value="properties" />
			<span class="button-blue"><input type="submit" value="Force refresh" /></span>
			<input type="hidden" name="refresh" value="true" />
		</form>
		<% if ((refreshSuccess!=null)&&(refreshSuccess.equals("true")))
		{%>
			<p><b>Refresh successful</b></p>
		<%}		
		%>
		<form name="contentForm" method="get" action="admin?action=properties">
		<input type="hidden" name="action" value="properties" />
		<table width="90%">
		<tr>
		<td align="left" width="20%">		
		<label  for="currlocale">Locale:</label>
		</td>
		<td align="left" width="80%">
		 <select name="currlocale" id="currlocale">
	     <% if (currentLocale.toString().equals("")) { %>
	        <option value="" selected="selected"></option>
	     <% } else { %>
	        <option value=""></option>
	     <% } %>
		 <% while(localeIter.hasNext()){
			   String thisLocale =  (String) localeIter.next();
		       if (currentLocale.toString().equals(thisLocale)) { %>
		       <option value="<%=thisLocale%>" selected="selected"><%=thisLocale%></option>  
		       <% } else { %>
		       <option value="<%=thisLocale%>"><%=thisLocale%></option>  
		       <% } %> 
		 <% }%>
		 </select>
		 </td>
		 </tr>
		 <tr>
		 <td align="left" width="20%">		
		 <label for="searchForKeyId">Search for key:</label>
		 </td>
		 <td align="left" width="80%">
				 <span><input type="text" name="searchForKey" id="searchForKeyId" value="<%=searchForKey%>" /></span>
		 </td>
		 </tr>
		  <tr>
		 <td align="left" width="20%">		
		 <label for="searchForValueId">Search for value:</label>
		 </td>
		 <td align="left" width="80%">
				 <input type="text" name="searchForValue" id="searchForValueId" value="<%=searchForValue%>" />&nbsp;&nbsp;<span class="button-blue"><input type="submit" value="Display properties" /></span>
		 </td>
		 </tr>
	 
	      </table>
		</form>
	
		<br /><br />
		<% if ((!searchForKey.equals("")||!searchForValue.equals(""))) { 
			ArrayList keyList = PropertiesManager.getFullKeyList();
			boolean noSearchKey = searchForKey.equals("");
			boolean noSearchValue = searchForValue.equals("");
		%>
		Locale displayed:  <%=currentLocale%>
		
		  <table cellspacing="1" cellpadding="0" class="basic-table">
		  <tr class="violet-med-dark">
		      <th>Key</th>
		      <th>Content value</th>
		  </tr>
		  <%
		     boolean odd = true;
		     boolean printThisRow;
		     for (int i =0;i < keyList.size();i++) {
		     
		     
		     String key = (String)keyList.get(i);
		     String value = PropertiesManager.getString(key,currentLocale);
		     if (value == null) value = "";
		   	 
		   	 if (noSearchKey&&noSearchValue)
		   	 {
			   printThisRow = true;
			 }
			 else if ((!noSearchKey)&&(key.indexOf(searchForKey)!=-1))
			 {
			 	printThisRow = true;
			 }
			 else if ((!noSearchValue)&&(value.indexOf(searchForValue)!=-1))
			 {
			 	printThisRow = true;
			 }
			 else
			 {
			 	printThisRow = false;
			 }
			 
			 if (printThisRow)
			 {
			 	odd = !odd;
		  %>
		  <% if (odd) { %>
		  <tr class="odd">
		  <% } else { %>
		  <tr class="even">
		  <% } %>
        	  <td width="20%"><big><%= key %><big></td> 
        	  <td width="80%"><big><%= value %></big></td> 
  		  </tr>
          <% }
            }
             
          
          %> 
          </table>
				
				
		<% } %>
		<hr /> 







</div>
</div>
</body>
</html>
