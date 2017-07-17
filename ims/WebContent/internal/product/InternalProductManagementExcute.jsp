<%@page import="common.util.Filter"%>
<%@page import="internal.query.ProductQuerier"%>
<%@page import="common.util.OrderBy"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.ParseException"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="common.util.Time"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="database.model.InternalProduct"%>
<%@ page import="java.util.List"%>
<%--
  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/20/2017
  Time: 12:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%
	//set requet encoding
	request.setCharacterEncoding("UTF-8");

	//get request param
	boolean isQuery = (request.getQueryString() != null);
	String productId = Filter.nullFilter(request.getParameter("productId"));
	String name = Filter.nullFilter(request.getParameter("name"));
	String dateAfter = Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = Filter.nullFilter(request.getParameter("dateBefore"));
	String status = Filter.nullFilter(request.getParameter("status"));
	String asin = Filter.nullFilter(request.getParameter("asin"));
	//order by params
	String sortedColumnId = Filter.nullFilter(request.getParameter("sortedColumnId"));
	String sortOrder = Filter.nullFilter(request.getParameter("sortOrder"));

	//date format
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	// order by Map: <sorted column id, database table column name>
	// select from internal_product
	Map<String, String> orderByMap = new HashMap<>();
	orderByMap.put("1", "product_id");
	orderByMap.put("2", "name");
	orderByMap.put("3", "create_time");
	orderByMap.put("4", "status");
	orderByMap.put("5", "asin");

	//set default order by
	sortedColumnId = !orderByMap.containsKey(sortedColumnId) ? "1" : sortedColumnId;//set default orderBy value
	sortOrder = !OrderBy.ASC_DESC_MAP.containsKey(sortOrder) ? OrderBy.DESCENDING : sortOrder;//set default ascDesc value

	//validate param format
	if ("" != productId) {
		boolean isInt = false;
		try {
			Integer.parseInt(productId);

			isInt = true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (!isInt) {
			out.print("Incorrect parameter format: Product ID!");
			return;
		}
	}
	if ("" != dateAfter) {
		boolean isDate = false;
		try {
			sdf.parse(dateAfter);

			isDate = true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!isDate) {
			out.print("Incorrect parameter format: Date After!");
			return;
		}
	}
	if ("" != dateBefore) {
		boolean isDate = false;
		try {
			sdf.parse(dateBefore);

			isDate = true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!isDate) {
			out.print("Incorrect parameter format: Date Before!");
			return;
		}
	}

	//convert string to timestamp
	Timestamp after = "" != dateAfter ? new Timestamp(sdf.parse(dateAfter).getTime()) : null;
	Timestamp before = "" != dateBefore ? new Timestamp(sdf.parse(dateBefore).getTime()) : null;

	List<InternalProduct> productList = null;
	if (isQuery) {
		//set order by value only for database use
		String orderBy = orderByMap.get(sortedColumnId);
		String ascDesc = OrderBy.ASC_DESC_MAP.get(sortOrder);

		productList = new ProductQuerier().queryProduct(productId, name, after, before, status, asin, orderBy,
				ascDesc);
	}
%>

