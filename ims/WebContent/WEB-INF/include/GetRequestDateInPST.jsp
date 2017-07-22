<%@ page import="java.util.TimeZone"%>
<%@ page import="common.util.Time"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%--
    Get request date in PST time zone

    Main action:
    1, Set TimeZone default to PST
    2, Change 2 date params from yyyy-MM-dd formate to XMLGregorianCalendar in PST time zone:
        Input params:dateAfter,dateBefore
        Output params:createdAfter,createdBefore

  Created by IntelliJ IDEA.
  User: Eric Li
  Date: 3/3/2017
  Time: 12:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%
	//Note: only post method was permitted
	if (!"POST".equalsIgnoreCase(request.getMethod())) {
		//get visitor ip
		String ip = common.util.IP.getIpAddr(request);
		System.err.println("Illegal access, the visitor IP is: " + ip);

		//response
		out.println("Illegal access!");
		return;
	}

	//get request params
	String dateAfter = common.util.Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = common.util.Filter.nullFilter(request.getParameter("dateBefore"));

	//set time zone to PST
	//
	TimeZone PST = Time.PST;
	TimeZone.setDefault(PST);
	TimeZone currentTZone = TimeZone.getDefault();
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	Timestamp now = new Timestamp(System.currentTimeMillis());

	//declare date params
	XMLGregorianCalendar createdAfter = null;
	XMLGregorianCalendar createdBefore = null;
	//parse and set date params
	if (dateAfter != null && !"".equals(dateAfter)) {
		//throw exception when date format is incorrect
		createdAfter = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateAfter).getTime()));
	}
	if (dateBefore != null && !"".equals(dateBefore)) {
		//throw exception when date format is incorrect
		createdBefore = Time.getTime(currentTZone, new Timestamp(sdf.parse(dateBefore).getTime()));
	}

	System.out.print(request.getRequestURI());
	System.out.print("\t");
	System.out.print("dateAfter=");
	System.out.print(dateAfter);
	System.out.print("\t");
	System.out.print("dateBefore=");
	System.out.print(dateBefore);
	System.out.print("\t");
	System.out.print("createdAfter=");
	System.out.print(createdAfter);
	System.out.print("\t");
	System.out.print("createdBefore=");
	System.out.println(createdBefore);

	//parameter completeness validate
	if (createdAfter == null || !createdAfter.isValid() || createdBefore == null || !createdBefore.isValid()) {
		String eMsg = "Illegal argument: createdAfter and createdBefore! They could not be null and they must be valid";
		System.err.print(request.getRequestURI());
		System.err.print("\t");
		System.err.println(eMsg);
		throw new IllegalArgumentException(eMsg);
	}
%>