<%@page import="java.sql.Timestamp"%>
<%@page import="amazon.mws.order.SkuSum"%>
<%@page import="amazon.mws.order.ListOrderAndOrderItemsManager"%>
<%@page import="java.util.List"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="common.util.Time"%>
<%@ page import="java.text.ParseException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/jsp/PermissionCheck.jsp"%>
<%
	//request params
	//
	boolean isQuery = (request.getQueryString() != null);
	String dateAfter = common.util.Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = common.util.Filter.nullFilter(request.getParameter("dateBefore"));

	//set time zone to PST
	//
	// 	TimeZone PST = Time.PST;
	// 	TimeZone.setDefault(PST);
	TimeZone currentTZone = TimeZone.getDefault();
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	//action when query submit
	//
	if (isQuery) {
		//set action params
		//
		XMLGregorianCalendar createdAfter = null;
		XMLGregorianCalendar createdBefore = null;

		boolean isDateAfterValid = true;
		boolean isDateBeforeValid = true;
		if (dateAfter != null && !"".equals(dateAfter)) {
			isDateAfterValid = false;
			try {
				createdAfter = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateAfter).getTime()));
				isDateAfterValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (dateBefore != null && !"".equals(dateBefore)) {
			isDateBeforeValid = false;
			try {
				createdBefore = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateBefore).getTime()));
				isDateBeforeValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!isDateAfterValid || !isDateBeforeValid) {
			out.print("incorrect date, correct date format is " + pattern);
			return;
		}

		//query database to get sku sum
		List<SkuSum> skuSums = ListOrderAndOrderItemsManager.getSumByPruchaseDate(createdAfter, createdBefore);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--include jquery lib--%>
<script type="text/javascript"
	src="../../include/js/jquery/jquery-3.1.1.min.js"></script>
<%--set background color of tr while mouseover and mouseout--%>
<script type="text/javascript" src="../../include/js/table.js"></script>
<%--define the css style of table--%>
<link type="text/css" rel="stylesheet"
	href="../../include/css/table.css">
<title>SKU SUM</title>
</head>
<body>
	<div align="center">
		<h2>SKU SUM</h2>
	</div>
	<div id="timeScopeDiv" align="center">
		<h4><%=createdAfter%>
			to
			<%=createdBefore%>
		</h4>
	</div>
	<div id="resultDiv" class="table-c">
		<table align="center">
			<tr class="title">
				<th></th>
				<th>SKU</th>
				<th>priceAmount</th>
				<th>discountAmount</th>
				<th>quantityOrdered</th>
				<th>quantityShipped</th>
			</tr>
			<%
				if (skuSums != null && skuSums.size() > 0) {
						int rows = 0;
						for (SkuSum skuSum : skuSums) {
			%>
			<tr class="data">
				<td><%=++rows%></td>
				<td><%=skuSum.getSku()%></td>
				<td align="right"><%=skuSum.getPriceAmount()%></td>
				<td align="right"><%=skuSum.getDiscountAmount()%></td>
				<td align="right"><%=skuSum.getQuantityOrdered()%></td>
				<td align="right"><%=skuSum.getQuantityShipped()%></td>
			</tr>
			<%
				} //loop end
			%>
		</table>
	</div>
	<%
		} else {
	%>
	<div align="center">There is no matched result.</div>
	<%
		}
	%>
</body>
</html>
<%
	} //if(isQuery) end
%>