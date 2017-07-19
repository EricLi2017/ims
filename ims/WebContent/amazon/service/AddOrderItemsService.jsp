<%@page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@ page import="java.text.ParseException"%>
<%@page import="common.util.Time"%>
<%@ page import="amazon.mws.order.ListOrderAndOrderItemsManager"%>
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
        if (dateAfter != null && !"".equals(dateAfter)
                && dateBefore != null && !"".equals(dateBefore)) {
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
        //query database to get the previous orders with order items
        int previousRows = ListOrderAndOrderItemsManager.getCountWithOrderItemsByPurchaseDateFromDB(createdAfter, createdBefore);

        //query MWS and insert order items into database
        int insertRows = ListOrderAndOrderItemsManager.insertOrderItems(createdAfter, createdBefore);

        //query database to get the total orders with order items
        int totalRows = ListOrderAndOrderItemsManager.getCountWithOrderItemsByPurchaseDateFromDB(createdAfter, createdBefore);

        //query database to get the total orders without order items
        int totalRowsWithout = ListOrderAndOrderItemsManager.getCountWithoutOrderItemsByPurchaseDateFromMWS(createdAfter, createdBefore);

        out.print("Insert " + insertRows + " rows order items into database successfully.");
        out.print("<p></p>");
        out.print("There are " + previousRows + " orders that has related order items in database before.");
        out.print("<br>");
        out.print("There are " + totalRows + " orders that has related order items in database now.");
        out.print("<p></p>");
        out.print("Ant there are " + totalRowsWithout + " orders that has no related order items in database.");
        out.print("<p></p>");
        out.print(createdAfter);
        out.print(" to ");
        out.print(createdBefore);
    }
%>