<%@page import="java.sql.Timestamp"%>
<%@page import="amazon.mws.order.ListOrdersManager"%>
<%@page import="common.util.Time"%>
<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
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
		if (dateAfter != null && !"".equals(dateAfter) && dateBefore != null && !"".equals(dateBefore)) {
			try {
				createdAfter = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateAfter).getTime()));
				createdBefore = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateBefore).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (createdAfter == null || createdBefore == null) {
			out.print("incorrect date");
			return;
		}

		//call MWS to query count
		int mwsCount = ListOrdersManager.getCountFromMWS(createdAfter, createdBefore);

		out.println("There are " + mwsCount + " orders in MWS.");
		out.println("<p></p>");
		out.println(createdAfter);
		out.println(" to ");
		out.println(createdBefore);
	}
%>