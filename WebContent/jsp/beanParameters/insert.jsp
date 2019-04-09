<%@ page import="br.com.hwork.servlet.PropertiesManager" %>
<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tld/hwork.tld" prefix="hwork" %>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanParameters/script.js"></script>

<% 
	reports.bean.BeanParameters beanPage = (reports.bean.BeanParameters)request.getAttribute("beanParameters");
%>

<script language="JavaScript"> mensagemErro('<%= beanPage.getMessage()%>');</script>

<style type="text/css">
<!--
.divDataCombo {
	overflow: hidden;
	visibility: hidden;
	z-index: 1000;
	position: absolute;
}
.divRadio {
	overflow: hidden;
	visibility: hidden;
	z-index: 1000;
	position: relative;
}
-->
</style>
<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<xml id="Columns">
  <Columns>
    <rowset tagid="comboListColumns"></rowset>
  </Columns>
</xml>
<form method="post" action="<%=request.getContextPath()%>/beanParameters.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="objIds">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter"><%=beanPage.getPObject().getClass().getName().substring(4)%> - Add</td>
  </tr>
  <tr> 
    <td>
	  <div id="tabGuia" style="width:100%; height:20px; z-index:1; overflow: hidden" >
		<% String tabName = PropertiesManager.getString("app.tab.parameters",request); %>
	     <jsp:include flush="true" page='<%="../tabSheets1.jsp?tabSheetOn=" + tabName%>'/>
	  </div>
    </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>              <td width="32%" class="dataLabel">*Parameter Name : </td>              <td width="66%" class="inputLocation">                <input type="hidden" name="parametersId" value="<%=beanPage.getParametersId()%>">                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">                <input type="text" class="dataInput" name="parameterName" tabIndex="1" maxlength="30" size="30" value="<%=beanPage.getParameterName()%>">              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">*Class Type : </td>              <td width="66%" class="inputLocation">                <%
                  Vector objsJavaClassId = reports.JavaClass.findAll();
                  String[] idsJavaClassId = {"classType"};
                  String[] objValuesJavaClassId = {"classType"};
                %>

                <hwork:ComboList name="classType" 
                  ids="<%=idsJavaClassId%>" 
                  objValues="<%=objValuesJavaClassId%>" 
                  onEvent="tabIndex=2"
				  blank="false"
                  objs="<%=objsJavaClassId%>"> 
                </hwork:ComboList> 
              </td>            </tr>            <tr>              <td width="32%" class="dataLabel">Parameter Description : </td>              <td width="66%" class="inputLocation">                <input type="text" class="dataInput" name="parameterDescription" tabIndex="3" maxlength="50" size="50" value="<%=beanPage.getParameterDescription()%>">              </td>            </tr>            <tr>
              <td width="32%" class="dataLabel">SQL Statement : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="sql" tabIndex="3" rows="3" cols="55"><%=beanPage.getSql()%></textarea>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Is Required? : </td>
              <td width="66%" class="inputLocation">
                <Select name="isRequired" tabIndex="4">
                   <option value="Yes" <%if (beanPage.getIsRequired().equals("Yes")){out.print("selected");}%>>Yes</option>
                   <option value="No" <%if (beanPage.getIsRequired().equals("No")){out.print("selected");}%>>No</option>
                </Select>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Input Type : </td>
              <td width="66%" class="inputLocation">
                <Select name="inputType" tabIndex="5" onChange="javascript:releaseDiv(this.value);">
                   <option value="Text" <%if (beanPage.getInputType().equals("Text")){out.print("selected");}%>>Text</option>
                   <option value="Radio" <%if (beanPage.getInputType().equals("Radio")){out.print("selected");}%>>Radio</option>
                   <option value="Data Combo" <%if (beanPage.getInputType().equals("Data Combo")){out.print("selected");}%>>Data Combo</option>
                </Select>
              </td>
            </tr>
            <tr>
              <td colspan="2">
	<div class="divDataCombo" id="divDataCombo" scrolling="no">
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>
              <td width="32%" class="dataLabel">SQL Input : </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="tableInput" tabIndex="3" rows="4" cols="55"><%=beanPage.getTableInput()%></textarea>
                <input type="submit" value=" Load fields "  style="width: 69px;" name="btnSave" class="stdButton" onMouseOver="javascript:mensagem('Click here to load fields.');" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_FORM_INSERT%>');" >
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Field Key : </td>
              <td width="66%" class="inputLocation">
	      <%  reports.bean.BeanParameters beanParams = new reports.bean.BeanParameters();
	      	  beanParams.setReportId(beanPage.getReportId());
	      	  beanParams.setTableInput(beanPage.getTableInput());
              Vector objsFieldKey = beanParams.getColumnsFromSQL();
              String[] idsFieldKey = {"fieldName"};
              String[] objValuesFieldKey = {"fieldName"};
          %>
                <hwork:ComboList name="fieldKey" 
                  ids="<%=idsFieldKey%>"  
                  objValues="<%=objValuesFieldKey%>" 
                  onEvent="tabIndex=\"6\""
                  blank="true"
                  objs="<%=objsFieldKey%>"
                  selected="<%=beanPage.getFieldKey()%>">
                </hwork:ComboList>
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Field Display : </td>
              <td width="66%" class="inputLocation">
	      <%  
              Vector objsFieldDisplay = beanParams.getColumnsFromSQL();
              String[] idsFieldDisplay = {"fieldName"};
              String[] objValuesFieldDisplay = {"fieldName"}; 
          %>
                <hwork:ComboList name="fieldDisplay" 
                  ids="<%=idsFieldDisplay%>" 
                  objValues="<%=objValuesFieldDisplay%>" 
                  onEvent="tabIndex=\"6\""
                  blank="true"
                  objs="<%=objsFieldDisplay%>"
                  selected="<%=beanPage.getFieldDisplay()%>">
                </hwork:ComboList>
              </td>
            </tr>
          </table> 
	</div>
		  </td>
            </tr>
            <tr> 
              <td colspan="2">
			<div class="divRadio" id="divRadio" scrolling="no">
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>
              <td width="32%" class="dataLabel">
              Radio Options with "=" & separated by ";" :<br> 
              ex.: 0=Approved, Yes=Yes, No=No, etc.
              </td>
              <td width="66%" class="inputLocation">
                <textarea class="dataInput" name="radioOptions" tabIndex="9" rows="4" cols="40"><%=beanPage.getRadioOptions()%></textarea>
              </td>
            </tr>
          </table>
		  </div>
		  </td>
            </tr>
          </table>
      </td>
	</tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
    </tr>
    <script>seta_foco_no_primeiro();
		    releaseDiv(document.forms[0].inputType.value);
    </script>
	<tr>
	  <td align="center">
		<table border="0" cellPadding="0" cellSpacing="4">
		  <tr>
			<td><input type="submit" value=" <bean:message key="app.button.save"/> " style="width: 49px;" name="btnSave" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_INSERT%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.save.msg"/>');"></td>
			<td><input type="submit" value=" <bean:message key="app.button.cancel"/> " style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');" onMouseOver="javascript:mensagem('<bean:message key="app.button.cancel.msg"/>');"></td>
		  </tr>
		</table>
	  </td>
	</tr>
</table>
</form>
</body>


