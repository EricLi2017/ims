<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/20/2017
  Time: 4:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Add Internal Product</title>
<%--page style--%>
<link rel="stylesheet" href="../../include/css/page.css">
<%--use the css style for input area --%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/input.css">
</head>
<body>
	<%--Page Navigation Menu --%>
	<%@ include file="/include/page/Menu.jsp"%>

	<%--Page Main Content --%>
	<div class="content">

		<h1 style="text-align: center">Add Internal Product</h1>

		<%--input product data--%>

		<div>
			<form method="post" action="../service/AddProductService.jsp">
				<table align="center" width="100%">
					<tr>
						<th width="30%" class="required">Name</th>
						<th width="50%">Description</th>
						<th width="10%">ASIN</th>
						<th width="10%" class="required">Status</th>
					</tr>
					<tr>
						<td><input type="text" name="name" title=""
							style="width: 100%"></td>
						<td><input type="text" name="description" title=""
							style="width: 100%"></td>
						<td><input type="text" name="asin" title=""
							style="width: 100%"></td>
						<td><select name="status">
								<option value="Active" selected>Active</option>
								<option value="Inactive">Inactive</option>
						</select></td>
					</tr>
				</table>
				<div>
					<input type="submit" value="Submit"><input type="reset"
						value="Reset">
				</div>
			</form>
		</div>
	</div>


	<%--Page Footer --%>
	<%@ include file="/include/page/Footer.jsp"%>
</body>
</html>
