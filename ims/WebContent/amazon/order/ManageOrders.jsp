<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%@ include file="ManageOrdersExcute.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage Orders</title>
<%--define the css style of query result table--%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/table.css">
<%--jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--jquery ui for datepicker function--%>
<script type="text/javascript"
	src="../../include/js/jquery-ui-1.12.1/jquery-ui.js"></script>
<%--jquery ui for datepicker style--%>
<link rel="stylesheet"
	href="../../include/js/jquery-ui-1.12.1/themes/redmond/jquery-ui.css">

<%--set background color of table tr while mouseover and mouseout--%>
<script type="text/javascript" src="../../include/js/table.js"></script>
<%--special only for this page--%>
<script type="text/javascript" language="javascript">
	$(document).ready(function() {
		/*set date*/
		$("#dateAfter").datepicker({
			dateFormat : "yy-mm-dd"
		});
		$("#dateBefore").datepicker({
			dateFormat : "yy-mm-dd"
		});

	});//end of $(document).ready()

	function sub() {
		if ($("#dateAfter").val() == "") {
			window.alert('Please input correct date!');
			$("#dateAfter").focus();
			return false;
		}
		if ($("#dateBefore").val() == "") {
			window.alert('Please input correct date!');
			$("#dateBefore").focus();
			return false;
		}
		return true;
	}
</script>
</head>
<body>

	<h1 align="center">Manage Orders</h1>

	<table align="center">
		<tr align="center">
			<th>Limit</th>
			<td>Max count: 6; Resume speed: 1 per minute; Max result number:
				1-100</td>
		</tr>
		<tr align="center">
			<th>Time</th>
			<td>Current Time Zone : <%=currentTZone.getID()%> , <%=currentTZone.getRawOffset() >= 0 ? "+" : ""%><%=currentTZone.getRawOffset() / 3600000%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Current Time:<%=Time.getTime(currentTZone, new Timestamp(System.currentTimeMillis()))%>
			</td>
		</tr>
	</table>
	<p><%=request.getQueryString()%>
	</p>

	<!-- Query form -->
	<form action="" method="get" name="dateForm">
		<table align="center">

			<tr>
				<td>Order Purchase Date After:</td>
				<td><input type="text" id="dateAfter" name="dateAfter"
					value="<%=isQuery ? dateAfter : ""%>" title="<%=pattern%>">
					00:00</td>
				<td>(<%=pattern%>)
				</td>
			<tr>
				<td>Order Purchase Date Before:</td>
				<td><input type="text" id="dateBefore" name="dateBefore"
					value="<%=isQuery ? dateBefore : ""%>" title="<%=pattern%>">
					00:00</td>
				<td><input type="submit" value="Search" onclick="return sub()"></td>
			</tr>
		</table>
	</form>

	<!-- Query result -->
	<%
		//action when query submit
		//
		if (isQuery) {
	%>
	<p></p>
	<div>
		<table width="100%">
			<tr>
				<td>There are <%=dbCount%> orders in database: <%=rows%> has
					related order items and <%=noRows%> has not. <br> <%=amazonOrderIds%>
					<br> <%=orderMatchResult%>
				</td>
				<td align="right"><a
					href="QueryOrderCountFromMWS.jsp?<%=request.getQueryString()%>"
					target="_blank">Query Order Count From MWS</a></td>
				<td>
				<td></td>
			</tr>
			<tr>
				<td><%=createdAfter%> to <%=createdBefore%></td>
				<td><a
					href="AddOrdersService.jsp?<%=request.getQueryString()%>"
					target="_blank">Add Orders To IMS From MWS</a></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td align="right"></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td align="right"><a
					href="AddOrderItemsService.jsp?<%=request.getQueryString()%>"
					target="_blank">Add Order Items To IMS From MWS</a></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td align="right"><a
					href="SKUSum.jsp?<%=request.getQueryString()%>" target="_blank">View
						SKU Sum</a></td>
			</tr>
		</table>
	</div>
	<%
		} //end of if(isQuery)
	%>

</body>
</html>