<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="common.util.Filter"%>
<%@page import="common.util.OrderBy"%>
<%@ page import="amazon.db.query.QueryProductAndOrder"%>
<%@ page import="common.util.Time"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.xml.datatype.XMLGregorianCalendar"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.util.List"%>
<%
	//query params ()
	boolean isQuery = (request.getQueryString() != null);
	String sku = Filter.nullFilter(request.getParameter("sku"));
	String fnsku = Filter.nullFilter(request.getParameter("fnsku"));
	String asin = Filter.nullFilter(request.getParameter("asin"));
	String name = Filter.nullFilter(request.getParameter("name"));
	String dateAfter = Filter.nullFilter(request.getParameter("dateAfter"));
	String dateBefore = Filter.nullFilter(request.getParameter("dateBefore"));
	String level = Filter.nullFilter(request.getParameter("level"));
	//order by params
	String sortedColumnId = Filter.nullFilter(request.getParameter("sortedColumnId"));
	String sortOrder = Filter.nullFilter(request.getParameter("sortOrder"));

	Map<String, String> sortedColumnMap = new HashMap<>();
	sortedColumnMap.putAll(QueryProductAndOrder.SORTED_COLUMN_MAP_1);
	sortedColumnMap.putAll(QueryProductAndOrder.SORTED_COLUMN_MAP_2);
	OrderBy orderBy = new OrderBy(sortedColumnId, sortedColumnMap, sortOrder);
	//set default order by
	sortedColumnId = !sortedColumnMap.containsKey(sortedColumnId) ? "1" : sortedColumnId;//set default orderBy value
	sortOrder = !OrderBy.ASC_DESC_MAP.containsKey(sortOrder) ? OrderBy.DESCENDING : sortOrder;//set default ascDesc value

	//set time zone to PST
	//
	// 	TimeZone PST = Time.PST;
	// 	TimeZone.setDefault(PST);
	TimeZone currentTZone = TimeZone.getDefault();
	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	Timestamp now = new Timestamp(System.currentTimeMillis());

	//set default query conditions
	if (!isQuery) {
		//set default level
		level = "1";

		//set default dateAfter
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DAY_OF_MONTH, -14);
		dateAfter = sdf.format(calendar.getTime());
	}
	System.out.println("sku=" + sku);
	System.out.println("fnsku=" + fnsku);
	System.out.println("asin=" + asin);
	System.out.println("name=" + name);
	System.out.println("dateAfter=" + dateAfter);
	System.out.println("dateBefore=" + dateBefore);
	System.out.println("level=" + level);

	//params showed in page
	List<QueryProductAndOrder.ProductAndOrder> productAndOrders = null;
	if (isQuery) {
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
			out.print("Incorrect date, correct date format is " + pattern);
			return;
		}

		Integer levelInt = null;
		if (!level.isEmpty()) {
			boolean isLevel = false;
			try {
				levelInt = Integer.parseInt(level);
				isLevel = true;
			} catch (NumberFormatException e) {

			}
			if (!isLevel) {
				out.print("Incorrect level, level should be integer");
				return;
			}
		}

		productAndOrders = QueryProductAndOrder.querySkuSalesSum(Time.getTime(createdAfter),
				Time.getTime(createdBefore), sku, asin, fnsku, name, levelInt, sortedColumnId, sortOrder);
	}
%>