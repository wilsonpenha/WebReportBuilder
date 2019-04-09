<%@ page import = "reports.bean.BeanObject, br.com.hwork.text.Formatter, java.util.Vector, reports.Groups"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="hwork" uri='/WEB-INF/tld/hwork.tld'%>

<script language="javascript" src="<%=request.getContextPath()%>/jsp/beanReportServer/script.js"></script>

<% 
	reports.bean.BeanReport beanPage = (reports.bean.BeanReport)request.getAttribute("beanReport");
%>

<%beanPage.selectObject();%>

<body topmargin="0" leftmargin="0" bottommargin="0" rightmargin="0">
<form method="post" action="<%=request.getContextPath()%>/beanReportServer.do" onSubmit="return ConfirmaSubmit(this,'<%=beanPage.getBeanForm()%>')">
<input type="hidden" name="dbAction">
<input type="hidden" name="formAction" value="<%=beanPage.getDbAction()%>">
<input type="hidden" name="blockNumber" value="<%=beanPage.getBlockNumber()%>">
<input type="hidden" name="blockSize" value="<%=beanPage.getBlockSize()%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr> 
    <td height="30" class="topTitleCenter">Report Server - Parameters</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>
          <table width="100%" border="0" cellPadding="0" cellSpacing="0">
            <tr>
              <td width="32%" class="dataLabel">Report Name : </td>
              <td width="66%" class="inputLocation">
                <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
                <input type="text" class="dataInput" name="reportName" tabIndex="2" readonly size="50" value="<%=beanPage.getReportName()%>">
              </td>
            </tr>
            <tr>
              <td width="32%" class="dataLabel">Description : </td>
              <td width="66%" class="inputLocation">
                <input type="text" class="dataInput" name="description" tabIndex="3" readonly size="60" value="<%=beanPage.getDescription()%>">
              </td>
            </tr>
            </form>
			<form method="post" action="<%=request.getContextPath()%>/factory.do" name="reportForm" target="winReport">
			<input type="hidden" name="dbAction">
            <input type="hidden" name="reportId" value="<%=beanPage.getReportId()%>">
            <input type="hidden" name="reload" value="true">
            <input type="hidden" name="designer" value="false">
            <tr>
              <td width="32%" class="dataLabel">Report Orientation : </td>
              <td width="66%" class="inputLocation">
                <Select name="orientation" tabIndex="9">
                   <option value="Portrait" <%if (beanPage.getOrientation().equals("Portrait")){out.print("selected");}%>>Portrait</option>
                   <option value="Landscape" <%if (beanPage.getOrientation().equals("Landscape")){out.print("selected");}%>>Landscape</option>
                </Select>
              </td>
            </tr>
			<% if (beanPage.getParametersInput().equals("")){%>
	            <input type="hidden" name="paramName">
    	        <input type="hidden" name="paramValue">
			<%}%>	
			<%=beanPage.getParametersInput()%>            

	            <tr>
	              <td colspan="2" style="text-align:center" class="dataLabel">Orientation for Headers and Footers</td>
	            </tr>
	            <tr>
	              <td colspan="2" style="text-align:center" class="dataLabel">Header Orientation :
	              <input type="radio" name="headerOrientation" checked value="Vertical"> Vertical
	              <input type="radio" name="headerOrientation" value="Horizontal"> Horizontal / Footer Orientation :
	              <input type="radio" name="footerOrientation" value="Vertical"> Vertical
	              <input type="radio" name="footerOrientation" checked value="Horizontal"> Horizontal
	              </td>
	            </tr>
			<%Vector reportColumns = beanPage.getTitleColumns();
			  if (reportColumns.size()>0){%>
	            <tr>
	              <td width="32%" class="dataLabel">Title</td>
	              <td width="66%" class="inputLocation">&nbsp;&nbsp;Selection</td>
	            </tr>
	            <tr  valign="top">
	              <td width="32%" class="dataLabel" valign="top">&nbsp;</td>
	              <td width="66%" class="inputLocation">
	              <%=beanPage.getTagReport()%>
	              </td>
	            </tr>
	            <tr height="10">
	              <td width="32%" class="dataLabel"></td>
	              <td width="66%" class="inputLocation"></td>
	            </tr>
			<%}%>

			<%Vector pageColumns = beanPage.getPageColumns();
			  if (pageColumns.size()>0){%>
	            <tr>
	              <td width="32%" class="dataLabel">Page</td>
	              <td width="66%" class="inputLocation">&nbsp;&nbsp;Selection</td>
	            </tr>
	            <tr  valign="top">
	              <td width="32%" class="dataLabel" valign="top">&nbsp;</td>
	              <td width="66%" class="inputLocation">
	              <%=beanPage.getTagPage()%>
	              </td>
	            </tr>
	            <tr height="10">
	              <td width="32%" class="dataLabel"></td>
	              <td width="66%" class="inputLocation"></td>
	            </tr>
			<%}%>

			<%Vector groups = beanPage.getGroups();
			  if (groups.size()>0){%>
	            <tr>
	              <td colspan="2" style="text-align:center" class="dataLabel">Groups Selection</td>
	            </tr>
			<%	for (int i =0;i<groups.size();i++){%>
	            <tr  valign="top">
	              <td width="32%" class="dataLabel" valign="top"><div style="vertical-align: top">
	              <input type="checkbox" name="groupsIds" checked onClick="setGrpCols('<%=((Groups)groups.elementAt(i)).getGroupsId()%>|',this);"
	              value="<%=((Groups)groups.elementAt(i)).getGroupsId()%>">&nbsp;&nbsp;<%=((Groups)groups.elementAt(i)).getGroupName()%>&nbsp;&nbsp;</div></td>
	              <td width="66%" class="inputLocation">
	              <%=beanPage.getTagGroups((Groups)groups.elementAt(i))%>
	              </td>
	            </tr>
	            <tr height="10">
	              <td width="32%" class="dataLabel"></td>
	              <td width="66%" class="inputLocation"></td>
	            </tr>
			  <%}%>
			<%}%>

			<%Vector detailColumns = beanPage.getDetailColumns();
			  if (detailColumns.size()>0){%>
	            <tr>
	              <td width="32%" class="dataLabel">Detail</td>
	              <td width="66%" class="inputLocation">&nbsp;&nbsp;Selection</td>
	            </tr>
	            <tr  valign="top">
	              <td width="32%" class="dataLabel" valign="top">&nbsp;</td>
	              <td width="66%" class="inputLocation">
	              <%=beanPage.getTagDetail()%>
	              </td>
	            </tr>
	            <tr height="10">
	              <td width="32%" class="dataLabel"></td>
	              <td width="66%" class="inputLocation"></td>
	            </tr>
			<%}%>

			<%Vector summaryColumns = beanPage.getSummaryColumns();
			  if (summaryColumns.size()>0){%>
	            <tr>
	              <td width="32%" class="dataLabel">Summary</td>
	              <td width="66%" class="inputLocation">&nbsp;&nbsp;Selection</td>
	            </tr>
	            <tr  valign="top">
	              <td width="32%" class="dataLabel" valign="top">&nbsp;</td>
	              <td width="66%" class="inputLocation">
	              <%=beanPage.getTagSummary()%>
	              </td>
	            </tr>
	            <tr height="10">
	              <td width="32%" class="dataLabel"></td>
	              <td width="66%" class="inputLocation"></td>
	            </tr>
			<%}%>

            <tr>
             <td width="32%" class="dataLabel">Report output : </td>
              <td width="66%" colspan="3" class="inputLocation">
                <input type="radio" name="format"  value="html" tabIndex="14" checked> HTML 
                <input type="radio" name="format"  value="htmlNoBreaks" tabIndex="14"> HTML no Breaks 
                <input type="radio" name="format"  value="pdf" tabIndex="14" > PDF
                <input type="radio" name="format"  value="csv" tabIndex="14" > CSV 
                <input type="radio" name="format"  value="xls" tabIndex="14" > XLS 
               </td>
            </tr>
          </table>
      </td>
	</tr>
    <script>seta_foco_no_primeiro()</script>
	<script>
		function setGrpCols(groupId, group){
			if (document.forms[1].gColsIds.length){
				for (i=0;i<document.forms[1].gColsIds.length;i++){
					if (document.forms[1].gColsIds[i].value.indexOf(groupId)>=0 &&
						document.forms[1].gColsIds[i].value!=group.value){
						if (document.forms[1].gColsIds[i].checked==true)
							document.forms[1].gColsIds[i].checked=false;
						else if(group.checked==true)
							document.forms[1].gColsIds[i].checked=true;
					}
				}
			}
		}
		function setGroupChecked(obj){
			groupId = obj.value.substring(0,obj.value.indexOf('|')-1);
			if (document.forms[1].groupsIds.length){
				for (i=0;i<document.forms[1].groupsIds.length;i++){
					if (document.forms[1].groupsIds[i].value==groupId)
						document.forms[1].groupsIds[i].checked=true;
				}
			}else{
				document.forms[1].groupsIds.checked=true;
			}
		}
		function checkDetailComuns(obj){
			allUnChecked = true;
			if (document.forms[1].detailIds.length){
				for (i=0;i<document.forms[1].detailIds.length;i++){
					if (document.forms[1].detailIds[i].checked){
						allUnChecked=false;
						break;
					}
				}
			}
			if (allUnChecked){
				alert('You must have to provide one DETAIL COLUMN!');
				obj.checked=true;
			}
		}
	</script>
		<tr>
		  <td align="center">
			<table border="0" cellPadding="0" cellSpacing="4">
			  <tr>
				<td><input type="button" value=" Cancel "  style="width: 67px;" name="btnReturn" class="stdButton" onClick="javascript:setAction(document.forms[0],'<%=BeanObject.ACTION_SELECT_LIST%>');document.forms[0].submit();" onMouseOver="javascript:mensagem('Click here to back to list.');"></td>
				<td><input type="button" value=" Run "     style="width: 38px;" name="btnNew" class="stdButton" onClick="javascript:setAction(document.forms[1],'<%=BeanObject.ACTION_SELECT_LIST%>');document.forms[1].submit();" onMouseOver="javascript:mensagem('Click here to run this Report.');"></td>
			  </tr>
			</table>
		  </td>
		</tr>
</table>
</form>
</body>
