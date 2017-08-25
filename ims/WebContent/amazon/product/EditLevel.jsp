<%@page import="amazon.db.query.AmazonProductQuerier"%>
<%@page import="common.util.Filter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%--Permission Check--%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//get param from request
	String sku = Filter.nullFilter(request.getParameter("sku"));

	//validate param required
	if (sku.isEmpty()) {
		out.print("Missing required parameter!");
		return;
	}

	//selet data from database
	Integer level = AmazonProductQuerier.selectLevelBySku(sku);
%>
<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset=UTF-8">
<title>Edit Level of Product</title>
<%--Required Head Core Elements--%>
<%@ include file="/include/jsp/head/Head-Core.jsp"%>
<%--Optional Head Date Elements--%>
<%@ include file="/include/jsp/head/Head-Date.jsp"%>
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/jsp/body/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<h1 style="text-align: center">
			Edit Level of Product (<span class="required">SKU=<%=sku%></span>)
		</h1>

		<%--edit product data--%>
		<div>
			<form method="post" action="../service/EditLevelService.jsp">

				<div style="text-align: center;">
					<input type="text" name="level"
						value="<%=level == null ? "" : level%>"> <input
						type="hidden" name="sku" value="<%=sku%>"><input
						type="submit" value="Submit">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input
						type="reset" value="Reset">
				</div>
			</form>
		</div>


	</div>

	<%--Page Footer --%>
	<%@ include file="/include/jsp/body/Footer.jsp"%>
</body>
</html>