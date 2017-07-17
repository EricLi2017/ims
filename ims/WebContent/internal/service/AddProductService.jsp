<%@page import="common.util.Filter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="internal.db.edit.InternalProductDatabase"%>
<%@page import="internal.db.model.InternalProduct"%>
<%@ page import="java.util.Enumeration"%><%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/20/2017
  Time: 5:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%--Permission Check--%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");
	//todo

	//     out.print(request.getParameterMap());
	//     out.print("<br>");
	//     out.print(request.getParameterNames());
	//     out.print("<br>");
	//    Enumeration<String> names=request.getParameterNames();
	//
	//    while(names.hasMoreElements()){
	//        out.print(names.nextElement());
	//        out.print("<br>");
	//    }

	//get request param
	String[] nameArray = request.getParameterValues("name");
	String[] descriptionArray = request.getParameterValues("description");
	String[] statusArray = request.getParameterValues("status");
	String[] asinArray = request.getParameterValues("asin");//unique key

	//validate param requried
	int rows = nameArray.length;
	if (descriptionArray.length != rows || statusArray.length != rows || asinArray.length != rows) {
		out.print("parmas error!");
		return;
	}
	for (int i = 0; i < rows; i++) {
		if (Filter.nullFilter(nameArray[i]).isEmpty() || Filter.nullFilter(statusArray[i]).isEmpty()) {
			out.print("Missing required prameters at the data line " + (i + 1));
			return;
		}
	}

	int inputRows = 0;
	int insertRows = 0;

	//insert
	List<InternalProduct> products = new ArrayList<InternalProduct>();
	InternalProduct product;
	for (int i = 0; i < rows; i++) {
		// 		System.out.println("name[" + i + "]" + nameArray[i]);
		// 		System.out.println("description[" + i + "]" + descriptionArray[i]);
		// 		System.out.println("status[" + i + "]" + statusArray[i]);
		// 		System.out.println("asin[" + i + "]" + asinArray[i]);
		inputRows++;
		product = new InternalProduct();
		product.setName(Filter.nullFilter(nameArray[i]));
		product.setDescription(Filter.nullFilter(descriptionArray[i]).isEmpty() ? null
				: Filter.nullFilter(descriptionArray[i]));
		product.setStatus(Filter.nullFilter(statusArray[i]));
		product.setAsin(Filter.nullFilter(asinArray[i]).isEmpty() ? null : Filter.nullFilter(asinArray[i]));

		products.add(product);
	}
	if (inputRows > 0 && products.size() > 0) {//there are input rows
		insertRows += new InternalProductDatabase().insert(products);
	}

	//respond
	String title = (insertRows > 0 && insertRows == inputRows) ? "Success" : "Failed";
	String titleColor = insertRows == inputRows ? "green" : "red";
	String detail = "insert rows / input rows: " + insertRows + " / " + inputRows;
	response.sendRedirect("Result.jsp?title=" + title + "&titleColor=" + titleColor + "&detail=" + detail);
%>
