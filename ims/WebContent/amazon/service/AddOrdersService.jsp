<%@page import="amazon.mws.order.ListOrdersManager"%>
<%@page import="common.util.Time"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@ page import="java.text.ParseException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/PermissionCheck.jsp"%>
<%
	//request params
	//
	boolean isQuery = (request.getQueryString() != null);
	String dateAfter = common.util.Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = common.util.Filter.nullFilter(request.getParameter("dateBefore"));

	//set time zone to PST
	//
	TimeZone PST = Time.PST;
	TimeZone.setDefault(PST);
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
		if (dateAfter != null && !"".equals(dateAfter) && dateBefore != null && !"".equals(dateBefore)) {
			try {
				createdAfter = Time.getTime(currentTZone, sdf.parse(dateAfter).getTime());
				createdBefore = Time.getTime(currentTZone, sdf.parse(dateBefore).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (createdAfter == null || createdBefore == null) {
			out.print("incorrect date");
			return;
		}
		//query database to get current rows
		int previousRows = ListOrdersManager.getCountFromDB(createdAfter, createdBefore);

		//query MWS and insert orders to database
		int insertRows = ListOrdersManager.insertShippedOrders(createdAfter, createdBefore);

		//query database to get total rows
		int totalRows = ListOrdersManager.getCountFromDB(createdAfter, createdBefore);

		out.print("Insert " + insertRows + " orders to database successfully.");
		out.print("<p></p>");
		out.print("There are " + previousRows + " orders in database before.");
		out.print("<br>");
		out.print("There are " + totalRows + " orders in database now.");
		out.print("<p></p>");
		out.print(createdAfter);
		out.print(" to ");
		out.print(createdBefore);
	}
%>