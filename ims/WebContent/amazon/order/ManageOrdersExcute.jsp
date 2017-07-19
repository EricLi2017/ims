<%@ page import="java.util.List"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="common.util.Time"%>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="amazon.mws.order.ListOrdersManager"%>
<%@ page import="amazon.mws.order.ListOrdersAndOrderItemsDatabase"%>
<%@ page import="amazon.mws.order.ListOrderAndOrderItemsManager"%>
<%@ page import="amazon.db.query.OrderMatch"%>
<%
	//request params
	//
	boolean isQuery = (request.getQueryString() != null);
	boolean isQueryMWS = "Y".equalsIgnoreCase(request.getParameter("isQueryMWS"));
	boolean isGetOrders = "Y".equalsIgnoreCase(request.getParameter("isGetOrders"));
	String dateAfter = common.util.Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = common.util.Filter.nullFilter(request.getParameter("dateBefore"));

	//set time zone to PST
	//
	TimeZone PST = Time.PST;
	TimeZone.setDefault(PST);
	TimeZone currentTZone = TimeZone.getDefault();
	Timestamp defaultDateAfter;
	Timestamp defaultDateBefore;
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	Timestamp now = new Timestamp(System.currentTimeMillis());

	//action when query submit
	//
	int dbCount = 0;
	int rows = 0;
	int noRows = 0;
	List<String> amazonOrderIds = null;
	OrderMatch.OrderMatchResult orderMatchResult = null;//result of check order num and item num in orders and order_items
	XMLGregorianCalendar createdAfter = null;
	XMLGregorianCalendar createdBefore = null;
	if (isQuery) {
		//validate date format
		//
		boolean isDateAfterValid = true;
		boolean isDateBeforeValid = true;
		if (dateAfter != null && !"".equals(dateAfter)) {
			isDateAfterValid = false;
			try {
				createdAfter = Time.getTime(currentTZone, sdf.parse(dateAfter).getTime());
				isDateAfterValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (dateBefore != null && !"".equals(dateBefore)) {
			isDateBeforeValid = false;
			try {
				createdBefore = Time.getTime(currentTZone, sdf.parse(dateBefore).getTime());
				isDateBeforeValid = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!isDateAfterValid || !isDateBeforeValid) {
			out.print("incorrect date, correct date format is " + pattern);
			return;
		}

		//select form database
		//
		//query database to get count of orders
		dbCount = ListOrdersManager.getCountFromDB(createdAfter, createdBefore);

		rows = ListOrderAndOrderItemsManager.getCountWithOrderItemsByPurchaseDateFromDB(createdAfter,
				createdBefore);
		noRows = ListOrderAndOrderItemsManager.getCountWithoutOrderItemsByPurchaseDateFromMWS(createdAfter,
				createdBefore);

		//check order num and item num in orders and order_items
		orderMatchResult = new OrderMatch().check(createdAfter, createdBefore);

		amazonOrderIds = new ListOrdersAndOrderItemsDatabase().selectOrderIdsWithoutOrderItemsByPruchaseDate(
				Time.getTimeInPST(createdAfter), Time.getTimeInPST(createdBefore));

		System.out.println("there are " + rows + " rows with order items");
		System.out.println("there are " + noRows + " rows without order items");
		System.out.println("there are " + amazonOrderIds == null ? null
				: amazonOrderIds.size() + " rows without order items");
	}
%>